package com.eservice.api.service.common;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_sign.ContactSign;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.design_dep_info.DesignDepInfo;
import com.eservice.api.model.design_dep_info.DesignDepInfoDetail;
import com.eservice.api.model.install_group.InstallGroup;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.role.Role;
import com.eservice.api.model.task.Task;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.ContactFormService;
import com.eservice.api.service.impl.*;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tk.mybatis.mapper.entity.Condition;

/**
 * Class Description:通用服务类
 *
 * @author Wilson Hu
 * @date 22/12/2017
 */
@Service
public class CommonService {
    @Resource
    private ContractSignServiceImpl contractSignService;
    @Resource
    private OrderSignServiceImpl orderSignService;
    @Resource
    private RoleServiceImpl roleService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private ProcessRecordServiceImpl processRecordService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private MqttMessageHelper mqttMessageHelper;
    @Resource
    private TaskServiceImpl taskService;
    @Resource
    private InstallGroupServiceImpl installGroupService;
    @Resource
    private UserServiceImpl userService;

//    @Value("${sinsimPocess_call_aftersale}")
//    private String sinsimPocess_call_aftersale;

    @Resource
    private ContactFormService contactFormService;

    @Resource
    private CommonService commonService;

    @Resource
    private ContractServiceImpl contractService;
    @Resource
    private DesignDepInfoServiceImpl designDepInfoService;

    Logger logger = Logger.getLogger(CommonService.class);

//    public String gatewayIpFromDockerFile ="aaa"; //main是静态域，所以不方便从main参数里获取值，已改为从文件里读取。
    /**
     * 用于返回对应合同的所有签核记录，每一次提交审核以后，都需要通过该API获取所有审核内容，再设置审核状态
     *
     * @param contractId
     * @return result, 如果签核流程不存在返回null， 未结束则返回正在签核中的名称，结束则返回“FINISHED”
     */
    public String getCurrentSignStep(Integer contractId) {
        String result = null;

        //通过合同号找到有效的订单签核记录
        List<OrderSign> orderSignList = orderSignService.getValidOrderSigns(contractId);
        //找到有效的合同签核记录，其实就是最新的合同簽和記錄
        ContractSign contractSign1 = contractSignService.detailByContractId(String.valueOf(contractId));
        HashMap<Integer, List<SignContentItem>> signContentSortByNumberMap = new HashMap<>();
        for (OrderSign os : orderSignList) {
            String contentStr = os.getSignContent();
            List<SignContentItem> orderSignContentList = JSON.parseArray(contentStr, SignContentItem.class);
            for (SignContentItem item : orderSignContentList) {
                if (signContentSortByNumberMap.get(item.getNumber()) != null) {
                    signContentSortByNumberMap.get(item.getNumber()).add(item);
                } else {
                    List<SignContentItem> list = new ArrayList<>();
                    list.add(item);
                    signContentSortByNumberMap.put(item.getNumber(), list);
                }
            }
        }
        List<SignContentItem> contractSignContentList = JSONObject.parseArray(contractSign1.getSignContent(), SignContentItem.class);
        for (SignContentItem item : contractSignContentList) {
            if (signContentSortByNumberMap.get(item.getNumber()) != null) {
                signContentSortByNumberMap.get(item.getNumber()).add(item);
            } else {
                List<SignContentItem> list = new ArrayList<>();
                list.add(item);
                signContentSortByNumberMap.put(item.getNumber(), list);
            }
        }

        //签核流程不存在
        if (signContentSortByNumberMap.size() == 0) {
            return result;
        }
        // 将map.entrySet()转换成list
        List<Map.Entry<Integer, List<SignContentItem>>> list = new ArrayList<>(signContentSortByNumberMap.entrySet());
        // 通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<Integer, List<SignContentItem>>>() {
            @Override
            public int compare(Map.Entry<Integer, List<SignContentItem>> o1, Map.Entry<Integer, List<SignContentItem>> o2) {
                // 升序排序
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        Iterator<Map.Entry<Integer, List<SignContentItem>>> entries = signContentSortByNumberMap.entrySet().iterator();
        boolean currentStepFound = false;
        while (entries.hasNext() && !currentStepFound) {
            Map.Entry<Integer, List<SignContentItem>> entry = entries.next();
            Integer key = entry.getKey();
            List<SignContentItem> value = entry.getValue();
            for (SignContentItem item : value) {
                //根據簽和順序，找到最先的被拒絕或者未簽合的流程
                if (item.getResult().equals(Constant.SIGN_REJECT) || item.getResult().equals(Constant.SIGN_INITIAL)) {
                    List<Role> roleList = roleService.findAll();
                    for (Role role : roleList) {
                        if (role.getId().equals(item.getRoleId())) {
                            result = role.getRoleName();
                        }
                    }
                    currentStepFound = true;
                }
            }
        }
        //如果没有找到，表示该签核流程已经完成，此时返回一个特殊的String给调用者
        if (!currentStepFound) {
            result = Constant.SIGN_FINISHED;
        }

        return result;
    }

    /**
     * 根据合同编号对应的需求单的机器
     * 目前 合同签核已经不存在了，即，只对订单进行签核。即应该已经不调用了。
     *
     * @param contractId
     */
    public void createMachineByContractId(Integer contractId) {
        Condition condition = new Condition(MachineOrder.class);
        condition.createCriteria().andCondition("contract_id = ", contractId);
        List<MachineOrder> orderList = machineOrderService.findByCondition(condition);
        for (MachineOrder orderItem : orderList) {
            //选取有效需求单，无效需求单对应的机器数不cover在内
            if (orderItem.getStatus().equals(Constant.ORDER_CHECKING_FINISHED)
                    || orderItem.getStatus().equals(Constant.ORDER_SPLITED)) {
                Condition tempCondition = new Condition(Machine.class);
                tempCondition.createCriteria().andCondition("order_id = ", orderItem.getId());
                List<Machine> machineExistList = machineService.findByCondition(tempCondition);
                int haveToCreate = orderItem.getMachineNum() - machineExistList.size();
                int i = 1;
                while (i <= haveToCreate) {
                    Machine machine = new Machine();
                    machine.setMachineStrId(Utils.createMachineBasicId() + i);
                    machine.setOrderId(orderItem.getId());
                    machine.setMachineType(orderItem.getMachineType());
                    machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_INITIAL)));
                    machine.setCreateTime(new Date());
                    machineService.save(machine);
                    i++;
                }
            }
        }
    }

    /**
     * 目前已经没有合同签核，合同不用签核，即都是在订单签核完成时生成机器，或者是在：改单时机器数量变多，则生成机器。
     * 生成需求单编号对应的机器
     *
     * @param orderItem
     */
    public void createMachineByOrderId(MachineOrder orderItem) {

        logger.info("createMachineByOrderId，　orderNum: " + orderItem.getOrderNum());
        //选取有效需求单，无效需求单对应的机器数不cover在内
        Condition tempCondition = new Condition(Machine.class);
        tempCondition.createCriteria().andCondition("order_id = ", orderItem.getId());
        List<Machine> machineExistList = machineService.findByCondition(tempCondition);
        int haveToCreate = orderItem.getMachineNum() - machineExistList.size();
        logger.info("machineExistList size: " + machineExistList.size() + ", haveToCreate: " + haveToCreate);
        int i = 1;
        while (i <= haveToCreate) {
            Machine machine = new Machine();
            machine.setMachineStrId(Utils.createMachineBasicId() + i);
            machine.setOrderId(orderItem.getId());
            machine.setMachineType(orderItem.getMachineType());
            machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_INITIAL)));
            machine.setCreateTime(new Date());
            //如果合同加急，则对合同内的所有机器都加急。
            if (orderItem.getAllUrgent() == null || !orderItem.getAllUrgent()) {
                machine.setIsUrgent(false);
            } else {
                machine.setIsUrgent(true);
            }
            machineService.save(machine);
            i++;
            logger.info("have created nameplate: " + machine.getNameplate());
        }
        //有出现过订单审批完成后，却没有生成机器的情况。
        if (haveToCreate < 1) {
            logger.warn("!Attention: haveToCreate is " + haveToCreate);
        }
    }

    /**
     * @param path      保存文件的总路径
     * @param file      文件名称
     * @param machineID 机器的具体ID（保存装车单时，machineId可以为NULL）
     * @param orderNum  装车单对应的需求单编号（保存Abnormal/Quality 图片时，orderNum可为NULL）
     * @param type      文件类型 （不同类型文件名生成规则不同）
     *                  最终生成类似：machineID123_Abnormal_2018-01-10-11-15-56.png
     * @param number    表示第几个文件
     * @return 文件路径
     */
    public String saveFile(String path,
                           MultipartFile file,
                           @RequestParam(defaultValue = "") String machineID,
                           @RequestParam(defaultValue = "") String orderNum,
                           int type,
                           @RequestParam(defaultValue = "0") int number) throws IOException {
        String targetFileName = null;
        try {
            if (path != null && !file.isEmpty()) {
                //取后缀名
                String fileName = file.getOriginalFilename();
                targetFileName = path + formatFileName(type, fileName.replaceAll("/", "-"), machineID, orderNum.replaceAll("/", "-"), number);
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(targetFileName)));
                out.write(file.getBytes());
                out.flush();
                out.close();
                logger.info("====CommonService.saveFile(): success ========" + targetFileName);
            }
        } catch (IOException e) {
            throw e;
        }
        return targetFileName;
    }

    public String formatFileName(int type,
                                 String fileName,
                                 @RequestParam(defaultValue = "") String machineID,
                                 @RequestParam(defaultValue = "") String orderNum,
                                 @RequestParam(defaultValue = "0") int number) {
        String targetFileName = "";
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        //指定北京时间
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String dateStr = formatter.format(date);

        String fileType;
        switch (type) {
            case Constant.ABNORMAL_IMAGE:
                fileType = "Abnormal";
                break;
            case Constant.QUALITY_IMAGE:
                fileType = "Quality";
                break;
            case Constant.LOADING_FILE:
                fileType = "LoadingFile";
                dateStr = "";
                break;
            case Constant.LXD_ATTACHED_FILE_BY_CREATER:
                fileType = "lxdAttached_by_creater";
                dateStr = "";
                break;
            case Constant.LXD_ATTACHED_FILE_DURING_SIGN:
                fileType = "lxdAttached_during_sign";
                dateStr = "";
                break;
            case Constant.DESIGN_ATTACHED_FILE:
                fileType = "designAttached";
                dateStr = "";
                break;
            case Constant.OPTIMIZE_ATTACHED_FILE:
                fileType = "optimize";
                dateStr = "";
                break;

            default:
                fileType = " ";
                break;
        }
        targetFileName = orderNum + "_" + machineID + "_" + fileType + "_" + dateStr + "_" + number + suffixName;
        return targetFileName;
    }

    public boolean updateTaskRecordRelatedStatus(TaskRecord tr) {
        if (tr == null || tr.getProcessRecordId() == null) {
            return false;
        } else {
            Integer prId = tr.getProcessRecordId();
            ProcessRecord pr = processRecordService.findById(prId);
            Machine machine = machineService.findById(pr.getMachineId());
            boolean isNeedUpdateMachine = false;
            if (pr != null) {
                String nodeData = pr.getNodeData();
                List<NodeDataModel> ndList = JSON.parseArray(nodeData, NodeDataModel.class);
                NodeDataModel ndItem = null;
                Integer index = -1;
                for (int i = 0; i < ndList.size(); i++) {
                    if (Integer.parseInt(ndList.get(i).getKey()) == tr.getNodeKey()) {
                        index = i;
                        break;
                    }
                }
                if (index > -1) {
                    ndItem = ndList.get(index);
                    ndItem.setTaskStatus(tr.getStatus().toString());

                    if (tr.getStatus().intValue() == Constant.TASK_PLANED.intValue()
                            || tr.getStatus().intValue() == Constant.TASK_INSTALL_WAITING.intValue()) {
                        if (tr.getInstallBeginTime() == null) {
                            String date = Utils.getFormatStringDate(new Date(), "yyyy-MM-dd HH:mm:ss");
                            ndItem.setBeginTime(date);
                        }
                    }
                    if (tr.getInstallBeginTime() != null) {
                        String date = Utils.getFormatStringDate(tr.getInstallBeginTime(), "yyyy-MM-dd HH:mm:ss");
                        ndItem.setBeginTime(date);
                    }
                    //质检完成，工序才算完成
                    if (tr.getQualityEndTime() != null) {
                        String date = Utils.getFormatStringDate(tr.getQualityEndTime(), "yyyy-MM-dd HH:mm:ss");
                        ndItem.setEndTime(date);
                    }
                    //组长信息
                    if (tr.getLeader() != null && tr.getLeader().length() > 0) {
                        ndItem.setLeader(tr.getLeader());
                    }
                    //工作人员信息
                    if (tr.getWorkerList() != null && tr.getWorkerList().length() > 0) {
                        ndItem.setWorkList(tr.getWorkerList());
                    }
                    ndList.set(index, ndItem);
                    //如果当前工序是质检完成状态或者跳过状态，需要检查其子节点是否可以开始 -->3期时，工序安装完成，状态不再是“质检完成”而是“安装完成”
                    if (tr.getStatus().intValue() == Constant.TASK_QUALITY_DONE.intValue()
                            || tr.getStatus().intValue() == Constant.TASK_INSTALLED.intValue()
                            || tr.getStatus().intValue() == Constant.TASK_SKIP.intValue()) {
                        List<LinkDataModel> linkDataList = JSON.parseArray(pr.getLinkData(), LinkDataModel.class);
                        for (LinkDataModel item : linkDataList) {
                            if (String.valueOf(item.getFrom()).equals(String.valueOf(ndItem.getKey()))) {
                                for (NodeDataModel childNode : ndList) {
                                    //先找到子节点
                                    if (childNode.getKey().equals(String.valueOf(item.getTo()))) {
                                        //找到子节点的所有父节点
                                        boolean allParentFinished = true;
                                        for (LinkDataModel parentOfChild : linkDataList) {
                                            if (!allParentFinished) {
                                                break;
                                            }
                                            if (String.valueOf(parentOfChild.getTo()).equals(childNode.getKey())) {
                                                for (NodeDataModel parentOfChildNode : ndList) {
                                                    if(!allParentFinished) {
                                                        break;
                                                    }
                                                    if(String.valueOf(parentOfChild.getFrom()).equals(parentOfChildNode.getKey())) {
                                                        if (parentOfChildNode.getCategory() != null && (parentOfChildNode.getCategory().equals("Start") || parentOfChildNode.getCategory().equals("End"))) {
                                                            break;
                                                        }
                                                        //3期质检，安装完成是真的“安装完成”。3期之前，安装完成时，状态是“质检完成”
                                                        if ((Integer.valueOf(parentOfChildNode.getTaskStatus()) != Constant.TASK_QUALITY_DONE.intValue()
                                                                && Integer.valueOf(parentOfChildNode.getTaskStatus()) != Constant.TASK_INSTALLED.intValue())
                                                                && Integer.valueOf(parentOfChildNode.getTaskStatus()) != Constant.TASK_SKIP.intValue()) {
                                                            allParentFinished = false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //子节点的所有父节点都已经完成，则更新子节点的状态
                                        if (allParentFinished) {
                                            //如果子工序不是结束“End”
                                            if(!"End".equals(childNode.getCategory())) {
                                                if(Integer.valueOf(childNode.getTaskStatus()) < Constant.TASK_INSTALL_WAITING.intValue()) {
                                                    String dateStr = Utils.getFormatStringDate(new Date(), "yyyy-MM-dd HH:mm:ss");
                                                    childNode.setBeginTime(dateStr);
                                                    childNode.setTaskStatus(Constant.TASK_INSTALL_WAITING.toString());
                                                    List<TaskRecord> taskRecordList = taskRecordService.getTaskRecordData(null, prId);
                                                    for (TaskRecord record : taskRecordList) {
                                                        if (String.valueOf(record.getNodeKey().intValue()).equals(childNode.getKey())) {
                                                            record.setUpdateTime(new Date());
                                                            record.setStatus(Constant.TASK_INSTALL_WAITING);
                                                            taskRecordService.update(record);
                                                            //MQTT 通知下一道工序可以开始安装
                                                            ServerToClientMsg msg = new ServerToClientMsg();
                                                            MachineOrder machineOrder = machineOrderService.findById(machine.getOrderId());
                                                            msg.setOrderNum(machineOrder.getOrderNum());
                                                            msg.setNameplate(machine.getNameplate());
                                                            //找到工序对应的group_id
                                                            String taskName = record.getTaskName();
                                                            Condition condition = new Condition(Task.class);
                                                            condition.createCriteria().andCondition("task_name = ", taskName);
                                                            List<Task> taskList = taskService.findByCondition(condition);
                                                            if (taskList == null || taskList.size() <= 0) {
                                                                throw new RuntimeException();
                                                            }
                                                            mqttMessageHelper.sendToClient(Constant.S2C_TASK_INSTALL + taskList.get(0).getGroupId(), JSON.toJSONString(msg));
                                                            //这个break需要去掉，因为存在多个子工序可以安装的情况
                                                            //break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Boolean isFinished = true;
                for (int i = 0; i < ndList.size() && isFinished; i++) {
                    //开始和结束节点不考虑在内
                    if(ndList.get(i).getCategory() != null
                            && (ndList.get(i).getCategory().equals("Start") || ndList.get(i).getCategory().equals("End"))) {
                        continue;
                    }
                    if (ndList.get(i).getTaskStatus() != null &&
                            (Integer.parseInt(ndList.get(i).getTaskStatus()) != Constant.TASK_QUALITY_DONE.intValue() && Integer.parseInt(ndList.get(i).getTaskStatus()) != Constant.TASK_INSTALLED.intValue())) {
                        isFinished = false;
                    }
                }
                //所有工序完成
                if (isFinished &&
                        (tr.getStatus() == Constant.TASK_QUALITY_DONE.intValue() || tr.getStatus() == Constant.TASK_INSTALLED.intValue())) {
                    pr.setEndTime(new Date());
                    //安装完成
                    machine.setStatus(Constant.MACHINE_INSTALLED);
                    isNeedUpdateMachine = true;



                }

                if (machine.getStatus().equals(Constant.MACHINE_PLANING)) {
                    //安装中
                    machine.setStatus(Constant.MACHINE_INSTALLING);
                    isNeedUpdateMachine = true;
                }
                if (isNeedUpdateMachine) {
                    machine.setUpdateTime(new Date());
                    machineService.update(machine);
                }
                pr.setNodeData(JSON.toJSONString(ndList));
                processRecordService.update(pr);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 处理 12+1 这种形式
     * 比如头数写 12+1，返回应该为13
     *
     */
    public int getRealSumValue(String str){

        String[] numbers = str.trim().split("\\+");
        int headSum = 0;
        for(int i =0; i<numbers.length; i++){
            headSum = headSum + Integer.valueOf(numbers[i]);
        }
        return headSum;
    }
	
    /**
     * 秒数 转 天，小时，分钟
     */
    public String secondsToMinHourDay(long milliSecond){
        String ret = null;

        long second = milliSecond/1000;
        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        if(days>0){
            return days + "天" + hours + "小时" + minutes + "分" + second + "秒";
        }else if (hours>0){
            return hours + "小时" + minutes + "分" + second + "秒";
        } else if(minutes>0){
            return minutes + "分" + second + "秒";
        } else {
            return second + "秒";
        }
    }
    /**
     * 秒数 转  分钟
     */
    public long secondsToMin(long milliSecond){
        String ret = null;

        long second = milliSecond/1000;
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数

        //1~59秒归为1分钟
        if( minutes ==0 && second>0){
            minutes = 1;
        }
        return minutes;
    }

    /**
     * 发送MQTT消息给订阅的app(安装组)。
     * 实例：mqtt topic: /s2c/task_remind/1, msg: {"nameplate":"namePlate123"} 其中1是 taskName为上轴安装的安装组的groupId
     *
     * @param taskName：
     * @param topic : MQTT topic
     * @param nameplate
     * @return  结果信息
     */
    public String sendMqttMsg(  String taskName, String topic, String nameplate){

        String resultMsg = null;
        // taskName转groupId, 一个安装组可以有多种任务。
        InstallGroup installGroup = installGroupService.getInstallGroupByTaskName(taskName);
        if(installGroup == null){
            resultMsg = "错误，根据taskName " + taskName + " 找不到对应的安装组";
            return resultMsg;
        }
        ServerToClientMsg msg = new ServerToClientMsg();
        msg.setNameplate(nameplate);
        mqttMessageHelper.sendToClient(topic + installGroup.getId(), JSON.toJSONString(msg));
        resultMsg = "try to send mqtt: " + topic + installGroup.getId() + " with message " + msg.getNameplate();
        return resultMsg;

    }

    //查看该账号是否可以看敏感信息
    public boolean isDisplayPrice(String account){
        //只有总经理，销售，财务等用户，生成的excel里才显示金额信息. '6','7','9','14','15'
        Boolean displayPrice = false;
        User user = userService.selectByAccount(account);
        if (user != null) {
            Integer roleId = user.getRoleId();
            if ((1 == roleId)           //管理员
                    || (6 == roleId)           //总经理
                    || (7 == roleId)    //销售部经理
                    || (9 == roleId)    //销售员
                    || (13 == roleId)   //成本核算员
                    || (14 == roleId)   //财务经理
                    || (15 == roleId)) {//财务会计
                displayPrice = true;
            }
        } else {
            displayPrice = false;
        }
        return displayPrice;
    }

    /**
     *  执行curl命令行命令
     *  会有异步等待时间，问题不大。
     *  --> 如果网络不通，比如IP不对，会好几秒才返回，所以需要正确获取主机docker0的IP （各docker的Gateway）
     *
     *  特别注意： 这是在docker内部执行的。
     */
    public String execCurl(String[] cmds) {
        logger.info("执行curl开始" );
        ProcessBuilder process = new ProcessBuilder(cmds);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            logger.info("执行curl 完成 " + builder.toString());
            return builder.toString();

        } catch (IOException e) {
            logger.info("执行curl 异常 " + e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 废弃，因为 docker inspect 命令无法在java环境里执行，只能在服务器主机执行。
     * @return
     */
    public String getDockerGatewayIp() {
        // docker的 gateway 就是服务器本机 docker0 网卡的ip
        String gatewayIp = "";

        String cmdToGetGatewayIp = "docker inspect -f '{{range .NetworkSettings.Networks}}{{.Gateway}}{{end}}'  allinone_server_process_1";
        gatewayIp = executeLinuxCmd(cmdToGetGatewayIp);
        /**
         * 这个linux命令是由JAVA执行的，即在docker里执行的，这样是无法获取 服务器本机 docker0 网卡的ip的。
         * 只能在服务器本机执行才能获取 服务器本机 docker0 网卡的ip
         */

        return gatewayIp;
    }

    /**
     * docker的gateway的IP是会变化的，可以在文件里改，不用重新编译。
     * @return
     */
    public String getDockerGatewayIpFromFile() {
        // docker的 gateway 就是服务器本机 docker0 网卡的ip
        String gatewayIp = "";

        String cmdToGetGatewayIp = "cat /opt/sinsim/upload-data/dockerGatewayIp";
        gatewayIp = executeLinuxCmd(cmdToGetGatewayIp);
        /**
         * 这个linux命令是由JAVA执行的，即在docker里执行的，这样是无法获取 服务器本机 docker0 网卡的ip的。
         * 只能在服务器本机执行才能获取 服务器本机 docker0 网卡的ip
         */

        return gatewayIp;
    }
    /**
     * 执行Linux命令
     * 【注意】 这个执行，是在docker镜像内部执行。java里的执行都是docker里执行，不是在服务器本机执行。
     * @param cmd
     * @return 返回的是命令执行的结果（String）。
     */
    public String executeLinuxCmd(String cmd) {

        logger.info("执行命令[ " + cmd + "]");
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmd);
            String line;
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer out = new StringBuffer();
            while ((line = stdoutReader.readLine()) != null ) {
                out.append(line);
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("执行结果InterruptedException: " + e.getMessage());
            }
            process.destroy();
            logger.info("执行命令 返回 " + out.toString());
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 发送信息给售后系统,通知签核
     * @param accountX
     * @param machineOrderNum_X
     * @param lxdNum_X
     * @return
     */
    public String sendSignInfoViWxMsg(@RequestParam String accountX,
                                      @RequestParam(defaultValue = "") String machineOrderNum_X,
                                      @RequestParam(defaultValue = "") String lxdNum_X,
                                      @RequestParam(defaultValue = "") String StrCreateDate_X,
                                      @RequestParam(defaultValue = "") String department_X,
                                      @RequestParam(defaultValue = "") String applicantPerson_X,
                                      @RequestParam(defaultValue = "") String msgInfo_X ) {
        String result = null;
        //该账号是否接收消息推送
        User user = null;
        user = userService.selectByAccount(accountX);
        if( user.getAcceptWxMsg() != null) {
            if (user.getAcceptWxMsg().intValue() == 0) {
                result = accountX + " 被设定为不接收微信消息推送";
                logger.info(result);
                return result;
            }
        }


//return "skip sendSignInfoViWxMsg";
 //注意，本地和服务器不一样
        logger.info("step000");
        String docker0_ip = getDockerGatewayIpFromFile();
//        String docker0_ip = gatewayIpFromDockerFile;
        logger.info("step001, docker0_ip:" + docker0_ip);
//        String url = sinsimPocess_call_aftersale
        String url = docker0_ip + "/api/"
                + "for/sinimproccess/sendRemind";
        logger.info("推送,准备开始. curl url:" + url
                + ", accountX:" + accountX
                + ", machineOrderNum_X:" + machineOrderNum_X
                + ", lxdNum_X:" + lxdNum_X
                + ", department_X" + department_X
                + ", applicantPerson_X" + applicantPerson_X
                + ", createDate_X:" + StrCreateDate_X
                + ", msgInfo_X:" + msgInfo_X );
        try {
            /**
             * 事先做对账号等可能为中文的字符串进行encode转码,然后在售后端收到之后解码
             * //OK在服务器:
             *  curl -X POST -G
             *   --data-urlencode "account=汤能萍"
             *   --data-urlencode "machineOrderNumber=XXX2000"
             *   localhost:/api/for/sinimproccess/sendRemind
             *   但是在代码里不行.
             */

            accountX = URLEncoder.encode(accountX, "UTF-8");
            machineOrderNum_X = URLEncoder.encode(machineOrderNum_X, "UTF-8");
            lxdNum_X = URLEncoder.encode(lxdNum_X, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        accountX = "account=accountX".replaceAll("accountX",accountX);

        logger.info("订单签核推送给售后,(解码后) curl url:" + url
                + ", accountX:" + accountX
                + ", machineOrderNumX:" + machineOrderNum_X
                + "StrCreateDate_X.:" + StrCreateDate_X
                + ",department_X:" + department_X
                + ",applicantPerson_X:" + applicantPerson_X
                + ",msgInfo_X:" + msgInfo_X);
        StrCreateDate_X = "StrCreateDate=StrCreateDate_X".replaceAll("StrCreateDate_X",StrCreateDate_X);
        department_X = "department=department_X".replaceAll("department_X",department_X);
        applicantPerson_X = "applicantPerson=applicantPerson_X".replaceAll("applicantPerson_X",applicantPerson_X);
        msgInfo_X = "msgInfo=msgInfo_X".replaceAll("msgInfo_X",msgInfo_X);

        if(!machineOrderNum_X.equals("") ){
            machineOrderNum_X = "machineOrderNum=machineOrderNum_X".replaceAll("machineOrderNum_X",machineOrderNum_X);
            String[] cmds = {"curl",
                    "-X",
                    "POST",
                    "-G",
                    "--data-urlencode", ///这些貌似未起作用,最终还是要事先做encode转码,然后在售后端收到之后解码.
                    accountX,
                    "--data-urlencode",
                    machineOrderNum_X,
                    "--data-urlencode",
                    StrCreateDate_X,
                    "--data-urlencode",
                    department_X,
                    "--data-urlencode",
                    applicantPerson_X,
                    "--data-urlencode",
                    msgInfo_X,
                    url,
                    "-H",
                    "accept: */*",
                    "-H",
                    "Content-Type: application/json;charset=UTF-8"
            };
            result = execCurl(cmds);
       //     executeLinuxCmd("curl -X POST localhost/api/for/sinimproccess/sendRemind?account=neimao&&machineOrderNumX=mo111&&msgInfo=msg222");
            logger.info("result:" + result);
        }
        if(!lxdNum_X.equals("") ){
            lxdNum_X = "lxdNum=lxdNum_X".replaceAll("lxdNum_X",lxdNum_X);
            String[] cmds = {"curl",
                    "-X",
                    "POST",
                    "-G",
                    "--data-urlencode", ///这些貌似未起作用,最终还是要事先做encode转码,然后在售后端收到之后解码.
                    accountX,
                    "--data-urlencode",
                    lxdNum_X,
                    "--data-urlencode",
                    StrCreateDate_X,
                    "--data-urlencode",
                    department_X,
                    "--data-urlencode",
                    applicantPerson_X,
                    "--data-urlencode",
                    msgInfo_X,
                    url,
                    "-H",
                    "accept: */*",
                    "-H",
                    "Content-Type: application/json;charset=UTF-8"
            };

            result = execCurl(cmds);
            logger.info("result:" + result);
        }

        return result;
    }

    //根据联系单的 签核信息 返回信息中的所有签核人（虽然在签核流程里，但没有经过签核的人就不用了）
    public List<User> getUsersInLxdSign(ContactSign cs ) {
        List<SignContentItem> contactSignContentList = JSON.parseArray(cs.getSignContent(), SignContentItem.class);
        List<User> userList = new ArrayList<>();
        for (SignContentItem item : contactSignContentList) {
//                    * 签核结果
//                    * "0" --> "初始化"
//                    * "1" --> "同意"
//                    * "2" --> "拒绝"

//            if( ! item.getResult().equals(0)) {//（虽然在签核流程里，但没有经过签核的人就不用了）
//                userList.add(userService.selectByAccount(item.getUser()));
//            }
            /**
             * 被拒绝的联系单，其实result已经被设置为“初始化"0了。
             * 所以，要根据意见来判断是否参加过签核（签核意见是不允许空的）
             */
            if( item.getComment() != null && ! item.getComment().isEmpty()) {
                userList.add(userService.selectByAccount(item.getUser()));
            }
        }
        return userList;
    }

    //根据订单的 签核信息 返回信息中的所有签核人（虽然在签核流程里，但没有经过签核的人就不用了）
    public List<User> getUsersInMachineOrderSign(OrderSign os) {

        List<SignContentItem> orderSignContentList = JSON.parseArray(os.getSignContent(), SignContentItem.class);
        List<User> userList = new ArrayList<>();

        for (SignContentItem item : orderSignContentList) {
//            if( item.getResult().equals(Constant.SIGN_APPROVE )|| item.getResult().equals(Constant.SIGN_REJECT) ) {//（虽然在签核流程里，但没有经过签核的人就不用了）
//                userList.add(userService.selectByAccount(item.getUser()));
//            }
            /**
             * 被拒绝的订单，其实result已经被设置为“初始化"0了。
             * 所以，要根据意见来判断是否参加过签核（签核意见是不允许空的）
             * 对应内贸订单，外贸经理不需要审核，内容为 "--"
             */
            if( item.getComment() != null && ! item.getComment().isEmpty() && ! item.getComment().equals("--")) {
                userList.add(userService.selectByAccount(item.getUser()));
            }
        }
        return userList;
    }

    /**
     * 推送公众号消息给轮到联系单签核的人（通过售后系统）
     * @param cs
     * @param cf
     * @param haveReject 是否驳回 （ 虽然这个信息可以从cs中提取，但是调用该函数的地方前面已经提前过了，方便用）
     */
    public void pushLxdMsgToAftersale(ContactSign cs,
                                      ContactForm cf,
                                      boolean haveReject,
                                      String msgInfo ) {

        //指定格式，因为售后解析要用到
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //指定北京时间
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String createDateStr = formatter.format(cf.getCreateDate());
        if (cs.getCurrentStep().equals(Constant.SIGN_FINISHED)) {
            //  审核完成时，通知发起人
            logger.info(cf.getContactTitle() + ", 审核完成时，通知发起人");
            List<UserDetail> userList = userService.selectUsers(cf.getApplicantPerson(), null, null, null, null, 1);
            if (userList.isEmpty() || userList == null) {
                logger.error("根据 " + cf.getApplicantPerson() + "找不到User");
            } else {
                //找到发起人
                UserDetail toUser = userList.get(0);
                commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                        "",
                        cf.getNum(),
                        createDateStr,
                        cf.getApplicantDepartment(),
                        cf.getApplicantPerson(),
                        msgInfo);
            }
        } else {
            /**
             * 审核没有结束，
             *      驳回  ：推送给参与签核的人，以及发起人
             *      无驳回：推送给下一个轮到签核的角色
             */
            Role role = roleService.findBy("roleName", cs.getCurrentStep());
            if (role == null) {
                logger.error("根据该 role_name " + cs.getCurrentStep() + "找不到Role");
                return;
            }
            List<UserDetail> userList = userService.selectUsers(null, null, role.getId(), null, null,1);

            if (haveReject) {
                List<User> userNameList = commonService.getUsersInLxdSign(cs);
                for (User toUser : userNameList) {
                    logger.info("联系单被拒绝，发给参与签核的人 " + toUser.getAccount());
                    commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                            "",
                            cf.getNum(),
                            createDateStr,
                            cf.getApplicantDepartment(),
                            cf.getApplicantPerson(),
                            msgInfo);
                }
                logger.info("联系单被拒绝，发给发起签核的人 " + cf.getApplicantPerson());
                commonService.sendSignInfoViWxMsg(cf.getApplicantPerson(),
                        "",
                        cf.getNum(),
                        createDateStr,
                        cf.getApplicantDepartment(),
                        cf.getApplicantPerson(),
                        msgInfo);

            } else {
                /**
                 * 没有被拒绝，只需要发给下一个
                 */
                //如果是销售部经理还要细分发给哪个经理，
                if (role.getRoleName().equals(Constant.SING_STEP_SALES_MANAGER)) {
                    String userSalesManagerAccount = cf.getDesignatedSaleManager();
                    logger.info(" 发给被指定的销售部经理 " + userSalesManagerAccount);
                    commonService.sendSignInfoViWxMsg(userSalesManagerAccount,
                            "",
                            cf.getNum(),
                            createDateStr,
                            cf.getApplicantDepartment(),
                            cf.getApplicantPerson(),
                            msgInfo);
                } else {

                    if (userList.isEmpty() || userList == null) {
                        logger.error("根据该roleId " + role.getId() + "找不到User");
                    } else {
                        //可能有多个负责人，比如研发部现在就两个经理，都通知。
                        for (UserDetail toUser : userList) {
                            ContactForm contactForm = contactFormService.findById(cs.getContactFormId());
                            logger.info(" 发给参与签核的角色(可能多个) " + toUser.getAccount());
                            commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                                    "",
                                    contactForm.getNum(),
                                    createDateStr,
                                    cf.getApplicantDepartment(),
                                    cf.getApplicantPerson(),
                                    msgInfo);
                        }
                    }
                }
            }
        }
    }

    /**
     * 推送公众号消息给轮到 订单签核的人（通过售后系统）
     * @param orderSignObj
     * @param contract
     * @param machineOrder
     * @param haveReject 是否驳回 （ 虽然这个信息可以从cs中提取，但是调用该函数的地方前面已经提前过了，方便用）
     */
    public void pushMachineOrderMsgToAftersale(OrderSign orderSignObj,
                                               Contract contract,
                                               MachineOrder machineOrder,
                                               boolean haveReject,
                                               String msgInfo) {
        //指定格式，因为售后解析要用到
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //指定北京时间
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String createDateStr = formatter.format(machineOrder.getCreateTime());
        /**
         * 签核结束，推送给订单录单人；
         */
        if (orderSignObj.getCurrentStep().equals(Constant.SIGN_FINISHED)) {
            List<UserDetail> userList = userService.selectUsers(contract.getRecordUser(), null, null, null, null,1);
            if (userList.isEmpty() || userList == null) {
                logger.error("根据 " + contract.getRecordUser() + "找不到User");
            } else {
                //找到录单人
                UserDetail toUser = userList.get(0);
                logger.info("订单签核结束，推送给订单录单人");
                commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                        machineOrder.getOrderNum(),
                        "",
                        createDateStr,
                        contract.getMarketGroupName(),
                        contract.getRecordUser(),
                        msgInfo);
            }
        } else {
            /**
             * 签核没有结束:
             *      驳回：推送给所有参与签核的人；
             *      非驳回：推送给轮到签核的角色
             */
            Role role = roleService.findBy("roleName", orderSignObj.getCurrentStep());
            if (role == null) {
                logger.error("根据该 role_name " + orderSignObj.getCurrentStep() + "找不到Role");
                return;
            }

            if (haveReject) {
                //驳回，发给所有参与签核的人。+ 录单人
                List<User> userList = null;
                userList = commonService.getUsersInMachineOrderSign(orderSignObj);
                if(userList != null && userList.size() !=0) {
                    for (User toUser : userList) {
                        logger.info("订单拒绝，发给参与签核的人 " + toUser.getAccount());
                        commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                                machineOrder.getOrderNum(),
                                "",
                                createDateStr,
                                contract.getMarketGroupName(),
                                contract.getRecordUser(),
                                msgInfo);
                    }
                } else {
                    logger.warn(machineOrder.getOrderNum() + ", 订单在此次拒绝前，没有参与过签核的人, 异常不可能");
                }

                //考虑录单人和签核人可能是相同的，这种情况只需要发一次
                boolean isRecorderInUserList = false;
                for (User toUser : userList) {
                    if (toUser.getAccount().equals(contract.getRecordUser())) {
                        isRecorderInUserList = true;
                        break;
                    }
                }
                if( ! isRecorderInUserList) {
                    logger.info("订单驳回，发给录单人 " + contract.getRecordUser());
                    commonService.sendSignInfoViWxMsg(contract.getRecordUser(),
                            machineOrder.getOrderNum(),
                            "",
                            createDateStr,
                            contract.getMarketGroupName(),
                            contract.getRecordUser(),
                            msgInfo);
                }
            } else {
                //没有驳回，发给下1个签核人
                //如果是销售部经理还要细分发给哪个部门的销售经理，
                if (role.getRoleName().equals(Constant.SING_STEP_SALES_MANAGER)) {
                    logger.info("推送签核给销售部经理");
                    List<UserDetail> toSalesManagerList = null;
                    //旧的签核记录里，没有SalesDepartment
                    if(orderSignObj.getSalesDepartment() !=null ) {
                        /**
                         * 外贸总监，user.market_group_name 不再是外贸一部、
                         * 外贸经理，user.market_group_name 还是外贸二部。
                         * 内贸部的订单：发给内贸经理
                         * 外贸1部的订单（外贸一部的销售员+外贸总监所录入）：发给外贸经理
                         * 外贸2部的订单（外贸二部的销售员+外贸经理所录入）：发给外贸经理
                         */
                        if (orderSignObj.getSalesDepartment().equals(Constant.STR_DEPARTMENT_DOMESTIC)) {
                            toSalesManagerList = userService.selectUsers(null, null, Constant.ROLE_ID_SALES_MANAGER, null, Constant.STR_DEPARTMENT_DOMESTIC, 1);
                        } else if (orderSignObj.getSalesDepartment().equals(Constant.STR_DEPARTMENT_FOREIGN_1)){
                            toSalesManagerList = userService.selectUsers(null, null, Constant.ROLE_ID_SALES_MANAGER, null, Constant.STR_DEPARTMENT_FOREIGN_FUZZY, 1);
                        } else if (orderSignObj.getSalesDepartment().equals(Constant.STR_DEPARTMENT_FOREIGN_2)){
                            //同上,即外贸一部二部，都是由外贸经理来审核
                            toSalesManagerList = userService.selectUsers(null, null, Constant.ROLE_ID_SALES_MANAGER, null, Constant.STR_DEPARTMENT_FOREIGN_FUZZY, 1);
                        }

                        if(toSalesManagerList != null) {
                            for (UserDetail toUser : toSalesManagerList) {
                                logger.info("订单继续签核，发给下销售经理  " + toUser.getAccount());
                                commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                                        machineOrder.getOrderNum(),
                                        "",
                                        createDateStr,
                                        contract.getMarketGroupName(),
                                        contract.getRecordUser(),
                                        msgInfo);
                            }
                        } else {
                            logger.info("推送消息，找不到销售部经理");
                        }
                    } else{
                        /**
                         * 外贸总监，角色即要有外贸经理的权限，又，，，目前特殊化的东西有点多。
                         * 外贸总监的销售部门是空的，不是一部也不是二部
                         */
                        logger.info("odersign 销售部门为空");
                    }

                } else {
                    List<UserDetail> userList = userService.selectUsers(null, null, role.getId(), null, null,1);
                    if (userList.isEmpty() || userList == null) {
                        logger.error("根据该roleId " + role.getId() + "找不到User");
                    } else {
                        //可能有多个负责人，比如研发部现在就两个经理，都通知
                        for (UserDetail toUser : userList) {
                            logger.info("订单继续签核，发给下1个签核人 " + toUser.getAccount());
                            commonService.sendSignInfoViWxMsg(toUser.getAccount(),
                                    machineOrder.getOrderNum(),
                                    "",
                                    createDateStr,
                                    contract.getMarketGroupName(),
                                    contract.getRecordUser(),
                                    msgInfo);
                        }
                    }
                }
            }
        }
    }

    /**
     *  在生成订单、改单、拆单 时，自动生成设计单 ---签核完成时--再改：技术部签核完成就生成设计单。
     *
     * @param machineOrder
     */
    public void createDesignDepInfo(MachineOrder machineOrder) {
        DesignDepInfo designDepInfo = new DesignDepInfo();
        designDepInfo.setDesignStatus(Constant.STR_DESIGN_STATUS_UNPLANNED);
        designDepInfo.setOrderNum(machineOrder.getOrderNum());
        if (machineOrder.getOrderNum() == null) {
            logger.error(machineOrder.getId() + "订单号为null");
        }

        designDepInfo.setSaleman(machineOrder.getSellman());
        try {
            List<Contract> list = contractService.getContractByOrderNumber(machineOrder.getOrderNum());
            if(list.size() >1){
                logger.error(machineOrder.getOrderNum() + ":该编号存在多个合同" + "自动创建设计单 失败");
                return;
            }
            Contract contract1 = list.get(0);
            if (contract1 != null) {
                designDepInfo.setGuestName(contract1.getCustomerName());
            }
            designDepInfo.setCountry(machineOrder.getCountry());
            designDepInfo.setMachineNum(machineOrder.getMachineNum());
            /**
             * 不仅仅是初始化状态，因为还有改单，拆单对应的状态
             */
//        designDepInfo.setOrderSignStatus(Constant.ORDER_INITIAL);
            designDepInfo.setOrderSignStatus(machineOrder.getStatus()); //注意这里是订单状态，不是订单签核状态
            designDepInfo.setOrderId(machineOrder.getId());
            designDepInfo.setCreatedDate(new Date());
            designDepInfo.setUpdatedDate(new Date());
            designDepInfoService.save(designDepInfo);
            logger.info("根据订单" + machineOrder.getOrderNum() + " 自动创建设计单");
        } catch (Exception e) {
            logger.error("自动创建设计单 失败 " + e.getMessage());
        }
    }

    /**
     * 订单状态变化时，要同步更新到对应的设计单。（不是新增，新增是：在生成订单、改单、拆单 时，自动生成设计单）
     * @param machineOrder
     */
    public void syncMachineOrderStatusInDesignDepInfo(MachineOrder machineOrder){
        ///设计单里的状态 也要改 -->改的地方多，统一放在定时器里去更新状态-->废弃，因为订单可能很多
        List<DesignDepInfoDetail> designDepInfoDetailList = designDepInfoService.selectDesignDepInfo(
                machineOrder.getOrderNum(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
 
        if(designDepInfoDetailList !=null && designDepInfoDetailList.size() !=0) {
            designDepInfoDetailList.get(0).setOrderSignStatus(machineOrder.getStatus());
            designDepInfoService.update(designDepInfoDetailList.get(0));
        } else {
            logger.warn("[syncMachineOrderStatusInDesignDepInfo]" + machineOrder.getOrderNum()
                    + ":根据该订单号找不到设计单，设计单还没生成，或是没有设计单之前的旧订单！");
        }
    }
	
    /**
     * 获取某个角色的订单签核意见
     */
    public String getSignContent(OrderSign orderSign, Integer roleID){
        String contentStr = orderSign.getSignContent();
        List<SignContentItem> orderSignContentList = JSON.parseArray(contentStr, SignContentItem.class);

        if(orderSignContentList == null || orderSignContentList.isEmpty()){
            logger.warn("ERROR: orderSignContentList 为空");
            return "ERROR: orderSignContentList 为空";
        }
        for (SignContentItem item : orderSignContentList) {
            if(item.getRoleId() == roleID) {
                logger.info("roleID: " + roleID + "的comment为：" + item.getComment());
                return item.getComment();
            }
        }
        return "ERROR: 找不到该RoleID对应的签核";
    }
}

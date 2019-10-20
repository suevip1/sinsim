package com.eservice.api.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.role.Role;
import com.eservice.api.model.task.Task;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.impl.*;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    Logger logger = Logger.getLogger(CommonService.class);

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
     * 目前 合同签核已经不存在了，即，只对订单进行签核。
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
    public String saveFile(String path, MultipartFile file,
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

    public String formatFileName(int type, String fileName,
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
                    //如果当前工序是质检完成状态或者跳过状态，需要检查其子节点是否可以开始
                    if (tr.getStatus().intValue() == Constant.TASK_QUALITY_DONE.intValue() || tr.getStatus().intValue() == Constant.TASK_SKIP.intValue()) {
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
                                                        if (Integer.valueOf(parentOfChildNode.getTaskStatus()) != Constant.TASK_QUALITY_DONE.intValue()
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
                    if (ndList.get(i).getTaskStatus() != null && Integer.parseInt(ndList.get(i).getTaskStatus()) != Constant.TASK_QUALITY_DONE.intValue()) {
                        isFinished = false;
                    }
                }
                //所有工序完成
                if (isFinished && tr.getStatus() == Constant.TASK_QUALITY_DONE.intValue()) {
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
}

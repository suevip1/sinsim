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
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.impl.*;
import org.apache.log4j.Logger;
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
     * @param path      保存文件的总路径
     * @param file      文件名称
     * @param machineID 机器的具体ID（保存装车单时，machineId可以为NULL）
     * @param orderNum  装车单对应的需求单编号（保存Abnormal/Quality 图片时，orderNum可为NULL）
     * @param type      文件类型 （不同类型文件名生成规则不同）
     *                  最终生成类似：machineID123_Abnormal_2018-01-10-11-15-56.png
     * @return 文件路径
     */
    public String saveFile(String path, MultipartFile file, @RequestParam(defaultValue = "") String machineID,  @RequestParam(defaultValue = "")String orderNum, int type) {
        String targetFileName = null;
        if (path != null) {
            if (!file.isEmpty()) {
                try {
                    //取后缀名
                    String fileName = file.getOriginalFilename();
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));

                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    //指定北京时间
                    formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    String dateStr = formatter.format(date);

                    String fileType;
                    if (Constant.ABNORMAL_IMAGE == type) {
                        fileType = "Abnormal";
                        targetFileName = path + machineID + "_" + orderNum+"_" + fileType + "_" + dateStr + suffixName;
                    } else if (Constant.QUALITY_IMAGE == type) {
                        fileType = "Quality";
                        targetFileName = path + machineID + "_" + orderNum+"_" + fileType + "_" + dateStr + suffixName;
                    } else if (Constant.LOADING_FILE == type) {
                        fileType = "LoadingFile";
                        /**
                         * 一个需求单对应一种机器，对应唯一装车单，不用加时间戳，新上传的覆盖旧的装车单，上传时间更新在装车单的update_time
                         */
                        targetFileName = path + machineID + "_" + orderNum+"_" + fileType  + suffixName;
                    } else {
                        fileType = "";//return targetFileName;//"UnknownFileTypeError";
                    }

                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(targetFileName)));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                    logger.info("====CommonService.saveFile(): success ========" + targetFileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return targetFileName;
    }

    public boolean updateTaskRecordRelatedStatus(TaskRecord tr) {
        if(tr == null || tr.getProcessRecordId() == null) {
            return false;
        }else {
            Integer prId = tr.getProcessRecordId();
            ProcessRecord pr = processRecordService.findById(prId);
            Machine machine = machineService.findById(pr.getMachineId());
            boolean isNeedUpdateMachine = false;
            if (pr != null) {
                String nodeData = pr.getNodeData();
                List<NodeDataModel> ndList = JSON.parseArray(nodeData, NodeDataModel.class);
                NodeDataModel ndItem = null;
                Integer index = -1;
                Boolean isFinished = true;
                for (int i = 0; i < ndList.size(); i++) {
                    if (Integer.parseInt(ndList.get(i).getKey()) == tr.getNodeKey()) {
                        index = i;
                    }
                    if (ndList.get(i).getTaskStatus() != null
                            && Integer.parseInt(ndList.get(i).getTaskStatus()) < Constant.TASK_QUALITY_DONE) {
                        isFinished = false;
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
                    //TODO：如果当前工序是质检完成状态，需要检查其子节点是否可以开始
                    if(tr.getStatus().intValue() == Constant.TASK_QUALITY_DONE.intValue()) {
                        List<LinkDataModel> linkDataList = JSON.parseArray(pr.getLinkData(), LinkDataModel.class);
                        for (LinkDataModel item: linkDataList) {
                            if(item.getFrom().equals(ndItem.getKey())) {
                                for (NodeDataModel childNode: ndList) {
                                    //先找到子节点
                                    if(childNode.getKey().equals(item.getTo())) {
                                        //找到子节点的所有父节点
                                        boolean allParentFinished = true;
                                        for (LinkDataModel parentOfChild: linkDataList) {
                                            if(!allParentFinished) {
                                                break;
                                            }
                                            if(parentOfChild.getTo().equals(childNode.getKey())) {
                                                for (NodeDataModel parentOfChildNode : ndList) {
                                                    if(!allParentFinished) {
                                                        break;
                                                    }
                                                    if(!parentOfChildNode.getTaskStatus().equals(Constant.TASK_QUALITY_DONE)) {
                                                        allParentFinished = false;
                                                    }
                                                }
                                            }
                                        }
                                        //子节点的所有父节点都已经完成，则更新子节点的状态
                                        if(allParentFinished) {
                                            String dateStr = Utils.getFormatStringDate(new Date(), "yyyy-MM-dd HH:mm:ss");
                                            childNode.setBeginTime(dateStr);
                                            childNode.setTaskStatus(Constant.TASK_INSTALL_WAITING.toString());
                                            //TODO:更新task record状态为“TASK_INSTALL_WAITING”
                                            List<TaskRecord> taskRecordList = taskRecordService.getTaskRecordData(null, prId);
                                            for (TaskRecord record: taskRecordList) {
                                                if(record.getNodeKey().equals(childNode.getKey())) {
                                                    record.setStatus(Constant.TASK_INSTALL_WAITING);
                                                    taskRecordService.update(record);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //所有工序完成
                if (isFinished && tr.getStatus() == Constant.TASK_QUALITY_DONE.intValue()) {
                    pr.setEndTime(new Date());
                    //安装完成
                    machine.setStatus(Constant.MACHINE_INSTALLED);
                    isNeedUpdateMachine=true;
                }
                pr.setNodeData(JSON.toJSONString(ndList));
                processRecordService.update(pr);

                if (machine.getStatus().equals(Constant.MACHINE_PLANING)) {
                    //安装中
                    machine.setStatus(Constant.MACHINE_INSTALLING);
                    isNeedUpdateMachine=true;
                }
                if(isNeedUpdateMachine) {
                    machine.setUpdateTime(new Date());
                    machineService.update(machine);
                }

            }
            return true;
        }
    }
}

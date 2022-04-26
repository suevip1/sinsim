package com.eservice.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.AbstractService;
import com.eservice.api.dao.MachineOrderMapper;
import com.eservice.api.model.contact_form.ContactFormDetail;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/24.
 */
@Service
@Transactional
public class MachineOrderServiceImpl extends AbstractService<MachineOrder> implements MachineOrderService {
    @Resource
    private MachineOrderMapper machineOrderMapper;

    @Resource
    private ContactFormServiceImpl contactFormService;

    @Resource
    private CommonService commonService;
    public MachineOrderDetail getOrderAllDetail(Integer id) {
        return machineOrderMapper.getOrderAllDetail(id);
    }

    public List<MachineOrderDetail> selectOrder(Integer id,
                                                Integer contract_id,
                                                String order_num,
                                                String contract_num,
                                                String status,
                                                String sellman,
                                                String customer,
                                                String marketGroupName,
                                                String query_start_time,
                                                String query_finish_time,
                                                String queryStartTimeSign,
                                                String queryFinishTimeSign,
                                                String machine_name,
                                                String oderSignCurrentStep,
                                                String searchDepartment,
                                                String queryStartTimePlanShipDate, //这个是查询 计划日期（生产部的回复交期）
                                                String queryFinishTimePlanShipDate,//这个是查询 计划日期（生产部的回复交期）
                                                String needleNum,   //针数
                                                String headNum,     //头数
                                                Boolean is_fuzzy) {

        String[] arr;
        if(status == null || status.equals("")){
            arr = null;
        } else {
            arr = status.split(",");
        }

        List<MachineOrderDetail> machineOrderDetailList;
        /**
         * Note: 对每个订单都查询一次该订单的联系单状态，然后赋值。
         *
         * 订单中【毛利率】取消单独录入，
         * 改为财务意见栏填写，格式为【毛利率：20%，偏低5%】样式，
         * 需要财务部统一按格式录入，财务报表中显示【毛利率】一栏
         * 所以从财务部成本核算员审核意见中，截取【毛利率】开始的字段
         */
        if (is_fuzzy) {
            machineOrderDetailList = machineOrderMapper.selectOrderFuzzy(
                    id,
                    contract_id,
                    order_num,
                    contract_num,
                    arr,
                    sellman,
                    customer,
                    marketGroupName,
                    query_start_time,
                    query_finish_time,
                    queryStartTimeSign,
                    queryFinishTimeSign,
                    machine_name,
                    oderSignCurrentStep,
                    searchDepartment,
                    queryStartTimePlanShipDate,
                    queryFinishTimePlanShipDate,
                    needleNum,
                    headNum
            );
            for (int i = 0; i < machineOrderDetailList.size(); i++) {
                machineOrderDetailList.get(i).setContactFormDetailList(getRelatedLxdByOrderNum(machineOrderDetailList.get(i).getOrderNum()));
                // 不应该在每次查询时都设置一次值。已改为在成本核算员签核时设置。
                /**
                 * 注意，毛利率不再在订单录入时填写，需要等成本核算员签核之后，才从签核信息张截取
                 */
//                machineOrderDetailList.get(i).setGrossProfit(getGrossProfitByOrderSignContent(machineOrderDetailList.get(i).getOrderSign()));//
            }
        } else {
            machineOrderDetailList = machineOrderMapper.selectOrder(
                    id,
                    contract_id,
                    order_num,
                    contract_num,
                    arr,
                    sellman,
                    customer,
                    marketGroupName,
                    query_start_time,
                    query_finish_time,
                    queryStartTimeSign,
                    queryFinishTimeSign,
                    machine_name,
                    oderSignCurrentStep,
                    searchDepartment,
                    queryStartTimePlanShipDate,
                    queryFinishTimePlanShipDate,
                    needleNum,
                    headNum );
            for (int i = 0; i < machineOrderDetailList.size(); i++) {
                machineOrderDetailList.get(i).setContactFormDetailList(getRelatedLxdByOrderNum(machineOrderDetailList.get(i).getOrderNum()));
//                machineOrderDetailList.get(i).setGrossProfit(getGrossProfitByOrderSignContent(machineOrderDetailList.get(i).getOrderSign()));//
            }
        }
        return machineOrderDetailList;
    }

    /**
     * 订单对应的所有联系单
     */
    public List<ContactFormDetail> getRelatedLxdByOrderNum(String orderNum) {
        List<ContactFormDetail> contactFormDetailsList = new ArrayList<>();
        //根据需求单查找对应联系单，会用用最原始的需求单号（除去括号），所以可查出所有历史的联系单
        if(orderNum != null) {
            if(orderNum.contains("(")) {
                orderNum = orderNum.substring(0, orderNum.indexOf("("));
            }
            contactFormDetailsList = contactFormService.selectContacts(
                    null,
                    null,
                    null,
                    orderNum,
                    null,
                    null,
                    null,// 可能有多个联系单，所以不能用 Constant.STR_LXD_CHECKING_FINISHED,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        }
        return contactFormDetailsList;

//        if(contactFormDetailsList !=null && contactFormDetailsList.size() !=0){
//            //有多个联系单，取最后一个为准
//            if(contactFormDetailsList.get(contactFormDetailsList.size() -1).getStatus().equals(Constant.STR_LXD_CHECKING_FINISHED)){
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
    }

    //从订单的成本核算员签核意见中，查找毛利率 ---以弃用，改为成本核算员在订单里填写
    public String getGrossProfitByOrderSignContent(OrderSign orderSign) {
        String grossProfitString = "";
        List<SignContentItem> signContentItemList = JSON.parseArray(orderSign.getSignContent(), SignContentItem.class);
        for (SignContentItem signContentItem : signContentItemList) {
            //格式为【毛利率：20%，偏低5%】样式，需要财务部统一按格式录入，财务报表中显示【毛利率】一栏
            if(signContentItem.getRoleId().equals(Constant.ROLE_ID_COST_ACCOUNTANT)){
                int start = 0;
                start = signContentItem.getComment().indexOf("【毛利率】");
                if(start == -1) {
                    start = signContentItem.getComment().indexOf("[毛利率]");
                }
                if(start == -1) {
                    start = signContentItem.getComment().indexOf("毛利率");
                }
                if(start != -1) {
                    grossProfitString = signContentItem.getComment().substring(start);
                }
            }
        }
        return grossProfitString;
    }

    public void saveAndGetID(MachineOrder machineOrder) {
        machineOrderMapper.saveAndGetID(machineOrder);
 
        /**
         * 在创建订单时就创建设计单，会有问题，比如创建订单时，改动订单号等，会带来要删除旧设计单并新建新的设计单等相关问题
         * 所以改为 订单审批完成时再创建设计单。 这样简化逻辑。
         */
//        commonService.createDesignDepInfo(machineOrder);
 
    }

    public MachineOrder searchOrderIdByOrderLoadingListId(Integer ollId) {
        return machineOrderMapper.searchOrderIdByOrderLoadingListId(ollId);
    }

    public Integer getUsedMachineTypeCount(Integer machineTypeId) {
        return machineOrderMapper.getUsedMachineTypeCount(machineTypeId).get(0);//SQL查询出来的结构是List<Integer>,第一个元素就是查询出来的count
    }

    public MachineOrder getMachineOrder(String orderNum) {
        return machineOrderMapper.getMachineOrder(orderNum);
    }

    public MachineOrder getMachineOrderByNameplate(String nameplate) {
        return machineOrderMapper.getMachineOrderByNameplate(nameplate);
    }

}

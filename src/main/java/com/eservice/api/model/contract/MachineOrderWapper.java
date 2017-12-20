package com.eservice.api.model.contract;

import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.order_sign.OrderSign;

/**
 * Class Description:用于处理从前端submit增加合同时候，需求单相关的数据
 *
 * @author Wilson Hu
 * @date 18/12/2017
 */
public class MachineOrderWapper {

    private MachineOrder machineOrder;

    private OrderDetail orderDetail;

    private OrderSign orderSign;

    public OrderSign getOrderSign(){
        return orderSign;
    }

    public void setOrderSign(OrderSign orderSign) {
        this.orderSign = orderSign;
    }

    public MachineOrder getMachineOrder() {
        return machineOrder;
    }

    public void setMachineOrder(MachineOrder machineOrder) {
        this.machineOrder = machineOrder;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }
}

package com.eservice.api.model.contract;

import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_detail.OrderDetail;

/**
 * Class Description:用于处理从前端submit增加合同时候，需求单相关的数据
 *
 * @author Wilson Hu
 * @date 18/12/2017
 */
public class MachineOrderWapper {

    private MachineOrder machineOrder;

    private OrderDetail orderDetail;

    private String orderSignData;

    public String getOrderSignData() {
        return orderSignData;
    }

    public void setOrderSignData(String orderSignData) {
        this.orderSignData = orderSignData;
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

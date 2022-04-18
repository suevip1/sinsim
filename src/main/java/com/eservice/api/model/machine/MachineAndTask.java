package com.eservice.api.model.machine;

import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.task_record.TaskRecord;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

///机器信息 + 工序信息，目前用在 订单结构表、安装进度页面。
public class MachineAndTask extends Machine {

    private MachineOrder machineOrder;

//    private OrderDetail orderDetail;

    //该机器对应的各个工序列表, 只需要任务名称、结束时间
    private List<TaskRecord> taskRecordList;

    private MachineType machineTypeWhole;///为了区别于Machine.MachineType

    public MachineOrder getMachineOrder() {
        return machineOrder;
    }

    public void setMachineOrder(MachineOrder machineOrder) {
        this.machineOrder = machineOrder;
    }

//    public OrderDetail getOrderDetail() {
//        return orderDetail;
//    }
//
//    public void setOrderDetail(OrderDetail orderDetail) {
//        this.orderDetail = orderDetail;
//    }

    public List<TaskRecord> getTaskRecordList() {
        return taskRecordList;
    }

    public void setTaskRecordList(List<TaskRecord> taskRecord) {
        this.taskRecordList = taskRecord;
    }

    public MachineType getmachineTypeWhole() {
        return machineTypeWhole;
    }

    public void setMachineTypeWhole(MachineType machineType) {
        this.machineTypeWhole = machineType;
    }


    ///订单结构表用到 剪线，跳跃方式
    private String electricTrim;
    private String  axleJump;

    public String getElectricTrim() {
        return electricTrim;
    }

    public void setElectricTrim(String electricTrim) {
        this.electricTrim = electricTrim;
    }

    public String getAxleJump() {
        return axleJump;
    }

    public void setAxleJump(String axleJump) {
        this.axleJump = axleJump;
    }

}

package com.eservice.api.model.quality_inspect_record;

import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;

import javax.persistence.*;
import java.util.Date;

@Table(name = "quality_inspect_record")
//暂未使用
public class QualityInspectRecordDetail extends QualityInspectRecord {
//    //质检对应工序的名称
//    String taskName;
//
//    public String getTaskName() {
//        return taskName;
//    }
//
//    public void setTaskName(String taskName) {
//        this.taskName = taskName;
//    }

    //下面这些信息 是为了给app端用，本来可以不需要，但是为了减少app端的改动先这样提供。
    private Machine machine;

    private MachineOrder machineOrder;

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public MachineOrder getMachineOrder() {
        return machineOrder;
    }

    public void setMachineOrder(MachineOrder machineOrder) {
        this.machineOrder = machineOrder;
    }
}
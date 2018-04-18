package com.eservice.api.model.task_record;

import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_loading_list.OrderLoadingList;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.task.Task;
import com.eservice.api.model.task_plan.TaskPlan;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task_record")
public class TaskRecordDetail extends TaskRecord{

    /**
    task_record相关的：
     process_Record 信息，
    machine信息,order_loading_list等
     */
    private ProcessRecord processRecord;
    private Machine machine;
    private MachineOrder machineOrder;

    /**
     *OrderLoadingList里包括装车单(联系单已经不需要了，在改单流程里体现了联系单）
     */
    private OrderLoadingList orderLoadingList;

    /**
     *安装组需要plan信息
     */
    private TaskPlan taskPlan;

    /**
     * Task信息
     */
    private Task task;

    /**
    获得Task_record的机器信息
     */
    public Machine getMachine(){ return  this.machine;}

    /**
     * 设置Task_record的机器信息
     */
    public void setMachine(Machine machine) { this.machine = machine; }

    public ProcessRecord getProcessRecord() {return  this.processRecord; }
    public void setProcessRecord(ProcessRecord processRecord) { this.processRecord = processRecord; }
    public MachineOrder getMachineOrder() { return machineOrder; }
    public void setMachineOrder(MachineOrder machineOrder) { this.machineOrder = machineOrder; }
    public OrderLoadingList getOrderLoadingList() { return this.orderLoadingList;}
    public void setOrderLoadingList(OrderLoadingList orderLoadingList) { this.orderLoadingList = orderLoadingList; }
    public TaskPlan getTaskPlan() {return this.taskPlan; }
    public void setTaskPlan(TaskPlan taskPlan) {this.taskPlan = taskPlan;}

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
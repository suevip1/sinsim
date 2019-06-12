package com.eservice.api.service.common;

public class TaskRcordPlanTime {
    private Integer id;
    private Integer planTimespan;
    private String taskName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanTimespan() {
        return planTimespan;
    }

    public void setPlanTimespan(Integer planTimespan) {
        this.planTimespan = planTimespan;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setPlanTaskName(String taskName) {
        this.taskName = taskName;
    }

}
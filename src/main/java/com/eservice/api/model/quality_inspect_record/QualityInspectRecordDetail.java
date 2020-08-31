package com.eservice.api.model.quality_inspect_record;

import javax.persistence.*;
import java.util.Date;

@Table(name = "quality_inspect_record")
public class QualityInspectRecordDetail extends QualityInspectRecord {
    //质检对应工序的名称
    String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
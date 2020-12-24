package com.eservice.api.model.quality_inspect;

import javax.persistence.*;

@Table(name = "quality_inspect")
public class QualityInspect {
    /**
     * 不同机型，全部检验条目都放进去，对于不需要的质检项，可以让质检员按下不需要质检（质检员自己能判断）。机型和质检条目无关。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 类型：过程检验、出厂检验 
     */
    @Column(name = "inspect_type")
    private String inspectType;

    /**
     * 该项质检的名称（唯一性）
     * QualityInspectRecord和QualityInspect如何联系： 共同的 inspect_name ，inspect_name是唯一的。
     */
    @Column(name = "inspect_name")
    private String inspectName;

    /**
     * 质检内容
     */
    @Column(name = "inspect_content")
    private String inspectContent;

    /**
     * 等级，“重要，一般” 只是标明出来。
     */
    private String level;

    /**
     * 阶段1，阶段2，阶段3，阶段4.。。
     */
    private String phase;

    /**
     * 质检对应的工序, 一个工序可以有多个检验条目， “无此检验条目” 可以一个个点，也可以按照工序一次点一堆条目。
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 是否启用
     */
    private Byte valid;

    /**
     * 获取不同机型，全部检验条目都放进去，对于不需要的质检项，可以让质检员按下不需要质检（质检员自己能判断）。机型和质检条目无关。
     *
     * @return id - 不同机型，全部检验条目都放进去，对于不需要的质检项，可以让质检员按下不需要质检（质检员自己能判断）。机型和质检条目无关。
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置不同机型，全部检验条目都放进去，对于不需要的质检项，可以让质检员按下不需要质检（质检员自己能判断）。机型和质检条目无关。
     *
     * @param id 不同机型，全部检验条目都放进去，对于不需要的质检项，可以让质检员按下不需要质检（质检员自己能判断）。机型和质检条目无关。
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取类型：过程检验、出厂检验 
     *
     * @return inspect_type - 类型：过程检验、出厂检验 
     */
    public String getInspectType() {
        return inspectType;
    }

    /**
     * 设置类型：过程检验、出厂检验 
     *
     * @param inspectType 类型：过程检验、出厂检验 
     */
    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }

    /**
     * 获取该项质检的名称（唯一性）
     *
     * @return inspect_name - 该项质检的名称（唯一性）
     */
    public String getInspectName() {
        return inspectName;
    }

    /**
     * 设置该项质检的名称（唯一性）
     *
     * @param inspectName 该项质检的名称（唯一性）
     */
    public void setInspectName(String inspectName) {
        this.inspectName = inspectName;
    }

    /**
     * 获取质检内容
     *
     * @return inspect_content - 质检内容
     */
    public String getInspectContent() {
        return inspectContent;
    }

    /**
     * 设置质检内容
     *
     * @param inspectContent 质检内容
     */
    public void setInspectContent(String inspectContent) {
        this.inspectContent = inspectContent;
    }

    /**
     * 获取等级，“重要，一般” 只是标明出来。
     *
     * @return level - 等级，“重要，一般” 只是标明出来。
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置等级，“重要，一般” 只是标明出来。
     *
     * @param level 等级，“重要，一般” 只是标明出来。
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取阶段1，阶段2，阶段3，阶段4.。。
     *
     * @return phase - 阶段1，阶段2，阶段3，阶段4.。。
     */
    public String getPhase() {
        return phase;
    }

    /**
     * 设置阶段1，阶段2，阶段3，阶段4.。。
     *
     * @param phase 阶段1，阶段2，阶段3，阶段4.。。
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * 获取质检对应的工序, 一个工序可以有多个检验条目， “无此检验条目” 可以一个个点，也可以按照工序一次点一堆条目。
     *
     * @return task_name - 质检对应的工序, 一个工序可以有多个检验条目， “无此检验条目” 可以一个个点，也可以按照工序一次点一堆条目。
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置质检对应的工序, 一个工序可以有多个检验条目， “无此检验条目” 可以一个个点，也可以按照工序一次点一堆条目。
     *
     * @param taskName 质检对应的工序, 一个工序可以有多个检验条目， “无此检验条目” 可以一个个点，也可以按照工序一次点一堆条目。
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取是否启用
     *
     * @return valid - 是否启用
     */
    public Byte getValid() {
        return valid;
    }

    /**
     * 设置是否启用
     *
     * @param valid 是否启用
     */
    public void setValid(Byte valid) {
        this.valid = valid;
    }
}
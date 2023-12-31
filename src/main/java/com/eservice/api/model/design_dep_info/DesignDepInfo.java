package com.eservice.api.model.design_dep_info;

import java.util.Date;
import javax.persistence.*;

@Table(name = "design_dep_info")
public class DesignDepInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 订单号， 可读性强，注意不是唯一，比如废弃的订单号
     */
    @Column(name = "order_num")
    private String orderNum;

    private String saleman;

    /**
     * 客户名称
     */
    @Column(name = "guest_name")
    private String guestName;

    private String country;

    /**
     * 机器数量
     */
    @Column(name = "machine_num")
    private Integer machineNum;

    /**
     * 备注信息,比如关于机型的改进点，等等。方便后续查询。
     */
    private String remark;

    /**
     * 订单的审核状态, 确切说的订单的状态。变量名称先不改了。
     */
    @Column(name = "order_sign_status")
    private Byte orderSignStatus;

    /**
     * 安排的设计人员
     */
    private String designer;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * 设备规格
     */
    @Column(name = "machine_spec")
    private String machineSpec;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 人工选择是否完成图纸装车单
     */
    @Column(name = "loading_file_done")
    private Boolean loadingFileDone;

    /**
     * 人工选择是否完成图纸
     */
    @Column(name = "drawing_file_done")
    private Boolean drawingFileDone;

    public Boolean getDrawingFileDone() {
        return drawingFileDone;
    }

    public void setDrawingFileDone(Boolean drawingFileDone) {
        this.drawingFileDone = drawingFileDone;
    }

    public String getLoadingFiles() {
        return loadingFiles;
    }

    public void setLoadingFiles(String loadingFiles) {
        this.loadingFiles = loadingFiles;
    }

    public String getDrawingFiles() {
        return drawingFiles;
    }

    public void setDrawingFiles(String drawingFiles) {
        this.drawingFiles = drawingFiles;
    }

    /**
     * 装车单 附件文件，空表示没有完成
     */
    @Column(name = "loading_files")
    private String loadingFiles;

    /**
     * 机架图纸、装车单 附件文件，空表示没有完成
     */
    @Column(name = "drawing_files")
    private String drawingFiles;
    /**
     * 点孔、方管 是否需要，0不需要1需要
     */
    @Column(name = "hole_tube_required")
    private Boolean holeTubeRequired;

    /**
     * 点孔 的附件文件，空表示没有完成
     */
    @Column(name = "hole_files")
    private String holeFiles;

    public String getHoleFiles() {
        return holeFiles;
    }

    public void setHoleFiles(String holeFiles) {
        this.holeFiles = holeFiles;
    }

    public String getTubeFiles() {
        return tubeFiles;
    }

    public void setTubeFiles(String tubeFiles) {
        this.tubeFiles = tubeFiles;
    }

    /**
     *  方管的附件文件，空表示没有完成
     */

    @Column(name = "tube_files")
    private String tubeFiles;
    /**
     * bom表文件,选是和否就好，不需要附件。
     */
    @Column(name = "bom_required")
    private Boolean bomRequired;

    /**
     * 是否需要罩盖，0不需要，1需要
     */
    @Column(name = "cover_required")
    private Boolean coverRequired;

    /**
     * 罩盖附件
     */
    @Column(name = "cover_file")
    private String coverFile;

    /**
     * 未计划  设计中   完成（全部完成）/改单
     */
    @Column(name = "design_status")
    private String designStatus;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return order_id
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取订单号， 可读性强，注意不是唯一，比如废弃的订单号
     *
     * @return order_num - 订单号， 可读性强，注意不是唯一，比如废弃的订单号
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * 设置订单号， 可读性强，注意不是唯一，比如废弃的订单号
     *
     * @param orderNum 订单号， 可读性强，注意不是唯一，比如废弃的订单号
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return saleman
     */
    public String getSaleman() {
        return saleman;
    }

    /**
     * @param saleman
     */
    public void setSaleman(String saleman) {
        this.saleman = saleman;
    }

    /**
     * 获取客户名称
     *
     * @return guest_name - 客户名称
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * 设置客户名称
     *
     * @param guestName 客户名称
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取机器数量
     *
     * @return machine_num - 机器数量
     */
    public Integer getMachineNum() {
        return machineNum;
    }

    /**
     * 设置机器数量
     *
     * @param machineNum 机器数量
     */
    public void setMachineNum(Integer machineNum) {
        this.machineNum = machineNum;
    }

    /**
     * 获取备注信息,比如关于机型的改进点，等等。方便后续查询。
     *
     * @return remark - 备注信息,比如关于机型的改进点，等等。方便后续查询。
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息,比如关于机型的改进点，等等。方便后续查询。
     *
     * @param remark 备注信息,比如关于机型的改进点，等等。方便后续查询。
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取订单的审核状态
     *
     * @return order_sign_status - 订单的审核状态
     */
    public Byte getOrderSignStatus() {
        return orderSignStatus;
    }

    /**
     * 设置订单的审核状态
     *
     * @param orderSignStatus 订单的审核状态
     */
    public void setOrderSignStatus(Byte orderSignStatus) {
        this.orderSignStatus = orderSignStatus;
    }

    /**
     * 获取安排的设计人员
     *
     * @return designer - 安排的设计人员
     */
    public String getDesigner() {
        return designer;
    }

    /**
     * 设置安排的设计人员
     *
     * @param designer 安排的设计人员
     */
    public void setDesigner(String designer) {
        this.designer = designer;
    }

    /**
     * @return created_date
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 获取设备规格
     *
     * @return machine_spec - 设备规格
     */
    public String getMachineSpec() {
        return machineSpec;
    }

    /**
     * 设置设备规格
     *
     * @param machineSpec 设备规格
     */
    public void setMachineSpec(String machineSpec) {
        this.machineSpec = machineSpec;
    }

    /**
     * 获取关键字
     *
     * @return keywords - 关键字
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * 设置关键字
     *
     * @param keywords 关键字
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * 获取人工选择是否完成图纸
     *
     * @return drawing_loading_done - 人工选择是否完成图纸
     */
    public Boolean getLoadingFileDone() {
        return loadingFileDone;
    }

    /**
     * 设置人工选择是否完成图纸
     *
     * @param loadingFileDone 人工选择是否完成图纸
     */
    public void setLoadingFileDone(Boolean loadingFileDone) {
        this.loadingFileDone = loadingFileDone;
    }

    /**
     * 获取点孔、方管 是否需要，0不需要1需要
     *
     * @return hole_tube_required - 点孔、方管 是否需要，0不需要1需要
     */
    public Boolean getHoleTubeRequired() {
        return holeTubeRequired;
    }

    /**
     * 设置点孔、方管 是否需要，0不需要1需要
     *
     * @param holeTubeRequired 点孔、方管 是否需要，0不需要1需要
     */
    public void setHoleTubeRequired(Boolean holeTubeRequired) {
        this.holeTubeRequired = holeTubeRequired;
    }

    /**
     * 获取bom表文件,选是和否就好，不需要附件。
     *
     * @return bom_required - bom表文件,选是和否就好，不需要附件。
     */
    public Boolean getBomRequired() {
        return bomRequired;
    }

    /**
     * 设置bom表文件,选是和否就好，不需要附件。
     *
     * @param bomRequired bom表文件,选是和否就好，不需要附件。
     */
    public void setBomRequired(Boolean bomRequired) {
        this.bomRequired = bomRequired;
    }

    /**
     * 获取是否需要罩盖，0不需要，1需要
     *
     * @return cover_required - 是否需要罩盖，0不需要，1需要
     */
    public Boolean getCoverRequired() {
        return coverRequired;
    }

    /**
     * 设置是否需要罩盖，0不需要，1需要
     *
     * @param coverRequired 是否需要罩盖，0不需要，1需要
     */
    public void setCoverRequired(Boolean coverRequired) {
        this.coverRequired = coverRequired;
    }

    /**
     * 获取罩盖附件
     *
     * @return cover_file - 罩盖附件
     */
    public String getCoverFile() {
        return coverFile;
    }

    /**
     * 设置罩盖附件
     *
     * @param coverFile 罩盖附件
     */
    public void setCoverFile(String coverFile) {
        this.coverFile = coverFile;
    }

    /**
     * 获取未计划  设计中   完成（全部完成）/改单
     *
     * @return design_status - 未计划  设计中   完成（全部完成）/改单
     */
    public String getDesignStatus() {
        return designStatus;
    }

    /**
     * 设置未计划  设计中   完成（全部完成）/改单
     *
     * @param designStatus 未计划  设计中   完成（全部完成）/改单
     */
    public void setDesignStatus(String designStatus) {
        this.designStatus = designStatus;
    }

    //点孔 是否完成
    @Column(name = "hole_done")
    private Boolean holeDone;


    public Boolean getHoleDone() {
        return holeDone;
    }

    public void setHoleDone(Boolean holeDone) {
        this.holeDone = holeDone;
    }

    public Boolean getTubeDone() {
        return tubeDone;
    }

    public void setTubeDone(Boolean tubeDone) {
        this.tubeDone = tubeDone;
    }

    //方管 是否完成
    @Column(name = "tube_done")
    private Boolean tubeDone;

    //BOM是否完成
    @Column(name = "bom_done")
    private Boolean bomDone;

    //是否完成
    @Column(name = "cover_done")
    private Boolean coverDone;

    public Boolean getBomDone() {
        return bomDone;
    }

    public void setBomDone(Boolean bomDone) {
        this.bomDone = bomDone;
    }

    public Boolean getCoverDone() {
        return coverDone;
    }

    public void setCoverDone(Boolean coverDone) {
        this.coverDone = coverDone;
    }

    public String getDrawingMan() {
        return drawingMan;
    }

    public void setDrawingMan(String drawingMan) {
        this.drawingMan = drawingMan;
    }

    public String getLoadingMan() {
        return loadingMan;
    }

    public void setLoadingMan(String loadingMan) {
        this.loadingMan = loadingMan;
    }

    //图纸  的更新人
    @Column(name = "drawing_man")
    private String drawingMan;

    // 装车单 的更新人
    @Column(name = "loading_man")
    private String loadingMan;

    //图纸  的更新时间
    @Column(name = "drawing_update_time")
    private Date drawingUpdateTime;

    // 装车单 的更新时间

    public Date getDrawingUpdateTime() {
        return drawingUpdateTime;
    }

    public void setDrawingUpdateTime(Date drawingUpdateTime) {
        this.drawingUpdateTime = drawingUpdateTime;
    }

    public Date getLoadingUpdateTime() {
        return loadingUpdateTime;
    }

    public void setLoadingUpdateTime(Date loadingUpdateTime) {
        this.loadingUpdateTime = loadingUpdateTime;
    }

    @Column(name = "loading_update_time")
    private Date loadingUpdateTime;

    //点孔 的更新人
    @Column(name = "hole_man")
    private String holeMan;


    // 方管 的更新人
    @Column(name = "tube_man")
    private String tubeMan;

    public String getHoleMan() {
        return holeMan;
    }

    public void setHoleMan(String holeMan) {
        this.holeMan = holeMan;
    }

    public String getTubeMan() {
        return tubeMan;
    }

    public void setTubeMan(String tubeMan) {
        this.tubeMan = tubeMan;
    }

    //点孔 的更新时间
    @Column(name = "hole_update_time")
    private Date holeUpdateTime;

    // 方管 的更新时间
    @Column(name = "tube_update_time")
    private Date tubeUpdateTime;

    //BOM 的更新人
    @Column(name = "bom_man")
    private String bomMan;

    //BOM 的更新时间
    @Column(name = "bom_update_time")
    private Date bomUpdateTime;

    //罩盖 的更新人
    @Column(name = "cover_man")
    private String coverMan;

    //罩盖 的更新时间
    @Column(name = "cover_update_time")
    private Date coverUpdateTime;

    public String getBomMan() {
        return bomMan;
    }

    public void setBomMan(String bomMan) {
        this.bomMan = bomMan;
    }

    public Date getBomUpdateTime() {
        return bomUpdateTime;
    }

    public void setBomUpdateTime(Date bomUpdateTime) {
        this.bomUpdateTime = bomUpdateTime;
    }

    public Date getHoleUpdateTime() {
        return holeUpdateTime;
    }

    public void setHoleUpdateTime(Date holeUpdateTime) {
        this.holeUpdateTime = holeUpdateTime;
    }

    public Date getTubeUpdateTime() {
        return tubeUpdateTime;
    }

    public void setTubeUpdateTime(Date tubeUpdateTime) {
        this.tubeUpdateTime = tubeUpdateTime;
    }

    public String getCoverMan() {
        return coverMan;
    }

    public void setCoverMan(String coverMan) {
        this.coverMan = coverMan;
    }

    public Date getCoverUpdateTime() {
        return coverUpdateTime;
    }

    public void setCoverUpdateTime(Date coverUpdateTime) {
        this.coverUpdateTime = coverUpdateTime;
    }
}
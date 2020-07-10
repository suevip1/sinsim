package com.eservice.api.model.machine_order;

import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormDetail;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.order_change_record.OrderChangeRecord;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.model.order_split_record.OrderSplitRecord;
import com.eservice.api.model.order_loading_list.OrderLoadingList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Table(name = "machine_order")
public class MachineOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录
     */
    @Column(name = "original_order_id")
    private Integer originalOrderId;

    /**
     * 合同对应的数据库ID
     */
    private Integer contractId;

    /**
     * Order详细信息，通过它来多表关联
     */
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    /**
     * 创建订单的ID， 只有销售员和销售主管可以创建订单
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 合同编号
     */
    @Column(name = "contract_num")
    private String contractNum;

    /**
     * 客户姓名
     */
    private String customer;

    /**
     * 国家
     */
    private String country;

    /**
     * 商标
     */
    private String brand;

//    /**
//     * 机器类型
//     */
//    @Column(name = "machine_type")
//    private Integer machineType;

    private  MachineType machineType;
    /**
     * 针数
     */
    @Column(name = "needle_num")
    private String needleNum;

    /**
     * 头数
     */
    @Column(name = "head_num")
    private String headNum;

    /**
     * 头距(由销售预填、销售更改)
     */
    @Column(name = "head_distance")
    private String headDistance;

    /**
     * X-行程
     */
    @Column(name = "x_distance")
    private String xDistance;

    /**
     * Y-行程
     */
    @Column(name = "y_distance")
    private String yDistance;

    /**
     * 包装方式
     */
    @Column(name = "package_method")
    private String packageMethod;

    /**
     * 绕线机配置
     */
    @Column(name = "wrap_machine")
    private String wrapMachine;
    /**
     * 绕线机置换
     */
    @Column(name = "wrap_machine_change")
    private String wrapMachineChange;

    @Column(name = "package_mark")
    private String packageMark;

    /**
     * 机器台数
     */
    @Column(name = "machine_num")
    private Byte machineNum;

    /**
     * 机器价格（不包括装置）
     */
    @Column(name = "machine_price")
    private String machinePrice;

    /**
     * 居间费用
     */
    @Column(name = "intermediary_price")
    private String intermediaryPrice;

    /**
     * 价格优惠
     */
    @Column(name = "discounts")
    private String discounts;

    /**
     * 需求单总价格优惠金额，内贸使用
     */
    @Column(name = "order_total_discounts")
    private String orderTotalDiscounts;


    /**
     * 机器装置信息，以JSON字符串的形式进行保存（name/number/price）
     */
    @Column(name = "equipment")
    private String equipment;

    @Column(name = "contract_ship_date")
    private Date contractShipDate;

    @Column(name = "plan_ship_date")
    private Date planShipDate;

    /**
     * 订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求
     */
    private String sellman;

    /**
     * 保修类型
     */
    private String maintainType;

    /**
     * 保修人员
     */
    @Column(name = "maintain_person")
    private String maintainPerson;


    /**
     * 表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可
     */
    private Byte status;

    /**
     * 订单创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 订单信息更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 订单结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

       /**
     * 合同支付方式
     */
    @Column(name="pay_method")
    private String payMethod;

    /**
     * 币种
     */
    @Column(name="currency_type")
    private String currencyType;

    private String nameplate;

    /**
     * 备注信息
     */
    private String mark;

    /*
    订单详情,和order_detail表关联
     */
    private OrderDetail orderDetail;


    /**
     * 订单签核情况
     */
    private OrderSign orderSign;

    /*
    *机器
    */
    private Machine machine;

    /**
     * 订单是否有效
     */
    @Column(name = "valid")
    private Integer valid;

    public String getNameplate()
    {
        return nameplate;
    }

    public void setNameplate(String nameplate)
    {
        this.nameplate = nameplate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * 需求单对应的联系单
     */
    private List<ContactFormDetail> contactFormDetailList = new ArrayList<>();

    public List<ContactFormDetail> getContactFormDetailList() {
        return contactFormDetailList;
    }

    public void setContactFormDetailList(List<ContactFormDetail> contactFormDetailList) {
        this.contactFormDetailList = contactFormDetailList;
    }

    /**
     * 改单记录
     */
    private OrderChangeRecord orderChangeRecord;

    public OrderChangeRecord getOrderChangeRecord() {
        return orderChangeRecord;
    }

    public void setOrderChangeRecord(OrderChangeRecord orderChangeRecord) {
        this.orderChangeRecord = orderChangeRecord;
    }

    /**
     * 拆单记录
     */
    private OrderSplitRecord orderSplitRecord;

    public OrderSplitRecord getOrderSplitRecord() {
        return orderSplitRecord;
    }

    public void setOrderSplitRecord(OrderSplitRecord orderSplitRecord) {
        this.orderSplitRecord = orderSplitRecord;
    }


    /**
     * 装车单、联系单
     */
    private OrderLoadingList orderLoadingList;

    public OrderLoadingList getOrderLoadingList() {
        return orderLoadingList;
    }

    public void setOrderLoadingList(OrderLoadingList orderLoadingList) {
        this.orderLoadingList = orderLoadingList;
    }

    //    /*
//    机器类型，和machine_type关联
//     */
//    private MachineType machineType;


    public String getWrapMachine() {
        return wrapMachine;
    }

    public void setWrapMachine(String wrapMachine) {
        this.wrapMachine = wrapMachine;
    }

    public String getWrapMachineChange() {
        return wrapMachineChange;
    }

    public void setWrapMachineChange(String wrapMachineChange) {
        this.wrapMachineChange = wrapMachineChange;
    }

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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public OrderSign getOrderSign() {
        return orderSign;
    }

    public void setOrderSign(OrderSign orderSign) {
        this.orderSign = orderSign;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * 获取在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录
     *
     * @return original_order_id - 在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录
     */
    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    /**
     * 设置在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录
     *
     * @param originalOrderId 在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录
     */
    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    /**
     * 获取Order详细信息，通过它来多表关联
     *
     * @return order_detail_id - Order详细信息，通过它来多表关联
     */
    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    /**
     * 设置Order详细信息，通过它来多表关联
     *
     * @param orderDetailId Order详细信息，通过它来多表关联
     */
    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     * 获取创建订单的ID， 只有销售员和销售主管可以创建订单
     *
     * @return create_user_id - 创建订单的ID， 只有销售员和销售主管可以创建订单
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建订单的ID， 只有销售员和销售主管可以创建订单
     *
     * @param createUserId 创建订单的ID， 只有销售员和销售主管可以创建订单
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取合同编号
     *
     * @return contract_num - 合同编号
     */
    public String getContractNum() {
        return contractNum;
    }

    /**
     * 设置合同编号
     *
     * @param contractNum 合同编号
     */
    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    /**
     * 获取客户姓名
     *
     * @return customer - 客户姓名
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * 设置客户姓名
     *
     * @param customer 客户姓名
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * 获取国家
     *
     * @return country - 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置国家
     *
     * @param country 国家
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取商标
     *
     * @return brand - 商标
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 设置商标
     *
     * @param brand 商标
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

//    /**
//     * 获取机器类型
//     *
//     * @return machine_type - 机器类型
//     */
//    public Integer getMachineType() {
//        return machineType;
//    }

//    /**
//     * 设置机器类型
//     *
//     * @param machineType 机器类型
//     */
//    public void setMachineType(Integer machineType) {
//        this.machineType = machineType;
//    }

    /**
     * 获取针数
     *
     * @return needle_num - 针数
     */
    public String getNeedleNum() {
        return needleNum;
    }

    /**
     * 设置针数
     *
     * @param needleNum 针数
     */
    public void setNeedleNum(String needleNum) {
        this.needleNum = needleNum;
    }

    /**
     * 获取头数
     *
     * @return head_num - 头数
     */
    public String getHeadNum() {
        return headNum;
    }

    /**
     * 设置头数
     *
     * @param headNum 头数
     */
    public void setHeadNum(String headNum) {
        this.headNum = headNum;
    }

    /**
     * 获取头距(由销售预填、销售更改)
     *
     * @return head_distance - 头距(由销售预填、销售更改)
     */
    public String getHeadDistance() {
        return headDistance;
    }

    /**
     * 设置头距(由销售预填、销售更改)
     *
     * @param headDistance 头距(由销售预填、销售更改)
     */
    public void setHeadDistance(String headDistance) {
        this.headDistance = headDistance;
    }

    /**
     * 获取X-行程
     *
     * @return x_distance - X-行程
     */
    public String getxDistance() {
        return xDistance;
    }

    /**
     * 设置X-行程
     *
     * @param xDistance X-行程
     */
    public void setxDistance(String xDistance) {
        this.xDistance = xDistance;
    }

    /**
     * 获取Y-行程
     *
     * @return y_distance - Y-行程
     */
    public String getyDistance() {
        return yDistance;
    }

    /**
     * 设置Y-行程
     *
     * @param yDistance Y-行程
     */
    public void setyDistance(String yDistance) {
        this.yDistance = yDistance;
    }

    /**
     * 获取包装方式
     *
     * @return package_method - 包装方式
     */
    public String getPackageMethod() {
        return packageMethod;
    }

    /**
     * 设置包装方式
     *
     * @param packageMethod 包装方式
     */
    public void setPackageMethod(String packageMethod) {
        this.packageMethod = packageMethod;
    }


    /*
    ///获取包装备注
    */
    public String getPackageMark() {
        return packageMark;
    }

    //设置包装备注
    public void setPackageMark(String packageMark) {
        this.packageMark = packageMark;
    }

    /**
     * 获取机器台数
     *
     * @return machine_num - 机器台数
     */
    public Byte getMachineNum() {
        return machineNum;
    }

    /**
     * 设置机器台数
     *
     * @param machineNum 机器台数
     */
    public void setMachineNum(Byte machineNum) {
        this.machineNum = machineNum;
    }

    /**
     * 获取机器价格（不包括装置）
     *
     * @return machine_price - 机器价格（不包括装置）
     */
    public String getMachinePrice() {
        return machinePrice;
    }

    /**
     * 设置机器价格（不包括装置）
     *
     * @param machinePrice 机器价格（不包括装置）
     */
    public void setMachinePrice(String machinePrice) {
        this.machinePrice = machinePrice;
    }

    public String getIntermediaryPrice() {
        return intermediaryPrice;
    }

    public void setIntermediaryPrice(String intermediaryPrice) {
        this.intermediaryPrice = intermediaryPrice;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getOrderTotalDiscounts() {
        return orderTotalDiscounts;
    }

    public void setOrderTotalDiscounts(String orderTotalDiscounts) {
        this.orderTotalDiscounts = orderTotalDiscounts;
    }

    /**
     * @return contract_ship_date
     */
    public Date getContractShipDate() {
        return contractShipDate;
    }

    /**
     * @param contractShipDate
     */
    public void setContractShipDate(Date contractShipDate) {
        this.contractShipDate = contractShipDate;
    }

    /**
     * @return plan_ship_date
     */
    public Date getPlanShipDate() {
        return planShipDate;
    }

    /**
     * @param planShipDate
     */
    public void setPlanShipDate(Date planShipDate) {
        this.planShipDate = planShipDate;
    }

    /**
     * 获取订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求
     *
     * @return sellman - 订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求
     */
    public String getSellman() {
        return sellman;
    }

    /**
     * 设置订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求
     *
     * @param sellman 订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求
     */
    public void setSellman(String sellman) {
        this.sellman = sellman;
    }

    /**
     * 获取表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可
     *
     * @return status - 表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可
     *
     * @param status 表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取订单创建时间
     *
     * @return create_time - 订单创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置订单创建时间
     *
     * @param createTime 订单创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取订单信息更新时间
     *
     * @return update_time - 订单信息更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置订单信息更新时间
     *
     * @param updateTime 订单信息更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取订单结束时间
     *
     * @return end_time - 订单结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置订单结束时间
     *
     * @param endTime 订单结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取备注信息
     *
     * @return mark - 备注信息
     */
    public String getMark() {
        return mark;
    }

    /**
     * 设置备注信息
     *
     * @param mark 备注信息
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * 该订单的机器全部加急；1表示加急,0表示取消加急(曾经加急后来取消了)，默认为null
     */
    private Boolean allUrgent;

    public Boolean getAllUrgent() {
        return allUrgent;
    }

    public void setAllUrgent(Boolean allUrgent) {
        this.allUrgent = allUrgent;
    }

    /**
    获取订单详情
     */
    public OrderDetail getOrderDetail(){
        return this.orderDetail;
    }
    /**
    设置订单详情
     */
    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail=orderDetail;
    }

    /**
    获取机器类型
     */
    public MachineType getMachineType() {  return this.machineType; }

    /**
    *设置机器类型
     */
    public void setMachineType(MachineType machineType) {this.machineType=machineType;}

    public String getMaintainType() {
        return maintainType;
    }

    public void setMaintainType(String maintainType) {
        this.maintainType = maintainType;
    }

    public String getMaintainPerson() {
        return maintainPerson;
    }

    public void setMaintainPerson(String maintainPerson) {
        this.maintainPerson = maintainPerson;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    //该订单的对应的联系单状态
    private boolean lxdPassed;

    public boolean isLxdPassed() {
        return lxdPassed;
    }

    public void setLxdPassed(boolean lxdPassed) {
        this.lxdPassed = lxdPassed;
    }

    @Column(name = "electric_trim")
    private String electricTrim;

    @Column(name = "electric_pc")
    private String electricPc;

    public String getElectricTrim() {
        return electricTrim;
    }

    public void setElectricTrim(String electricTrim) {
        this.electricTrim = electricTrim;
    }

    public String getElectricPc() {
        return electricPc;
    }

    public void setElectricPc(String electricPc) {
        this.electricPc = electricPc;
    }
}
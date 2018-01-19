package com.eservice.api.service.common;

/**
 * Class Description:常量定义类
 *
 * @author Wilson Hu
 * @date 22/12/2017
 */
public class Constant {
    /**
     * 签核流程结束（返回值）
     */
    public static String SIGN_FINISHED = "签核完成";

    /**
     * 签核各个步骤
     */
    public static Integer SALES_DEP_STEP= 1;
    public static Integer TECH_DEP_STEP = 2;
    public static Integer PMC_STEP= 3;
    public static Integer COST_ACCOUNT_STEP= 4;
    public static Integer FINANCIAL_DEP_RULE_STEP= 5;
    public static Integer GENERAL_MANAGER_STEP= 6;
    public static Integer FINANCIAL_DEP_DEPOSIT_STEP= 7;

    /**
     * 签核结果：“0”-->初始化；“1”-->接受； “2”-->驳回
     */
    public static final Integer SIGN_INITIAL = 0;
    public static final Integer SIGN_APPROVE = 1;
    public static final Integer SIGN_REJECT = 2;

    /**
     * 合同状态， 对应于contract表中的status
     */
    /**
     * 合同创建完成，未提交审核
     */
    public static final Byte CONTRACT_INITIAL = 0;
    /**
     * 审核中
     */
    public static final Byte CONTRACT_CHECKING = 1;
    /**
     * 审核完成
     */
    public static final Byte CONTRACT_CHECKING_FINISHED = 2;
    /**
     * 已改单
     */
    public static final Byte CONTRACT_CHANGED = 3;
    /**
     * 已拆单
     */
    public static final Byte CONTRACT_SPLITED = 4;
    /**
     * 已驳回，驳回状态下填单员可以再一次编辑合同和需求单内容，进行再一次审核
     */
    public static final Byte CONTRACT_REJECTED= 5;
    /**
     * 已取消
     */
    public static final Byte CONTRACT_CANCELED = 6;


    /**
     * 需求单状态，对应于machine_order中的status
     */
    /**
     * 需求单创建完成，未提交审核
     */
    public static final Byte ORDER_INITIAL = 0;
    /**
     * 审核中
     */
    public static final Byte ORDER_CHECKING = 1;
    /**
     * 审核完成
     */
    public static final Byte ORDER_CHECKING_FINISHED = 2;
    /**
     * 已改单
     */
    public static final Byte ORDER_CHANGED = 3;
    /**
     * 已拆单
     */
    public static final Byte ORDER_SPLITED = 4;
    /**
     * 已驳回，驳回状态下填单员可以再一次编辑合同和需求单内容，进行再一次审核
     */
    public static final Byte ORDER_REJECTED= 5;
    /**
     * 已取消
     */
    public static final Byte ORDER_CANCELED = 6;


    /**
     * 机器状态:
     * "0" --> 签核完成,机器创建的初始化状态
     * "1" --> 已加入计划生产
     * "2" --> 安装中
     * "3" --> 安装完成
     * "4" --> 改单
     * "5" --> 拆单
     * "6" --> 取消
     */
    public static final Byte MACHINE_INITIAL = 0;
    public static final Byte MACHINE_PLANING = 1;
    public static final Byte MACHINE_INSTALLING = 2;
    public static final Byte MACHINE_INSTALLED = 3;
    public static final Byte MACHINE_CHANGED = 4;
    public static final Byte MACHINE_SPLITED = 5;
    public static final Byte MACHINE_CANCELED = 6;

    /**
     * 保存文件的类型
     * "0" --> 异常
     * "1" --> 质检
     * "2" --> 装车单
     */
    public static final int ABNORMAL_IMAGE = 0;
    public static final int QUALITY_IMAGE = 1;
    public static final int LOADING_FILE = 2;
}

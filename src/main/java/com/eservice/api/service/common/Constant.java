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
     * 需求单状态
     */
    //合同创建完成，未提交审核
    public static final int ORDER_INITIAL = 0;
    //审核中
    public static final int ORDER_CHECKING = 1;
    //审核完成
    public static final int ORDER_CHECKING_FINISHED = 2;
    //已改单
    public static final int ORDER_CHANGED = 3;
    //已拆单
    public static final int ORDER_SPLITED = 4;
    //已驳回
    public static final int ORDER_REJECTED= 5;
    //已取消
    public static final int ORDER_CANCELED = 6;

    /**
     * 机器状态
     */
    //签核完成,机器创建的初始化状态
    public static final int MACHINE_INITIAL = 0;
    //已加入计划生产
    public static final int MACHINE_PLANING = 1;
    //安装中
    public static final int MACHINE_INSTALLING = 2;
    //安装完成
    public static final int MACHINE_INSTALLED = 3;
    //改单
    public static final int MACHINE_CHANGED = 4;
    //拆单
    public static final int MACHINE_SPLITED = 5;
    //取消
    public static final int MACHINE_CANCELED = 6;

    /**
     * 保存文件的类型
     */
    public static final int ABNORMAL_IMAGE = 0;//异常、
    public static final int QUALITY_IMAGE = 1;// 质检、
    public static final int LOADING_FILE = 2;// 装车单
}

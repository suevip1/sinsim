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
    public static final Byte CONTRACT_REJECTED = 5;
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
    public static final Byte ORDER_REJECTED = 5;
    /**
     * 已取消
     */
    public static final Byte ORDER_CANCELED = 6;


    /**
     * 机器状态:
     * "0" --> 签核完成,机器创建的初始化状态
     * "1" --> 配置完成，待计划
     * "2" --> 已加入计划生产
     * "3" --> 安装中
     * "4" --> 安装完成
     * "5" --> 改单
     * "6" --> 拆单
     * "7" --> 取消
     */
    public static final Byte MACHINE_INITIAL = 0;
    public static final Byte MACHINE_CONFIGURED = 1;
    public static final Byte MACHINE_PLANING = 2;
    public static final Byte MACHINE_INSTALLING = 3;
    public static final Byte MACHINE_INSTALLED = 4;
    public static final Byte MACHINE_CHANGED = 5;
    public static final Byte MACHINE_SPLITED = 6;
    public static final Byte MACHINE_CANCELED = 7;

    /**
     * Task(工序)安装状态
     * "0" --> 初始化状态
     * "1" --> 已计划 （没轮到安装）
	 * "2" --> 待安装
     * "3" --> 开始安装
     * "4" --> 安装完成
     * "5" --> 质检中
     * "6" --> 质检完成
     * "7" --> 安装异常
     * "8" --> 质检异常
	 * "9" --> 跳过
     */
    public static final Byte TASK_INITIAL = 0;
    public static final Byte TASK_PLANED = 1;
    public static final Byte TASK_INSTALL_WAITING = 2;
    public static final Byte TASK_INSTALLING = 3;
    public static final Byte TASK_INSTALLED = 4;
    public static final Byte TASK_QUALITY_DOING = 5;
    public static final Byte TASK_QUALITY_DONE = 6;
    public static final Byte TASK_INSTALL_ABNORMAL = 7;
    public static final Byte TASK_QUALITY_ABNORMAL = 8;
    public static final Byte TASK_SKIP = 9;

    public static final String STR_TASK_INITIAL = "初始化";
    public static final String STR_TASK_PLANED = "已计划";
    public static final String STR_TASK_INSTALL_WAITING = "待安装";
    public static final String STR_TASK_INSTALLING = "开始安装";
    public static final String STR_TASK_INSTALLED = "安装完成";
    public static final String STR_TASK_QUALITY_DOING = "质检中";
    public static final String STR_TASK_QUALITY_DONE = "质检完成";
    public static final String STR_TASK_INSTALL_ABNORMAL = "安装异常";
    public static final String STR_TASK_QUALITY_ABNORMAL = "质检异常";
    public static final String STR_TASK_SKIP = "已跳过";



    /**
     * 保存文件的类型
     * "0" --> 异常
     * "1" --> 质检
     * "2" --> 装车单
     */
    public static final int ABNORMAL_IMAGE = 0;
    public static final int QUALITY_IMAGE = 1;
    public static final int LOADING_FILE = 2;

    /**
     * 计划的方式："1"==>日计划， "2"==>弹性计划， "3" ==>自计划；未计划，但是在时间允许下安装组长自行安装的
     */
    public static final Byte DAILY_PLAN = 1;
    public static final Byte FLEX_PLAN = 2;
    public static final Byte SELF_PLAN = 3;

    /**
     * MQTT Topic： 后台通知前台App,机器进行了改单、拆单、取消操作
     */
    public static final String S2C_MACHINE_STATUS_CHANGE = "/s2c/machine_status_change";

    /**
     * MQTT Topic: 质检完成,后台通知前台App,下一道工序可以安装
     */
    public static final String S2C_TASK_INSTALL = "/s2c/task_install/";

    /**
     * MQTT Topic: 安装完成,后台通知前台App,可以质检
     */
    public static final String S2C_TASK_QUALITY = "/s2c/task_quality/";

    /**
     * MQTT Topic: 某工序的安装异常处理结束后，通知该工序安装组长
     */
    public static final String S2C_INSTALL_ABNORMAL_RESOLVE = "/s2c/install_abnormal_resolve/";

    /**
     * MQTT Topic: 某工序的质检异常处理结束后，通知该工序质检员
     */
    public static final String S2C_QUALITY_ABNORMAL_RESOLVE = "/s2c/quality_abnormal_resolve/";

    /**
     * 发生安装异常时，通知生产部管理员
     */
	public static final String S2C_INSTALL_ABNORMAL = "/s2c/install_abnormal/";

    /**
     * 发生质检异常时，通知生产部管理员
     */
    public static final String S2C_QUALITY_ABNORMAL = "/s2c/quality_abnormal/";

    /**
     * 发生安装异常时，通知对应质检员
     */
    public static final String S2C_INSTALL_ABNORMAL_TO_QUALITY = "/s2c/install_abnormal/quality/";
}

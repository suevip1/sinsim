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
    public static String SING_STEP_SALES_MANAGER = "销售部经理"; //销售部特殊

    /**
     * 签核结果：“0”-->初始化；“1”-->接受； “2”-->驳回; 3: 不需要签核，比如 内贸订单，不需要外贸总监签核
     */
    public static final Integer SIGN_INITIAL = 0;
    public static final Integer SIGN_APPROVE = 1;
    public static final Integer SIGN_REJECT = 2;
    public static final Integer SIGN_NO_NEED = 3;

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
     * 拆单 不必再审核
     */
    public static final Byte ORDER_SPLIT_FINISHED = 7;
    /**
     * 改单 不必再审核
     */
    public static final Byte ORDER_CHANGE_FINISHED = 8;

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
     * "8" --> 安装中且包含跳过工序的机器（包含跳过工序的机器不会被允许设置为完成，也即一定是在安装中）
     * "9" --> 已发货
     *  是否加急不作为一个状态，而作为一个属性。(订单加急，则该订单所有机器的加急属性也设为加急)
     */
    public static final Byte MACHINE_INITIAL = 0;
    public static final Byte MACHINE_CONFIGURED = 1;
    public static final Byte MACHINE_PLANING = 2;
    public static final Byte MACHINE_INSTALLING = 3;
    public static final Byte MACHINE_INSTALLED = 4;
    public static final Byte MACHINE_CHANGED = 5;
    public static final Byte MACHINE_SPLITED = 6;
    public static final Byte MACHINE_CANCELED = 7;
    public static final Byte MACHINE_INSTALLING_INCLUDE_SKIP_TASK = 8;
    public static final Byte MACHINE_SHIPPED = 9;

    public static final String STR_MACHINE_INITIAL = "初始化";
    public static final String STR_MACHINE_CONFIGURED = "待计划";
    public static final String STR_MACHINE_PLANING = "已计划";
    public static final String STR_MACHINE_INSTALLING = "安装中";
    public static final String STR_MACHINE_INSTALLED = "安装完成";
    public static final String STR_MACHINE_CHANGED = "改单";
    public static final String STR_MACHINE_SPLITED = "拆单";
    public static final String STR_MACHINE_CANCELED = "取消";
    public static final String STR_MACHINE_INSTALLING_INCLUDE_SKIP_TASK = "安装中且包含跳过工序的机器";
    public static final String STR_MACHINE_SHIPPED = "已发货";

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
     *
     *  3期新质检
     * "10" --> 未开始质检
     * "11" --> 无此检验条目
     * "12" --> 质检不合格
     * "13" --> 质检合格
     * "14" --> 未检
     */
    public static final Byte TASK_INITIAL = 0;
    public static final Byte TASK_PLANED = 1;
    public static final Byte TASK_INSTALL_WAITING = 2;
    public static final Byte TASK_INSTALLING = 3;
    public static final Byte TASK_INSTALLED = 4;
    public static final Byte TASK_QUALITY_DOING = 5;
    // 质检不再基于工序，这个可以拿掉 TODO
    public static final Byte TASK_QUALITY_DONE = 6;
    public static final Byte TASK_INSTALL_ABNORMAL = 7;
    public static final Byte TASK_QUALITY_ABNORMAL = 8;
    public static final Byte TASK_SKIP = 9;


    public static final Byte TASK_QUALITY_INSPECT_NOT_STARTED = 10;
    public static final Byte TASK_QUALITY_INSPECT_NO_SUCH_ITEM = 11;
    public static final Byte TASK_QUALITY_INSPECT_NG = 12;
    public static final Byte TASK_QUALITY_INSPECT_OK = 13;
    public static final Byte TASK_QUALITY_INSPECT_HAVE_NOT_CHECKED = 14;

    public static final String STR_TASK_INITIAL = "初始化";
    public static final String STR_TASK_PLANED = "已计划";
    public static final String STR_TASK_INSTALL_WAITING = "待安装";
    public static final String STR_TASK_INSTALLING = "开始安装";
    public static final String STR_TASK_INSTALLED = "安装完成"; ///
    public static final String STR_TASK_QUALITY_DOING = "质检中";
    public static final String STR_TASK_QUALITY_DONE = "质检完成";
    public static final String STR_TASK_INSTALL_ABNORMAL = "安装异常";
    public static final String STR_TASK_QUALITY_ABNORMAL = "质检异常";
    public static final String STR_TASK_SKIP = "已跳过";

    /**
     * 二期的质检和原先不同，在定了 机器位置，则就要发消息给质检人员。
     * 所以质检的状态，不和工序的状态共用。
     * 包括：
     * 该条质检未开始，无此检验条目，质检合格，质检不合格。
     * （没有质检中,因为是对一条质检内容而言,对机器有多条质检，才有质检中这样的状态）,
     * “未检”按钮（因为无法检查，比如安装好了盖住了无法打开检验）,
     */
    public static final String STR_QUALITY_INSPECT_NOT_STARTED = "待质检";
    public static final String STR_QUALITY_INSPECT_NOT_NEED = "无此检验条目";
    public static final String STR_QUALITY_INSPECT_FAILURE = "质检不合格";
    public static final String STR_QUALITY_INSPECT_SUCCESS = "质检合格";
    public static final String STR_QUALITY_INSPECT_HAVE_NOT_CHECKED = "未检";

    /**
     * 保存文件的类型
     * "0" --> 异常
     * "1" --> 质检
     * "2" --> 装车单
     * "3" --> 联系单创建者上传的附件
     * "4" --> 签核过程中上传的附件
     * "5" --> 设计单的附件
     * "6" --> 优化单的附件
     */
    public static final int ABNORMAL_IMAGE = 0;
    public static final int QUALITY_IMAGE = 1;
    public static final int LOADING_FILE = 2;
//    public static final int LXD_ATTACHED_FILE = 3;
    public static final int LXD_ATTACHED_FILE_BY_CREATER = 3;
    public static final int LXD_ATTACHED_FILE_DURING_SIGN = 4;
    public static final int DESIGN_ATTACHED_FILE = 5;
    public static final int OPTIMIZE_ATTACHED_FILE = 6;

    //设计页面，文件类型
    public static final String STR_DESIGN_UPLOAD_FILE_TYPE_DRAWING = "图纸";
    public static final String STR_DESIGN_UPLOAD_FILE_TYPE_LOADINGFILE = "装车单";
    public static final String STR_DESIGN_UPLOAD_FILE_TYPE_HOLE = "点孔";
    public static final String STR_DESIGN_UPLOAD_FILE_TYPE_TUBE= "方管";
    public static final String STR_DESIGN_UPLOAD_FILE_TYPE_BOM= "BOM";
    public static final String STR_DESIGN_UPLOAD_FILE_TYPE_COVER= "罩盖";

    public static final String STRING_LXD_ATTACHED_FILE_BY_CREATER = "联系单附件";
    public static final String STRING_LXD_ATTACHED_FILE_DURING_SIGN = "联系单签核过程附件"; 
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

    /**
     * MQTT Topic: 排产计划，发送给对应的安装组长
     */
    public static final String S2C_INSTALL_PLAN = "/s2c/install_plan/";

    /**
     *  提醒
     *  比如：前道工序A忘了扫码，后面的工序B直接去扫码，此时需要提醒A去扫码完成。
     */
    public static final String S2C_TASK_REMIND = "/s2c/task_remind/";

    /**
     * MQTT Topic： 后台通知前台App：该机器已经设置了区域，表示开始安装了，要过去质检了（虽然还未完成安装）。
     * 注意： 现在有两次质检通知：
     * 1. S2C_MACHINE_QUALITY_INSPECT
     * 2. S2C_TASK_QUALITY  -- 目前没有在用
     */
    public static final String S2C_MACHINE_QUALITY_INSPECT = "/s2c/machine_quality_inspect/";

    public enum ValidEnum {
        INVALID(0), VALID(1);
        private Integer value;

        private ValidEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }
    }

    public static final Byte INVALID = 0;
    public static final Byte VALID = 1;

    //联系单类型： 变更联系单，工作联系单
    public static final String STR_LXD_TYPE_BIANGENG = "变更";
    public static final String STR_LXD_TYPE_WORK = "工作";

    /**
     * 联系单状态， 对应于contact_form表中的status
     * 0 - 初始
     * 1 - 审核中
     * 2 - 审核完成
     * 3 - 审核被拒
     * 4 - 审核取消
     */
    public static final Byte LXD_INITIAL = 0;
    public static final Byte LXD_CHECKING = 1;
    public static final Byte LXD_CHECKING_FINISHED = 2;
    public static final Byte LXD_REJECTED  = 3;
    public static final Byte LXD_CANCELED = 4;

    public static final String STR_LXD_INITIAL = "联系单初始化";
    public static final String STR_LXD_CHECKING = "联系单审核中";
    public static final String STR_LXD_CHECKING_FINISHED = "联系单审核完成";
    public static final String STR_LXD_REJECTED  = "联系单审核被拒";
    public static final String STR_LXD_CANCELED = "联系单取消";

    public static final String STR_INSTALL_TYPE_WHOLE = "总装";
    public static final String STR_INSTALL_TYPE_PARTS = "部装";

    public static final String STR_FULFILL_STATUS_INITIAL = "落实单初始化";
    public static final String STR_FULFILL_STATUS_UN_ASSIGN = "未指定落实人员";
    public static final String STR_FULFILL_STATUS_FULFILLING = "落实进行中";
    public static final String STR_FULFILL_STATUS_DONE = "落实完成";

    //设计单 状态
    public static final String STR_DESIGN_STATUS_UNPLANNED = "待计划"; //未指定设计人员
    public static final String STR_DESIGN_STATUS_PLANNED = "已计划";
    public static final String STR_DESIGN_STATUS_DRAWING_DONE = "图纸完成";
    public static final String STR_DESIGN_STATUS_LOADING_DONE = "装车单完成";
    public static final String STR_DESIGN_STATUS_HOLE_DONE = "点孔完成";
    public static final String STR_DESIGN_STATUS_TUBE_DONE = "方管完成";
    public static final String STR_DESIGN_STATUS_COVER_DONE = "罩盖完成";
    public static final String STR_DESIGN_STATUS_BOM_DONE = "BOM完成";
    public static final String STR_DESIGN_STATUS_ALL_DONE = "全部完成";

    public static final Integer ROLE_ID_ADMIN = 1;
    public static final Integer ROLE_ID_PRODUCTION_ADMIN = 2;
    public static final Integer ROLE_ID_INSTALL_GROUP_LEADER = 3;
    public static final Integer ROLE_ID_PRODUCTION_MANAGER = 4;
    public static final Integer ROLE_ID_STAFF = 5;
    public static final Integer ROLE_ID_GM = 6;
    public static final Integer ROLE_ID_SALES_MANAGER = 7;
    public static final Integer ROLE_ID_FOREIGN_DIRECTOR = 30; //外贸总监
    public static final Integer ROLE_ID_TECH_MANAGER = 8;
    public static final Integer ROLE_ID_SALES = 9;
    public static final Integer ROLE_ID_TECHNICIAN = 10;
    public static final Integer ROLE_ID_QUALITY_INSPECTOR = 11;
    public static final Integer ROLE_ID_PMC = 12;
    public static final Integer ROLE_ID_COST_ACCOUNTANT = 13;//成本核算员

    public static final String STR_DEPARTMENT_DOMESTIC = "内贸部";
    public static final String STR_DEPARTMENT_FOREIGN_FUZZY = "外贸";//一部二部，统一到外贸部经理曹建挺签核，然后到总监骆晓军签核

    public static final String STR_MSG_PUSH_IS_START_TO_SIGN = "开始签核";
    public static final String STR_MSG_PUSH_IS_TURN_TO_SIGN = "轮到签核";
    public static final String STR_MSG_PUSH_SIGN_REFUESED = "签核被拒绝";
}

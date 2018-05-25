package com.eservice.api.model.order_detail;

import javax.persistence.*;

@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 特种：毛巾（色数）
     */
    @Column(name = "special_towel_color")
    private String specialTowelColor;

    /**
     * 特种： D轴
     */
    @Column(name = "special_towel_daxle")
    private String specialTowelDaxle;

    /**
     * 特种： H轴
     */
    @Column(name = "special_towel_haxle")
    private String specialTowelHaxle;

    /**
     * 特种：主电机
     */
    @Column(name = "special_towel_motor")
    private String specialTowelMotor;

    /**
     * 特种：特种：盘带头
     */
    @Column(name = "special_taping_head")
    private String specialTapingHead;

    /**
     * 特种：毛巾机针
     */
    @Column(name = "special_towel_needle")
    private String specialTowelNeedle;

    /**
     * 电气： 电脑
     */
    @Column(name = "electric_pc")
    private String electricPc;

    /**
     * 电气： 语言
     */
    @Column(name = "electric_language")
    private String electricLanguage;

    /**
     * 电气：主电机
     */
    @Column(name = "electric_motor")
    private String electricMotor;

    /**
     * 电气：X,Y电机
     */
    @Column(name = "electric_motor_xy")
    private String electricMotorXy;

    /**
     * 电气：剪线方式
     */
    @Column(name = "electric_trim")
    private String electricTrim;

    /**
     * 电气： 电源
     */
    @Column(name = "electric_power")
    private String electricPower;

    /**
     * 电气： 按钮开关
     */
    @Column(name = "electric_switch")
    private String electricSwitch;


    /**
     * 换色方式
     */
    @Column(name = "color_change_mode")
    private String colorChangeMode;
    /**
     * 电气： 加油系统
     */
    @Column(name = "electric_oil")
    private String electricOil;

    /**
     * 上下轴：j夹线器
     */
    @Column(name = "axle_split")
    private String axleSplit;

    /**
     * 上下轴：面板
     */
    @Column(name = "axle_panel")
    private String axlePanel;

    /**
     * 上下轴：机针
     */
    @Column(name = "axle_needle")
    private String axleNeedle;

    /**
     * 上下轴：机针类型
     */
    @Column(name = "axle_needle_type")
    private String axleNeedleType;

    /**
     * 上下轴：机头中导轨
     */
    @Column(name = "axle_rail")
    private String axleRail;

    /**
     * 上下轴：底检方式
     */
    @Column(name = "axle_down_check")
    private String axleDownCheck;

    /**
     * 上下轴：旋梭
     */
    @Column(name = "axle_hook")
    private String axleHook;

    /**
     * 上下轴：跳跃方式
     */
    @Column(name = "axle_jump")
    private String axleJump;

    /**
     * 上下轴：面线夹持
     */
    @Column(name = "axle_upper_thread")
    private String axleUpperThread;

    /**
     * 上下轴：附加装置（该部分由销售预填，技术进行确认或更改）
     */
    @Column(name = "axle_addition")
    private String axleAddition;

    /**
     * 机架台板：机架颜色 
     */
    @Column(name = "framework_color")
    private String frameworkColor;

    /**
     * 机架台板：台板
     */
    @Column(name = "framework_platen")
    private String frameworkPlaten;

    /**
     * 机架台板：台板颜色
     */
    @Column(name = "framework_platen_color")
    private String frameworkPlatenColor;

    /**
     * 机架台板：吊环
     */
    @Column(name = "framework_ring")
    private String frameworkRing;

    /**
     * 机架台板：电脑托架
     */
    @Column(name = "framework_bracket")
    private String frameworkBracket;

    /**
     * 机架台板：急停装置
     */
    @Column(name = "framework_stop")
    private String frameworkStop;

    /**
     * 机架台板：日光灯
     */
    @Column(name = "framework_light")
    private String frameworkLight;

    /**
     * 驱动：类型
     */
    @Column(name = "driver_type")
    private String driverType;

    /**
     * 驱动：方式
     */
    @Column(name = "driver_method")
    private String driverMethod;

    /**
     * 驱动：绷架孔
     */
    @Column(name = "driver_reel_hole")
    private String driverReelHole;

    /**
     * 驱动：横档数量
     */
    @Column(name = "driver_horizon_num")
    private Byte driverHorizonNum;

    /**
     * 驱动：直档数量
     */
    @Column(name = "driver_vertical_num")
    private Byte driverVerticalNum;

    /**
     * 驱动：绷架
     */
    @Column(name = "driver_reel")
    private String driverReel;

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
     * 获取特种：毛巾（色数）
     *
     * @return special_towel_color - 特种：毛巾（色数）
     */
    public String getSpecialTowelColor() {
        return specialTowelColor;
    }

    /**
     * 设置特种：毛巾（色数）
     *
     * @param specialTowelColor 特种：毛巾（色数）
     */
    public void setSpecialTowelColor(String specialTowelColor) {
        this.specialTowelColor = specialTowelColor;
    }

    /**
     * 获取特种： D轴
     *
     * @return special_towel_daxle - 特种： D轴
     */
    public String getSpecialTowelDaxle() {
        return specialTowelDaxle;
    }

    /**
     * 设置特种： D轴
     *
     * @param specialTowelDaxle 特种： D轴
     */
    public void setSpecialTowelDaxle(String specialTowelDaxle) {
        this.specialTowelDaxle = specialTowelDaxle;
    }

    /**
     * 获取特种： H轴
     *
     * @return special_towel_haxle - 特种： H轴
     */
    public String getSpecialTowelHaxle() {
        return specialTowelHaxle;
    }

    /**
     * 设置特种： H轴
     *
     * @param specialTowelHaxle 特种： H轴
     */
    public void setSpecialTowelHaxle(String specialTowelHaxle) {
        this.specialTowelHaxle = specialTowelHaxle;
    }

    /**
     * 获取特种：主电机
     *
     * @return special_towel_motor - 特种：主电机
     */
    public String getSpecialTowelMotor() {
        return specialTowelMotor;
    }

    /**
     * 设置特种：主电机
     *
     * @param specialTowelMotor 特种：主电机
     */
    public void setSpecialTowelMotor(String specialTowelMotor) {
        this.specialTowelMotor = specialTowelMotor;
    }

    /**
     * 获取特种：特种：盘带头
     *
     * @return special_taping_head - 特种：特种：盘带头
     */
    public String getSpecialTapingHead() {
        return specialTapingHead;
    }

    /**
     * 设置特种：特种：盘带头
     *
     * @param specialTapingHead 特种：特种：盘带头
     */
    public void setSpecialTapingHead(String specialTapingHead) {
        this.specialTapingHead = specialTapingHead;
    }

    /**
     * 获取特种：毛巾机针
     *
     * @return special_towel_needle - 特种：毛巾机针
     */
    public String getSpecialTowelNeedle() {
        return specialTowelNeedle;
    }

    /**
     * 设置特种：毛巾机针
     *
     * @param specialTowelNeedle 特种：毛巾机针
     */
    public void setSpecialTowelNeedle(String specialTowelNeedle) {
        this.specialTowelNeedle = specialTowelNeedle;
    }

    /**
     * 获取电气： 电脑
     *
     * @return electric_pc - 电气： 电脑
     */
    public String getElectricPc() {
        return electricPc;
    }

    /**
     * 设置电气： 电脑
     *
     * @param electricPc 电气： 电脑
     */
    public void setElectricPc(String electricPc) {
        this.electricPc = electricPc;
    }

    public String getElectricLanguage() {
        return electricLanguage;
    }

    public void setElectricLanguage(String electricLanguage) {
        this.electricLanguage = electricLanguage;
    }

    /**
     * 获取电气：主电机
     *
     * @return electric_motor - 电气：主电机
     */
    public String getElectricMotor() {
        return electricMotor;
    }

    /**
     * 设置电气：主电机
     *
     * @param electricMotor 电气：主电机
     */
    public void setElectricMotor(String electricMotor) {
        this.electricMotor = electricMotor;
    }

    /**
     * 获取电气：X,Y电机
     *
     * @return electric_motor_xy - 电气：X,Y电机
     */
    public String getElectricMotorXy() {
        return electricMotorXy;
    }

    /**
     * 设置电气：X,Y电机
     *
     * @param electricMotorXy 电气：X,Y电机
     */
    public void setElectricMotorXy(String electricMotorXy) {
        this.electricMotorXy = electricMotorXy;
    }

    /**
     * 获取电气：剪线方式
     *
     * @return electric_trim - 电气：剪线方式
     */
    public String getElectricTrim() {
        return electricTrim;
    }

    /**
     * 设置电气：剪线方式
     *
     * @param electricTrim 电气：剪线方式
     */
    public void setElectricTrim(String electricTrim) {
        this.electricTrim = electricTrim;
    }

    /**
     * 获取电气： 电源
     *
     * @return electric_power - 电气： 电源
     */
    public String getElectricPower() {
        return electricPower;
    }

    /**
     * 设置电气： 电源
     *
     * @param electricPower 电气： 电源
     */
    public void setElectricPower(String electricPower) {
        this.electricPower = electricPower;
    }

    /**
     * 获取电气： 按钮开关
     *
     * @return electric_switch - 电气： 按钮开关
     */
    public String getElectricSwitch() {
        return electricSwitch;
    }

    /**
     * 设置电气： 按钮开关
     *
     * @param electricSwitch 电气： 按钮开关
     */
    public void setElectricSwitch(String electricSwitch) {
        this.electricSwitch = electricSwitch;
    }



    public String getColorChangeMode() {
        return colorChangeMode;
    }

    public void setColorChangeMode(String colorChangeMode) {
        this.colorChangeMode = colorChangeMode;
    }


    /**
     * 获取电气： 加油系统
     *
     * @return electric_oil - 电气： 加油系统
     */
    public String getElectricOil() {
        return electricOil;
    }

    /**
     * 设置电气： 加油系统
     *
     * @param electricOil 电气： 加油系统
     */
    public void setElectricOil(String electricOil) {
        this.electricOil = electricOil;
    }

    /**
     * 获取上下轴：j夹线器
     *
     * @return axle_split - 上下轴：j夹线器
     */
    public String getAxleSplit() {
        return axleSplit;
    }

    /**
     * 设置上下轴：j夹线器
     *
     * @param axleSplit 上下轴：j夹线器
     */
    public void setAxleSplit(String axleSplit) {
        this.axleSplit = axleSplit;
    }

    /**
     * 获取上下轴：面板
     *
     * @return axle_panel - 上下轴：面板
     */
    public String getAxlePanel() {
        return axlePanel;
    }

    /**
     * 设置上下轴：面板
     *
     * @param axlePanel 上下轴：面板
     */
    public void setAxlePanel(String axlePanel) {
        this.axlePanel = axlePanel;
    }

    /**
     * 获取上下轴：机针
     *
     * @return axle_needle - 上下轴：机针
     */
    public String getAxleNeedle() {
        return axleNeedle;
    }

    /**
     * 设置上下轴：机针
     *
     * @param axleNeedle 上下轴：机针
     */
    public void setAxleNeedle(String axleNeedle) {
        this.axleNeedle = axleNeedle;
    }

    /**
     * 获取上下轴：机针
     *
     * @return axleNeedleType - 上下轴：机针
     */
    public String getAxleNeedleType() {
        return axleNeedleType;
    }

    /**
     * 设置上下轴：机针
     *
     * @param axleNeedleType 上下轴：机针
     */
    public void setAxleNeedleType(String axleNeedleType) {
        this.axleNeedleType = axleNeedleType;
    }


    /**
     * 获取上下轴：机头中导轨
     *
     * @return axle_rail - 上下轴：机头中导轨
     */
    public String getAxleRail() {
        return axleRail;
    }

    /**
     * 设置上下轴：机头中导轨
     *
     * @param axleRail 上下轴：机头中导轨
     */
    public void setAxleRail(String axleRail) {
        this.axleRail = axleRail;
    }

    /**
     * 获取上下轴：底检方式
     *
     * @return axle_down_check - 上下轴：底检方式
     */
    public String getAxleDownCheck() {
        return axleDownCheck;
    }

    /**
     * 设置上下轴：底检方式
     *
     * @param axleDownCheck 上下轴：底检方式
     */
    public void setAxleDownCheck(String axleDownCheck) {
        this.axleDownCheck = axleDownCheck;
    }

    /**
     * 获取上下轴：旋梭
     *
     * @return axle_hook - 上下轴：旋梭
     */
    public String getAxleHook() {
        return axleHook;
    }

    /**
     * 设置上下轴：旋梭
     *
     * @param axleHook 上下轴：旋梭
     */
    public void setAxleHook(String axleHook) {
        this.axleHook = axleHook;
    }

    /**
     * 获取上下轴：跳跃方式
     *
     * @return axle_jump - 上下轴：跳跃方式
     */
    public String getAxleJump() {
        return axleJump;
    }

    /**
     * 设置上下轴：跳跃方式
     *
     * @param axleJump 上下轴：跳跃方式
     */
    public void setAxleJump(String axleJump) {
        this.axleJump = axleJump;
    }

    /**
     * 获取上下轴：面线夹持
     *
     * @return axle_upper_thread - 上下轴：面线夹持
     */
    public String getAxleUpperThread() {
        return axleUpperThread;
    }

    /**
     * 设置上下轴：面线夹持
     *
     * @param axleUpperThread 上下轴：面线夹持
     */
    public void setAxleUpperThread(String axleUpperThread) {
        this.axleUpperThread = axleUpperThread;
    }

    /**
     * 获取上下轴：附加装置（该部分由销售预填，技术进行确认或更改）
     *
     * @return axle_addition - 上下轴：附加装置（该部分由销售预填，技术进行确认或更改）
     */
    public String getAxleAddition() {
        return axleAddition;
    }

    /**
     * 设置上下轴：附加装置（该部分由销售预填，技术进行确认或更改）
     *
     * @param axleAddition 上下轴：附加装置（该部分由销售预填，技术进行确认或更改）
     */
    public void setAxleAddition(String axleAddition) {
        this.axleAddition = axleAddition;
    }

    /**
     * 获取机架台板：机架颜色 
     *
     * @return framework_color - 机架台板：机架颜色 
     */
    public String getFrameworkColor() {
        return frameworkColor;
    }

    /**
     * 设置机架台板：机架颜色 
     *
     * @param frameworkColor 机架台板：机架颜色 
     */
    public void setFrameworkColor(String frameworkColor) {
        this.frameworkColor = frameworkColor;
    }

    /**
     * 获取机架台板：台板
     *
     * @return framework_platen - 机架台板：台板
     */
    public String getFrameworkPlaten() {
        return frameworkPlaten;
    }

    /**
     * 设置机架台板：台板
     *
     * @param frameworkPlaten 机架台板：台板
     */
    public void setFrameworkPlaten(String frameworkPlaten) {
        this.frameworkPlaten = frameworkPlaten;
    }

    /**
     * 获取机架台板：台板颜色
     *
     * @return framework_platen_color - 机架台板：台板颜色
     */
    public String getFrameworkPlatenColor() {
        return frameworkPlatenColor;
    }

    /**
     * 设置机架台板：台板颜色
     *
     * @param frameworkPlatenColor 机架台板：台板颜色
     */
    public void setFrameworkPlatenColor(String frameworkPlatenColor) {
        this.frameworkPlatenColor = frameworkPlatenColor;
    }

    /**
     * 获取机架台板：吊环
     *
     * @return framework_ring - 机架台板：吊环
     */
    public String getFrameworkRing() {
        return frameworkRing;
    }

    /**
     * 设置机架台板：吊环
     *
     * @param frameworkRing 机架台板：吊环
     */
    public void setFrameworkRing(String frameworkRing) {
        this.frameworkRing = frameworkRing;
    }

    /**
     * 获取机架台板：电脑托架
     *
     * @return framework_bracket - 机架台板：电脑托架
     */
    public String getFrameworkBracket() {
        return frameworkBracket;
    }

    /**
     * 设置机架台板：电脑托架
     *
     * @param frameworkBracket 机架台板：电脑托架
     */
    public void setFrameworkBracket(String frameworkBracket) {
        this.frameworkBracket = frameworkBracket;
    }

    /**
     * 获取机架台板：急停装置
     *
     * @return framework_stop - 机架台板：急停装置
     */
    public String getFrameworkStop() {
        return frameworkStop;
    }

    /**
     * 设置机架台板：急停装置
     *
     * @param frameworkStop 机架台板：急停装置
     */
    public void setFrameworkStop(String frameworkStop) {
        this.frameworkStop = frameworkStop;
    }

    /**
     * 获取机架台板：日光灯
     *
     * @return framework_light - 机架台板：日光灯
     */
    public String getFrameworkLight() {
        return frameworkLight;
    }

    /**
     * 设置机架台板：日光灯
     *
     * @param frameworkLight 机架台板：日光灯
     */
    public void setFrameworkLight(String frameworkLight) {
        this.frameworkLight = frameworkLight;
    }

    /**
     * 获取驱动：类型
     *
     * @return driver_type - 驱动：类型
     */
    public String getDriverType() {
        return driverType;
    }

    /**
     * 设置驱动：类型
     *
     * @param driverType 驱动：类型
     */
    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    /**
     * 获取驱动：方式
     *
     * @return driver_method - 驱动：方式
     */
    public String getDriverMethod() {
        return driverMethod;
    }

    /**
     * 设置驱动：方式
     *
     * @param driverMethod 驱动：方式
     */
    public void setDriverMethod(String driverMethod) {
        this.driverMethod = driverMethod;
    }

    /**
     * 获取驱动：绷架孔
     *
     * @return driver_reel_hole - 驱动：绷架孔
     */
    public String getDriverReelHole() {
        return driverReelHole;
    }

    /**
     * 设置驱动：绷架孔
     *
     * @param driverReelHole 驱动：绷架孔
     */
    public void setDriverReelHole(String driverReelHole) {
        this.driverReelHole = driverReelHole;
    }

    /**
     * 获取驱动：横档数量
     *
     * @return driver_horizon_num - 驱动：横档数量
     */
    public Byte getDriverHorizonNum() {
        return driverHorizonNum;
    }

    /**
     * 设置驱动：横档数量
     *
     * @param driverHorizonNum 驱动：横档数量
     */
    public void setDriverHorizonNum(Byte driverHorizonNum) {
        this.driverHorizonNum = driverHorizonNum;
    }

    /**
     * 获取驱动：直档数量
     *
     * @return driver_vertical_num - 驱动：直档数量
     */
    public Byte getDriverVerticalNum() {
        return driverVerticalNum;
    }

    /**
     * 设置驱动：直档数量
     *
     * @param driverVerticalNum 驱动：直档数量
     */
    public void setDriverVerticalNum(Byte driverVerticalNum) {
        this.driverVerticalNum = driverVerticalNum;
    }

    /**
     * 获取驱动：绷架
     *
     * @return driver_reel - 驱动：绷架
     */
    public String getDriverReel() {
        return driverReel;
    }

    /**
     * 设置驱动：绷架
     *
     * @param driverReel 驱动：绷架
     */
    public void setDriverReel(String driverReel) {
        this.driverReel = driverReel;
    }
}
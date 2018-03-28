package com.eservice.api.model.dashboard;

/**
 * Class Description:  Dashboard页面开头部分使用到机器安装中、改单中、拆单中、异常工序数等数据集合
 *
 * @author Wilson Hu
 * @date 3/28/2018
 */
public class Statistic {
    /**
     * 安装中机器，包括改、拆单状态机器，取消的机器不包含在内
     */
    private Integer installingMachineNum;

    /**
     * 改单中机器数
     */
    private Integer changeMachineNum;

    /**
     * 拆单中机器数
     */
    private Integer splitMachineNum;

    /**
     * 异常工序数
     */
    private Integer abnormalTaskNum;

    public Integer getInstallingMachineNum() {
        return installingMachineNum;
    }

    public void setInstallingMachineNum(Integer installingMachineNum) {
        this.installingMachineNum = installingMachineNum;
    }

    public Integer getChangeMachineNum() {
        return changeMachineNum;
    }

    public void setChangeMachineNum(Integer changeMachineNum) {
        this.changeMachineNum = changeMachineNum;
    }

    public Integer getSplitMachineNum() {
        return splitMachineNum;
    }

    public void setSplitMachineNum(Integer splitMachineNum) {
        this.splitMachineNum = splitMachineNum;
    }

    public Integer getAbnormalTaskNum() {
        return abnormalTaskNum;
    }

    public void setAbnormalTaskNum(Integer abnormalTaskNum) {
        this.abnormalTaskNum = abnormalTaskNum;
    }
}

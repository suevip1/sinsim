package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.install_plan_actual.InstallPlanActual;
import com.eservice.api.model.install_plan_actual.InstallPlanActualDetails;
import com.eservice.api.service.InstallPlanActualService;
import com.eservice.api.service.InstallPlanService;
import com.eservice.api.service.impl.InstallPlanActualServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/19.
*/
@RestController
@RequestMapping("/install/plan/actual")
public class InstallPlanActualController {
    @Resource
    private InstallPlanActualServiceImpl installPlanActualService;

    @Resource
    private InstallPlanService installPlanService;

    private Logger logger = Logger.getLogger(InstallPlanActualController.class);

    @PostMapping("/add")
    public Result add(String installPlanActual) {
        InstallPlanActual installPlanActual1 = JSON.parseObject(installPlanActual, InstallPlanActual.class);
        if (installPlanActual1 != null) {
            if (installPlanActual1.getInstallPlanId() == null) {
                return ResultGenerator.genFailResult("错误，InstallPlanId为null！");
            } else if (installPlanService.findById(installPlanActual1.getInstallPlanId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallPlanId " + installPlanActual1.getInstallPlanId() + " 找不到对应的plan ！");
            } else {
                /**
                 * 处理多次提交同一个排产，
                 * 如果该计划是第一次提交，则新增该次提交。
                 * 如果该计划的完成情况已经存在了，则在原先存在的基础上增加新完成的数据。（并检查合理性）
                 */
                InstallPlanActual installPlanActualExist = installPlanActualService.findById(installPlanActual1.getInstallPlanId());
                if( installPlanActualExist == null){
                    installPlanActual1.setCreateDate(new Date());
                    installPlanActualService.save(installPlanActual1);
                    logger.info("新增 installPlanActual1 " + installPlanActual1.getId());
                } else {
                    installPlanActualExist.setUpdateDate( new Date());

                    int newHeadCountDone = installPlanActualExist.getHeadCountDone() + installPlanActual1.getHeadCountDone();
                    installPlanActualExist.setHeadCountDone(newHeadCountDone);

                    installPlanActualExist.setCmtFeedback(installPlanActualExist.getCmtFeedback() + ";" + installPlanActual1.getCmtFeedback());

                    //
                    if(installPlanActualExist.getPcWireNum() != null && installPlanActual1.getPcWireNum() != null) {
                        String newPcWireNum = String.valueOf(Integer.valueOf(installPlanActualExist.getPcWireNum()) + Integer.valueOf(installPlanActual1.getPcWireNum()));
                        installPlanActualExist.setPcWireNum(newPcWireNum);
                    }

                    if(installPlanActualExist.getKouxianNum() != null && installPlanActual1.getKouxianNum() != null) {
                        String newKouxianNum = String.valueOf(Integer.valueOf(installPlanActualExist.getKouxianNum()) + Integer.valueOf(installPlanActual1.getKouxianNum()));
                        installPlanActualExist.setKouxianNum(newKouxianNum);
                    }

                    if(installPlanActualExist.getLightWireNum() != null && installPlanActual1.getLightWireNum() != null) {
                        String newLightWireNum = String.valueOf(Integer.valueOf(installPlanActualExist.getLightWireNum()) + Integer.valueOf(installPlanActual1.getLightWireNum()));
                        installPlanActualExist.setLightWireNum(newLightWireNum);
                    }

                    if(installPlanActualExist.getWarnSignalNum() != null && installPlanActual1.getWarnSignalNum() != null) {
                        String newWarnSignalNum = String.valueOf(Integer.valueOf(installPlanActualExist.getWarnSignalNum()) + Integer.valueOf(installPlanActual1.getWarnSignalNum()));
                        installPlanActualExist.setWarnSignalNum(newWarnSignalNum);
                    }

                    if(installPlanActualExist.getDeviceSignalNum() != null && installPlanActual1.getDeviceSignalNum() != null) {
                        String newDeviceSignalNum = String.valueOf(Integer.valueOf(installPlanActualExist.getDeviceSignalNum()) + Integer.valueOf(installPlanActual1.getDeviceSignalNum()));
                        installPlanActualExist.setDeviceSignalNum(newDeviceSignalNum);
                    }

                    if(installPlanActualExist.getWarnPowerNum() != null && installPlanActual1.getWarnPowerNum() != null) {
                        String newWarnPowerNum = String.valueOf(Integer.valueOf(installPlanActualExist.getWarnPowerNum()) + Integer.valueOf(installPlanActual1.getWarnPowerNum()));
                        installPlanActualExist.setWarnSignalNum(newWarnPowerNum);
                    }

                    if(installPlanActualExist.getDevicePowerNum() != null && installPlanActual1.getDevicePowerNum() != null) {
                        String newDevicePowerNum = String.valueOf(Integer.valueOf(installPlanActualExist.getDevicePowerNum()) + Integer.valueOf(installPlanActual1.getDevicePowerNum()));
                        installPlanActualExist.setDevicePowerNum(newDevicePowerNum);
                    }

                    if(installPlanActualExist.getDeviceBuxiuNum() != null && installPlanActual1.getDeviceBuxiuNum() != null) {
                        String newDeviceBuxiuNum = String.valueOf(Integer.valueOf(installPlanActualExist.getDeviceBuxiuNum()) + Integer.valueOf(installPlanActual1.getDeviceBuxiuNum()));
                        installPlanActualExist.setDeviceBuxiuNum(newDeviceBuxiuNum);
                    }

                    if(installPlanActualExist.getDeviceSwitchNum() != null && installPlanActual1.getDeviceSwitchNum() != null) {
                        String newDeviceSwitchNum = String.valueOf(Integer.valueOf(installPlanActualExist.getDeviceSwitchNum()) + Integer.valueOf(installPlanActual1.getDeviceSwitchNum()));
                        installPlanActualExist.setDeviceSwitchNum(newDeviceSwitchNum);
                    }

                    installPlanActualService.update(installPlanActualExist);
                    logger.info("更新 installPlanActual1 " + installPlanActual1.getId());
                }
            }
        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        installPlanActualService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String installPlanActual) {
        InstallPlanActual ipa = JSON.parseObject(installPlanActual, InstallPlanActual.class);
        if (ipa != null) {
            if (ipa.getInstallPlanId() == null) {
                return ResultGenerator.genFailResult("错误，InstallPlanId为null！");
            } else if (installPlanService.findById(ipa.getInstallPlanId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallPlanId " + ipa.getInstallPlanId() + " 找不到对应的plan ！");
            } else {
                ipa.setUpdateDate(new Date());
                installPlanActualService.update(ipa);
            }
        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InstallPlanActual installPlanActual = installPlanActualService.findById(id);
        return ResultGenerator.genSuccessResult(installPlanActual);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InstallPlanActual> list = installPlanActualService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据条件查询排产计划及其完成情况细节。
     *
     * @param orderNum 订单编号
     * @param nameplate 机器编号
     * @param installGroupName 安装组名称
     * @param type 排产类型： 部装，总装
     * @param queryStartTime 计划日期起始
     * @param queryFinishTime 计划日期结束
     * @return
     */
    @PostMapping("/selectInstallPlanActualDetails")
    public Result selectInstallPlanActualDetails(@RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "0") Integer size,
                                                 String orderNum,
                                                 String nameplate,
                                                 String installGroupName,
                                                 String type,
                                                 String queryStartTime,
                                                 String queryFinishTime) {
        PageHelper.startPage(page, size);
        List<InstallPlanActualDetails> list = installPlanActualService.selectInstallPlanActualDetails(orderNum,
                nameplate,
                installGroupName,
                type,
                queryStartTime,
                queryFinishTime);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

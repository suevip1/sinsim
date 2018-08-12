package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.service.MachineTypeService;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.eservice.api.service.impl.MachineTypeServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/14.
 */
@RestController
@RequestMapping("/machine/type")
public class MachineTypeController {
    @Resource
    private MachineTypeServiceImpl machineTypeService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;

    @PostMapping("/add")
    public Result add(String machineType) {
        MachineType model = JSON.parseObject(machineType, MachineType.class);
        List<MachineType> dataList = machineTypeService.selectByName(model.getName());
        if (dataList.size() > 0) {
            return ResultGenerator.genFailResult("名称与其它机型重复，请重新输入！");
        }
        machineTypeService.save(model);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        Integer count = machineOrderService.getUsedMachineTypeCount(id);
        if (count > 0) {
            return ResultGenerator.genFailResult("此机型已在使用中，不能删除！");
        }
        machineTypeService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String machineType) {
        MachineType model = JSON.parseObject(machineType, MachineType.class);
        Integer count = machineOrderService.getUsedMachineTypeCount(model.getId());
        if (count > 0) {
            //机器被使用，检查机型的名称是否更改
            MachineType typeInDB = machineTypeService.findById(model.getId());
            if(typeInDB != null && typeInDB.getName().equals(model.getName())) {
                //名称没变，变的可能是是否成品机信息
                machineTypeService.update(model);
                ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("此机型已在使用中，不能修改！");
            }
        }
        List<MachineType> dataList = machineTypeService.selectByName(model.getName());
        if (dataList.size() > 0) {
            for (MachineType item : dataList) {
                if (item.getId() != model.getId()) {
                    return ResultGenerator.genFailResult("名称与其它机型重复，请重新输入！");
                }
            }
        }
        machineTypeService.update(model);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        MachineType machineType = machineTypeService.findById(id);
        return ResultGenerator.genSuccessResult(machineType);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MachineType> list = machineTypeService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

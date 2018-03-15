package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine.MachinePlan;
import com.eservice.api.model.machine.MachineInfo;
import com.eservice.api.service.impl.MachineServiceImpl;
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
 * @date 2017/12/26.
 */
@RestController
@RequestMapping("/machine")
public class MachineController {
    @Resource
    private MachineServiceImpl machineService;

    @PostMapping("/add")
    public Result add(String machine) {
        Machine machine1 = JSON.parseObject(machine, Machine.class);
        machineService.save(machine1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        machineService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String machine) {
        Machine machine1 = JSON.parseObject(machine, Machine.class);
        machineService.update(machine1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Machine machine = machineService.findById(id);
        return ResultGenerator.genSuccessResult(machine);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Machine> list = machineService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectMachines")
    public Result selectMachines(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "0") Integer size,
                                 Integer id,
                                 Integer order_id,
                                 String machine_strid,
                                 String nameplate,
                                 String location,
                                 Byte status,
                                 Integer machine_type,
                                 String query_start_time,
                                 String query_finish_time,
                                 @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page, size);
        List<Machine> list = machineService.selectMachines(id, order_id, machine_strid, nameplate, location, status, machine_type, query_start_time, query_finish_time, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectPlanningMachines")
    public Result selectPlanningMachines(@RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "0") Integer size,
                                         String orderNum,
                                         String machineStrId,
                                         String nameplate,
                                         String location,
                                         Byte status,
                                         Integer machineType,
                                         Integer dateType,
                                         String query_start_time,
                                         String query_finish_time,
                                         @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page, size);
        List<MachinePlan> list = machineService.selectPlanningMachines(orderNum, machineStrId, nameplate, location, status, machineType, dateType, query_start_time, query_finish_time, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
	
    //
    @PostMapping("/selectConfigMachine")
    public Result selectConfigMachine(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            String location,
            Byte status,
            String query_start_time,
            String query_finish_time,
            @RequestParam(defaultValue = "0") Integer configStatus,
            @RequestParam(defaultValue = "true") Boolean is_fuzzy
    ) {
        PageHelper.startPage(page, size);
        List<MachineInfo> list = machineService.selectConfigMachine(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time,configStatus,is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectProcessMachine")
    public Result selectProcessMachine(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            String location,
            Byte status,
            String query_start_time,
            String query_finish_time,
            @RequestParam(defaultValue = "true") Boolean is_fuzzy
    ) {
        PageHelper.startPage(page, size);
        List<MachineInfo> list = machineService.selectProcessMachine(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time,is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectMachinesByNameplate")
    public Result selectMachinesNameplate(String nameplate) {
        Machine machine = machineService.selectMachinesByNameplate(nameplate);
        return ResultGenerator.genSuccessResult(machine);
    }

}

package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/24.
*/
@RestController
@RequestMapping("/machine/order")
public class MachineOrderController {
    @Resource
//    private MachineOrderService machineOrderService;
    private MachineOrderServiceImpl machineOrderService;

    @PostMapping("/add")
    public Result add(MachineOrder machineOrder) {
        machineOrderService.save(machineOrder);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        machineOrderService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(MachineOrder machineOrder) {
        machineOrderService.update(machineOrder);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        MachineOrder machineOrder = machineOrderService.findById(id);
        return ResultGenerator.genSuccessResult(machineOrder);
    }
	
    @PostMapping("/all_detail")
    public Result allDetail(@RequestParam Integer id) {
        MachineOrderDetail machineOrderDetail = machineOrderService.getOrderAllDetail(id);
        return ResultGenerator.genSuccessResult(machineOrderDetail);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MachineOrder> list = machineOrderService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /*
    根据条件查询订单。
    比如 查询 建立时间create_time在传入的参数 query_start_time 和 query_finish_time 之间的订单
     */
    @PostMapping("/selectOrder")
    public Result selectOrder(
            @RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "0") Integer size,

            Integer id,
            String contract_num,
            Integer status,
            String sellman,
            String customer,
            String query_start_time,
            String query_finish_time,
            String machine_name,
            @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page,size);
        List<MachineOrderDetail> list = machineOrderService.selectOrder(id, contract_num, status,sellman,customer,query_start_time,query_finish_time,machine_name,is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

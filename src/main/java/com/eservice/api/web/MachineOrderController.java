package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.eservice.api.service.impl.OrderDetailServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
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

    @Resource
    private OrderDetailServiceImpl orderDetailService;

    /*
        为保证 MachineOrder表和OrderDetail表的一致性，MachineOrder表和OrderDetail表，都在这里统一完成
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(String  machineOrder, String orderDetail) {
        MachineOrder machineOrder1 = JSON.parseObject(machineOrder,MachineOrder.class);
        OrderDetail orderDetail1 = JSON.parseObject(orderDetail,OrderDetail.class);
        orderDetailService.saveAndGetID(orderDetail1);
        Integer savedOrderDetailID = orderDetail1.getId();

        //orderDetail里返回的id 就是machineOrder里的order_detail_id
        machineOrder1.setOrderDetailId(savedOrderDetailID);
        machineOrderService.save(machineOrder1);

        return ResultGenerator.genSuccessResult();
    }

    /*
    同时删除MachineOrder表和OrderDetail表的相关内容
     */
    @PostMapping("/delete")
    @Transactional (rollbackFor = Exception.class)
    public Result delete(@RequestParam Integer id) {
        Integer orderDetailID = machineOrderService.findById(id).getOrderDetailId();
        machineOrderService.deleteById(id);//先删除主表
        orderDetailService.deleteById(orderDetailID);//再删除外键所在的表
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String machineOrder) {
        MachineOrder machineOrder1 = JSON.parseObject(machineOrder,MachineOrder.class);
        machineOrderService.update(machineOrder1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        MachineOrder machineOrder = machineOrderService.findById(id);
        return ResultGenerator.genSuccessResult(machineOrder);
    }
	
    @PostMapping("/allDetail")
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
    @PostMapping("/selectOrders")
    public Result selectOrders(
            @RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "0") Integer size,
            Integer id,
            Integer contract_id,
            String order_num,
            String contract_num,
            Integer status,
            String sellman,
            String customer,
            String query_start_time,
            String query_finish_time,
            String machine_name,
            @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page,size);
        List<MachineOrderDetail> list = machineOrderService.selectOrder(id, contract_id,order_num, contract_num, status,sellman,customer,query_start_time,query_finish_time,machine_name,is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/isOrderNumExist")
    public Result isOrderNumExist(@RequestParam String orderNum) {
        if (orderNum == null) {
            return ResultGenerator.genFailResult("请输入需求单编号！");
        } else {
            Condition condition = new Condition(MachineOrder.class);
            condition.createCriteria().andCondition("order_num = ", orderNum);
            List<MachineOrder> list = machineOrderService.findByCondition(condition);
            if (list.size() == 0) {
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("需求单编号已存在！");
            }
        }
    }
}

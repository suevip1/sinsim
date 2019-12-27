package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.OrderDetailServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.lang.reflect.Field;
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

    @Resource
    private OrderDetailServiceImpl orderDetailService;

    @Resource
    private MachineServiceImpl machineService;

    private Logger logger = Logger.getLogger(MachineOrderController.class);
    /*
        为保证 MachineOrder表和OrderDetail表的一致性，MachineOrder表和OrderDetail表，都在这里统一完成
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(String  machineOrder, String orderDetail) {
        MachineOrder machineOrder1 = JSON.parseObject(machineOrder, MachineOrder.class);
        OrderDetail orderDetail1 = JSON.parseObject(orderDetail, OrderDetail.class);
        /**
         * 订单 不允许同名
         * 带下划线的字段，不能用findBy(fieldName,....)
         */
        try {
            Class cl = Class.forName("com.eservice.api.model.machine_order.MachineOrder");
            Field fieldOrderNum = cl.getDeclaredField("orderNum");

            MachineOrder mo = null;
            mo = machineOrderService.findBy(fieldOrderNum.getName(), machineOrder1.getOrderNum());
            if (mo != null) {
                logger.error( "/machine/order/add fail: 该 order_num 已存在 " +  machineOrder1.getOrderNum() );
                return ResultGenerator.genFailResult(machineOrder1.getOrderNum() + " 该 order_num 已存在");
            }
        } catch (ClassNotFoundException e) {
            logger.error( "/machine/order/add fail: " +e.getMessage());
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            logger.error( "/machine/order/add fail: " +e.getMessage());
            e.printStackTrace();
        }
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
    //注意这个接口在不带参数时查询全部数据会有一定耗时，加了联系单查询。
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
            String marketGroupName,
            String query_start_time,
            String query_finish_time,
            String machine_name,
            @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page,size);
        List<MachineOrderDetail> list = machineOrderService.selectOrder(id, contract_id,order_num, contract_num, status,sellman,customer,marketGroupName,query_start_time,query_finish_time,machine_name,is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //根据 参数orderNum返回订单
    @PostMapping("/getMachineOrder")
    public Result getMachineOrder(
            @RequestParam String orderNum ) {
         MachineOrder machineOrder = machineOrderService.getMachineOrder(orderNum);
        return ResultGenerator.genSuccessResult(machineOrder);
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

    @PostMapping("/updateValid")
    public Result updateValid(@RequestParam Integer orderId) {
        if (orderId == null || orderId <= 0) {
            return ResultGenerator.genFailResult("需求单对应ID不正确，请联系管理员！");
        }
        MachineOrder machineOrder = machineOrderService.findById(orderId);
        if (machineOrder == null) {
            return ResultGenerator.genFailResult("需求单编号不存在！");
        } else {
            //检查需求单对应的机器是否生成，如果生成则不能删除
            List<Machine> machineList = machineService.selectMachines(null,orderId,null,null,null,null,null,null,null,null,false);
            if(machineList.size() > 0) {
                return ResultGenerator.genFailResult("需求单删除失败，对应机器已生成！");
            } else {
                machineOrder.setValid(Constant.ValidEnum.INVALID.getValue());
                machineOrder.setUpdateTime(new Date());
                machineOrderService.update(machineOrder);
                return ResultGenerator.genSuccessResult();
            }
        }
    }
}

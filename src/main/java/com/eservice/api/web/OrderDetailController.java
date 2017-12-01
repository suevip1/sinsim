package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.service.OrderDetailService;
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
* @author Wilson Hu
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/order/detail")
public class OrderDetailController {
    @Resource
    private OrderDetailService orderDetailService;

    /*
     为保证 MachineOrder和OrderDetail的一致性，该类的修改性接口不再暴露，而是通过 MachineOrderController统一暴露
  */
//    @PostMapping("/add")
//    public Result add(OrderDetail orderDetail) {
//        orderDetailService.save(orderDetail);
//        return ResultGenerator.genSuccessResult();
//    }

//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        orderDetailService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(OrderDetail orderDetail) {
//        orderDetailService.update(orderDetail);
//        return ResultGenerator.genSuccessResult();
//    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OrderDetail orderDetail = orderDetailService.findById(id);
        return ResultGenerator.genSuccessResult(orderDetail);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OrderDetail> list = orderDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

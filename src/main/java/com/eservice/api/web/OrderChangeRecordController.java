package com.eservice.api.web;

import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.order_change_record.OrderChangeRecord;
import com.eservice.api.model.order_change_record.OrderChangeRecordView;

import com.eservice.api.service.OrderChangeRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.eservice.api.service.impl.OrderChangeRecordServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/14.
 */
@RestController
@RequestMapping("/order/change/record")
public class OrderChangeRecordController {
    @Resource
    private OrderChangeRecordServiceImpl orderChangeRecordService;

    @PostMapping("/add")
    public Result add(OrderChangeRecord orderChangeRecord) {
        orderChangeRecordService.save(orderChangeRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        orderChangeRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(OrderChangeRecord orderChangeRecord) {
        orderChangeRecordService.update(orderChangeRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OrderChangeRecord orderChangeRecord = orderChangeRecordService.findById(id);
        return ResultGenerator.genSuccessResult(orderChangeRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OrderChangeRecord> list = orderChangeRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getChangeRecordList")
    public Result getChangeRecordList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, @RequestParam Integer id, @RequestParam Integer orderId) {
        PageHelper.startPage(page, size);
        List<OrderChangeRecordView> list = orderChangeRecordService.getChangeRecordList(id, orderId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

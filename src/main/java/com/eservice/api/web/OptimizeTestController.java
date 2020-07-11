package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.optimizeTest.OptimizeTest;
import com.eservice.api.service.impl.OptimizeTestServiceImpl;
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
* @date 2020/07/07.
*/
@RestController
@RequestMapping("/optimize/test")
public class OptimizeTestController {
    @Resource
    private OptimizeTestServiceImpl optimizeTestService;

    @PostMapping("/add")
    public Result add(String jsonOptimizeFormAllInfo) {
        OptimizeTest optimizeTest = JSON.parseObject(jsonOptimizeFormAllInfo, OptimizeTest.class);
        if (optimizeTest == null || optimizeTest.equals("")) {
            return ResultGenerator.genFailResult("JSON数据异常");
        }
        if(optimizeTest.getOrderNum() == null){
            return ResultGenerator.genFailResult("异常，getOrderNum 为空");
        }

        optimizeTestService.saveAndGetID(optimizeTest);
        return ResultGenerator.genSuccessResult(optimizeTest.getId());
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        optimizeTestService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(OptimizeTest optimize) {
        optimizeTestService.update(optimize);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OptimizeTest optimize = optimizeTestService.findById(id);
        return ResultGenerator.genSuccessResult(optimize);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OptimizeTest> list = optimizeTestService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * @param projectName
     * @param optimizePart
     * @param orderNum
     * @param queryStartTimeCreate 创建日期
     * @param queryFinishTimeCreate
     * @param machineType
     * @param purpose
     * @param owner
     * @param queryStartTimeUpdate 更新日期
     * @param queryFinishTimeUpdate
     * @return
     */
    @PostMapping("/selectOptimizeList")
    public Result selectOptimizeList(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "0") Integer size,
                                     String projectName,
                                     String optimizePart,
                                     String orderNum,
                                     String queryStartTimeCreate,
                                     String queryFinishTimeCreate,
                                     String machineType,
                                     String purpose,
                                     String owner,
                                     String queryStartTimeUpdate,
                                     String queryFinishTimeUpdate ) {
        PageHelper.startPage(page, size);
        List<OptimizeTest> list = optimizeTestService.selectOptimizeList(
                projectName,
                optimizePart,
                orderNum,
                queryStartTimeCreate,
                queryFinishTimeCreate,
                machineType,
                purpose,
                owner,
                queryStartTimeUpdate,
                queryFinishTimeUpdate);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.design_dep_info.DesignDepInfo;
import com.eservice.api.service.DesignDepInfoService;
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
* @date 2020/06/07.
*/
@RestController
@RequestMapping("/design/dep/info")
public class DesignDepInfoController {
    @Resource
    private DesignDepInfoService designDepInfoService;

    @PostMapping("/add")
    public Result add(DesignDepInfo designDepInfo) {
        designDepInfoService.save(designDepInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        designDepInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(DesignDepInfo designDepInfo) {
        designDepInfoService.update(designDepInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        DesignDepInfo designDepInfo = designDepInfoService.findById(id);
        return ResultGenerator.genSuccessResult(designDepInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DesignDepInfo> list = designDepInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

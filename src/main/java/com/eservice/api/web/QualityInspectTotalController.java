package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_inspect_total.QualityInspectTotal;
import com.eservice.api.service.QualityInspectTotalService;
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
* @date 2020/08/17.
*/
@RestController
@RequestMapping("/quality/inspect/total")
public class QualityInspectTotalController {
    @Resource
    private QualityInspectTotalService qualityInspectTotalService;

    @PostMapping("/add")
    public Result add(QualityInspectTotal qualityInspectTotal) {
        qualityInspectTotalService.save(qualityInspectTotal);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        qualityInspectTotalService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(QualityInspectTotal qualityInspectTotal) {
        qualityInspectTotalService.update(qualityInspectTotal);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        QualityInspectTotal qualityInspectTotal = qualityInspectTotalService.findById(id);
        return ResultGenerator.genSuccessResult(qualityInspectTotal);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QualityInspectTotal> list = qualityInspectTotalService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

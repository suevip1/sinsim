package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_inspect.QualityInspect;
import com.eservice.api.service.QualityInspectService;
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
@RequestMapping("/quality/inspect")
public class QualityInspectController {
    @Resource
    private QualityInspectService qualityInspectService;

    @PostMapping("/add")
    public Result add(QualityInspect qualityInspect) {
        qualityInspectService.save(qualityInspect);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        qualityInspectService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(QualityInspect qualityInspect) {
        qualityInspectService.update(qualityInspect);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        QualityInspect qualityInspect = qualityInspectService.findById(id);
        return ResultGenerator.genSuccessResult(qualityInspect);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QualityInspect> list = qualityInspectService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

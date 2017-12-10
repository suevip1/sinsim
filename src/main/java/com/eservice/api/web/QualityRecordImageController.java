package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.service.QualityRecordImageService;
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
* @date 2017/12/06.
*/
@RestController
@RequestMapping("/quality/record/image")
public class QualityRecordImageController {
    @Resource
    private QualityRecordImageService qualityRecordImageService;

    @PostMapping("/add")
    public Result add(String qualityRecordImage) {
        QualityRecordImage qualityRecordImage1 = JSON.parseObject(qualityRecordImage, QualityRecordImage.class);
        qualityRecordImageService.save(qualityRecordImage1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        qualityRecordImageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String qualityRecordImage) {
        QualityRecordImage qualityRecordImage1 = JSON.parseObject(qualityRecordImage, QualityRecordImage.class);
        qualityRecordImageService.update(qualityRecordImage1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        QualityRecordImage qualityRecordImage = qualityRecordImageService.findById(id);
        return ResultGenerator.genSuccessResult(qualityRecordImage);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QualityRecordImage> list = qualityRecordImageService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

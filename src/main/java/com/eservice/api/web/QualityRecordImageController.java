package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.service.QualityRecordImageService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    @Resource
    private CommonService commonService;
    @Resource
    private MachineServiceImpl machineService;
    @Value("${quality_images_saved_dir}")
    private String imagesSavedDir;

    /**
     * 增加 qualityRecordImage 时，也保存了图片,可以多张图片
     * (10+ 张图片应该没问题，quality_record_image.image字段长度1000，保存的路径长度不要超过这个值就好)。
     * @param qualityRecordImage
     * @param files
     * @return
     */
    @PostMapping("/add")
    public Result add(String qualityRecordImage, MultipartFile[] files) {
        QualityRecordImage qualityRecordImage1 = JSON.parseObject(qualityRecordImage, QualityRecordImage.class);
        Integer taskQualityRecordId = qualityRecordImage1.getTaskQualityRecordId();
        File dir = new File(imagesSavedDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        String machineID = searchMachineByTaskQualityRecordId(taskQualityRecordId);
        if(null == machineID){
            return ResultGenerator.genFailResult("Error: no machine found by the taskQualityRecordId, no records saved");
        }

        List<String> listResultPath = new ArrayList<>() ;
        for(int i=0; i<files.length; i++) {
            try {
                listResultPath.add( commonService.saveFile(imagesSavedDir, files[i], machineID, null, Constant.QUALITY_IMAGE, i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         if(listResultPath.size() == 0){
            return ResultGenerator.genFailResult("failed to save quality file, no records saved");
        } else {
            qualityRecordImage1.setImage(listResultPath.toString());
            qualityRecordImageService.save(qualityRecordImage1);
        }
        return ResultGenerator.genSuccessResult();
    }
    /**
     *根据taskQualityRecordId查找machineID
     * @param taskQualityRecordId
     * @return
     */
    @PostMapping("/searchMachineByTaskQualityRecordId")
    public String searchMachineByTaskQualityRecordId(@RequestParam Integer taskQualityRecordId ) {
        Machine machine = machineService.searchMachineByTaskQualityRecordId(taskQualityRecordId);
        if(machine != null) {
            return machine.getMachineStrId();
        }else {
            return null;
        }
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

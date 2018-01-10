package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal_image.AbnormalImage;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.AbnormalImageServiceImpl;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/abnormal/image")
public class AbnormalImageController {
    @Resource
    private AbnormalImageServiceImpl abnormalImageService;
    @Resource
    private CommonService commonService;
    @Resource
    private MachineServiceImpl machineService;

    /**
     * 增加abnormalImage时，也保存了图片，目前放在工程跟目录下。
     * @param abnormalImage
     * @param file1
     * @return
     */
    @PostMapping("/add")
    public Result add(String abnormalImage,MultipartFile file1) {
        AbnormalImage abnormalImage1 = JSON.parseObject(abnormalImage,AbnormalImage.class);
        ClassPathResource resource = new ClassPathResource("");
        String path = resource.getPath();
        Integer abnormalRecordId = abnormalImage1.getAbnormalRecordId();
        String machineID = searchMachineByAbnormalRecordId(abnormalRecordId);
        String result = commonService.saveFile(path,file1,machineID, Constant.ABNORMAL_IMAGE);
        if (result == null){
            return ResultGenerator.genFailResult("failed to save file, no records saved");
        } else {
            abnormalImageService.save(abnormalImage1);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据 abnormal_image 的 abnormal_record_id 来查找machine
     * abnormalImageId在add时还未知。
     * @param abnormalRecordId
     * @return
     */
    @PostMapping("/searchMachineByAbnormalRecordId")
    public String searchMachineByAbnormalRecordId(@RequestParam Integer abnormalRecordId ) {
        Machine machine = machineService.searchMachineByAbnormalRecordId(abnormalRecordId);
        return machine.getMachineId();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        abnormalImageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String abnormalImage) {
        AbnormalImage abnormalImage1 = JSON.parseObject(abnormalImage,AbnormalImage.class);
        abnormalImageService.update(abnormalImage1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AbnormalImage abnormalImage = abnormalImageService.findById(id);
        return ResultGenerator.genSuccessResult(abnormalImage);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AbnormalImage> list = abnormalImageService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

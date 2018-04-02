package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal_image.AbnormalImage;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.AbnormalImageServiceImpl;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
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
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Value("${abnormal_images_saved_dir}")
    private String imagesSavedDir;

    /**
     * 增加abnormalImage时，也保存了图片,可以多张图片。
     * @param abnormalImage
     * @param files
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestParam String abnormalImage,
                      @RequestParam MultipartFile[] files) {
        AbnormalImage abnormalImage1 = JSON.parseObject(abnormalImage,AbnormalImage.class);
        Integer abnormalRecordId = abnormalImage1.getAbnormalRecordId();
        File dir = new File(imagesSavedDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        String machineID = searchMachineByAbnormalRecordId(abnormalRecordId);
        if (machineID == null){
            return ResultGenerator.genFailResult("Error: no machine found by the abnormalRecordId, no records saved");
        }

        Integer orderId = machineService.searchMachineByAbnormalRecordId(abnormalRecordId).getOrderId();
        String orderNum = machineOrderService.findById(orderId).getOrderNum();
        List<String> listResultPath = new ArrayList<>() ;
        for(int i=0; i<files.length; i++) {
            try {
                listResultPath.add( commonService.saveFile(imagesSavedDir, files[i], machineID, orderNum, Constant.ABNORMAL_IMAGE,i ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (listResultPath.size() == 0){
            return ResultGenerator.genFailResult("failed to save file, no records saved");
        } else {
            abnormalImage1.setImage(listResultPath.toString());
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
        if(machine != null) {
            return machine.getMachineStrId();
        }else {
            return null;
        }
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

package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.optimizeTest.OptimizeTest;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineTypeServiceImpl;
import com.eservice.api.service.impl.OptimizeTestServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
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

    @Value("${optimize_saved_dir}")
    private String optimizeSavedDir;

    @Resource
    private CommonService commonService;

    @Resource
    private MachineTypeServiceImpl machineTypeService;

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
    public Result update(String jsonOptimizeFormAllInfo) {
        OptimizeTest optimizeTest = JSON.parseObject(jsonOptimizeFormAllInfo, OptimizeTest.class);

        if (optimizeTest == null || optimizeTest.equals("")) {
            return ResultGenerator.genFailResult("JSON数据异常");
        }
        if(optimizeTest.getId() == null){
            return ResultGenerator.genFailResult("异常，Id为空");
        }
        OptimizeTest optimizeTestOld = optimizeTestService.findById(optimizeTest.getId());
        if(optimizeTestOld == null){
            return ResultGenerator.genFailResult("异常，根据Id找不到对应的优化");
        }
        optimizeTest.setUpdateDate(new Date());
        optimizeTestService.update(optimizeTest);
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
        String strMachineTypeName = null;
        if(machineType != null && ! machineType.isEmpty()){
            MachineType mt = machineTypeService.findById(Integer.valueOf(machineType));
            if(mt != null){
                strMachineTypeName = mt.getName();
            }
        }

        List<OptimizeTest> list = optimizeTestService.selectOptimizeList(
                projectName,
                optimizePart,
                orderNum,
                queryStartTimeCreate,
                queryFinishTimeCreate,
                strMachineTypeName,
                purpose,
                owner,
                queryStartTimeUpdate,
                queryFinishTimeUpdate);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/uploadOptimizeFile")
    public Result uploadOptimizeFile(HttpServletRequest request) {
        try {

            String orderNum = request.getParameterValues("orderNum")[0];
            List<MultipartFile> fileList = ((MultipartHttpServletRequest) request).getFiles("file");
            if (fileList == null || fileList.size() == 0) {
                return ResultGenerator.genFailResult("Error: no available file");
            }
            MultipartFile file = fileList.get(0);
            File dir = new File(optimizeSavedDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                // save file， 只保存文件，不保存数据库，保存路径返回给前端，前端统一写入 。
                String resultPath = commonService.saveFile(optimizeSavedDir, file, "", orderNum,
                        Constant.OPTIMIZE_ATTACHED_FILE, 0);
                if (resultPath == null || resultPath == "") {
                    return ResultGenerator.genFailResult("文件名为空，优化附件上传失败！");
                }
                return ResultGenerator.genSuccessResult(resultPath);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultGenerator.genFailResult("设计附件 上传失败！" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("设计附件 上传失败！" + e.getMessage());
        }
    }

    /**
     * 根据优化ID 返回 对应的附件的文件名称 下载路径的前面部分是统一的，放在xxx_ip/download/下(nginx配置)，
     * 比如，访问下面地址可以下载优化单附件
     * http://xx.xx.xx.xx/optimizeAttached/曹457B__optimize__0.png
     *
     * @param optimizeTestId
     * @return 类似 曹457B__optimize__0.png
     */
    @PostMapping("/getOptimizeAttachedFile")
    public Result getOptimizeAttachedFile(@RequestParam Integer optimizeTestId) {

        OptimizeTest ot = optimizeTestService.findById(optimizeTestId);
        if (null == ot) {
            return ResultGenerator.genFailResult("根据该 optimizeTestId 找不到对应的联系单");
        }
        if (ot.getFiles() == null) {
            return ResultGenerator.genFailResult("该优化单没有附件");
        }

        String fileName = null;
        fileName = ot.getFiles().substring(optimizeSavedDir.length());
        return ResultGenerator.genSuccessResult(fileName);
    }
}

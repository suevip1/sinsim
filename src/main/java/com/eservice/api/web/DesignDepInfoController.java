package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.design_dep_info.DesignDepInfo;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.DesignDepInfoServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
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
* @date 2020/06/07.
*/
@RestController
@RequestMapping("/design/dep/info")
public class DesignDepInfoController {
    @Resource
    private DesignDepInfoServiceImpl designDepInfoService;

    @Value("${design_attached_saved_dir}")
    private String designAttachedSavedDir;

    @Resource
    private CommonService commonService;

    private Logger logger = Logger.getLogger(DesignDepInfoController.class);
    /**
     * 一次性同时上传 信息
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public Result add(String jsonDesignDepInfoFormAllInfo) {
        DesignDepInfo designDepInfo = JSON.parseObject(jsonDesignDepInfoFormAllInfo, DesignDepInfo.class);
        if (designDepInfo == null || designDepInfo.equals("")) {
            return ResultGenerator.genFailResult("JSON数据异常");
        }
        if(designDepInfo.getOrderId() == null){
            return ResultGenerator.genFailResult("异常，orderId为空");
        }
        designDepInfoService.saveAndGetID(designDepInfo);
        // 返回ID给前端，前端新增联系单时不关闭页面。
        return ResultGenerator.genSuccessResult(designDepInfo.getId());
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        designDepInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String jsonDesignDepInfoFormAllInfo) {
        DesignDepInfo designDepInfo = JSON.parseObject(jsonDesignDepInfoFormAllInfo, DesignDepInfo.class);

        if (designDepInfo == null || designDepInfo.equals("")) {
            return ResultGenerator.genFailResult("JSON数据异常");
        }
        if(designDepInfo.getId() == null){
            return ResultGenerator.genFailResult("异常，Id为空");
        }
        designDepInfoService.update(designDepInfo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 后端提示：No mapping found for HTTP request with URI [/design/depinfo/uploadDesignFiles]
     * in DispatcherServlet with name 'dispatcherServlet.
     * 其实是接口名称没有完全正确，多了个字符。
     */
    @PostMapping("/uploadDesignFile")
    public Result uploadDesignFile(HttpServletRequest request) {
        try {
            String orderNum = request.getParameterValues("orderNum")[0];
            String type = request.getParameterValues("type")[0];
            List<MultipartFile> fileList = ((MultipartHttpServletRequest) request).getFiles("file");
            if (fileList == null || fileList.size() == 0) {
                return ResultGenerator.genFailResult("Error: no available file");
            }
            MultipartFile file = fileList.get(0);
            File dir = new File(designAttachedSavedDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                // save file， 只保存文件，不保存数据库，保存路径返回给前端，前端统一写入 。
                String resultPath = commonService.saveFile(designAttachedSavedDir, file, type, orderNum,
                        Constant.DESIGN_ATTACHED_FILE, 0);
                if (resultPath == null || resultPath == "") {
                    return ResultGenerator.genFailResult("文件名为空，设计附件上传失败！");
                }
                logger.info("====/design/dep/info uploadDesignFile(): success======== " + resultPath);
                /**
                 * 在第一次新建时上传文件，designDepInfoID为空，则后端把各个更新日期设为当前日期即可
                 * 在编辑时更新上传文件，则把根据类型，把对应的Update日期更新。
                 */
                Integer designDepInfoID = Integer.valueOf(request.getParameterValues("designDepInfoID")[0]);
                if(designDepInfoID == null || designDepInfoID.equals(0)){
                    logger.info("在新创建的设计里上传文件，已经默认更新日期为当前时间");
                } else {
                    DesignDepInfo designDepInfo = designDepInfoService.findById(designDepInfoID);
                    if(designDepInfo == null){
                        logger.error("根据前端给的ID找不到 designDepInfo");
                    } else {
                        switch (type) {
                            case Constant.STR_DESIGN_UPLOAD_FILE_TYPE_DRAWING:
                                designDepInfo.setDrawingLoadingUpdateTime(new Date());
                                break;
                            case Constant.STR_DESIGN_UPLOAD_FILE_TYPE_HOLE:
                                designDepInfo.setHoleTubeUpdateTime(new Date());
                                break;
                            case Constant.STR_DESIGN_UPLOAD_FILE_TYPE_BOM:
                                designDepInfo.setBomUpdateTime(new Date());
                                break;
                            case Constant.STR_DESIGN_UPLOAD_FILE_TYPE_COVER:
                                designDepInfo.setCoverUpdateTime(new Date());
                                break;

                            default:
                                break;
                        }
                        designDepInfoService.update(designDepInfo);
                    }
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

    @PostMapping("/selectDesignDepInfo")
    public Result selectDesignDepInfo(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "0") Integer size,
                                      String orderNum,
                                      String saleman,
                                      String guestName,
                                      Integer orderStatus,//订单审核状态
                                      Integer drawingStatus,//图纸状态
                                      String machineSpec,
                                      String keyword,
                                      String designer,
                                      String updateDateStart,
                                      String updateDateEnd) {
        PageHelper.startPage(page, size);
        List<DesignDepInfo> list = designDepInfoService.selectDesignDepInfo(
                orderNum,
                saleman,
                guestName,
                orderStatus,
                drawingStatus,
                machineSpec,
                keyword,
                designer,
                updateDateStart,
                updateDateEnd);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_loading_list.OrderLoadingList;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.eservice.api.service.impl.OrderLoadingListServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2018/03/05.
*/
@RestController
@RequestMapping("/order/loading/list")
public class OrderLoadingListController {
    @Resource
    private OrderLoadingListServiceImpl orderLoadingListService;
    @Value("${ordre_loading_list_saved_dir}")
    private String orderLoadingListSavedDir;
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Resource
    private CommonService commonService;
    Logger logger = Logger.getLogger(CommonService.class);

    /**
     * 在增加记录的同时，也保存了装车单文件file1
     * 多个装车单文件处理:
     * 每次上传一个文件，用同个orderLoadingList JSON ( 比如{"createTime":1518402431000, "orderId":56,"type":2})
     * 上传多次,就上传了多个文件，都属于编号为56的需求单的装车单文件。
     *
     * @param orderLoadingList
     * @param file1            每次上传的一个文件
     * @return
     */
    @PostMapping("/add")
    public Result add(String orderLoadingList, MultipartFile file1) {
        OrderLoadingList orderLoadingList1 = JSON.parseObject(orderLoadingList, OrderLoadingList.class);
        Integer machineOrderId = orderLoadingList1.getOrderId();
//        String machineOrderId = searchOrderIdByOrderId(orderId);
        File dir = new File(orderLoadingListSavedDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (machineOrderId == null) {
            return ResultGenerator.genFailResult("Error: no machineOrder found by the ollId, no records saved");
        }
        String resultPath = commonService.saveFile(orderLoadingListSavedDir, file1, null, machineOrderId.toString(), Constant.LOADING_FILE,0);

        if (null == resultPath) {
            return ResultGenerator.genFailResult("failed to save OrderloadingList file, no records saved");
        } else {
            orderLoadingList1.setFileName(resultPath);
            orderLoadingListService.save(orderLoadingList1);
            logger.info("====/order/loading/list/add():======== " + resultPath);
            return ResultGenerator.genSuccessResult(resultPath);
        }
    }

    @PostMapping("/upload")
    public Result upload(HttpServletRequest request) {
        String data = request.getParameterValues("uploadData")[0];
        String orderNum = request.getParameterValues("orderNum")[0];
        OrderLoadingList orderLoadingList = JSON.parseObject(data, OrderLoadingList.class);
        List<MultipartFile> fileList = ((MultipartHttpServletRequest) request).getFiles("file");
        if (fileList == null || fileList.size() == 0) {
            return ResultGenerator.genFailResult("Error: no available file");
        }

        MultipartFile file = fileList.get(0);
        Integer machineOrderId = orderLoadingList.getOrderId();
        File dir = new File(orderLoadingListSavedDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (machineOrderId == null) {
            return ResultGenerator.genFailResult("Error: no machineOrder found by the ollId, no records saved");
        }
        String resultPath = commonService.saveFile(orderLoadingListSavedDir, file, orderNum, machineOrderId.toString(), Constant.LOADING_FILE,0);

        if (null == resultPath) {
            return ResultGenerator.genFailResult("failed to save OrderloadingList file, no records saved");
        } else {
            orderLoadingList.setFileName(resultPath);
            orderLoadingListService.save(orderLoadingList);
            logger.info("====/order/loading/list/upload():======== " + resultPath);
            return ResultGenerator.genSuccessResult(orderLoadingList.getId());
        }
    }

    /**
     * 根据 OrderLoadingList 的id返回需求单号 machine_order.order_num
     * 目前并没有用到，暂时不删
     *
     * @param orderLoadingListId
     * @return
     */
    @PostMapping("/searchOrderIdByOrderLoadingListId")
    public String searchOrderIdByOrderLoadingListId(@RequestParam Integer orderLoadingListId) {
        MachineOrder machineOrder = machineOrderService.searchOrderIdByOrderLoadingListId(orderLoadingListId);
        if (machineOrder != null) {
            return machineOrder.getOrderNum();
        } else {
            return null;
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        orderLoadingListService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String orderLoadingList) {
        OrderLoadingList orderLoadingList1 = JSON.parseObject(orderLoadingList, OrderLoadingList.class);
        orderLoadingListService.update(orderLoadingList1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OrderLoadingList orderLoadingList = orderLoadingListService.findById(id);
        return ResultGenerator.genSuccessResult(orderLoadingList);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OrderLoadingList> list = orderLoadingListService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据 order_id 返回 装车单的文件名称(可以有多个装车单)
     * 下载路径的前面部分是统一的，放在xxx_ip/download/下(nginx配置)，
     * 比如，访问下面地址可以下载该装车单
     * http://xx.xx.xx.xx/oll/null_1_LoadingFile_2018-02-24-10-12-2.xlsx
     *
     * @param order_id
     * @return
     */
    @PostMapping("/selectOrderLoadingFileNameByOrderId")
    public Result selectOrderLoadingFileNameByOrderId(@RequestParam Integer order_id) {
        List<OrderLoadingList> list = orderLoadingListService.selectFilePathByOrderId(order_id);
        List<String> fileNameList = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            fileNameList.add(list.get(i).getFileName().substring(orderLoadingListSavedDir.length()));
        }
        if (fileNameList.isEmpty()) {
            return ResultGenerator.genFailResult("No orderLoadingList found by the order_id !");
        } else {
            return ResultGenerator.genSuccessResult(fileNameList);
        }
    }
}

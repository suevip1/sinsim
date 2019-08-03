package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.install_group.InstallGroup;
import com.eservice.api.service.InstallGroupService;
import com.eservice.api.service.impl.InstallGroupServiceImpl;
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
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/install/group")
public class InstallGroupController {
    @Resource
    private InstallGroupServiceImpl installGroupService;

    @PostMapping("/add")
    public Result add(String installGroup) {
        InstallGroup group = JSONObject.parseObject(installGroup, InstallGroup.class);
        if(group != null) {
            installGroupService.save(group);
        }else {
            return ResultGenerator.genFailResult("参数错误！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        installGroupService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String installGroup) {
        InstallGroup group = JSONObject.parseObject(installGroup, InstallGroup.class);
        if(group != null && group.getId() != null) {
            installGroupService.update(group);
        }else {
            return ResultGenerator.genFailResult("参数错误！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InstallGroup installGroup = installGroupService.findById(id);
        return ResultGenerator.genSuccessResult(installGroup);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InstallGroup> list = installGroupService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getInstallGroupByType")
    public Result getInstallGroupByType(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "0") Integer size,
                                        @RequestParam String type) {
        PageHelper.startPage(page, size);
        List<InstallGroup> list = installGroupService.getInstallGroupByType(type);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

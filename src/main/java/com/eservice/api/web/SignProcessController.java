package com.eservice.api.web;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.sign_process.SignProcess;
import com.eservice.api.service.SignProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/09.
*/
@RestController
@RequestMapping("/sign/process")
public class SignProcessController {
    @Resource
    private SignProcessService signProcessService;

    @PostMapping("/add")
    public Result add(String signProcess) {
        SignProcess signProcess1 = JSONObject.parseObject(signProcess, SignProcess.class);
        ///
        if(signProcess1 != null) {
            if(signProcess1.getProcessName() == null || "".equals(signProcess1.getProcessName())) {
                ResultGenerator.genFailResult("流程名字为空！");
            }else if(signProcess1.getProcessContent() == null || "".equals(signProcess1.getProcessContent())) {
                ResultGenerator.genFailResult("流程内容为空！");
            }else {
                ///添加创建签核流程的时间
                signProcess1.setCreateTime(new Date());
                signProcessService.save(signProcess1);
            }
        } else {
            ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        signProcessService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String signProcess) {
        SignProcess signProcess1 = JSONObject.parseObject(signProcess, SignProcess.class);
        if(signProcess1 != null) {
            if(signProcess1.getProcessName() == null || "".equals(signProcess1.getProcessName())) {
                ResultGenerator.genFailResult("流程名字为空！");
            }else if(signProcess1.getProcessContent() == null || "".equals(signProcess1.getProcessContent())) {
                ResultGenerator.genFailResult("流程内容为空！");
            }else {
                ///添加修改签核流程的时间
                signProcess1.setUpdateTime(new Date());
                signProcessService.update(signProcess1);
            }
        } else {
            ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        SignProcess signProcess = signProcessService.findById(id);
        return ResultGenerator.genSuccessResult(signProcess);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SignProcess> list = signProcessService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

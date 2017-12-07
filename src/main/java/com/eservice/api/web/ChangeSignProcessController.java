package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.change_sign_process.ChangeSignProcess;
import com.eservice.api.service.ChangeSignProcessService;
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
* @date 2017/12/07.
*/
@RestController
@RequestMapping("/change/sign/process")
public class ChangeSignProcessController {
    @Resource
    private ChangeSignProcessService changeSignProcessService;

    @PostMapping("/add")
    public Result add(ChangeSignProcess changeSignProcess) {
        changeSignProcessService.save(changeSignProcess);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        changeSignProcessService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ChangeSignProcess changeSignProcess) {
        changeSignProcessService.update(changeSignProcess);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ChangeSignProcess changeSignProcess = changeSignProcessService.findById(id);
        return ResultGenerator.genSuccessResult(changeSignProcess);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ChangeSignProcess> list = changeSignProcessService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

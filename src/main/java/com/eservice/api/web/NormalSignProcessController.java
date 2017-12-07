package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.normal_sign_process.NormalSignProcess;
import com.eservice.api.service.NormalSignProcessService;
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
@RequestMapping("/normal/sign/process")
public class NormalSignProcessController {
    @Resource
    private NormalSignProcessService normalSignProcessService;

    @PostMapping("/add")
    public Result add(NormalSignProcess normalSignProcess) {
        normalSignProcessService.save(normalSignProcess);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        normalSignProcessService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(NormalSignProcess normalSignProcess) {
        normalSignProcessService.update(normalSignProcess);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        NormalSignProcess normalSignProcess = normalSignProcessService.findById(id);
        return ResultGenerator.genSuccessResult(normalSignProcess);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<NormalSignProcess> list = normalSignProcessService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

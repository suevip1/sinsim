package com.eservice.api.web;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.market_group.MarketGroup;
import com.eservice.api.service.MarketGroupService;
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
* @date 2018/04/19.
*/
@RestController
@RequestMapping("/market/group")
public class MarketGroupController {
    @Resource
    private MarketGroupService marketGroupService;

    @PostMapping("/add")
    public Result add(String marketGroup) {
        MarketGroup group = JSONObject.parseObject(marketGroup, MarketGroup.class);
        if(group != null) {
            marketGroupService.save(group);
        }else {
            return ResultGenerator.genFailResult("参数错误！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        marketGroupService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String marketGroup) {
        MarketGroup group = JSONObject.parseObject(marketGroup, MarketGroup.class);
        if(group != null && group.getId() != null) {
            marketGroupService.update(group);
        }else {
            return ResultGenerator.genFailResult("参数错误！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        MarketGroup marketGroup = marketGroupService.findById(id);
        return ResultGenerator.genSuccessResult(marketGroup);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MarketGroup> list = marketGroupService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

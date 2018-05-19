package com.eservice.api.web;

import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal.Abnormal;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.service.AbnormalService;
import com.eservice.api.service.impl.AbnormalRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2018/03/19.
 */
@RestController
@RequestMapping("/abnormal")
public class AbnormalController {
    @Resource
    private AbnormalService abnormalService;
    @Resource
    private AbnormalRecordServiceImpl abnormalRecordService;

    @PostMapping("/add")
    public Result add(Abnormal abnormal) {
        if (abnormal != null) {
            abnormal.setCreateTime(new Date());
        }
        abnormalService.save(abnormal);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        if (id == null || id <= 0) {
            return ResultGenerator.genFailResult("异常ID有误！");
        } else {
            //检查异常记录中是否使用了该异常，如果已经使用，不能删除
            Condition condition = new Condition(AbnormalRecord.class);
            condition.createCriteria().andCondition("abnormal_type = ", id);
            List<AbnormalRecord> list = abnormalRecordService.findByCondition(condition);
            if (list == null || list.size() > 0) {
                return ResultGenerator.genFailResult("该异常已经被使用，删除会导致之前的y异常记录有误；如不想使用，可把该项设置为无效!");
            }
        }
        abnormalService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Abnormal abnormal) {
        if (abnormal != null) {
            abnormal.setUpdateTime(new Date());
            abnormalService.update(abnormal);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("更新异常项的数据不存在！");
        }
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Abnormal abnormal = abnormalService.findById(id);
        return ResultGenerator.genSuccessResult(abnormal);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Abnormal> list = abnormalService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/validList")
    public Result validList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Abnormal> list = abnormalService.findAll();
        List<Abnormal> resList = new ArrayList<Abnormal>();
        for (Abnormal item : list) {
            if (item.getValid() == 1) {
                resList.add(item);
            }
        }
        list.clear();
        PageInfo pageInfo = new PageInfo(resList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

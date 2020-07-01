package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contact_fulfill.ContactFulfill;
import com.eservice.api.service.ContactFulfillService;
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
* @date 2020/07/01.
*/
@RestController
@RequestMapping("/contact/fulfill")
public class ContactFulfillController {
    @Resource
    private ContactFulfillService contactFulfillService;

    @PostMapping("/add")
    public Result add(ContactFulfill contactFulfill) {
        contactFulfillService.save(contactFulfill);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contactFulfillService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ContactFulfill contactFulfill) {
        contactFulfillService.update(contactFulfill);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ContactFulfill contactFulfill = contactFulfillService.findById(id);
        return ResultGenerator.genSuccessResult(contactFulfill);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ContactFulfill> list = contactFulfillService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

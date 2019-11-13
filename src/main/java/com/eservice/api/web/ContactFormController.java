package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultCode;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormDetail; 
import com.eservice.api.service.impl.ContactFormServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
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
* @date 2019/11/05.
*/
@RestController
@RequestMapping("/contact/form")
public class ContactFormController {
    @Resource
    private ContactFormServiceImpl contactFormService;

    private Logger logger = Logger.getLogger(ContactFormController.class);

    @PostMapping("/add")
    public Result add(String contactFormDetail) {

        if(contactFormDetail == null) {
            return ResultGenerator.genFailResult("string contactFormDetail is null");
        }
        Result result = checkTheContactFormValid(contactFormDetail);
        if (result.getCode() == ResultCode.FAIL.code) {
            logger.warn("不合法的 contactFormDetail: " + result.getMessage());
            return result;
        }
        ContactFormDetail contactFormDetail1 = JSON.parseObject(contactFormDetail, ContactFormDetail.class);
        contactFormDetail1.setCreateDate(new Date());
        contactFormService.save(contactFormDetail1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/checkTheContactFormValid")
    public Result checkTheContactFormValid(String contactFormDetail) {
        ContactFormDetail contactFormDetail1 = JSON.parseObject(contactFormDetail, ContactFormDetail.class);
        if (contactFormDetail1 == null) {
            return ResultGenerator.genFailResult("参数 contactFormDetail 不能为null ");
        }

        /**
         * 逐一检查各个必要参数的合法性
         */
        if (contactFormDetail1.getApplicantDepartment()== null) {
            return ResultGenerator.genFailResult("错误，getApplicantDepartment 为 null！");
        }

        if (contactFormDetail1.getContactTitle() == null) {
            return ResultGenerator.genFailResult("错误，getContactTitle 为 null！");
        }

        return ResultGenerator.genSuccessResult("OK");
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contactFormService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ContactForm contactForm) {
        contactFormService.update(contactForm);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ContactForm contactForm = contactFormService.findById(id);
        return ResultGenerator.genSuccessResult(contactForm);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ContactForm> list = contactFormService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 按条件模糊查下联系单信息
     * @param contactType 联系单类型
     * @param contractNum  订单 编号
     * @param applicantDepartment 发起部门
     * @param applicantPerson   发起人
     * @param status 状态
     * @param queryStartTime
     * @param queryFinishTime
     * @param isFuzzy
     * @return
     */
    @PostMapping("/selectContacts")
    public Result selectContacts(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "0") Integer size,
                                 String contactType,
                                 String contractNum,
                                 String applicantDepartment,
                                 String applicantPerson,
                                 Integer status,
                                 String queryStartTime,
                                 String queryFinishTime,
                                 @RequestParam(defaultValue = "true") Boolean isFuzzy) {
        PageHelper.startPage(page, size);

        List<ContactFormDetail> list = contactFormService.selectContacts(contactType,
                                                                    contractNum,
                                                                    applicantDepartment,
                                                                    applicantPerson,
                                                                    status,
                                                                    queryStartTime,
                                                                    queryFinishTime,
                                                                    isFuzzy);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

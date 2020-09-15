package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_sign.ContactSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.role.Role;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.ContactFormServiceImpl;
import com.eservice.api.service.impl.ContactSignServiceImpl;
import com.eservice.api.service.impl.RoleServiceImpl;
import com.eservice.api.service.impl.UserServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/contact/sign")
public class ContactSignController {
    @Resource
    private ContactSignServiceImpl contactSignService;

    @Resource
    private ContactFormServiceImpl contactFormService;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private CommonService commonService;
    private Logger logger = Logger.getLogger(ContactSignController.class);

    @PostMapping("/add")
    public Result add(ContactSign contactSign) {
        contactSignService.save(contactSign);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contactSignService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     *  联系单审核的更新，同时更新对应的联系单
     * @param contactSign
     * @return
     */
    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(String contactSign) {
        if (contactSign == null || "".equals(contactSign)) {
            ResultGenerator.genFailResult("联系单审核信息不能为空！");
        }
        logger.info("contactSign:" + contactSign);
		
        ContactSign cs = JSON.parseObject(contactSign,ContactSign.class);
        if (cs == null) {
            ResultGenerator.genFailResult("审核信息JSON解析失败！");
        }

        ContactForm cf = contactFormService.findById(cs.getContactFormId());
        if(null == cf){
            ResultGenerator.genFailResult("根据传入的签核信息里的联系单ID 找不到对应的联系单");
        }

        cs.setUpdateTime(new Date());
        List<SignContentItem> contactSignContentList = JSON.parseArray(cs.getSignContent(), SignContentItem.class);

        boolean haveReject = false;
        String currentStep = "";
        for (SignContentItem item : contactSignContentList) {
            if (item.getResult().equals(Constant.SIGN_REJECT)) {
                haveReject = true;
            }
            //签核在初始化，则把当前步骤设为发起部门
            if(item.getResult() == Constant.SIGN_INITIAL&&item.getShenHeEnabled()) {
                currentStep = roleService.findById(item.getRoleId()).getRoleName();
                break;
            }
        }

        //都已经签核
        if(!haveReject) {
            if(currentStep.equals("")) {
                currentStep = Constant.SIGN_FINISHED;
            }
            cs.setCurrentStep(currentStep);
        }
        contactSignService.update(cs);

        /**
         * 推送公众号消息给轮到联系单签核的人（通过售后系统）
         */
        commonService.pushLxdMsgToAftersale(cs,cf, haveReject);

        if (haveReject) {
            /**
             * 被拒绝了，
             * contactSign的每个状态，更新为 初始状态
             * contactForm的状态，更新为 被拒绝。
             */
            for (SignContentItem item : contactSignContentList) {
                item.setResult(Constant.SIGN_INITIAL);
            }
            cs.setSignContent(JSONObject.toJSONString(contactSignContentList));
            logger.warn("got Reject signContent: " + JSONObject.toJSONString(contactSignContentList));
            contactSignService.update(cs);

            cf.setStatus(Constant.STR_LXD_REJECTED);
            contactFormService.update(cf);
        } else {
            //没有被拒，且刚刚开始
            if (cf.getStatus().equals(Constant.STR_LXD_INITIAL)) {
                cf.setStatus(Constant.STR_LXD_CHECKING);
            }
            //没有被拒，且联系单签核完成
            if(currentStep.equals(Constant.SIGN_FINISHED)) {
                cf.setStatus(Constant.STR_LXD_CHECKING_FINISHED);
            }
            contactFormService.update(cf);
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ContactSign contactSign = contactSignService.findById(id);
        return ResultGenerator.genSuccessResult(contactSign);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ContactSign> list = contactSignService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据 contact_form_id 查询 contact_sign。 目前是一一对应。
     * @param contactFormId
     * @return
     */
    @PostMapping("/getContactSign")
    public Result getContactSign(@RequestParam Integer contactFormId) {
        ContactSign contactSign = contactSignService.getContactSignByLxdId(contactFormId);
        return ResultGenerator.genSuccessResult(contactSign);
    }
}

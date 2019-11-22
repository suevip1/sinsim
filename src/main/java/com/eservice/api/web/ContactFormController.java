package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.change_item.ChangeItem;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormAllInfo;
import com.eservice.api.model.contact_form.ContactFormDetail;
import com.eservice.api.model.contact_sign.ContactSign;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.ChangeItemServiceImpl;
import com.eservice.api.service.impl.ContactFormServiceImpl;
import com.eservice.api.service.impl.ContactSignServiceImpl;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
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

    @Resource
    private ContactSignServiceImpl contactSignService;

    @Resource
    private ChangeItemServiceImpl changeItemService;

    @Resource
    private MachineOrderServiceImpl machineOrderService;

    @Value("${lxd_attached_saved_dir}")
    private String lxdAttachedSavedDir;

    @Resource
    private CommonService commonService;

    private Logger logger = Logger.getLogger(ContactFormController.class);

    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(@RequestBody ContactFormAllInfo contactFormAllInfo) {
        String message = null;

        ContactForm contactForm = contactFormAllInfo.getContactForm();
        List<ChangeItem> changeItemList = contactFormAllInfo.getChangeItemList();
        ContactSign contactSign = contactFormAllInfo.getContactSign();

        try {
            if( null == contactForm){
                message = " contactForm 为空！";
                throw new RuntimeException();
            }

            if(contactForm.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)){
                if( null == changeItemList) {
                    message = " 类型为变更联系单时，变更条目不能为空！";
                    throw new RuntimeException();
                }
            }
            if( null == contactSign) {
                message = " contactSign 为空！";
                throw new RuntimeException();
            }

            //生成联系单
            contactForm.setCreateDate(new Date());
            contactFormService.saveAndGetID(contactForm);

            //生成联系单变更条目
            for(int i =0; i<changeItemList.size(); i++){
                changeItemList.get(i).setContactFormId(contactForm.getId());
                changeItemService.save(changeItemList.get(i));
            }

            //生成联系单的审核记录
            contactSign.setCreateTime(new Date());
            contactSign.setContactFormId(contactForm.getId());
            contactSignService.save(contactSign);

        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.warn("添加联系单/联系单变更条目/联系单审核信息 出错: " + message);
            return ResultGenerator.genFailResult("添加联系单/联系单变更条目/联系单审核信息 出错！" + message + ex.getMessage());
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/checkTheContactFormValid")
    public Result checkTheContactFormValid(String contactForm) {

        if (contactForm == null) {
            return ResultGenerator.genFailResult("参数 contactFormDetail 不能为null ");
        }
        ContactForm contactForm1 = JSON.parseObject(contactForm , ContactForm.class);
        if(contactForm1 == null){
            return ResultGenerator.genFailResult("错误，解析得到的 contactForm1为 null！");
        }
        /**
         * 逐一检查各个必要参数的合法性
         */
        if (contactForm1.getApplicantDepartment()== null) {
            return ResultGenerator.genFailResult("错误，getApplicantDepartment 为 null！");
        }

        if (contactForm1.getContactTitle() == null) {
            return ResultGenerator.genFailResult("错误，getContactTitle 为 null！");
        }

        if(contactForm1.getOrderId() == null){
            return ResultGenerator.genFailResult("错误，orderId 为 null！");
        }

        //根据id找订单编号
        MachineOrder machineOrder = machineOrderService.findById(contactForm1.getOrderId());
        if(machineOrder == null){
            return ResultGenerator.genFailResult("错误，根据该id找不到对应的订单 ");
        }
        return ResultGenerator.genSuccessResult("OK");
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contactFormService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String contactForm) {
        ContactForm contactForm1 = JSON.parseObject(contactForm, ContactForm.class);
        if(contactForm1 == null) {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        } else {
            contactForm1.setUpdateDate(new Date());
            contactFormService.update(contactForm1);
        }
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

    /**
     * 上传联系单附件文件
     * 只保存文件，不保存数据库，保存路径返回给前端，前端统一写入
     * @param request 参数带一个文件，以及一个lxdNum（联系单的编号，客户可见）
     * @return 后端保存的文件名称（相对地址在： lxdAttachedSavedDir）
     */
    @PostMapping("/uploadAttachedFile")
    public Result uploadAttachedFile(HttpServletRequest request) {
        try {
            //联系单的编号，在保存文件时，文件名称中包含。
            String lxdNum = request.getParameterValues("lxdNum")[0];
            List<MultipartFile> fileList = ((MultipartHttpServletRequest) request).getFiles("file");
            if (fileList == null || fileList.size() == 0) {
                return ResultGenerator.genFailResult("Error: no available file");
            }
            MultipartFile file = fileList.get(0);
            File dir = new File(lxdAttachedSavedDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                //save file， 只保存文件，不保存数据库，保存路径返回给前端，前端统一写入 。
                String resultPath = commonService.saveFile(lxdAttachedSavedDir, file, lxdNum, "lxd", Constant.LXD_ATTACHED_FILE, 0);
                if (resultPath == null || resultPath == "") {
                    return ResultGenerator.genFailResult("文件名为空，联系单附件上传失败！");
                }
                logger.info("====/contact/form/uploadAttachedFile(): success======== " + resultPath);
                return ResultGenerator.genSuccessResult(resultPath);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultGenerator.genFailResult("联系单附件 上传失败！" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("联系单附件 上传失败！" + e.getMessage());
        }
    }
}

package com.eservice.api.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.change_item.ChangeItem;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormAllInfo;
import com.eservice.api.model.contact_form.ContactFormDetail;
import com.eservice.api.model.contact_fulfill.ContactFulfill;
import com.eservice.api.model.contact_sign.ContactSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.machine_order.MachineOrder;

import com.eservice.api.model.user.User;
import com.eservice.api.service.ContactFormService;

import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Class Description: xxx
 * 
 * @author Wilson Hu
 * @date 2019/11/05.
 */
@RestController
@RequestMapping("/contact/form")
public class ContactFormController {
    @Resource
    private ContactFormServiceImpl contactFormService;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private ContactSignServiceImpl contactSignService;

    @Resource
    private ChangeItemServiceImpl changeItemService;

    @Resource
    private MachineOrderServiceImpl machineOrderService;

    @Resource
    private ContactFulfillServiceImpl contactFulfillService;

    @Value("${lxd_attached_saved_dir}")
    private String lxdAttachedSavedDir;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private CommonService commonService;

    @Resource
    private UserServiceImpl userService;

    private Logger logger = Logger.getLogger(ContactFormController.class);

    /**
     * 一次性同时上传 联系单，联系单的变更条目，联系单的签核信息
     * 
     * @param jsonContactFormAllInfo
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(String jsonContactFormAllInfo) {
        ContactFormAllInfo contactFormAllInfo = JSON.parseObject(jsonContactFormAllInfo, ContactFormAllInfo.class);
        if (contactFormAllInfo == null || contactFormAllInfo.equals("")) {
            return ResultGenerator.genFailResult("JSON数据异常");
        }

        String message = null;
        ContactForm contactForm = contactFormAllInfo.getContactForm();
        List<ChangeItem> changeItemList = contactFormAllInfo.getChangeItemList();
        ContactSign contactSign = contactFormAllInfo.getContactSign();

        try {
            if (null == contactForm) {
                message = " contactForm 为空！";
                throw new RuntimeException();
            }

            if (contactForm.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)) {
                if (null == changeItemList) {
                    message = " 类型为变更联系单时，变更条目不能为空！";
                    throw new RuntimeException();
                }
            }
            if (null == contactSign) {
                message = " contactSign 为空！";
                throw new RuntimeException();
            }

            // 生成联系单
            contactForm.setCreateDate(new Date());
            contactForm.setStatus(Constant.STR_LXD_INITIAL);

            String firstPartOfLxdNum = contactForm.getNum().replace("xxx", "");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String thisYear = formatter.format(new Date());
            String lastSerialNumber = getLxdLastSerialNumber(thisYear, contactForm.getApplicantDepartment());
            if (lastSerialNumber == "") {
                String firstLxdOfTheDepartment = contactForm.getNum().replace("xxx", "001");
                contactForm.setNum(firstLxdOfTheDepartment);
            } else {
                Integer newSerialNumber;
                try {
                    newSerialNumber = Integer.valueOf(lastSerialNumber) + 1;
                } catch (Exception e) {
                    logger.warn("exception: " + e);
                    newSerialNumber = 0;
                }
                String newNum = firstPartOfLxdNum + String.format("%03d", newSerialNumber);
                contactForm.setNum(newNum);
            }

            //如果是在新建联系单就添加附件，此时联系单单号未确定。需要后端来更新。
            if(contactForm.getAttachedFile().contains("xxx") ){
                String oldNameOfAttachedFile = contactForm.getAttachedFile();
                String newNameOfAttachedFile = contactForm.getAttachedFile().replace("xxx",contactForm.getNum().split("-")[2]);

                //根据订单名称 重新命名附件文件
                File file = new File(oldNameOfAttachedFile);
                if(file == null || !file.exists()){
                    logger.error(oldNameOfAttachedFile + "文件不存在！");
                } else {
                    file.renameTo(new File(newNameOfAttachedFile));
                }
                contactForm.setAttachedFile(newNameOfAttachedFile);
            }

            contactFormService.saveAndGetID(contactForm);

            // 生成联系单变更条目， 如果类型不是变更联系单时，这部分为空
            if (contactForm.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)) {
                for (int i = 0; i < changeItemList.size(); i++) {
                    changeItemList.get(i).setContactFormId(contactForm.getId());
                    changeItemService.save(changeItemList.get(i));
                }
            }

            // 生成联系单的审核记录
            contactSign.setCreateTime(new Date());
            contactSign.setContactFormId(contactForm.getId());
            contactSignService.save(contactSign);

            /**
             * 联系单的落实，也在add时添加，比如技术部（联系单的主要落实者）也可以在发起联系单时，直接指定落实信息。
             */
            ContactFulfill contactFulfill = contactFormAllInfo.getContactFulfill();
            if(contactFulfill != null ) {
                contactFulfill.setContactFormId(contactForm.getId());
                if(contactFulfill.getFulfillMan() == null || contactFulfill.getFulfillMan().equals("")){
                    contactFulfill.setStatus(Constant.STR_FULFILL_STATUS_UN_ASSIGN);
                } else {
                    contactFulfill.setStatus(Constant.STR_FULFILL_STATUS_FULFILLING);
                }
                contactFulfill.setCreateDate(new Date());
                contactFulfillService.save(contactFulfill);
            } else {
                logger.info("新增联系单时，落实单为空");
            }
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.warn("添加联系单/联系单变更条目/联系单审核信息 出错: " + message);
            return ResultGenerator.genFailResult("添加联系单/联系单变更条目/联系单审核信息 出错！" + message + ex.getMessage());
        }
        // 返回ID给前端，前端新增联系单时不关闭页面。
        return ResultGenerator.genSuccessResult(contactForm.getId());
    }

    /**
     * 需要查询某年、某部门已存在的联系单的最后的联系单单号。
     * 
     * @param year       格式 yyyy
     * @param department 发起部门（角色）
     * @return
     */
    @PostMapping("/getLxdLastSerialNumber")
    public String getLxdLastSerialNumber(@RequestParam String year, @RequestParam String department) {
        List<ContactForm> contactFormList = contactFormService.getLxdLastSerialNumber(year, department);
        if (contactFormList != null && contactFormList.size() != 0) {
            // 外1-20-111
            String[] arr = contactFormList.get(contactFormList.size() - 1).getNum().split("-");
            return arr[arr.length - 1];
        } else {
            return "";
        }
    }

    @PostMapping("/checkTheContactFormValid")
    public Result checkTheContactFormValid(String contactForm) {

        if (contactForm == null) {
            return ResultGenerator.genFailResult("参数 contactFormDetail 不能为null ");
        }
        ContactForm contactForm1 = JSON.parseObject(contactForm, ContactForm.class);
        if (contactForm1 == null) {
            return ResultGenerator.genFailResult("错误，解析得到的 contactForm1为 null！");
        }
        /**
         * 逐一检查各个必要参数的合法性
         */
        if (contactForm1.getApplicantDepartment() == null) {
            return ResultGenerator.genFailResult("错误，getApplicantDepartment 为 null！");
        }

        if (contactForm1.getContactTitle() == null) {
            return ResultGenerator.genFailResult("错误，getContactTitle 为 null！");
        }

        // if(contactForm1.getOrderNum() == null){
        // return ResultGenerator.genFailResult("错误，orderNum 为 null！");
        // }

        // 根据订单号找订单
        MachineOrder machineOrder = machineOrderService.getMachineOrder(contactForm1.getOrderNum());
        if (machineOrder == null) {
            return ResultGenerator.genFailResult("错误，根据该订单号 找不到对应的订单 ");
        }
        return ResultGenerator.genSuccessResult("OK");
    }

    // 删除时，联系单对应的 变更条目，联系单签核，都删除。
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        ContactForm cf = contactFormService.findById(id);
        if (null == cf) {
            return ResultGenerator.genFailResult("根据该id" + id + " 找不到对应的联系单");
        }

        if (cf.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)) {
            List<ChangeItem> ciList = changeItemService.selectChangeItemList(id);
            for (int i = 0; i < ciList.size(); i++) {
                changeItemService.deleteById(ciList.get(i).getId());
            }
        }

        ContactSign cs = contactSignService.getContactSignByLxdId(id);
        contactSignService.deleteById(cs.getId());

        contactFormService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 一是修改联系单时要update （包括变更条目的修改），二是审核联系单时也要Update。 即使已经开始审批过程了，还要允许修改。
     * 允许财务部修改是否需要总经理审核
     * 
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/update")
    public Result update(String jsonContactFormAllInfo) {
        ContactFormAllInfo contactFormAllInfo = JSON.parseObject(jsonContactFormAllInfo, ContactFormAllInfo.class);
        if (contactFormAllInfo == null || contactFormAllInfo.equals("")) {
            return ResultGenerator.genFailResult("JSON数据异常");
        }

        logger.info("更新联系单 " +jsonContactFormAllInfo);
        String message = null;
        ContactForm contactForm = contactFormAllInfo.getContactForm();
        List<ChangeItem> changeItemList = contactFormAllInfo.getChangeItemList();
        ContactSign contactSign = contactFormAllInfo.getContactSign();

        try {
            if (null == contactForm) {
                message = " contactForm 为空！";
                throw new RuntimeException();
            }

            if (null == contactForm.getId()) {
                message = " contactForm 的Id 不能为空！";
                throw new RuntimeException();
            }
            if (contactFormService.findById(contactForm.getId()) == null) {
                message = " 根据该id找不到对应的联系单！";
                throw new RuntimeException();
            }

            if (contactForm.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)) {
                if (null == changeItemList) {
                    message = " 类型为变更联系单时，变更条目不能为空！";
                    throw new RuntimeException();
                }
            }
            if (null == contactSign) {
                message = " contactSign 为空！";
                throw new RuntimeException();
            }

            if (contactForm.getStatus().equals(Constant.STR_LXD_CHECKING) ) {
                // 重新计算当前审核阶段
                List<SignContentItem> contactSignContentList = JSON.parseArray(contactSign.getSignContent(),
                        SignContentItem.class);
                String currentStep = "";
                // int num=-1;
                for (SignContentItem item : contactSignContentList) {
                    String step = roleService.findById(item.getRoleId()).getRoleName();
                    // 每次更新，需按顺序检查，当前审核步骤是否变化
                    // 审核状态是初始化，并且是启用的就是当前步骤
                    if (item.getResult() == Constant.SIGN_INITIAL && item.getShenHeEnabled()) {
                        currentStep = step;
                        break;
                    }
                    // 检查当前current step 是否禁用掉
                    // if(step.equals(contactSign.getCurrentStep()))
                    // {
                    // num=item.getNumber();
                    // if(item.getResult() == Constant.SIGN_INITIAL&&item.getShenHeEnabled()) {
                    // break;
                    // }
                    // }else{
                    // if(num>-1&&item.getNumber()>num)
                    // {
                    // if(item.getResult() == Constant.SIGN_INITIAL&&item.getShenHeEnabled()) {
                    // currentStep=step;
                    // break;
                    // }
                    // }
                    // }
                }
                if (currentStep != "") {
                    contactSign.setCurrentStep(currentStep);
                    logger.info("当前审核阶段 不为空：" + currentStep);
                } else {
                    /**
                     * 当前审核阶段 为空, 表示已经不存在：需要审核但是还没审核（Result为0）的步骤，即已经全部审核完成了。
                     * 此时需要更新状态。【被取消的那一步，刚好是最后一步，且前面都已经完成审核】
                     * 实列：一开始是勾着王总审核的，王总前面全部审批完成，再修改取消了王总审核，此时审核应该为完成。
                     */
                    logger.info("当前审核阶段 为空：" + currentStep);
                    contactSign.setCurrentStep(Constant.SIGN_FINISHED);
                    contactForm.setStatus(Constant.STR_LXD_CHECKING_FINISHED);
                }
            }

            if (contactForm.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)) {
                // 更新联系单变更条目
                List<ChangeItem> changeItemListExist = changeItemService.selectChangeItemList(contactForm.getId());
                for (int i = 0; i < changeItemList.size(); i++) {
                    if (changeItemList.get(i).getContactFormId() != null) {
                        if (changeItemList.get(i).getContactFormId().intValue() != contactForm.getId().intValue()) {
                            message = " 变更条目里的ContactFormId 和 联系单的id 不匹配！";
                            throw new RuntimeException();
                        }
                    }
                    // step1.如果是已经存在的条目，则更新
                    if (changeItemService.findById(changeItemList.get(i).getId()) != null) {
                        changeItemService.update(changeItemList.get(i));
                        logger.info("更新了 id为 " + changeItemList.get(i).getId() + " 的变更条目");
                    } else {
                        // step2.如果是新增的条目，则新增。
                        changeItemList.get(i).setContactFormId(contactForm.getId());
                        changeItemService.save(changeItemList.get(i));
                        logger.info("新增了一个 " + changeItemList.get(i).getNewInfo() + " 的变更条目");
                    }
                    // step3. 如果目前已存在的条目 不在传进来的条目中，表示该条目应该删除。
                    boolean isIncluded = false;
//                    List<ChangeItem> changeItemListExist = changeItemService.selectChangeItemList(contactForm.getId());
                    for (int ei = 0; ei < changeItemListExist.size(); ei++) { // exist item
                        // 每个条目都和传进来的每个条目进行比较
                        isIncluded = false;
                        for (int ii = 0; ii < changeItemList.size(); ii++) { // input item
                            if (changeItemListExist.get(ei).getId().equals(  changeItemList.get(ii).getId()) ) {
                                isIncluded = true;
                                break;
                            }
                        }
                        if (!isIncluded) {
                            changeItemService.deleteById(changeItemListExist.get(ei).getId());
                            logger.info("删除了 id为 " + changeItemListExist.get(ei).getId() + " 的变更条目");
                        }
                    }

                }
                //如果传进来变更列表的是空的，说明要把之前旧的全部都删除
                if( changeItemList.size()==0 ){
                    for (int ei = 0; ei < changeItemListExist.size(); ei++) { // exist item
                        changeItemService.deleteById(changeItemListExist.get(ei).getId());
                        logger.info("all, 删除了 id为 " + changeItemListExist.get(ei).getId() + " 的变更条目");
                    }
                }

            }

            // 更新 联系单的审核记录
            contactSign.setUpdateTime(new Date());
            if (contactSign.getContactFormId().intValue() != contactForm.getId()) {
                message = " contactSign里的ContactFormId 和 联系单的id 不匹配！";
                throw new RuntimeException();
            }

            contactSignService.update(contactSign);// 统一更新审核数据， currentstep等
            contactForm.setUpdateDate(new Date());
            contactFormService.update(contactForm); // 统一更新联系单

            /**
             * 联系单的落实，新增/更新
             */
            ContactFulfill contactFulfill = contactFormAllInfo.getContactFulfill();
            if(contactFulfill != null ) {
                contactFulfill.setContactFormId(contactForm.getId());
                if(contactFulfill.getId() == null || contactFulfill.getId() ==0){
                    ////workaround：不知道为啥前端会传个为0的contactFulfill.id，照理应该是null
                    //新增加的落实信息，比如在二期旧的联系单上添加落实信息，比如在新建联系单时未写落实信息，后面再补上时。
                    if(contactFulfill.getFulfillMan() ==null){
                        contactFulfill.setStatus(Constant.STR_FULFILL_STATUS_UN_ASSIGN);
                    } else {
                        contactFulfill.setStatus(Constant.STR_FULFILL_STATUS_FULFILLING);
                    }
                    contactFulfill.setCreateDate(new Date());
                    contactFulfillService.save(contactFulfill);
                } else {
                    contactFulfill.setUpdateDate(new Date());
                    contactFulfillService.update(contactFulfill);
                }
            } else {
                logger.info("更新联系单时，落实单为空");
            }
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.warn("更新 联系单/联系单变更条目/联系单审核信息 出错: " + message);
            return ResultGenerator.genFailResult("更新 联系单/联系单变更条目/联系单审核信息 出错！" + message + ex.getMessage());
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ContactForm contactForm = contactFormService.findById(id);
        return ResultGenerator.genSuccessResult(contactForm);
    }

    /**
     * 根据联系单的ID 返回详细信息
     * 
     * @param contactFormId
     * @return
     */
    @PostMapping("/getAllInfo")
    public Result getAllInfo(@RequestParam Integer contactFormId) {
        ContactFormAllInfo contactFormAllInfo = contactFormService.getAllInfo(contactFormId);
        return ResultGenerator.genSuccessResult(contactFormAllInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ContactForm> list = contactFormService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 按条件 查下联系单信息
     *
     * @param contactNum         联系单 编号
     * @param contactTitle         联系单 主题
     * @param contactType         联系单类型
     * @param orderNum            订单 编号
     * @param applicantDepartment 发起部门
     * @param applicantPerson     发起人
     * @param userRoleName      查询者的角色 TODO:...
     * @param status              状态
     * @param queryStartTime
     * @param queryFinishTime
     * @Param currentStep -审核的当前步骤
     * @param isFuzzy
     * @return
     */
    @PostMapping("/selectContacts")
    public Result selectContacts(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "0") Integer size,
                                 String contactNum,
                                 String contactTitle,
                                 String contactType,
                                 String orderNum,
                                 String applicantDepartment,
                                 String applicantPerson,
                                 String userRoleName,
                                 Integer status,
                                 String queryStartTime,
                                 String queryFinishTime,
                                 String currentStep,
                                 @RequestParam(defaultValue = "true") Boolean isFuzzy) {
        PageHelper.startPage(page, size);

        String strStatus = null;
        // String -- integer 转换
        if (status != null) {
            if (status.equals(Integer.valueOf(Constant.LXD_INITIAL))) {
                strStatus = Constant.STR_LXD_INITIAL;
            } else if (status.equals(Integer.valueOf(Constant.LXD_CHECKING))) {
                strStatus = Constant.STR_LXD_CHECKING;
            } else if (status.equals(Integer.valueOf(Constant.LXD_CHECKING_FINISHED))) {
                strStatus = Constant.STR_LXD_CHECKING_FINISHED;
            } else if (status.equals(Integer.valueOf(Constant.LXD_REJECTED))) {
                strStatus = Constant.STR_LXD_REJECTED;
            } else if (status.equals(Integer.valueOf(Constant.LXD_CANCELED))) {
                strStatus = Constant.STR_LXD_CANCELED;
            }
        }
        List<ContactFormDetail> list = contactFormService.selectContacts(
                contactNum,
                contactTitle,
                contactType,
                orderNum,
                applicantDepartment,
                applicantPerson,
                userRoleName,
                strStatus,
                queryStartTime,
                queryFinishTime,
                currentStep,
                isFuzzy);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    /**
     * 上传联系单附件文件 只保存文件，不保存数据库，保存路径返回给前端，前端统一写入
     *
     * @param request 参数带一个文件，以及一个lxdNum（联系单的编号，客户可见）
     * @return 后端保存的文件名称（相对地址在： lxdAttachedSavedDir）
     */
    @PostMapping("/uploadAttachedFile")
    public Result uploadAttachedFile(HttpServletRequest request) {
        try {
            /**
             * 联系单的编号，在保存文件时，文件名称中包含。
             * 在第一次新建时，联系单单号 名称中包含 xxx, 这个在add时会倍更新，对应的文件也会重命名。
             */
            String lxdNum = request.getParameterValues("lxdNum")[0];
            String type = request.getParameterValues("type")[0]; //联系单附件 or 联系单签核过程的附件
            String uploadMan = request.getParameterValues("uploadMan")[0];
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
                int typeInt = 0;
                if(type.equals(Constant.STRING_LXD_ATTACHED_FILE_BY_CREATER)){
                    typeInt = Constant.LXD_ATTACHED_FILE_BY_CREATER;
                } else if (type.equals(Constant.STRING_LXD_ATTACHED_FILE_DURING_SIGN)){
                    typeInt = Constant.LXD_ATTACHED_FILE_DURING_SIGN;
                }
                // save file， 只保存文件，不保存数据库，保存路径返回给前端，前端统一写入 。
                String resultPath = commonService.saveFile(lxdAttachedSavedDir, file, "", lxdNum,
                        typeInt, 0);
                if (resultPath == null || resultPath == "") {
                    return ResultGenerator.genFailResult("文件名为空，联系单附件上传失败！");
                }
                logger.info("====/contact/form/uploadAttachedFile(): success======== " + resultPath);

                /**
                 * 如果是签核过程中上传的附件，在附件保存之后，还需要特别地把数据库更新。
                 */
                if(type.equals(Constant.STRING_LXD_ATTACHED_FILE_DURING_SIGN)) {
                    List<ContactFormDetail> list = contactFormService.selectContacts(
                            lxdNum,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,// 可能有多个联系单，所以不能用 Constant.STR_LXD_CHECKING_FINISHED,
                            null,
                            null,
                            null,
                            null,
                            null);
                    if (list == null || list.isEmpty()) {
                        return ResultGenerator.genFailResult(lxdNum + " 找不到对应的联系单！");
                    }
                    ContactForm contactForm = list.get(0);//
                    contactForm.setAttachedDuringSign(resultPath);
                    contactForm.setAttachedDuringSignMan(uploadMan);
                    contactFormService.update(contactForm);
                }
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

    /**
     * 联系单发起审核
     * 
     * @param lxdId
     * @return
     */
    @PostMapping("/startSign")
    @Transactional(rollbackFor = Exception.class)
    public Result startSign(@RequestParam Integer lxdId) {
        if (lxdId == null) {
            ResultGenerator.genFailResult("联系单ID为空！");
        } else {
            ContactSign contactSign = contactSignService.getContactSignByLxdId((lxdId));
            if (contactSign == null) {
                return ResultGenerator.genFailResult("根据联系单ID号获取 签核信息失败！");
            } else {
                List<SignContentItem> contactSignContentList = JSON.parseArray(contactSign.getSignContent(),
                        SignContentItem.class);
                String currentStep = "";
                for (SignContentItem item : contactSignContentList) {
                    // 签核在初始化，则把当前步骤设为发起部门
                    if (item.getResult() == Constant.SIGN_INITIAL && item.getShenHeEnabled()) {
                        currentStep = roleService.findById(item.getRoleId()).getRoleName();
                        break;
                    }
                }
                contactSign.setCurrentStep(currentStep);
                contactSignService.update(contactSign);// 更新审核数据currentstep

                // 更新联系单状态为 STR_LXD_CHECKING
                ContactForm contactForm = contactFormService.findById(lxdId);
                if (contactForm == null) {
                    return ResultGenerator.genFailResult("合同编号ID无效");
                } else {
                    contactForm.setStatus(Constant.STR_LXD_CHECKING);
                    contactForm.setUpdateDate(new Date());
                    contactFormService.update(contactForm);
                }
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据联系单ID 返回 联系单的附件的文件名称 下载路径的前面部分是统一的，放在xxx_ip/download/下(nginx配置)，
     * 比如，访问下面地址可以下载该装车单
     * http://xx.xx.xx.xx/lxdAttached/lxd_lxdNum111_lxdAttached__0.xlsx
     * 
     * @param contact_form_id
     * @param flag : 哪种附件，比如新建联系单时添加的附件，签核时添加的附件
     * @return 类似 lxd_lxdNum111_lxdAttached__0.xlsx
     */
    @PostMapping("/getLxdAttachedFile")
    public Result getLxdAttachedFile(@RequestParam Integer contact_form_id,
                                     @RequestParam String flag) {

        ContactForm cf = contactFormService.findById(contact_form_id);
        if (null == cf) {
            return ResultGenerator.genFailResult("根据该contact_form_id 找不到对应的联系单");
        }
        if (cf.getAttachedFile() == null) {
            return ResultGenerator.genFailResult("该联系单没有附件");
        }

        String fileName = null;
        if(flag.contains(Constant.STRING_LXD_ATTACHED_FILE_BY_CREATER)){
            fileName = cf.getAttachedFile().substring(lxdAttachedSavedDir.length());
        } else if (flag.contains(Constant.STRING_LXD_ATTACHED_FILE_DURING_SIGN)){
            fileName = cf.getAttachedDuringSign().substring(lxdAttachedSavedDir.length());
        } else {
            return ResultGenerator.genFailResult("没有对应的附件类型！");
        }

        return ResultGenerator.genSuccessResult(fileName);
    }

    @PostMapping("/buildLxdExcel")
    public Result buildLxdExcel(@RequestParam Integer contact_form_id, String account) {

        ContactFormAllInfo cf = contactFormService.getAllInfo(contact_form_id);
        if (null == cf) {
            return ResultGenerator.genFailResult("根据该contact_form_id 找不到对应的联系单");
        }

        Boolean isChange = cf.getContactForm().getContactType().indexOf(Constant.STR_LXD_TYPE_BIANGENG) >= 0;// 是否是变更单
        InputStream fs = null;
        POIFSFileSystem pfs = null;
        HSSFWorkbook wb = null;
        FileOutputStream out = null;
        String downloadPath = "";
        /*
         * 返回给docker外部下载
         */
        String downloadPathForNginx = "";

        Boolean displayEnable = commonService.isDisplayPrice(account);

        try {
            String templateFile = isChange ? "empty_lxd_change_template.xls" : "empty_lxd_work_template.xls";
            ClassPathResource resource = new ClassPathResource(templateFile);
            fs = resource.getInputStream();
            pfs = new POIFSFileSystem(fs);
            wb = new HSSFWorkbook(pfs);

            // 读取了模板内所有sheet1内容
            HSSFSheet workSheet = wb.getSheetAt(0);

            // 在相应的单元格进行赋值(A2)
            HSSFCell numCell = workSheet.getRow(1).getCell((short) 1);// 表单编号
            numCell.setCellValue(new HSSFRichTextString(cf.getContactForm().getNum()));

            HSSFCell dpartCell = workSheet.getRow(2).getCell((short) 2);// 提出部门
            dpartCell.setCellValue(cf.getContactForm().getApplicantDepartment());

            HSSFCell apCell = workSheet.getRow(2).getCell((short) 7);// 申请人
            apCell.setCellValue(cf.getContactForm().getApplicantPerson());

            HSSFCell statusCell = workSheet.getRow(2).getCell((short) 11);// 审核状态
            statusCell.setCellValue(cf.getContactForm().getStatus());

            HSSFCell dateCell = workSheet.getRow(3).getCell((short) 2);// 申请日期
            String createDate = cf.getContactForm().getCreateDate() == null ? ""
                    : formatter.format(cf.getContactForm().getCreateDate());
            dateCell.setCellValue(createDate);

            HSSFCell ECODateCell = workSheet.getRow(3).getCell((short) 9);// ECO希望完成日期
            String ecoDate = cf.getContactForm().getHopeDate() == null ? ""
                    : formatter.format(cf.getContactForm().getHopeDate());
            ECODateCell.setCellValue(ecoDate);

            HSSFCell titleCell = workSheet.getRow(4).getCell((short) 2);// 变更理由/主题
            titleCell.setCellValue(cf.getContactForm().getContactTitle());

            HSSFCell contentCell = workSheet.getRow(5).getCell((short) 2);// 变更内容
            contentCell.setCellValue(cf.getContactForm().getContactContent());
            short startRow = 6;
            if (isChange)// 是变更单
            {
                startRow = 7;
                // 设置变更项目内容
                HSSFRow curRow = workSheet.getRow(startRow);
                List<ChangeItem> changeItemList = cf.getChangeItemList();
                Integer index = 0;
                for (ChangeItem item : changeItemList) {
                    if (index > 0) {
                        startRow += index;
                        // insert new row
                        // set the style for each cell
                        insertRow(workSheet, startRow - 1, 1);
                        workSheet.addMergedRegion(new CellRangeAddress(7 + index, 7 + index, 1, 4));
                        workSheet.addMergedRegion(new CellRangeAddress(7 + index, 7 + index, 5, 9));
                        workSheet.addMergedRegion(new CellRangeAddress(7 + index, 7 + index, 10, 11));
                        curRow = workSheet.getRow(startRow);
                    }
                    curRow.getCell((short) 1).setCellValue(item.getOldInfo());
                    curRow.getCell((short) 5).setCellValue(item.getNewInfo());

                    if(displayEnable) {
                        curRow.getCell((short) 10).setCellValue(item.getRemarks());
                    } else {
                        curRow.getCell((short) 10).setCellValue("/");
                    }
                    index++;
                }
            } else {

            }

            for (short i = startRow; i < workSheet.getLastRowNum(); i++) {
                if (this.getCellStringValue(workSheet.getRow(i).getCell((short) 1)).indexOf("附件") >= 0) {
                    workSheet.getRow(i).getCell((short) 2).setCellValue("有");// 附件
                }
                if (this.getCellStringValue(workSheet.getRow(i).getCell((short) 1)).indexOf("签核角色") >= 0) {
                    ContactSign contactSign = cf.getContactSign();
                    List<SignContentItem> contactSignContentList = JSON.parseArray(contactSign.getSignContent(),
                            SignContentItem.class);
                    short j = 0;
                    for (SignContentItem item : contactSignContentList) {
                        j++;
                        String checkRole = roleService.findById(item.getRoleId()).getRoleName();

                        workSheet.getRow(i + j).getCell((short) 1).setCellValue(new HSSFRichTextString(checkRole));
                        workSheet.getRow(i + j).getCell((short) 2).setCellValue(new HSSFRichTextString(item.getUser()));

                        String date = item.getDate() == null ? "" : formatter.format(item.getDate());
                        workSheet.getRow(i + j).getCell((short) 3).setCellValue(new HSSFRichTextString(date));

                        //成本核算员跟财务经理的意见,只给销售人员和王总看。
                        if(item.getRoleId() == 13 ||item.getRoleId() ==14 ) {
                            if (displayEnable) {
                                workSheet.getRow(i + j).getCell((short) 4)
                                        .setCellValue(new HSSFRichTextString(item.getComment()));
                            } else {
                                workSheet.getRow(i + j).getCell((short) 4).setCellValue(new HSSFRichTextString("/"));
                            }
                        } else {
                            workSheet.getRow(i + j).getCell((short) 4)
                                    .setCellValue(new HSSFRichTextString(item.getComment()));
                        }
                        workSheet.getRow(i + j).getCell((short) 11)
                                .setCellValue(new HSSFRichTextString(this.getResultString(item.getResult())));

                        if (!item.getShenHeEnabled()) {
                            for (int q = 0; q < 11; q++) {
                                HSSFCellStyle style = workSheet.getRow(i + j).getCell(q + 1).getCellStyle();
                                style.setFillBackgroundColor((short)IndexedColors.GREY_80_PERCENT.index);
                                workSheet.getRow(i + j).getCell(q + 1).setCellStyle(style);
                            }

                        }
                    }
                    break;
                }
            }
            String fileName = cf.getContactForm().getNum().replaceAll("/", "-") + ".xls";
            downloadPath = lxdAttachedSavedDir + fileName;
//            downloadPathForNginx = "/excel/" + fileName;
            downloadPathForNginx = "/" + fileName;
            out = new FileOutputStream(downloadPath);
            wb.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
                pfs.close();
                out.close();
                wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("".equals(downloadPath)) {
            return ResultGenerator.genFailResult("生成文件失败!");
        } else {
            return ResultGenerator.genSuccessResult(downloadPathForNginx);
        }
    }

    void insertRow(HSSFSheet sheet, int starRow, int rows) {
        sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
        starRow = starRow - 1;
        for (int i = 0; i < rows; i++) {
            HSSFRow sourceRow = null;
            HSSFRow targetRow = null;
            HSSFCell sourceCell = null;
            HSSFCell targetCell = null;
            short m;
            starRow = starRow + 1;
            sourceRow = sheet.getRow(starRow);
            targetRow = sheet.createRow(starRow + 1);
            targetRow.setHeight(sourceRow.getHeight());

            for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {
                System.out.println("m:------>" + m);
                sourceCell = sourceRow.getCell(m);
                targetCell = targetRow.createCell(m);

                targetCell.setCellStyle(sourceCell.getCellStyle());
            }

        }
    }

    String getResultString(Integer resultIndex) {
        String res = "未审核";
        if (resultIndex == Constant.SIGN_APPROVE) {
            res = "接受";
        } else if (resultIndex == Constant.SIGN_REJECT) {
            res = "驳回";
        } else {
            res = "无需审核";
        }
        return res;
    }

    String getStringValue(Object obj) {
        String res = "";
        if (obj != null) {
            return obj.toString();
        }
        return res;
    }

    String getCellStringValue(HSSFCell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:// 字符串类型
            cellValue = cell.getStringCellValue();
            if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
                cellValue = " ";
            break;
        case HSSFCell.CELL_TYPE_NUMERIC: // 数值类型
            cellValue = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_FORMULA: // 公式
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellValue = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            cellValue = " ";
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            break;
        case HSSFCell.CELL_TYPE_ERROR:
            break;
        default:
            break;
        }
        return cellValue;
    }
}

package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.role.Role;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.DeviceServiceImpl;
import com.eservice.api.service.impl.RoleServiceImpl;
import com.eservice.api.service.impl.UserServiceImpl;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @Resource
    private DeviceServiceImpl deviceService;

    @Resource
    private MqttMessageHelper mqttMessageHelper;
    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private CommonService commonService;
    /**
     * 该值为default值， Android端传入的参数不能为“0”
     */
    private static String ZERO_STRING = "0";
    private Logger logger = Logger.getLogger(UserController.class);

    @PostMapping("/add")
    public Result add(String user) {
        User userObj = JSON.parseObject(user, User.class);
        if(userObj.getPassword() == null) {
            return ResultGenerator.genFailResult("密码不存在!");
        } else if (userObj.getAccount() == null || userObj.getAccount().isEmpty()) {
            return ResultGenerator.genFailResult("用户名不存在或为空！");
        } else if(userService.selectUsers(userObj.getAccount(),null,null,null,null).size() > 0){
            return ResultGenerator.genFailResult("账号已存在！");
        }else {
            userService.save(userObj);
            return ResultGenerator.genSuccessResult();
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        userService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String user) {
        User user1 = JSON.parseObject(user,User.class);
        ///防止插入空密码
        if("".equals(user1.getPassword())) {
            user1.setPassword(null);
        }
        if(user1.getGroupId() == null) {
            user1.setGroupId(0);
        }
        userService.update(user1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        User user = userService.findById(id);
        return ResultGenerator.genSuccessResult(user);
    }

    @PostMapping("/allDetail")
    public Result getUserAllDetail(@RequestParam Integer id) {
        UserDetail user = userService.getUserAllDetail(id);
        return ResultGenerator.genSuccessResult(user);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<User> list = userService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    @PostMapping("/selectUsers")
    public Result selectUsers(@RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "0") Integer size,
                              String account,
                              String name,
                              Integer roleId,
                              Integer groupId,
                              Integer valid) {
        PageHelper.startPage(page, size);
        List<UserDetail> list = userService.selectUsers(account,name,roleId,groupId,valid);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectAllQuality")
    public Result selectAllQuality(@RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        Condition tempCondition = new Condition(Role.class);
        tempCondition.createCriteria().andCondition("role_name = ", "质检员");
        List<Role> roleList = roleService.findByCondition(tempCondition);
        //这里正常情况下，list中有且仅有一个
        if(roleList != null && roleList.size()>0) {
            Condition userCondition = new Condition(User.class);
            userCondition.createCriteria().andCondition("role_id = ", roleList.get(0).getId());
            List<User> userList = userService.findByCondition(userCondition);
            PageInfo pageInfo = new PageInfo(userList);
            return ResultGenerator.genSuccessResult(pageInfo);
        } else {
            return ResultGenerator.genFailResult("质检员角色没有设置!");
        }

    }

    /**
     *
     * @param account
     * @param password
     * @param meid
     * @param isExtranet 如果是外网，则该参数有值true， 没有该参数，则是内网。 其他原有接口不影响
     * @return
     */
    @PostMapping("/requestLogin")
    public Result requestLogin(@RequestParam String account,
                               @RequestParam String password,
                               @RequestParam(defaultValue = "0") String meid,
                               @RequestParam(defaultValue = "false") Boolean isExtranet) {

        User user = userService.selectByAccount(account);
        if(user == null){
            return ResultGenerator.genFailResult(account + " 不存在！");
        } else if(user.getValid() == Constant.INVALID){
            return ResultGenerator.genFailResult("禁止登录，" + account + " 已设为离职");
        }

        boolean isPermitted = false;

        if(isExtranet) {
            logger.info(account + " 外网登录");
            Condition condition = new Condition(User.class);
            condition.createCriteria().andEqualTo("extranetPermit", 1);
            List<User> userAllowExtranetList = userService.findByCondition(condition);

            for (int i = 0; i < userAllowExtranetList.size(); i++) {
                if (userAllowExtranetList.get(i).getAccount().equals(account)) {
                    isPermitted = true;
                    break;
                }

            }
            if(!isPermitted){
                return ResultGenerator.genFailResult(account + " 用户没有外网登入权限！");
            }
        } else {
            logger.info(account + " 内网登录");
        }

        if(account == null || "".equals(account)) {
            return ResultGenerator.genFailResult("账号不能为空！");
        } else if(password == null || "".equals(password)) {
            return ResultGenerator.genFailResult("密码不能为空！");
        }else {
            //移动端MEID值需要传入，且不为“0”
            if(!ZERO_STRING.equals(meid)) {
                if(deviceService.findDeviceByMEID(meid) == null) {
                    return ResultGenerator.genFailResult("设备没有登陆权限！");
                }
            }
            UserDetail userDetail = userService.requestLogin(account, password);
            if(userDetail == null) {
                logger.info(account + "login 账号或密码不正确");
                return ResultGenerator.genFailResult("账号或密码不正确！");
            }else {
                logger.info(account + "login success");
                ///mqttMessageHelper.sendToClient("topic/client/2", JSON.toJSONString(userDetail));
                return ResultGenerator.genSuccessResult(userDetail);
            }
        }
    }

    /**
     * 根据账号返回User
     * @param account
     * @return
     */
    @PostMapping("/selectByAccount")
    public Result selectByAccount(@RequestParam String account) {
        User user = userService.selectByAccount(account);
        return ResultGenerator.genSuccessResult(user);
    }

    /**
     * 根据user的id号，获得该用户所在的install_group的所有在职安装工.
     * @param id
     * @return
     */
    @PostMapping("/selectAllInstallGroupByUserId")
    public Result selectAllInstallGroupByUserId(@RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "0") Integer size,
                                                @RequestParam Integer id) {
        PageHelper.startPage(page, size);
        List<UserDetail> list = userService.selectAllInstallGroupByUserId(id);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //测试 推送信息给售后
    @PostMapping("/sendSignInfoViWxMsg")
    public Result sendSignInfoViWxMsg(@RequestParam String account) {
        String result = commonService.sendSignInfoViWxMsg(account,"11","22");
        return ResultGenerator.genSuccessResult(result);
    }

}

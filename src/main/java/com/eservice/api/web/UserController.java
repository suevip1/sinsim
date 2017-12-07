package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.impl.DeviceServiceImpl;
import com.eservice.api.service.impl.UserServiceImpl;
import com.eservice.api.service.mqtt.MqttMessageHelper;
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
    MqttMessageHelper mqttMessageHelper;

    /**
     * 该值为default值， Android端传入的参数不能为“0”
     */
    private static String ZERO_STRING = "0";

    @PostMapping("/add")
    public Result add(String user) {
        User userObj = JSON.parseObject(user, User.class);
        if(userObj.getPassword() == null) {
            return ResultGenerator.genFailResult("密码不存在!");
        } else if (userObj.getAccount() == null || userObj.getAccount().isEmpty()) {
            return ResultGenerator.genFailResult("用户名不存在或为空！");
        } else {
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

    @PostMapping("/requestLogin")
    public Result requestLogin(@RequestParam String account, @RequestParam String password, @RequestParam(defaultValue = "0") String meid) {
        boolean result = true;

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
                return ResultGenerator.genFailResult("账号或密码不正确！");
            }else {
                ///mqttMessageHelper.sendToClient("topic/client/2", JSON.toJSONString(userDetail));
                return ResultGenerator.genSuccessResult(userDetail);
            }
        }
    }
}

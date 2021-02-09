package com.eservice.api.service.impl;

import com.eservice.api.dao.UserMapper;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.UserService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

    public UserDetail getUserAllDetail(Integer id) {
        return userMapper.getUserAllDetail(id);
    }

    public List<UserDetail> selectUsers(String account, String name, Integer roleId, Integer groupId, String marketGroupName, Integer valid) {
        return userMapper.selectUsers(account, name, roleId, groupId, marketGroupName, valid);
    }

    public UserDetail requestLogin(String account, String password) {
        return userMapper.requestLogin(account, password);
    }

    public User selectByAccount(String account){
        return userMapper.selectByAccount(account);
    }

    public List<UserDetail> selectAllInstallGroupByUserId(Integer id){
        return userMapper.selectAllInstallGroupByUserId(id);
    }
    public List<User> getUsersHaveOptimizePermit(String thatPermit){
        return userMapper.getUsersHaveOptimizePermit(thatPermit);
    }
}

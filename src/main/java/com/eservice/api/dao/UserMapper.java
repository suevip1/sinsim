package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends Mapper<User> {
    UserDetail getUserAllDetail(@Param("id") Integer id );

    UserDetail requestLogin(@Param("account")String account, @Param("password")String password);
}
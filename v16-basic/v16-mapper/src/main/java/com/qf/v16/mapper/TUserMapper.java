package com.qf.v16.mapper;

import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser>{
    TUser selectByUsername(String username);
}
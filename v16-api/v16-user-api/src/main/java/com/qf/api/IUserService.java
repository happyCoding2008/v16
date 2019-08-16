package com.qf.api;

import com.qf.v16.common.base.IBaseService;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TUser;

/**
 * @author huangguizhao
 */
public interface IUserService extends IBaseService<TUser>{

    /**
     * 登录验证
     * @param user 账号密码信息
     * @return data 返回uuid
     */
    public ResultBean checkLogin(TUser user);

    /**
     * 验证当前用户的登录状态
     * @param uuid 保存客户端的凭证信息
     * @return
     */
    public ResultBean checkIsLogin(String uuid);

    /**
     * 注销当前用户的登录状态，删除redis中的凭证
     * @param uuid
     * @return
     */
    public ResultBean logout(String uuid);

    /**
     * 解析令牌，获取令牌里面存储的id信息
     * @param token
     * @return
     */
    public ResultBean parseTokenGetId(String token);
}

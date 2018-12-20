package cn.liucw.shiro.service;

import cn.liucw.shiro.model.User;

/**
 * @author liucw
 * @version 1.0
 * @date 2017/12/3
 */
public interface UserService {
    User getByUserName(String userName);
}

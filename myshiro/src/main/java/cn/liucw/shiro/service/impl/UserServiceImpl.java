package cn.liucw.shiro.service.impl;

import cn.liucw.shiro.model.User;
import cn.liucw.shiro.model.UserCriteria;
import cn.liucw.shiro.model.mapper.UserMapper;
import cn.liucw.shiro.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liucw
 * @version 1.0
 * @date 2017/12/3
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
//
//    @Autowired
//    private UserMapper userMapper;

    @Override
    public User getByUserName(String userName) {
//        UserCriteria criteria = new UserCriteria();
//        criteria.createCriteria()
//                .andUsernameEqualTo(userName)
//                .andStatusEqualTo(1);
//
//        List<User> resultList = userMapper.selectByExample(criteria);
//        return CollectionUtils.isNotEmpty(resultList) ? resultList.get(0) : null;
        return null;
    }

/*    public List<User> getRoles(String userName) {
        String sql = "";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        return userMapper.selectListBySql(sql, paramMap);
    }

    public Set<String> getPermissions(String userName) {
        return userDao.getPermissions(userName);
    }*/
}

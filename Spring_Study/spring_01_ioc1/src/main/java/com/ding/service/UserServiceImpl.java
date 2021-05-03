package com.ding.service;

import com.ding.dao.UserDao;
import com.ding.dao.UserDaoMySqlImpl;

public class UserServiceImpl implements UserService{

    private UserDao userDao;

    // 利用set进行动态实现值的注入
    public void setUserDao(UserDao userDao) {
        this.userDao =  userDao;
    }

    public void getUser() {
        userDao.getUser();
    }
}

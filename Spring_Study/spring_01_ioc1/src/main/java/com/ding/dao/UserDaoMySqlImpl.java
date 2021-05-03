package com.ding.dao;

public class UserDaoMySqlImpl implements UserDao{
    @Override
    public void getUser() {
        System.out.println("Mysql获取用户数据");
    }
}

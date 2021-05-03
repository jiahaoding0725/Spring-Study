package com.ding.mapper;

import com.ding.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl extends SqlSessionDaoSupport implements UserMapper {
    public List<User> selectUser() {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.getMapper(UserMapper.class).selectUser();
    }

    @Override
    public int addUser(User user) {
        return  getSqlSession().getMapper(UserMapper.class).addUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return  getSqlSession().getMapper(UserMapper.class).deleteUser(id);
    }
}

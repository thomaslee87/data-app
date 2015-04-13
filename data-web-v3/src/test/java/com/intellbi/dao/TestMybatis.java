package com.intellbi.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.intellbit.dao.UserDao;
import com.intellbit.dataobject.UserDO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
public class TestMybatis {

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetAll() {
        List<UserDO> users = userDao.findAll();
        for(UserDO user : users) {
            System.out.println(user);
        }
    }
}
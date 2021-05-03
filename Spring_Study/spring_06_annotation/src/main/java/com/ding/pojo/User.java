package com.ding.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// 等价于 <bean id="user" class="com.ding.pojo.User"/>
@Component
@Scope("prototype")
public class User {
    @Value("丁家豪")
    public String name;
}

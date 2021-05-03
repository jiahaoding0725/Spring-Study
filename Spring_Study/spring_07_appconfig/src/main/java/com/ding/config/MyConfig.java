package com.ding.config;

import com.ding.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// 这也也会被Spring托管，注册到容器中，
// 因为它也是一个@Component，@Configuration就是一个配置类。

@Configuration
@ComponentScan("com.ding.pojo")
@Import("MyConfig2")
public class MyConfig {

    // 方法的名字 相当于 bean中的id属性
    // 方法的返回值 相当于bean标签中的class属性
    @Bean
    public User getUser(){
        return new User();
    }
}

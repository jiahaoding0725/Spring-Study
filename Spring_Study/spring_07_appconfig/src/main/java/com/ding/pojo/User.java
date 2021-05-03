package com.ding.pojo;

import org.springframework.beans.factory.annotation.Value;

public class User {
    private String name;


    public String getName() {
        return name;
    }

    @Value("丁家豪")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}

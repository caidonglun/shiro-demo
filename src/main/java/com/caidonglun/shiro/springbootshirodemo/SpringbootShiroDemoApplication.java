package com.caidonglun.shiro.springbootshirodemo;

import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringbootShiroDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShiroDemoApplication.class, args);
    }

    @Bean
    public SimpleAuthorizationInfo getSimpleAuthorizationInfo(){
        return  new SimpleAuthorizationInfo();
    }

}

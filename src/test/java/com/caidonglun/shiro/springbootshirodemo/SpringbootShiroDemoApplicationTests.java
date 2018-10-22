package com.caidonglun.shiro.springbootshirodemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootShiroDemoApplicationTests {

    Logger logger=LoggerFactory.getLogger(getClass());

    @Test
    public void contextLoads(HttpServletRequest request, HttpServletResponse response) {

    }

}

package com.caidonglun.shiro.springbootshirodemo.dao;

import com.caidonglun.shiro.springbootshirodemo.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface StudentDao {

    @Select("select * from student where username=#{username}")
    Student findStuent(String username);

}

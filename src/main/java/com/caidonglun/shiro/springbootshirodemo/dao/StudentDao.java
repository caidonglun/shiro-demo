package com.caidonglun.shiro.springbootshirodemo.dao;

import com.caidonglun.shiro.springbootshirodemo.entity.Permission;
import com.caidonglun.shiro.springbootshirodemo.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface StudentDao {

    @Select("select * from student where username=#{username}")
    Student findStuent(String username);

    @Select("SELECT roleName,permissionName FROM users u,t_role r,t_permission p WHERE u.roleId=r.id AND p.`roleId`=r.`id` AND username=#{arg0}")
    Permission findPermissions(String name);
}

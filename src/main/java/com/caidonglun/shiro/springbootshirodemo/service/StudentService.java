package com.caidonglun.shiro.springbootshirodemo.service;

import com.caidonglun.shiro.springbootshirodemo.dao.StudentDao;
import com.caidonglun.shiro.springbootshirodemo.entity.Permission;
import com.caidonglun.shiro.springbootshirodemo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StudentService {
    @Autowired
    StudentDao studentDao;

    public Student findStudent(String username) {

        return  studentDao.findStuent(username);
    }



    public Student addUser(Map<String,Object> map) {
        return new Student();
    }

    public Permission finRoleAndPermission(String name) {
         return studentDao.findPermissions(name);

    }
}

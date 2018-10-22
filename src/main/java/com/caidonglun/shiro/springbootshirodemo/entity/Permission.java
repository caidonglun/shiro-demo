package com.caidonglun.shiro.springbootshirodemo.entity;

import java.io.Serializable;

public class Permission implements Serializable {

    private String roleName;
    private String permissionName;

    @Override
    public String toString() {
        return "Permission{" +
                "roleName='" + roleName + '\'' +
                ", permissionName='" + permissionName + '\'' +
                '}';
    }

    public Permission() {
    }

    public Permission(String roleName, String permissionName) {
        this.roleName = roleName;
        this.permissionName = permissionName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}

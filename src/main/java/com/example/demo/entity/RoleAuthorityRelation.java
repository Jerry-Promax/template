package com.example.demo.entity;

import lombok.Data;

/**
 * 角色权限关系实体类
 * <p>
 * 作者：jerry
 * 日期：2025-07-24
 */
@Data
public class RoleAuthorityRelation {
    private Long id;
    private Long roleId; // 角色ID
    private Long authorityId; // 权限ID
}
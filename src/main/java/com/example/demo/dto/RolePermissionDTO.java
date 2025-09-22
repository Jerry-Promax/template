package com.example.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-22
 */
@Data
public class RolePermissionDTO {
    private Integer roleId;
    private List<Integer> permissionIds;
}
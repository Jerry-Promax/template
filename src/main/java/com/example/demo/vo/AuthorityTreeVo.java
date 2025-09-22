package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityTreeVo {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<AuthorityTreeVo> children; // 子权限列表
}

package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityVO {
    private Integer id;
    private String name;
    private String desc;
    private Integer type;
    private Long parentId;
    private String parentName; // 父级权限名称
    private String authority; // 资源路径
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createAt;
}
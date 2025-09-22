package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-18
 */
@Accessors(chain = true)
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 8444473027670783298L;

    private Integer id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色描述
     */
    private String desc;

    /**
     * 审计字段，创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createAt;
}
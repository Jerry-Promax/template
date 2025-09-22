package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-18
 */
@Data
@Accessors(chain = true)
public class Authority implements GrantedAuthority{
    private static final long serialVersionUID = -8770868540836182134L;
    /**
     * 权限id
     */
    private Integer id;

    /**
     * 父权限id
     */
    private Integer parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String desc;

    /**
     * 权限资源,当type为1时有值
     */
    private String authority;

    /**
     * 权限类型:0 菜单；1 接口权限
     */
    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createAt;
    @Override
    public String getAuthority() {
        return this.authority;
    }
}
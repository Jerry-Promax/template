package com.example.demo.service;

import com.example.demo.common.Page;
import com.example.demo.entity.Authority;
import com.example.demo.entity.Role;
import com.example.demo.vo.AuthorityTreeVo;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
public interface RoleService {

    Page selectAllRole(Integer pageNum, Integer pageSize, String username);

    void addRole(Role role);

    void deleteRole(Integer id);

    void updateRole(Role role);

    List<AuthorityTreeVo> ownAuthority(Integer rid);

    void assignAuthority(Integer rid,List<Integer> ids);
}

package com.example.demo.service.impl;

import com.example.demo.common.Page;
import com.example.demo.entity.Authority;
import com.example.demo.entity.Role;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.service.RoleService;
import com.example.demo.vo.AuthorityTreeVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Override
    public Page selectAllRole(Integer pageNum, Integer pageSize, String roleName) {
        Integer skipNum = 0;
        if (pageNum != null && pageSize != null ) {
            skipNum = (pageNum-1)*pageSize;
        }

        List<Role> roleList = roleMapper.selectAllRole(skipNum, pageSize, roleName);
        Integer count = roleMapper.totalCount(roleName);
        Page page = new Page();
        page.setList(roleList);
        page.setTotal(count);
        return page;
    }

    @Override
    public void addRole(Role role) {
        roleMapper.addRole(role);
    }

    @Override
    public void deleteRole(Integer id) {
        roleMapper.deleteRole(id);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateRole(role);
    }

    @Override
    public List<AuthorityTreeVo> ownAuthority(Integer rid) {
        // 1. 获取角色拥有的所有权限ID
        List<Integer> ownAuthorityIds = roleMapper.ownAuthority(rid);

        // 2. 获取完整的权限树结构
        List<AuthorityTreeVo> allAuthorities = roleMapper.selectAllAuthorities();

        // 3. 过滤出叶子节点权限
        List<AuthorityTreeVo> leafAuthorities = new ArrayList<>();
        for (Integer authorityId : ownAuthorityIds) {
            // 在完整权限树中查找该权限
            AuthorityTreeVo auth = findAuthorityById(allAuthorities, authorityId);
            if (auth != null) {
                // 如果是叶子节点（没有子节点），添加到结果
                if (isLeafNode(auth, allAuthorities)) {
                    leafAuthorities.add(auth);
                }
            }
        }
        return leafAuthorities;
    }

    // 辅助方法：在权限树中查找指定ID的权限
    private AuthorityTreeVo findAuthorityById(List<AuthorityTreeVo> authorities, Integer id) {
        for (AuthorityTreeVo auth : authorities) {
            if (auth.getId().equals(id)) return auth;
            if (auth.getChildren() != null) {
                AuthorityTreeVo found = findAuthorityById(auth.getChildren(), id);
                if (found != null) return found;
            }
        }
        return null;
    }

    // 辅助方法：判断是否是叶子节点
    private boolean isLeafNode(AuthorityTreeVo auth, List<AuthorityTreeVo> allAuthorities) {
        // 如果权限对象本身有children属性
        if (auth.getChildren() != null && !auth.getChildren().isEmpty()) {
            return false;
        }

        // 或者在完整权限树中检查是否有子节点
        AuthorityTreeVo fullAuth = findAuthorityById(allAuthorities, auth.getId());
        return fullAuth.getChildren() == null || fullAuth.getChildren().isEmpty();
    }

    @Transactional
    @Override
    public void assignAuthority(Integer rid,List<Integer> ids) {
        // 先删除原有权限
        roleMapper.deleteAuthority(rid);
        // 再批量插入
        for (Integer aid:ids) {
            roleMapper.assignAuthority(aid,rid);
        }
    }
}

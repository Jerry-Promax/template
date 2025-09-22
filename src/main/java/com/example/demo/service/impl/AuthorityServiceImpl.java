package com.example.demo.service.impl;

import com.example.demo.common.Page;
import com.example.demo.entity.Authority;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.service.AuthorityService;
import com.example.demo.vo.AuthorityTreeVo;
import com.example.demo.vo.AuthorityVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
@Service
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    private AuthorityMapper authorityMapper;
    @Override
    public Page selectAllAuthority(Integer pageNum, Integer pageSize, String authorityName) {
        Integer skipNum = (pageNum-1)*pageSize;
        List<AuthorityVO> authorities = authorityMapper.selectAllAuthorityByPage(skipNum,pageSize,authorityName);
        for (AuthorityVO authority: authorities) {
            Long parentId = authority.getParentId();
            if (parentId!=null){
                String parentName = authorityMapper.getParentName(parentId);
                authority.setParentName(parentName);
            }
        }
        Integer count = authorityMapper.count(authorityName);
        Page page = new Page();
        page.setTotal(count);
        page.setList(authorities);
        return page;
    }

    @Override
    public List<AuthorityVO> selectAllAuthorityParents() {
        return authorityMapper.getParentPermissions();
    }

    @Override
    public int addAuthority(Authority authority) {
        // 判断是否有父级权限
        if (authority.getParentId()!=null){
            // 查询父级权限的最大子权限主键
            String parentIdStr = String.valueOf(authority.getParentId());
            String maxChildId = authorityMapper.findMaxChildId(parentIdStr);
            // 生成新主键（父级主键 + 两位顺序编码）
            if (maxChildId == null) {
                // 父级下无子权限，从 01 开始
                authority.setId(Integer.parseInt(parentIdStr + "01"));
            }else {
                // 父级下有子权限，取最后两位 +1
                int seq = Integer.parseInt(maxChildId.substring(parentIdStr.length())) + 1;
                authority.setId(Integer.parseInt(parentIdStr + String.format("%02d", seq)));
            }
        }else {
            // 顶级权限：直接用自增或手动维护（这里假设手动维护，或用数据库自增）
            // 示例：查询当前最大顶级主键 +1（需根据实际逻辑调整）
            Integer maxTopId = authorityMapper.findMaxTopId();
            authority.setId(maxTopId + 1);
        }
        return authorityMapper.addAuthority(authority);
    }

    @Override
    public void deleteAuthority(Integer id) {
        authorityMapper.deleteAuthority(id);
    }

    @Override
    public void updateAuthority(Authority authority) {
        authorityMapper.updateAuthority(authority);
    }

    /**
     * 构建权限树
     */
    @Override
    public List<AuthorityTreeVo> buildAuthorityTree() {
        List<AuthorityTreeVo> allAuthority = authorityMapper.selectAllAuthority();
        List<AuthorityTreeVo> rootNodes = allAuthority.stream()
                .filter(authorityTreeVo -> authorityTreeVo.getParentId() == null)
                .collect(Collectors.toList());
        for (AuthorityTreeVo root:rootNodes) {
            buildChildren(root, allAuthority);
        }
        return rootNodes;
    }

    private void buildChildren(AuthorityTreeVo parent, List<AuthorityTreeVo> allAuthority) {
        List<AuthorityTreeVo> children = allAuthority.stream()
                .filter(authorityTreeVo -> parent.getId().equals(authorityTreeVo.getParentId()))
                .collect(Collectors.toList());
//        log.info("所有节点:{}",allAuthority);
//        log.info("父节点:{}",parent);
//        log.info("子节点：{}",children);
        for (AuthorityTreeVo child: children) {
            buildChildren(child,allAuthority);
        }
        parent.setChildren(children);
    }

}
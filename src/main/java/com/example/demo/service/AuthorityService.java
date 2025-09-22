package com.example.demo.service;

import com.example.demo.common.Page;
import com.example.demo.entity.Authority;
import com.example.demo.vo.AuthorityTreeVo;
import com.example.demo.vo.AuthorityVO;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
public interface AuthorityService {
    Page selectAllAuthority(Integer pageNum, Integer pageSize, String roleName);

    List<AuthorityVO> selectAllAuthorityParents();

    int addAuthority(Authority authority);

    void deleteAuthority(Integer id);

    void updateAuthority(Authority authority);

    List<AuthorityTreeVo> buildAuthorityTree();
}

package com.example.demo.service.impl;

import com.example.demo.entity.Authority;
import com.example.demo.entity.Manager;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.ManageMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现的是内部类，已踩坑
 * <p>
 * 作者：jerry
 * 日期：2025-07-18
 */
@Service
public class ManagerServiceImpl implements UserDetailsService {

    @Resource
    private ManageMapper manageMapper;
    @Resource
    private AuthorityMapper authorityMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        Manager manager = manageMapper.findByUsername(s);
        // 判断是否查到用户，没查到抛出异常
        if (manager == null) {
            throw new UsernameNotFoundException("未找到用户名为 " + s + " 的用户");
        }
        // 再根据id来查询用户的权限，最后将查到的用户封装返回
        List<Authority> authorities = authorityMapper.findById(manager.getId());
        manager.setAuthorities(authorities);
        return manager;
    }
}
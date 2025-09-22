package com.example.demo.service.impl;

import com.example.demo.common.BaseContext;
import com.example.demo.common.Page;
import com.example.demo.common.StatusConstant;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Authority;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.TokenUtils;
import com.example.demo.vo.AuthorityVO;
import com.example.demo.vo.UserVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenUtils tokenUtils;
    @Resource
    private ManagerServiceImpl managerService;
    @Resource
    private AuthorityMapper authorityMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserVo login(UserDto user) {
        User dbUser = userMapper.selectByUsernameExact(user.getUsername());
        if (dbUser == null) {
            throw new ServiceException("账号不存在");
        }
        if (!user.getPassword().equals(dbUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        if (!Objects.equals(dbUser.getStatus(), StatusConstant.NORMAL)) {
            throw new ServiceException("用户未审核或为黑名单用户");
        }
        // 4. 加载用户详情（包含权限）
        UserDetails userDetails = managerService.loadUserByUsername(user.getUsername());
        // 5. 生成JWT令牌
        String token = tokenUtils.genToken(userDetails);
        log.info("生成的原始 Token: {}", token);
        List<String> authoritiesList = authorityMapper.findById(dbUser.getId()).stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());
        return UserVo.builder()
                        .token(token)
                                .user(dbUser)
                                        .authorities(authoritiesList)
                                            .build();
    }

    @Override
    public void register(User user) {
        User dbUser = userMapper.selectByUsernameExact(user.getUsername());
        if (dbUser != null) {
            throw new ServiceException("用户名已存在");
        }
        userMapper.insertUser(user);
    }

    @Override
    public Page selectAllUser(Integer pageNum, Integer pageSize,String username,Integer status) {
        Integer skipNum = (pageNum-1)*pageSize; // num:1,size5,skip:0,  2,5,10
        List<User> userList = userMapper.selectAllUser(skipNum,pageSize,username,status);
        Integer count = userMapper.selectCountByPage(username,status);
        Page page = new Page();
        page.setList(userList);
        page.setTotal(count);
        return page;
    }

    @Transactional
    @Override
    public void updatePerson(User user) {
        // 还需要校验修改个人的信息是否合法，后续在做
        userMapper.updatePerson(user);
        List<Role> role = roleMapper.selectAllRole(null, null, user.getRole());
        userMapper.updatePersonRoleRel(user.getId(),role.get(0).getId());
    }

    @Override
    public void resetPwd(UserDto userDto) {
        // 先根据前端传来的数据来查找数据库对应的用户
         User dbUser = userMapper.selectByUsernameExact(userDto.getUsername());
        // 然后判断用户是否存在
        if (dbUser == null) {
            throw new ServiceException("用户不存在");
        }
        // 接下来判断用户的手机号是否一致
        if (!userDto.getTel().equals(dbUser.getTel())){
            throw new ServiceException("手机号错误");
        }
        // 最后如果数据都正确，则重置密码
        dbUser.setPassword("123456");
        userMapper.updatePerson(dbUser);
    }

    @Override
    public void addUser(User user) {
        // 可以自己选择校验数据是否合法
        List<String> existUsername = userMapper.selectAllUserName();
        for (String username:existUsername) {
            if (user.getUsername().equals(username)) {
                throw new ServiceException("用户名已存在");
            }
        }
        userMapper.insertUser(user);
        List<Role> role = roleMapper.selectAllRole(null, null, user.getRole());
        userMapper.insertPersonRoleRel(user.getId(),role.get(0).getId());
    }

    @Override
    public void deleteUser(Integer id) {
//        User currentUser = tokenUtils.getCurrentUser();
//        Long currentId = Long.valueOf(currentUser.getId());
        log.info("当前用户的id为:{}",BaseContext.getCurrentId());
        if (Objects.equals(id, BaseContext.getCurrentId())){
            throw new ServiceException("不能删除当前用户");
        }
        userMapper.deleteUser(id);
    }

    @Override
    public void deleteBatchUser(List<Integer> ids) {
        for (Integer id:ids) {
            if (!Objects.equals(id, BaseContext.getCurrentId())){
                userMapper.deleteUser(id);
            }else{
                throw new ServiceException("不能删除当前用户");
            }
        }
    }
    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public void auditUser(Integer id) {
        userMapper.auditUser(id);
    }

    @Override
    public void blackUser(Integer id, boolean isBlack) {
        userMapper.blackUser(id,isBlack ? StatusConstant.NORMAL : StatusConstant.DISABLE);
    }

}
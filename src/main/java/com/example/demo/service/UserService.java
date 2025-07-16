package com.example.demo.service;

import com.example.demo.common.Page;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */

public interface UserService {
    User login(UserDto user);

    void register(User user);

    Page selectAllUser(Integer PageNum, Integer pageSize,String username,Integer status);

    void updatePerson(User user);

    void resetPwd(UserDto userDto);

    void addUser(User user);

    void deleteUser(Long id);

    void deleteBatchUser(List<Long> ids);

    List<User> selectAll();

    void auditUser(Integer id);

    void blackUser(Integer id, boolean isBlack);

//    User selectByUsername(String username);
}

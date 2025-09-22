package com.example.demo.service;

import com.example.demo.common.Page;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Manager;
import com.example.demo.entity.User;
import com.example.demo.vo.UserVo;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */

public interface UserService {
    UserVo login(UserDto user);

    void register(User user);

    Page selectAllUser(Integer PageNum, Integer pageSize,String username,Integer status);

    void updatePerson(User user);

    void resetPwd(UserDto userDto);

    void addUser(User user);

    void deleteUser(Integer id);

    void deleteBatchUser(List<Integer> ids);

    List<User> selectAll();

    void auditUser(Integer id);

    void blackUser(Integer id, boolean isBlack);
}

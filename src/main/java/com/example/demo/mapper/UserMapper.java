package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@Mapper
public interface UserMapper {
    @Select("select * from all_user where username = #{username}")
    User selectByUsernameExact(String username);

    void insertUser(User user);

    @Select("select * from all_user where id = #{userId}")
    User selectById(Integer userId);

    List<User> selectAllUser(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize,@Param("username") String username,@Param("status") Integer status);
    Integer selectCountByPage(@Param("username") String username,@Param("status") Integer status);

    void updatePerson(User user);

    @Select("select username from all_user ")
    List<String> selectAllUserName();

    @Delete("delete from all_user where id = #{id}")
    void deleteUser(Long id);

    @Select("select * from all_user;")
    List<User> selectAll();

    @Update("update all_user set status = 2 where id = #{id};")
    void auditUser(Integer id);

    @Update("update all_user set status = #{status} where id = #{id};")
    void blackUser(@Param("id") Integer id,@Param("status") Integer status);

//    User selectByUsername(String username);
}

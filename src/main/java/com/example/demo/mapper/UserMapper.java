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

//    @Options(useGeneratedKeys = true, keyProperty = "uid")
    void insertUser(User user);

    @Select("select * from all_user where id = #{userId}")
    User selectById(Integer userId);

    List<User> selectAllUser(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize,@Param("username") String username,@Param("status") Integer status);
    Integer selectCountByPage(@Param("username") String username,@Param("status") Integer status);

    void updatePerson(User user);

    @Select("select username from all_user ")
    List<String> selectAllUserName();

    @Delete("delete from all_user where id = #{id}")
    void deleteUser(Integer id);

    @Select("select * from all_user;")
    List<User> selectAll();

    @Update("update all_user set status = 2 where id = #{id};")
    void auditUser(Integer id);

    @Update("update all_user set status = #{status} where id = #{id};")
    void blackUser(@Param("id") Integer id,@Param("status") Integer status);

//    @Select("select * from all_user where username = #{username};")
//    Manager selectByUsername(String username);
    @Update("update ss_user_role_rel set rid = #{rid} where uid = #{uid};")
    void updatePersonRoleRel(Integer uid, Integer rid);
    @Insert("insert into ss_user_role_rel (rid, uid) values (#{rid},#{uid});")
    void insertPersonRoleRel(Integer uid, Integer rid);
}

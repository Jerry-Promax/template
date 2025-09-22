package com.example.demo.mapper;


import com.example.demo.entity.Authority;
import com.example.demo.entity.Role;
import com.example.demo.vo.AuthorityTreeVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
@Mapper
public interface RoleMapper {
    List<Role> selectAllRole(@Param("pageNum") Integer skipNum, @Param("pageSize") Integer pageSize, @Param("roleName") String roleName);

    Integer totalCount(String roleName);

    @Insert("insert into ss_role (name, `desc`) values (#{name},#{desc});")
    void addRole(Role role);

    @Delete("delete from ss_role where id = #{id};")
    void deleteRole(Integer id);

    @Update("update ss_role set name = #{name},`desc`= #{desc} where id = #{id};")
    void updateRole(Role role);

    @Select("select authority_id from ss_authority_role_rel where role_id = #{rid};")
    List<Integer> ownAuthority(Integer rid);

    @Select("select * from ss_authority where id = #{authorityId};")
    @Result(column = "resource",property = "authority")
    Authority selectById(Integer authorityId);

    @Delete("delete from ss_authority_role_rel where role_id = #{rid};")
    void deleteAuthority(Integer rid);

    @Insert("insert into ss_authority_role_rel (authority_id, role_id) values (#{aid},#{rid});")
    void assignAuthority(Integer aid, Integer rid);

    List<AuthorityTreeVo> selectAllAuthorities();
}

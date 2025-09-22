package com.example.demo.mapper;

import com.example.demo.entity.Authority;
import com.example.demo.vo.AuthorityTreeVo;
import com.example.demo.vo.AuthorityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-18
 */
@Mapper
public interface AuthorityMapper {
    // 通过用户id来查找其所拥有的权限
    List<Authority> findById(Integer id);

    List<AuthorityVO> selectAllAuthorityByPage(@Param("pageNum") Integer skipNum, @Param("pageSize") Integer pageSize,@Param("authorityName") String authorityName);

    Integer count(String authorityName);

    List<AuthorityVO> getParentPermissions();

    @Select("select name from ss_authority where id = #{id};")
    String getParentName(Long id);

    int addAuthority(Authority authority);

    void deleteAuthority(Integer id);

    void updateAuthority(Authority authority);

    // 查找最大子主键
    @Select("select MAX(id) from ss_authority where parent_id = #{parentIdStr} and id like concat(#{parentIdStr},'__')")
    String findMaxChildId(String parentIdStr);

    // 查找当前最大的顶级权限主键
    @Select("SELECT MAX(id) FROM ss_authority WHERE parent_id IS NULL")
    Integer findMaxTopId();

    @Select("select * from ss_authority;")
    List<AuthorityTreeVo> selectAllAuthority();
}

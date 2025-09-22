package com.example.demo.mapper;

import com.example.demo.entity.Logs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-26
 */
@Mapper
public interface LogsMapper {
    @Select("select * from logs WHERE IF(#{operation} IS NOT NULL, operation = #{operation}, 1=1) limit #{skipNum},#{pageSize};")
    List<Logs> selectAll(Integer skipNum, Integer pageSize, String operation);

    @Delete("delete from logs where id = #{id};")
    void deleteLogs(Integer id);

    @Select("select count(*) from logs WHERE IF(#{operation} IS NOT NULL, operation = #{operation}, 1=1);")
    Integer selectCountByPage(String operation);

    @Insert("insert into logs (operation, type, ip, user, time) values (#{operation},#{type},#{ip},#{user},#{time});")
    void insert(Logs logs);
}

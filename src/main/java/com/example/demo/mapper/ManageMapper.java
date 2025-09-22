package com.example.demo.mapper;

import com.example.demo.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-18
 */
@Mapper
public interface ManageMapper {

    Manager findByUsername(String s);
}
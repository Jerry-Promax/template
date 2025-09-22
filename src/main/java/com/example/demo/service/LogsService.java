package com.example.demo.service;

import com.example.demo.common.Page;
import com.example.demo.entity.Logs;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */

public interface LogsService {

    void deleteBatch(List<Integer> ids);

    void deleteLog(Integer id);

    Page selectAll(Integer pageNum, Integer pageSize, String operation);

    void save(Logs logs);
}

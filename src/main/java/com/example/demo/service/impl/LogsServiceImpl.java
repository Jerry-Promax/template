package com.example.demo.service.impl;

import com.example.demo.common.Page;
import com.example.demo.entity.Logs;
import com.example.demo.mapper.LogsMapper;
import com.example.demo.service.LogsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
@Service
@Slf4j
public class LogsServiceImpl implements LogsService {
    @Resource
    private LogsMapper logsMapper;

    @Override
    public void deleteBatch(List<Integer> ids) {
        for (Integer id:ids) {
            logsMapper.deleteLogs(id);
        }
    }

    @Override
    public void deleteLog(Integer id) {
        logsMapper.deleteLogs(id);
    }

    @Override
    public Page selectAll(Integer pageNum, Integer pageSize, String operation) {
        Integer skipNum = (pageNum-1)*pageSize;
        List<Logs> logsList = logsMapper.selectAll(skipNum,pageSize,operation);
        Integer count = logsMapper.selectCountByPage(operation);
        Page page = new Page();
        page.setList(logsList);
        page.setTotal(count);
        return page;
    }

    @Override
    public void save(Logs logs) {
        logsMapper.insert(logs);
    }
}
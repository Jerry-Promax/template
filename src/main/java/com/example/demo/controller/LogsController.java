package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo.common.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Manager;
import com.example.demo.entity.User;
import com.example.demo.service.LogsService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.ManagerServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-05
 */
    @RequestMapping("/logs")
    @RestController
    @Slf4j
    public class LogsController {

        /**
         * 查询
         */
        @Resource
        private LogsService logsService;
        @PreAuthorize("hasAnyAuthority('/logs/selectAllLogs')")
        @GetMapping("/selectAllLogs")
        public Result selectAllUser(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(required = false) String operation){
            Page page = logsService.selectAll(pageNum,pageSize,operation);
            return Result.success(page);
        }


        /**
         * 删除
         * @param id
         * @return
         */
        @PreAuthorize("hasAnyAuthority('/logs/deleteLog')")
        @DeleteMapping("/deleteLog/{id}")
        public Result deleteUser(@PathVariable Integer id){
            logsService.deleteLog(id);
            return Result.success("删除成功");
        }

        /**
         * 批量删除
         * @param ids
         * @return
         */
        @PostMapping("/deleteBatch")
        public Result deleteBatchUser(@RequestBody List<Integer> ids){
            log.info("ids:{}",ids);
            logsService.deleteBatch(ids);
            return Result.success();
        }

    }
package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo.common.Ignore;
import com.example.demo.common.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Flight;
import com.example.demo.entity.User;
import com.example.demo.service.FlightService;
import com.example.demo.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-05
 */
@RequestMapping("/flight")
@RestController
@Slf4j
public class FlightController {

    /**
     * 查询所有航班
     */
    @Resource
    private FlightService flightService;
    @GetMapping("/selectAllFlight")
    public Result selectAllUser(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(required = false) String flightNumber){
        Page page = flightService.selectAllFlight(pageNum,pageSize,flightNumber);
        return Result.success(page);
    }

    /**
     * 修改航班信息
     * @param flight
     * @return
     */

    @PutMapping("/updateFlight")
    public Result updateFlight(@RequestBody Flight flight){
        flightService.updateFlight(flight);
        return Result.success();
    }

    /**
     * 增加航班
     * @param flight
     * @return
     */
    @PostMapping("/addFlight")
    public Result addUser(@RequestBody Flight flight){
        flightService.addFlight(flight);
        return Result.success();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Ignore
    @DeleteMapping("/deleteFlight/{id}")
    public Result deleteUser(@PathVariable Long id){
        flightService.deleteFlight(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @Ignore
    @PostMapping("/deleteBatchFlight")
    public Result deleteBatchUser(@RequestBody List<Long> ids){
        log.info("ids:{}",ids);
        flightService.deleteBatchFlight(ids);
        return Result.success();
    }


//    /**
//     * 批量导出数据
//     * @param username
//     * @param response
//     */
//    @GetMapping("/exportData")
//    public void exportData(@RequestParam(required = false) String username,
//                           HttpServletResponse response) throws IOException {
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//        List<User> list = new ArrayList<>();
//        if (StrUtil.isBlank(username)){
//            list = userService.selectAll();
//        }
//        writer.write(list,true);
//        // 设置响应头
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("用户信息表", StandardCharsets.UTF_8)+".xlsx");
//
//        ServletOutputStream servletOutputStream = response.getOutputStream();
//        writer.flush(servletOutputStream,true);
//        writer.close();
//        servletOutputStream.flush();
//        servletOutputStream.close();
//    }
}
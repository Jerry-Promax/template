package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo.common.Ignore;
import com.example.demo.common.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-05
 */
@RequestMapping("/home")
@RestController
@Slf4j
public class AdminController {

    /**
     * 查询所有用户
     */
    @Resource
    private UserService userService;
    @GetMapping("/selectAllUser")
    public Result selectAllUser(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(required = false) String username,
                                @RequestParam Integer status){
        Page page = userService.selectAllUser(pageNum,pageSize,username,status);
        return Result.success(page);
    }

    /**
     * 修改个人信息
     * @param user
     * @return
     */

    @PutMapping("/updatePerson")
    public Result updatePerson(@RequestBody User user){
        userService.updatePerson(user);
        return Result.success();
    }

    /**
     * 增加用户
     * @param user
     * @return
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user){
        userService.addUser(user);
        return Result.success();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Ignore
    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @Ignore
    @PostMapping("/deleteBatchUser")
    public Result deleteBatchUser(@RequestBody List<Long> ids){
        log.info("ids:{}",ids);
        userService.deleteBatchUser(ids);
        return Result.success();
    }

//    /**
//     * 根据账户名字来查询用户,支持模糊和精准
//     * @param username
//     * @return
//     */
//    @Ignore
//    @GetMapping("/selectByUsername")
//    public Result selectByUsername(String username){
//        User user = userService.selectByUsername(username);
//        return Result.success(user);
//    }

    /**
     * 批量导出数据
     * @param username
     * @param response
     */
    @GetMapping("/exportData")
    public void exportData(@RequestParam(required = false) String username,
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> list = new ArrayList<>();
        if (StrUtil.isBlank(username)){
            list = userService.selectAll();
        }
        writer.write(list,true);
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("用户信息表", StandardCharsets.UTF_8)+".xlsx");

        ServletOutputStream servletOutputStream = response.getOutputStream();
        writer.flush(servletOutputStream,true);
        writer.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    /**
     * 审核用户
     * @param id
     * @return
     */
    @Ignore
    @PostMapping("/auditUser/{id}")
    public Result auditUser(@PathVariable Integer id){
        userService.auditUser(id);
        return Result.success();
    }

    /**
     * 拉黑或取消拉黑用户
     * @param id
     * @return
     */
    @Ignore
    @PostMapping("/isBlackUser/{id}")
    public Result isBlackUser(@PathVariable Integer id,
                              @RequestParam Boolean isBlack){
        userService.blackUser(id,isBlack);
        return Result.success();
    }
}
package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo.common.*;
import com.example.demo.entity.Manager;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.ManagerServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-05
 */
@RequestMapping("/user")
@RestController
@Slf4j
public class AdminController {

    /**
     * 查询所有用户
     */
    @Resource
    private UserService userService;
    @PreAuthorize("hasAuthority('/home/selectAllUser'+ #status)")
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

    @Resource
    private ManagerServiceImpl managerService;
    // 还有一种办法就是让普通用户无法看到用户列表这个功能，可以通过前端的路由守卫来判断，目前用的是判断这个用户是否是管理员以及只能修改自己的信息
    @PreAuthorize("hasAuthority('/home/updateUser')")
    @PutMapping("/updatePerson")
    public Result updatePerson(@RequestBody User user, HttpServletRequest httpServletRequest){
        // 获取当前用户，这是一种方法，通过名字来查找到指定用户，因为获取Authentication对象的时候，
        //        return new UsernamePasswordAuthenticationToken(
        //                username,
        //                null,
        //                userDetails.getAuthorities()
        //        );
        // 是返回的username，密码一般为空，以及用户的权限，所以我可以通过联系security的上下文，然后获取到Authentication点进去源码可以知道，
        // Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities，这是他们所代表的三个参
        // 数，所以获取principal就是获取到我刚刚存的用户名，再通过用户名查找对象信息，调用的是他的内部类loadUserByUsername()接口，我实现了
        // 这个的接口。
        String currentUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Manager currentUser = (Manager) managerService.loadUserByUsername(currentUserName);
        log.info("当前用户为：{}",currentUser);
        if (!Objects.equals(currentUser.getRole(), "管理员")) {
            if (!currentUser.getId().equals(user.getId())) {
                return Result.error("普通用户仅能修改自身信息");
            }
        }
        userService.updatePerson(user);
        return Result.success();
    }

    /**
     * 增加用户
     * @param user
     * @return
     */
    @HoneyLogs(operation = "用户" ,type = LogType.ADD)
    @PreAuthorize("hasAuthority('/home/addUser')")
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user){
        userService.addUser(user);
        return Result.success();
    }
    
    /**
     * 查询用户状态
     * @return
     */

    @GetMapping("/selectUserStatus")
    public Result selectUserStatus() {
        Map<String, Object> statusData = new HashMap<>();
        statusData.put("待审核", userService.selectAllUser(0, 0, null, StatusConstant.PENDING).getTotal());
        statusData.put("普通", userService.selectAllUser(0, 0, null, StatusConstant.NORMAL).getTotal());
        statusData.put("黑名单", userService.selectAllUser(0, 0, null, StatusConstant.DISABLE).getTotal());
        log.info("用户统计：{}",statusData);
        return Result.success(statusData);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @HoneyLogs(operation = "用户" ,type = LogType.DELETE)
    @PreAuthorize("hasAuthority('/home/deleteUser')")
    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return Result.success("删除成功");
    }

    /**
     * 删除审核用户
     * @param id
     * @return
     */
    @HoneyLogs(operation = "待审核用户" ,type = LogType.DELETE)
    @PreAuthorize("hasAuthority('/home/deleteUnAuditUser')")
    @DeleteMapping("/deleteUnAuditUser/{id}")
    public Result deleteUnAuditUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return Result.success("从审核用户列表中删除");
    }

    /**
     * 删除黑名单用户
     * @param id
     * @return
     */
    @HoneyLogs(operation = "黑名单用户" ,type = LogType.DELETE)
    @PreAuthorize("hasAuthority('/home/deleteBlackUser')")
    @DeleteMapping("/deleteBlackUser/{id}")
    public Result deleteBlackUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return Result.success("从黑名单里面删除");
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @HoneyLogs(operation = "批量删除用户" ,type = LogType.DELETE)
    @PreAuthorize("hasAuthority('/home/deleteBatchUser')")
    @PostMapping("/deleteBatchUser")
    public Result deleteBatchUser(@RequestBody List<Integer> ids){
        log.info("ids:{}",ids);
        userService.deleteBatchUser(ids);
        return Result.success();
    }


    /**
     * 批量导出数据
     * @param username
     * @param response
     */
    @GetMapping("/exportData")
    @HoneyLogs(operation = "导出文件" ,type = LogType.EXPORT)
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
    @PreAuthorize("hasAuthority('/home/auditUser')")
    @PostMapping("/auditUser/{id}")
    @HoneyLogs(operation = "审核用户" ,type = LogType.AUDIT)
    public Result auditUser(@PathVariable Integer id){
        userService.auditUser(id);
        return Result.success();
    }

    /**
     * 拉黑或取消拉黑用户
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/home/isBlackUser')")
    @PostMapping("/isBlackUser/{id}")
    @HoneyLogs(operation = "黑名单" ,type = LogType.BLACK)
    public Result isBlackUser(@PathVariable Integer id,
                              @RequestParam Boolean isBlack){
        userService.blackUser(id,isBlack);
        return Result.success();
    }
}
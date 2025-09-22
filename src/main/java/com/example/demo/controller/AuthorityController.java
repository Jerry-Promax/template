package com.example.demo.controller;

import com.example.demo.common.HoneyLogs;
import com.example.demo.common.LogType;
import com.example.demo.common.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Authority;
import com.example.demo.entity.Role;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.RoleService;
import com.example.demo.vo.AuthorityTreeVo;
import com.example.demo.vo.AuthorityVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-21
 */
@RequestMapping("/permission")
@RestController
@Slf4j
public class AuthorityController {
    @Resource
    private AuthorityService authorityService;

    /**
     * 查看所用权限
     * @param pageNum
     * @param pageSize
     * @param authorityName
     * @return
     */
    @PreAuthorize("hasAuthority('/permission/list')")
    @GetMapping("/list")
    public Result roleList(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(required = false) String authorityName){
        Page page = authorityService.selectAllAuthority(pageNum,pageSize,authorityName);
        return Result.success(page);
    }
    /**
     * 查看所有菜单,就是查询条件type=0的
     * 用于新增/编辑权限时选择父级权限
     */
//    @PreAuthorize("hasAuthority('api:/permission/parents')")
    @GetMapping("/parents")
    public Result roleList(){
        List<AuthorityVO> authorityList = authorityService.selectAllAuthorityParents();
        return Result.success(authorityList);
    }


    /**
     * 增加权限
     * @param authority
     * @return
     */
    @PreAuthorize("hasAuthority('/permission/add')")
    @PostMapping("/add")
    public Result addPermission(@RequestBody Authority authority){
        int n = authorityService.addAuthority(authority);
        log.info("新增了{}条数据",n);
        return Result.success();
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/permission/delete')")
    @DeleteMapping("/delete/{id}")
    @HoneyLogs(operation = "权限" ,type = LogType.DELETE)
    public Result deletePermission(@PathVariable Integer id){
        authorityService.deleteAuthority(id);
        return Result.success();
    }
    /**
     * 修改权限
     */
    @PreAuthorize("hasAuthority('/permission/update')")
    @PutMapping("/update")
    public Result updateRole(@RequestBody Authority authority){
        authorityService.updateAuthority(authority);
        return Result.success();
    }

    /**
     * 权限树
     * @return
     */
//    @PreAuthorize("hasAuthority('api:/permission/tree')")
    @GetMapping("/tree")
    public Result tree(){
        List<AuthorityTreeVo> list = authorityService.buildAuthorityTree();
        return Result.success(list);
    }
}
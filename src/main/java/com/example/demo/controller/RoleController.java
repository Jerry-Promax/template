package com.example.demo.controller;

import com.example.demo.common.Page;
import com.example.demo.common.Result;
import com.example.demo.dto.RolePermissionDTO;
import com.example.demo.entity.Authority;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.vo.AuthorityTreeVo;
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
@RequestMapping("/role")
@RestController
@Slf4j
public class RoleController {
    @Resource
    private RoleService roleService;

    /**
     * 查看角色
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @return
     */
    @PreAuthorize("hasAuthority('/role/list')")
    @GetMapping("/list")
    public Result roleList(@RequestParam(required = false) Integer pageNum,
                           @RequestParam(required = false) Integer pageSize,
                           @RequestParam(required = false) String roleName){
        Page page = roleService.selectAllRole(pageNum,pageSize,roleName);
        return Result.success(page);
    }

    /**
     * 增加角色
     * @param role
     * @return
     */
    @PreAuthorize("hasAuthority('/role/addRole')")
    @PostMapping("/addRole")
    public Result addRole(@RequestBody Role role){
        roleService.addRole(role);
        return Result.success();
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/role/deleteRole')")
    @DeleteMapping("/deleteRole/{id}")
    public Result deleteRole(@PathVariable Integer id){
        roleService.deleteRole(id);
        return Result.success();
    }
    /**
     * 修改角色
     */
    @PreAuthorize("hasAuthority('/role/updateRole')")
    @PutMapping("/updateRole")
    public Result updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success();
    }

    /**
     * 某个角色目前所有的最底层权限，因为我能访问的到最底层的权限，自然也能访问的到他的上一级权限
     * @param rid
     * @return
     */
    @PreAuthorize("hasAuthority('/role/permissions')")
    @GetMapping("/permissions/{rid}")
    public Result ownAuthority(@PathVariable Integer rid){
        List<AuthorityTreeVo> listOwnAuthority = roleService.ownAuthority(rid);
        return Result.success(listOwnAuthority);
    }

    /**
     * 分配权限
     * @param rolePermissionDTO
     * @return
     */
    @PreAuthorize("hasAuthority('/role/assignPermissions')")
    @PostMapping("/assignPermissions")
    public Result assignAuthority(@RequestBody RolePermissionDTO rolePermissionDTO){
        roleService.assignAuthority(rolePermissionDTO.getRoleId(),rolePermissionDTO.getPermissionIds());
        return Result.success("权限分配成功");
    }

}
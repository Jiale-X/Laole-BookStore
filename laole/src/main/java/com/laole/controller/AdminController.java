package com.laole.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.laole.common.Result;
import com.laole.entity.Admin;
import com.laole.entity.User;
import com.laole.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laole
 * @since 2025-08-16
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/list")//查询所有
    public List<Admin> list(){
        return adminService.listAll();
    }
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        List list=adminService.lambdaQuery()
                .eq(Admin::getAccount,admin.getAccount())
                .eq(Admin::getPassword,admin.getPassword()).list();

        return list.size()>0?Result.suc(list.get(0)):Result.fail();
    }
    @PostMapping("/listV")
    public Result listV(@RequestBody Admin admin){
        LambdaQueryWrapper<Admin> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(admin.getName())){
            lambdaQueryWrapper.like(Admin::getName,admin.getName());
        }
        return Result.suc(adminService.list(lambdaQueryWrapper));
    }
}

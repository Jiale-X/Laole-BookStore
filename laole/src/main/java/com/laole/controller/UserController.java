package com.laole.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.laole.common.QueryPageParam;
import com.laole.common.Result;
import com.laole.entity.Book;
import com.laole.entity.User;
import com.laole.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping
    public String hello(){
        return "hello laole";
    }

    @Autowired
    private UserService userService;
    @GetMapping("/list")//查询所有
    public List<User> list(){
        return userService.listAll();
    }
    @GetMapping ("/findByAccount")
    public Result findByNo(@RequestParam String account){
        List list=userService.lambdaQuery().eq(User::getAccount,account).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List list = userService.lambdaQuery()
                .eq(User::getAccount, user.getAccount())
                .eq(User::getPassword, user.getPassword()).list();
        return list.size()>0?Result.suc(list.get(0)):Result.fail();
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        return userService.save(user)?Result.suc():Result.fail();
    }
    @PostMapping("/save")//新增
    public Result save(@RequestBody User user){
        return userService.save(user)?Result.suc():Result.fail();
    }
    @PostMapping("/update")//更新
    public Result update(@RequestBody User user){
        int id=user.getId();
        User user1=userService.getById(id);
        user.setBalance(user1.getBalance());
        return userService.updateById(user)?Result.suc():Result.fail();
    }
    @PostMapping("/updateBuy")//购买操作后更新
    public Result updateBuy(@RequestBody User user){
        int id=user.getId();
        User user1=userService.getById(id);
        user.setAge(user1.getAge());
        user.setSex(user1.getSex());
        return userService.updateById(user)?Result.suc():Result.fail();
    }
    @PostMapping("/updateBalance")//充值后更新
    public Result updateBalance(@RequestBody User user){
        int id=user.getId();
        User user1=userService.getById(id);
        user.setBalance(user.getBalance()+user1.getBalance());
        return userService.updateById(user)?Result.suc():Result.fail();
    }
    @GetMapping("/del")//删除
    public Result del(@RequestParam String id){
        return userService.removeById(id)?Result.suc():Result.fail();
    }
    @PostMapping("/listV")
    public Result listV(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName,user.getName());
        }
        return Result.suc(userService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param=query.getParam();
        String name=(String) param.get("name");
        String sex=(String) param.get("sex");
//        System.out.println("name=="+name);
//        System.out.println("sex=="+sex);

        Page<User> page=new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)&&!"null".equals(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }

        if (StringUtils.isNotBlank(sex)){
            lambdaQueryWrapper.eq(User::getSex,sex);
        }

        IPage result=userService.page(page,lambdaQueryWrapper);

        return Result.suc(result.getRecords(),result.getTotal());
    }

}

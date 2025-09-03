package com.laole.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.laole.common.QueryPageParam;
import com.laole.common.Result;
import com.laole.entity.Book;
import com.laole.entity.User;
import com.laole.service.BookService;
import com.laole.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laole
 * @since 2025-08-15
 */
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @GetMapping("/list")//查询所有
    public List<Book> list(){
        return bookService.listAll();
    }
    @GetMapping ("/findByname")
    public Result findByNo(@RequestParam String name){
        List list=bookService.lambdaQuery().eq(Book::getName,name).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }

    @PostMapping("/save")//新增
    public Result save(@RequestBody Book book){
        return bookService.save(book)?Result.suc():Result.fail();
    }
    @PostMapping("/update")//更新
    public Result update(@RequestBody Book book){
        return bookService.updateById(book)?Result.suc():Result.fail();
    }
    @PostMapping("/updateNum")//更新
    public Result updateNum(@RequestBody Book book){
        int id= book.getId();
        Book book1=bookService.getById(id);
        int num=book1.getNumber();
        int n=book.getNumber();
        if("1".equals(book.getAction())){
            n=n;
        }else if("2".equals(book.getAction())){
            if (num<n){
                return Result.fail();
            }
            n=-n;
        }
        num=num+n;
        book.setNumber(num);
        return bookService.updateById(book)?Result.suc():Result.fail();
    }
    @PostMapping("/updateBuy")//更新
    public Result updateBuy(@RequestBody Book book){
        int id= book.getId();
        Book book1=bookService.getById(id);
        User user=userService.getById(book.getUserId());
        int number=book1.getNumber();
        int n=book.getNumber();
        if(n>number) return Result.failNum();
        else if (n*book1.getPrice()>user.getBalance()) return Result.failMoney();
        number=number-n;
        book.setNumber(number);
        return bookService.updateById(book)?Result.suc():Result.fail();
    }
    @GetMapping("/del")//删除
    public Result del(@RequestParam String id){
        return bookService.removeById(id)?Result.suc():Result.fail();
    }
    @PostMapping("/listV")
    public Result listV(@RequestBody Book book){
        LambdaQueryWrapper<Book> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(book.getName())){
            lambdaQueryWrapper.like(Book::getName,book.getName());
        }
        return Result.suc(bookService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param=query.getParam();
        String name=(String) param.get("name");
        String number=(String) param.get("number");
//        System.out.println("name=="+name);
//        System.out.println("number=="+number);

        Page<Book> page=new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Book> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)&&!"null".equals(name)){
            lambdaQueryWrapper.like(Book::getName,name);
        }
        if (StringUtils.isNotBlank(number)){
            if(number.equals("0")){
                lambdaQueryWrapper.eq(Book::getNumber,number);
            }else {
                lambdaQueryWrapper.ne(Book::getNumber,0);
            }
        }
        IPage result=bookService.page(page,lambdaQueryWrapper);

        return Result.suc(result.getRecords(),result.getTotal());
    }

}

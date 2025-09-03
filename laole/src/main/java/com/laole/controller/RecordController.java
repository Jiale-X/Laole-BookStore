package com.laole.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.laole.common.QueryPageParam;
import com.laole.common.Result;
import com.laole.entity.Record;
import com.laole.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Soundbank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laole
 * @since 2025-08-20
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @GetMapping("list")
    public List<Record> list(){
        return recordService.list();
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param=query.getParam();
        String userAccount=(String) param.get("userAccount");
        String userName=(String) param.get("userName");
        String bookName=(String) param.get("bookName");
        String select=(String) param.get("select");

        Page<Record> page=new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Record> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(userAccount)&&!"null".equals(userAccount)){
            lambdaQueryWrapper.like(Record::getUserAccount,userAccount);
        }

        if (StringUtils.isNotBlank(userName)&&!"null".equals(userName)){
            lambdaQueryWrapper.like(Record::getUserName,userName);
        }

        if (StringUtils.isNotBlank(bookName)&&!"null".equals(bookName)){
            lambdaQueryWrapper.like(Record::getBookName,bookName);
        }

        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date=localDateTime.format(formatter);
        if (select.equals("1")){
            date=date.substring(0,4);
            lambdaQueryWrapper.like(Record::getTime,date);
        }else if(select.equals("2")){
            date=date.substring(0,7);
            lambdaQueryWrapper.like(Record::getTime,date);
        }else if (select.equals("3")){
            date=date.substring(0,10);
            lambdaQueryWrapper.like(Record::getTime,date);
        }

        IPage result=recordService.page(page,lambdaQueryWrapper);

        return Result.suc(result.getRecords(),result.getTotal());
    }
    @PostMapping("/save")
    public Result save(@RequestBody Record record){


        LocalDateTime localDateTime=LocalDateTime.now();
        record.setTime(localDateTime);

        return recordService.save(record)?Result.suc():Result.fail();
    }
}

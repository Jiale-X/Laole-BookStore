package com.laole.common;

import lombok.Data;

@Data
public class Result {
    private int code;//编码200/400
    private String msg;
    private long total;
    private Object data;
    public static Result fail(){
        return result(400,"失败",0L,null);
    }
    public static Result failNum(){
        return result(401,"失败",0L,null);
    }
    public static Result failMoney(){
        return result(402,"失败",0L,null);
    }
    public static Result suc(){
        return result(200,"成功",0L,null);
    }
    public static Result suc(Object data){
        return result(200,"成功",0L,data);
    }
    public static Result suc(Object data,long total){
        return result(200,"成功",total,data);
    }
    private static Result result(int code,String msg,long total,Object data){
        Result res=new Result();
        res.setCode(code);
        res.setData(data);
        res.setMsg(msg);
        res.setTotal(total);
        return res;
    }
}

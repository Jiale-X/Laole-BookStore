package com.laole.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laole.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    List<User> listAll();
}

package com.laole.service;

import com.laole.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.laole.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laole
 * @since 2025-08-15
 */
public interface BookService extends IService<Book> {

    List<Book> listAll();
}

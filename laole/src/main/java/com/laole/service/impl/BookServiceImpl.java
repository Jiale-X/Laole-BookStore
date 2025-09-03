package com.laole.service.impl;

import com.laole.entity.Book;
import com.laole.entity.User;
import com.laole.mapper.BookMapper;
import com.laole.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laole
 * @since 2025-08-15
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Resource
    private BookMapper bookMapper;
    @Override
    public List<Book> listAll() {
        return bookMapper.listAll();
    }
}

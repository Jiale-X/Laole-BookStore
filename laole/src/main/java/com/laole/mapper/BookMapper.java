package com.laole.mapper;

import com.laole.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author laole
 * @since 2025-08-15
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    List<Book> listAll();
}

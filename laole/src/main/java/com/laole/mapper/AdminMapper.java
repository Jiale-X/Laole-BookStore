package com.laole.mapper;

import com.laole.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author laole
 * @since 2025-08-16
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    List<Admin> listAll();
}

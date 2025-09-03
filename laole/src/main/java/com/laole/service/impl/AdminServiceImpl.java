package com.laole.service.impl;

import com.laole.entity.Admin;
import com.laole.entity.User;
import com.laole.mapper.AdminMapper;
import com.laole.service.AdminService;
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
 * @since 2025-08-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private AdminMapper adminMapper;
    @Override
    public List<Admin> listAll() {
        return adminMapper.listAll();
    }
}

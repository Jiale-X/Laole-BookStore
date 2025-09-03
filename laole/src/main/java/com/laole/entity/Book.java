package com.laole.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author laole
 * @since 2025-08-15
 */
@Data
@TableName(value = "book")
public class Book implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String author;

    private String publisher;

    private Integer number;

    private Float price;

    @TableField(exist = false)
    private String action;

    @TableField(exist = false)
    private String money;

    @TableField(exist = false)
    private String userId;
}

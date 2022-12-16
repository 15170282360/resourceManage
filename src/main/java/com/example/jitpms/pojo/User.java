package com.example.jitpms.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 
 * </p>
 *
 * @author 吴伟龙
 * @since 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @NotEmpty()
    @TableId(value = "u_id", type = IdType.AUTO)
    private Integer u_id;

    /**
     * 用户名
     */
    @TableField("u_name")
    private String u_name;

    /**
     * 用户性别
     */
    @TableField("u_sex")
    private String u_sex;

    /**
     * 用户年龄
     */
    @TableField("u_age")
    private Integer u_age;

    /**
     * 用户密码
     */
    @TableField("u_password")
    private String u_password;

    /**
     * 用户头像
     */
    @TableField("u_photo")
    private String u_photo;

    /**
     * 身份id
     */
    @TableField("s_id")
    private Integer s_id;


}

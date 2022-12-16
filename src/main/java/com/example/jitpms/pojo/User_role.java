package com.example.jitpms.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("user_role")
public class User_role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("u_id")
    private Integer u_id;

    @TableField("r_id")
    private Integer r_id;


}

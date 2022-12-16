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
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "r_id", type = IdType.AUTO)
    private Integer r_id;

    @TableField("r_name")
    private String r_name;

    @TableField("s_id")
    private Integer s_id;


}

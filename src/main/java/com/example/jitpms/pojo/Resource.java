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
@TableName("resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "re_id", type = IdType.AUTO)
    private Integer re_id;

    @TableField("re_name")
    private String re_name;


}

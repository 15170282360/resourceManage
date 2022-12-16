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
 * @author long
 * @since 2022-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("status_resource")
public class Status_resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("s_id")
    private Integer s_id;

    @TableField("re_id")
    private Integer re_id;


}

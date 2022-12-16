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
@TableName("status")
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 身份id
     */
    @TableId(value = "s_id", type = IdType.AUTO)
    private Integer s_id;

    /**
     * 名称
     */
    @TableField("s_name")
    private String s_name;


}

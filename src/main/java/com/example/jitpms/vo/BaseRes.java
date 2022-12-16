package com.example.jitpms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRes<T> {
    private int code;   //200-成功；300-失败；400-存在问题；500-后端问题
    private String msg; //消息
    private T data;     //数据

    public void setCM(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseRes(T data) {
        this.code = 200;
        this.msg = "成功";
        this.data = data;
    }
}

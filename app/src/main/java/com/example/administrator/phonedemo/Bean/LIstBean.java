package com.example.administrator.phonedemo.Bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class LIstBean {

    /**
     * code : 1002
     * msg : 失败
     * data : []
     */

    private int code;
    private String msg;
    private List<?> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}

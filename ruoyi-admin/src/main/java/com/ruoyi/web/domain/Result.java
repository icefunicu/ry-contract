package com.ruoyi.web.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: Result
 * @Package: org.resrun.controller.vo
 * @ClassName: Result
 * @copyright 北京资源律动科技有限公司
 */

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 4186430915088458662L;

    private boolean success = true;
    private String message = "";
    private Integer code = 200;
    private T result;
    private long timestamp = System.currentTimeMillis();


    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result();
        r.setSuccess(true);
        r.setCode(200);
        r.setResult(data);
        return r;
    }



    public static <T> Result<T> error(String msg, T data) {
        Result<T> r = new Result();
        r.setSuccess(false);
        r.setCode(500);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public Result(boolean success, String message, Integer code, T result, long timestamp) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.result = result;
        this.timestamp = timestamp;
    }

    public Result() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

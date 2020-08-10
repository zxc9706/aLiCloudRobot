package Result;

/**
 * @Author zxc
 * @DateTime 2020/8/10 9:44 上午
 */
/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    private int code = 0;
    /**
     * 消息内容
     */
    private String msg = "success";

    /**
     * 内部信息
     */
    private String internalMsg="";

    /**
     * 响应数据
     */
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success(){
        return code == 0 ? true : false;
    }

    /**
     * 返回
     *     "code": 8000,
     *     "msg": "服务器开小差了...",
     */
    public Result<T> error() {
//        this.code = EpmetErrorCode.SERVER_ERROR.getCode();
//        this.msg = EpmetErrorCode.getMsg(code);
//        if (StringUtils.isBlank(this.msg)) {
//            this.msg = com.epmet.commons.tools.utils.MessageUtils.getMessage(this.code);
//        }
        return this;
    }

    /**
     * 根据错误编码查询msg返回
     */
    public Result<T> error(int code) {
        this.code = code;
//        this.msg = EpmetErrorCode.getMsg(code);
//        if (StringUtils.isBlank(this.msg)) {
//            this.msg = com.epmet.commons.tools.utils.MessageUtils.getMessage(this.code);
//        }
        return this;
    }

    /**
     * 传入错误编码+msg返回
     */
    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    /**
     *
     * @param msg
     * @return 此方法废弃，统一使用
     * logger.error(XXXX);
     * throw new RenException(XXXX);
     * XXXX定义常量里
     */
    @Deprecated
    public Result<T> error(String msg) {

        this.msg = msg;
        return this;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getInternalMsg() {
        return internalMsg;
    }

    public void setInternalMsg(String internalMsg) {
        this.internalMsg = internalMsg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}


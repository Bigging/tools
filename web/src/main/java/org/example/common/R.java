package org.example.common;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
    public static final int STATE_SUCCESS = 0;
    public static final int STATE_ERROR = 500;

    @Serial
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", STATE_SUCCESS);
    }

    public static R error() {
        return error(STATE_ERROR, "未知异常，请稍后再试");
    }

    public static R error(String msg) {
        return error(STATE_ERROR, msg);
    }

    public static R DBError(String msg) {
        return error(401, msg);
    }

    public static R paramsError(String msg) {
        return error(400, msg);
    }

    public static R authenticationError(String msg) {
        return error(402, msg);
    }

    public static R DBError() {
        return error(401, "操作数据库失败！");
    }

    public static R paramsError() {
        return error(400, "参数异常");
    }

    public static R authenticationError() {
        return error(402, "登录认证失败");
    }


    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R resultError(String msg) {
        return error("fail", msg);
    }

    public static R resultSuccess(String msg) {
        return error("success", msg);
    }

    public static R error(String code, String msg) {
        return ok(code, msg);
    }

    public static R ok(String code, String msg) {
        R r = new R();
        r.put("resultCode", code);
        r.put("resultMsg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }


}

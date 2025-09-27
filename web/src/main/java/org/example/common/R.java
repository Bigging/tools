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

    public static R error(String msg) {
        return error(STATE_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
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

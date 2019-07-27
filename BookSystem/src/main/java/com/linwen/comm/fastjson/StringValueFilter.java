package com.linwen.comm.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;

public class StringValueFilter implements ValueFilter {
    @Override
    public Object process(Object o, String s, Object o1) {
        if (o1 instanceof JSONArray) {
            return JSON.toJSONString(o1);
        }
        if (o1 instanceof JSONObject) {
            return JSON.toJSONString(o1);
        }
        return o1;
    }
}

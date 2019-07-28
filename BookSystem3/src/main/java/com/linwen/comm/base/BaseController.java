package com.linwen.comm.base;

import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.converter.StringToDateConverter;
import com.linwen.comm.converter.StringToNullConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linwen on 18-11-28.
 */
public class BaseController {
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;

    public String getGlobalIP() {

        return "192.168.1,192.168.15";
    }

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new StringToDateConverter(dateFormat, false));
        binder.registerCustomEditor(String.class, new StringToNullConverter());
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    private JSONObject getRetJsonObject(String session, int ret, String msg, String method, Object obj) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", session);
        jsonObject.put("ret", ret);
        jsonObject.put("msg", msg);
        jsonObject.put("method", method);
        jsonObject.put("data", obj);
        return jsonObject;
    }

    public static void main(String[] args) {
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("sessionId", 11);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", 11);
        jsonObject.put("ret", 11);
        jsonObject.put("msg", 11);
        jsonObject.put("method", 11);
        jsonObject.put("data", 11);
        System.out.println(jsonObject);
    }

    public JSONObject JsonFailResult(String session, int ret, String msg, String method,
                                     Object obj) {

        JSONObject jsonObject = getRetJsonObject(session, ret, msg, method, obj);
        return jsonObject;
    }

    public JSONObject JsonFailResult(String session) {

        return JsonFailResult(session, 1, "失败", null, null);
    }

    public JSONObject JsonFailResult(String session, String msg, String method, Object obj) {

        return JsonFailResult(session, 1, msg, method, obj);
    }

    public JSONObject JsonFailResult(String session, String msg, Object obj) {

        return JsonFailResult(session, 1, msg, null, obj);
    }

    public JSONObject JsonFailResult(String session, int ret, String msg, Object obj) {

        return JsonFailResult(session, ret, msg, null, obj);
    }

    public JSONObject JsonFailResult(String session, String msg) {

        return JsonFailResult(session, 1, msg, null, null);
    }

    public JSONObject JsonFailResult(String session, int ret, String msg) {

        return JsonFailResult(session, ret, msg, null, null);
    }

    public JSONObject JsonSuccessResult(String session, int ret, String msg, String method,
                                        Object obj) {

        JSONObject jsonObject = getRetJsonObject(session, ret, msg, method, obj);
        return jsonObject;
    }


    public JSONObject JsonSuccessResult(String session) {

        return JsonSuccessResult(session, 0, "成功", null, null);
    }

    public JSONObject JsonSuccessResult(String session, String msg, String method, Object obj) {

        return JsonSuccessResult(session, 0, msg, method, obj);
    }

    public JSONObject JsonSuccessResult(String session, String msg, Object obj) {

        return JsonSuccessResult(session, 0, msg, null, obj);
    }

    public JSONObject JsonSuccessResult(String session, int ret, String msg, Object obj) {

        return JsonSuccessResult(session, ret, msg, null, obj);
    }

    public JSONObject JsonSuccessResult(String session, String msg) {

        return JsonSuccessResult(session, 0, msg, null, null);
    }

    public JSONObject JsonSuccessResult(String session, Object obj) {

        return JsonSuccessResult(session, 0, "成功", null, obj);
    }

    public JSONObject JsonSuccessResult(String session, int ret, String msg) {

        return JsonSuccessResult(session, ret, msg, null, null);
    }


}

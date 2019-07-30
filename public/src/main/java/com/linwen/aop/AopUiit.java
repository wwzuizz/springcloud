package com.linwen.aop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.base.Query;
import com.linwen.comm.base.QueryItem;
import com.linwen.comm.converter.multirequestbody.MultiRequestBodyArgumentResolver;
import com.linwen.comm.validation.annotation.OAuthRequired;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AopUiit {

    public static void edit(ProceedingJoinPoint pjp) throws IllegalAccessException {
        Object[] objects = pjp.getArgs();
        for (Object _obj : objects) {
            if (_obj instanceof BaseCondition) {
                for (Field field : _obj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getType().getName().equals("java.lang.String")) {
                        String string = (String) field.get(_obj);
                        if (string != null) {
                            string = string.replace("\u0000", "%");
                            field.set(_obj, string);
                        }
                    }
                }
            }
        }
    }

    public static Object handleOauth(ProceedingJoinPoint pjp, Method method) {
        OAuthRequired oAuthRequired = method.getAnnotation(OAuthRequired.class);
        if (oAuthRequired != null) {
            Object object = pjp.getTarget();
            if (object instanceof BaseController) {
                BaseController baseController = (BaseController) object;
//                if (BaseController.isLogin() == null) {
//                    return baseController.JsonFailResult("", 2, "请登录后操作");
//                }
            }
        }
        return null;
    }


    /**
     * 处理调用接口之前的参数
     *
     * @param objects
     */
    public static void settingHttpParameter(Object Object, Object[] objects) {
        HttpServletRequest httpServletRequest = null;
        BaseCondition baseCondition = null;
        if (Object instanceof BaseController) {
            BaseController baseController = (BaseController) Object;
            if (baseController != null && baseController.getHttpServletRequest() != null) {
                httpServletRequest = baseController.getHttpServletRequest();
            }
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof BaseCondition) {
                baseCondition = (BaseCondition) objects[i];
                break;
            }
        }
        if (baseCondition != null && httpServletRequest != null) {
            String contentType = httpServletRequest.getContentType();
            if (MultiRequestBodyArgumentResolver.isJson(contentType)) {
                try {

                    String resultStr = (String) httpServletRequest.getAttribute(MultiRequestBodyArgumentResolver.JSONBODY_ATTRIBUTE);
                    if (!"".equals(resultStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(resultStr);
                        for (java.lang.Object key :
                                jsonObject.keySet()) {
                            if (key.equals(Query.ORDERBY)) {
                                JSONArray jsonArray = jsonObject.getJSONArray((String) key);
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    if (item.containsKey(Query.ORDER) && item.containsKey(Query.BY) && (item.getString(Query.BY).equals("desc") || item.getString(Query.BY).equals("asc"))) {
                                        baseCondition.addOrder(item.getString(Query.ORDER), item.getString(Query.BY));
                                    }
                                }
                            } else {
                                String strkey = (String) key;
                                String tempKey = strkey.replace(Query._EXP, "");
                                if (strkey.endsWith(Query._EXP)) {
                                    JSONArray jsonArray = jsonObject.getJSONArray((String) key);
                                    QueryItem queryItem = null;
                                    List<QueryItem> queryItemList = new ArrayList<>();
                                    JSONObject object = null;
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        queryItem = new QueryItem();
                                        object = jsonArray.getJSONObject(i);
                                        queryItem.setExp(object.getString(Query.EXP));
                                        queryItem.setValue(object.getString(Query.VALUE));
                                        queryItemList.add(queryItem);
                                    }
                                    baseCondition.addQuery(tempKey, queryItemList);

                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Map<String, String[]> map = httpServletRequest.getParameterMap();
                //form表单传输
                for (String key :
                        map.keySet()) {
                    if (key.equals(Query.ORDERBY)) {
                        String[] value = map.get(key);
                        if (value.length == 1) {
                            JSONArray jsonArray = JSONArray.parseArray(value[0]);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.containsKey(Query.ORDER) && jsonObject.containsKey(Query.BY) && (jsonObject.getString(Query.BY).equals("desc") || jsonObject.getString(Query.BY).equals("asc"))) {
                                    baseCondition.addOrder(jsonObject.getString(Query.ORDER), jsonObject.getString(Query.BY));
                                }
                            }
                        }
                    } else {
                        String[] value = map.get(key);
                        if (value.length == 1 && key.endsWith(Query._EXP)) {
                            String tempKey = key.replace(Query._EXP, "");
                            JSONArray jsonArray = JSONArray.parseArray(value[0]);
                            QueryItem queryItem = null;
                            List<QueryItem> queryItemList = new ArrayList<>();
                            JSONObject object = null;
                            for (int i = 0; i < jsonArray.size(); i++) {
                                queryItem = new QueryItem();
                                object = jsonArray.getJSONObject(i);
                                queryItem.setExp(object.getString(Query.EXP));
                                queryItem.setValue(object.getString(Query.VALUE));
                                queryItemList.add(queryItem);
                            }
                            baseCondition.addQuery(tempKey, queryItemList);
                        }
                    }
                }
            }

        }
    }
}

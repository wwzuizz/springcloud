package com.linwen.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.file.FileTool;
import com.linwen.comm.validation.annotation.NotNull;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.annotation.ValidationRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

public class DocGen {
    public static void DocGen(JoinPoint jp, Object returning, String servletPath) {
        //生成文档
        File file = new File("/home/linwen/");
        if (!file.exists()) {
            FileTool.mkdirsFile(file);
        }

        JSONObject jsonObject = null;
        String json = "";
        file = new File("/home/linwen/json.json");
        if (file.exists()) {
            json = FileTool.readJson("/home/linwen/json.json");
        }
        if ("".equals(json)) {
            jsonObject = new JSONObject();
        } else {
            jsonObject = JSON.parseObject(json);
        }
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        boolean isAdd = true;
        String key = "";
        JSONArray jsonArray = null;
        JSONObject adminOrApp = null;
        if (servletPath.contains("app")) {
            key = "app";
            adminOrApp = jsonObject.getJSONObject("app");
            adminOrApp.put("prefix", "");
            adminOrApp.put("name", "");
            jsonArray = adminOrApp.getJSONArray("interfaces");
        }
        if (servletPath.contains("admin")) {
            key = "admin";
            adminOrApp = jsonObject.getJSONObject("admin");
            if (adminOrApp == null) {
                adminOrApp = new JSONObject();
            }
            adminOrApp.put("prefix", "");
            adminOrApp.put("name", "");
            jsonArray = adminOrApp.getJSONArray("interfaces");
        }
        JSONObject item = null;
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int ii = 0; ii < jsonArray.size(); ii++) {
                item = jsonArray.getJSONObject(ii);
                if (item != null && item.containsKey("path") && item.getString("path").equals(servletPath)) {
                    isAdd = false;
                    boolean isDelete = addJson(jp, JSON.toJSON(returning), servletPath, method, item);
                    if (isDelete) {
                        jsonArray.remove(ii);
                    }
                } else {
                    jsonArray.remove(ii);
                }
            }
            if (isAdd) {
                item = new JSONObject();
                addJson(jp, JSON.toJSON(returning), servletPath, method, item);
                item.put("createdTime", new Date().getTime());
                jsonArray.add(item);
            }
        } else {
            jsonArray = new JSONArray();
            item = new JSONObject();
            addJson(jp, JSON.toJSON(returning), servletPath, method, item);
            item.put("createdTime", new Date().getTime());
            jsonArray.add(item);
        }
        adminOrApp.put("interfaces", jsonArray);
        jsonObject.put(key, adminOrApp);
        FileTool.byteOutPut("/home/linwen/json.json", JSON.toJSONBytes(jsonObject));
    }

    public static boolean addJson(JoinPoint jp, Object returning, String servletPath, Method method, JSONObject item) {
        Object[] objects = jp.getArgs();
        Object object = jp.getTarget();
        method.getParameterAnnotations();
        if (object instanceof BaseController) {
            BaseController baseController = (BaseController) object;
            if (baseController != null && baseController.getHttpServletRequest() != null) {
                ValidationRequest validationRequest = method.getAnnotation(ValidationRequest.class);
                if (validationRequest == null) {
                    return true;
                }
                item.put("category", validationRequest.category());
                item.put("name", validationRequest.name());
                item.put("description", validationRequest.description());
                item.put("method", validationRequest.method());
                item.put("path", servletPath);
                item.put("response", returning);
                item.put("updatedTime", new Date().getTime());
                NotNull notNull = null;
                ValidParameter validParameter = null;
                Field field = null;
                Object obj = null;
                JSONArray params = new JSONArray();
                JSONObject param = null;
                boolean isValid = false;
                for (int i = 0; i < objects.length; i++) {
                    obj = objects[i];
                    if (obj instanceof BaseCondition) {
                        Field[] fields = obj.getClass().getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            param = new JSONObject();
                            field = fields[j];
                            validParameter = field.getAnnotation(ValidParameter.class);
                            if (validParameter == null) {
                                continue;
                            }
                            String[] validRanges = validParameter.validRanges();
                            for (int k = 0; k < validRanges.length; k++) {
                                if (validRanges[k].equals(validationRequest.validRange())) {
                                    isValid = true;
                                }
                            }
                            if (isValid) {
                                param.put("name", validParameter.name());
                                param.put("description", validParameter.description());
                                notNull = validParameter.notNull();
                                //判断不能为空
                                if (notNull != null && notNull.isValid()) {
                                    param.put("required", notNull.isValid());
                                } else {
                                    param.put("required", false);
                                }
                                param.put("parameterType", validParameter.parameterType().name());
                                params.add(param);
                            } else {
                                return true;
                            }
                        }
                    }
                }
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    validParameter = parameters[i].getAnnotation(ValidParameter.class);
                    if (validParameter != null) {
                        System.out.println(validParameter.name());
                    }
                }
                item.put("params", params);
                return false;
            }
        }
        return true;
    }
}

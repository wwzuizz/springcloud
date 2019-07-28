package com.linwen.comm.converter.multirequestbody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.converter.StringToDateConverter;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 多RequestBody解析器
 *
 * @author linwen
 * @date 2018/08/27
 */
public class MultiRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String JSONBODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    public static boolean isJson(String contentType) {
        return contentType != null && contentType.contains("application/json");
    }

    /**
     * 设置支持的方法参数类型
     *
     * @param parameter 方法参数
     * @return 支持的类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 支持带@MultiRequestBody注解的参数
        return parameter.hasParameterAnnotation(MultiRequestBody.class);
    }

    /**
     * 参数解析，利用fastjson
     * 注意：非基本类型返回null会报空指针异常，要通过反射或者JSON工具类创建一个空对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String[]> parameterMap = servletRequest.getParameterMap();
        MultiRequestBody parameterAnnotation = parameter.getParameterAnnotation(MultiRequestBody.class);
        String key = parameterAnnotation.value();
        String jsonBody = getRequestBody(webRequest);
        boolean isAnalysisJson = false;
        isAnalysisJson = !"".equals(jsonBody);
        JSONObject jsonObject = null;
        if (isAnalysisJson) {
            return jsonToEntity(parameter, parameterAnnotation, key, jsonBody);
        }
        return ParameterToEntity(parameter, servletRequest, parameterMap, parameterAnnotation, key);
    }

    /**
     * json解析
     *
     * @param parameter
     * @param parameterAnnotation
     * @param key
     * @param jsonBody
     * @return
     * @throws Exception
     */
    private Object jsonToEntity(MethodParameter parameter, MultiRequestBody parameterAnnotation, String key, String jsonBody) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jsonObject2 = JSON.parseObject(jsonBody);
            for (String keu :
                    jsonObject2.keySet()) {
                Object object = jsonObject2.get(keu);
                if (!"".equals(object)) {
                    jsonObject.put(keu, object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("json解析错误");
        }
        boolean isKeyInParameter = false;
        if (jsonObject.containsKey(key)) {
            isKeyInParameter = true;
        }
        key = parameter.getParameterName();
        if (jsonObject.containsKey(key)) {
            isKeyInParameter = true;
        }
        Class<?> parameterType = parameter.getParameterType();

        if (isKeyInParameter) {
            Object value = jsonObject.get(key);
            // 通过注解的value或者参数名解析，能拿到value进行解析
            if (value != null && !"".equals(value)) {
                //基本类型
                if (parameterType.isPrimitive()) {
                    return parsePrimitive(parameterType.getName(), value);
                }
                // 基本类型包装类(包含String)
                if (isBasicDataTypes(parameterType)) {
                    return parseBasicTypeWrapper(parameterType, value);
                }
                // 其他复杂对象
                return JSON.parseObject(value.toString(), parameterType);
            } else {
                if (parameterAnnotation.required()) {
                    throw new IllegalArgumentException(String.format("required param %s is not present", key));
                }
            }
        }
        return JSON.toJavaObject(jsonObject, parameterType);
    }

    /**
     * 实体解析
     *
     * @param parameter
     * @param servletRequest
     * @param parameterMap
     * @param parameterAnnotation
     * @param key
     * @return
     */
    private Object ParameterToEntity(MethodParameter parameter, HttpServletRequest servletRequest, Map<String, String[]> parameterMap, MultiRequestBody parameterAnnotation, String key) {
        Object value;
        boolean isKeyInParameter = false;
        boolean isError = false;
        if (parameterMap.containsKey(key)) {
            isKeyInParameter = true;
        }
        key = parameter.getParameterName();
        if (parameterMap.containsKey(key)) {
            isKeyInParameter = true;
        }
        if (isKeyInParameter) {
            String[] values = servletRequest.getParameterMap().get(key);
            value = values != null ? values[0] : null;
            // 获取的注解后的类型
            Class<?> parameterType = parameter.getParameterType();
            // 通过注解的value或者参数名解析，能拿到value进行解析
            if (value != null && !"".equals(value)) {
                //基本类型
                if (parameterType.isPrimitive()) {
                    return parsePrimitive(parameterType.getName(), value);
                }
                // 基本类型包装类(包含String)
                if (isBasicDataTypes(parameterType)) {
                    return parseBasicTypeWrapper(parameterType, value);
                }
                // 其他复杂对象
                return JSON.parseObject(value.toString(), parameterType);
            } else {
                if (parameterAnnotation.required()) {
                    throw new IllegalArgumentException(String.format("required param %s is not present", key));
                }
            }
        }
        //不存在，表明是要解析到实体类中
        return mapToEntity(parameterMap, parameter.getNestedParameterType());
    }

    public <T> T mapToEntity(Map<String, String[]> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            List<Field> fieldList = new ArrayList<>();
            fieldList.addAll(Arrays.asList(entity.getDeclaredFields()));
            List<Field> fieldParentList = new ArrayList<>();
            entity = (Class<T>) entity.getSuperclass(); //得到父类,然后赋给自己
            while (entity != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                fieldParentList.addAll(Arrays.asList(entity.getDeclaredFields()));
                entity = (Class<T>) entity.getSuperclass(); //得到父类,然后赋给自己
            }
            for (Field field : fieldList) {
                if (map.containsKey(field.getName())) {
                    settingField(map, t, field);
                }
            }
            for (Field field : fieldParentList) {
                if (map.containsKey(field.getName())) {
                    settingField(map, t, field);
                }
            }
            return t;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t;
    }

    private <T> void settingField(Map<String, String[]> map, T t, Field field) throws IllegalAccessException {
        boolean flag = field.isAccessible();
        field.setAccessible(true);

        String[] object = map.get(field.getName());
        if (object != null && !"".equals(object[0])) {
            //基础类型
            if (isBasicDataTypes(field.getType()))
                field.set(t, parseBasicTypeWrapper(field.getType(), object[0]));
            if (Date.class.equals(field.getType())) {
                StringToDateConverter stringToDateConverter = new StringToDateConverter(new SimpleDateFormat("yyyy-MM-dd"), false);
                field.set(t, stringToDateConverter.convert(object[0]));
            }
        }
        field.setAccessible(flag);
    }

    // 基本类型解析
    private Object parsePrimitive(String parameterTypeName, Object value) {
        final String booleanTypeName = "boolean";
        if (booleanTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Boolean.valueOf(value.toString());
            }
            return Boolean.valueOf(false);
        }
        final String intTypeName = "int";
        if (intTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Integer.valueOf(value.toString());
            }
            return Integer.valueOf(0);
        }
        final String charTypeName = "char";
        if (charTypeName.equals(parameterTypeName)) {
            return value.toString().charAt(0);
        }
        final String shortTypeName = "short";
        if (shortTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Short.valueOf(value.toString());
            } else {
                return Short.valueOf((short) 0);
            }
        }
        final String longTypeName = "long";
        if (longTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Long.valueOf(value.toString());
            } else {
                return Long.valueOf(0);
            }
        }
        final String floatTypeName = "float";
        if (floatTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Float.valueOf(value.toString());
            } else {
                return Float.valueOf(0);
            }
        }
        final String doubleTypeName = "double";
        if (doubleTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Double.valueOf(value.toString());
            } else {
                return Double.valueOf(0);
            }
        }
        final String byteTypeName = "byte";
        if (byteTypeName.equals(parameterTypeName)) {
            if (!"".equals(value)) {
                return Byte.valueOf(value.toString());
            } else {
                return Byte.valueOf("");
            }
        }
        return null;
    }

    // 基本类型包装类型解析
    private Object parseBasicTypeWrapper(Class<?> parameterType, Object value) {
        if (Number.class.isAssignableFrom(parameterType)) {
            if (StringUtils.isEmpty(String.valueOf(value))) {
                return null;
            }
            if (parameterType == Integer.class) {
                return Integer.valueOf(String.valueOf(value)).intValue();
            } else if (parameterType == Short.class) {
                return Short.valueOf(String.valueOf(value)).shortValue();
            } else if (parameterType == Long.class) {
                return Long.valueOf(String.valueOf(value)).longValue();
            } else if (parameterType == Float.class) {
                return Float.valueOf(String.valueOf(value)).floatValue();
            } else if (parameterType == Double.class) {
                return Double.valueOf(String.valueOf(value)).doubleValue();
            } else if (parameterType == Byte.class) {
                return Byte.valueOf(String.valueOf(value)).byteValue();
            }
        } else if (parameterType == Boolean.class) {
            return Boolean.valueOf(String.valueOf(value));
//        } else if (parameterType == Character.class) {
//            return Character.valueOf(String.valueOf(value));
        } else if (parameterType == String.class) {
            return String.valueOf(value);
        }
        return null;
    }

    /**
     * 检查是不是基本类型包装类
     */
    private boolean isBasicDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(String.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }

    /**
     * 获取请求体JSON字符串
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = servletRequest.getContentType();
        if (isJson(contentType)) {
            // 有就直接获取
            String jsonBody = (String) webRequest.getAttribute(JSONBODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
            // 没有就从请求中读取
            if (jsonBody == null) {
                try {
                    jsonBody = IOUtils.toString(servletRequest.getReader());
                    webRequest.setAttribute(JSONBODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonBody;

        }
        return "";
    }
}

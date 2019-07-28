package com.linwen.comm.validation;


import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.converter.multirequestbody.MultiRequestBodyArgumentResolver;
import com.linwen.comm.spring.SpringUtil;
import com.linwen.comm.validation.annotation.*;
import com.linwen.comm.validation.enums.ParameterType;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by linwen on 18-12-23.
 * 处理请求校验的参数
 */
public class ValidationParameters {
    public Object getParameterValue(Map<String, String[]> parameters, String key) {
        if (parameters.containsKey(key)) {
            return parameters.get(key)[0];
        }
        return null;
    }

    public Object getParameterValue(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.get(key);
        }
        return null;
    }

    /**
     * 校验请求的参数
     *
     * @param pjp
     * @return
     */
    public Object handleParameters(ProceedingJoinPoint pjp, Method method) {
        Object[] objects = pjp.getArgs();
        Object object = pjp.getTarget();
        if (object instanceof BaseController) {
            BaseController baseController = (BaseController) object;
            HttpServletRequest httpServletRequest = null;
            if (baseController != null && baseController.getHttpServletRequest() != null) {
                httpServletRequest = baseController.getHttpServletRequest();
                Map<String, String[]> parameters = httpServletRequest.getParameterMap();
                String contentType = httpServletRequest.getContentType();
                JSONObject jsonObject = null;
                boolean isJson = false;
                if (MultiRequestBodyArgumentResolver.isJson(contentType)) {//json
                    //直接row传输
                    String resultStr = (String) httpServletRequest.getAttribute(MultiRequestBodyArgumentResolver.JSONBODY_ATTRIBUTE);
                    jsonObject = JSONObject.parseObject(resultStr);
                    isJson = true;
                }
                ValidationRequest validationRequest = method.getAnnotation(ValidationRequest.class);
                if (validationRequest == null) {
                    return null;
                }
                String[] validRanges = null;
                NotNull notNull = null;
                boolean isValidParameter = false;
                boolean isValidExistDB = false;
                boolean isValidNotExistDB = false;
                boolean isValidNotNull = false;
                ValidParameter validParameter = null;
                Field field = null;
                ParameterValueExistDB valueExistDB = null;
                ParameterValueNotExistDB valueNotExistDB = null;
                ValidInfo[] valids = null;
                String validRet = null;
                Object value = null;
                Object obj = null;
                for (int i = 0; i < objects.length; i++) {
                    obj = objects[i];
                    if (obj instanceof BaseCondition) {
                        Field[] fields = obj.getClass().getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            field = fields[j];
                            validParameter = field.getAnnotation(ValidParameter.class);
                            if (validParameter == null) {
                                return null;
                            }
                            isValidParameter = isValid(validParameter.validRanges(), validationRequest.validRange());

                            valueExistDB = validParameter.ValueExistDB();
                            isValidExistDB = isValid(valueExistDB.validRanges(), validationRequest.validRange());

                            valueNotExistDB = validParameter.ValueNotExistDB();
                            isValidNotExistDB = isValid(valueNotExistDB.validRanges(), validationRequest.validRange());

                            notNull = validParameter.notNull();
                            isValidNotNull = isValid(notNull.validRanges(), validationRequest.validRange());

                            if (!isValidParameter && !isValidNotNull && !isValidNotExistDB && !isValidExistDB) {
                                return null;
                            }

                            valids = validParameter.valids();
                            if (!"".equals(validParameter.fieldName())) {
                                if (isJson) {
                                    value = getParameterValue(jsonObject, validParameter.fieldName());
                                } else {//from-data
                                    value = getParameterValue(parameters, validParameter.fieldName());
                                }
                            }
                            //判断不能为空
                            if (isValidNotNull && notNull != null && notNull.isValid() && value == null) {
                                return baseController.JsonFailResult(httpServletRequest.getSession().getId(), notNull.message());
                            }
                            if (value != null) {
                                if (valids != null && valids.length > 0) {
                                    validRet = validsParameter(valids, validParameter, String.valueOf(value), validationRequest.validRange());
                                    if (validRet != null) {
                                        return baseController.JsonFailResult(httpServletRequest.getSession().getId(), validRet);
                                    }
                                }
                                if ((isValidExistDB && valueExistDB != null && valueExistDB.isValid())) {
                                    if (dBValueIsValid(valueExistDB.ValidClass(), valueExistDB.table(), 1, validParameter.fieldName(), valueExistDB.column(), String.valueOf(value), validParameter.parameterType()))
                                        return baseController.JsonFailResult(httpServletRequest.getSession().getId(), valueExistDB.message());
                                }
                                if ((isValidNotExistDB && valueNotExistDB != null && valueNotExistDB.isValid())) {
                                    if (dBValueIsValid(valueNotExistDB.ValidClass(), valueNotExistDB.table(), 2, validParameter.fieldName(), valueNotExistDB.column(), String.valueOf(value), validParameter.parameterType()))
                                        return baseController.JsonFailResult(httpServletRequest.getSession().getId(), valueNotExistDB.message());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean dBValueIsValid(Class<? extends Validator>[] classPaths, String table, int type, String key, String columnDB, String value, ParameterType parameterType) {
        if (classPaths.length == 0) {
            return false;
        }
        Validator validator = SpringUtil.getBean(classPaths[0]);
        //校验不通过返回true
        return validator.isValid(table, type, key, columnDB, value, parameterType);
    }

    public boolean isValid(String[] validRanges, String validRange) {
        boolean isValid = false;
        for (int k = 0; k < validRanges.length; k++) {
            if (validRanges[k].equals(validRange) || "all".equals(validRanges[k])) {
                isValid = true;
            }
        }
        return isValid;
    }

    public String validsParameter(ValidInfo[] valids, ValidParameter validParameter, String value, String validRange) {
        ValidInfo valid = null;
        String parameterValue = value;
        String key = validParameter.fieldName();
        ParameterType parameterType = validParameter.parameterType();
        if ("".equals(parameterValue)) {
            return null;
        }
        boolean isValid = false;
        for (int i = 0; i < valids.length; i++) {
            valid = valids[i];
            isValid = isValid(valid.validRanges(), validRange);
            if (!isValid) {
                continue;
            }
            switch (valid.name()) {
                case equal:
                    return equal(valid, key, parameterValue, parameterType);
                case notEqual:
                    return notEqual(valid, key, parameterValue, parameterType);
                case greaterThan:
                    return greaterThan(valid, key, parameterValue, parameterType);
                case greaterThanOrEqualTo:
                    return greaterThanOrEqualTo(valid, key, parameterValue, parameterType);
                case lessThan:
                    return lessThan(valid, key, parameterValue, parameterType);
                case lessThanOrEqualTo:
                    return lessThanOrEqualTo(valid, key, parameterValue, parameterType);
                case in:
                    return in(valid, key, parameterValue, parameterType);
            }
        }
        return null;
    }

    public String equal(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            switch (parameterType) {
                case String:
                    if (value.compareTo(valid.targetValue()) != 0) {
                        return valid.message();
                    }
                    break;
                case Long:
                    Long numberLong = Long.valueOf(value);
                    Long targetValueLong = Long.valueOf(valid.targetValue());
                    if (numberLong.compareTo(targetValueLong) != 0) {
                        return valid.message();
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    Double targetValueDouble = Double.valueOf(valid.targetValue());
                    if (numberDouble.compareTo(targetValueDouble) != 0) {
                        return valid.message();
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    Integer targetValueInteger = Integer.valueOf(valid.targetValue());
                    if (numberInteger.compareTo(targetValueInteger) != 0) {
                        return valid.message();
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }

    public String notEqual(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            switch (parameterType) {
                case String:
                    if (value.compareTo(valid.targetValue()) == 0) {
                        return valid.message();
                    }
                    break;
                case Long:
                    Long numberLong = Long.valueOf(value);
                    Long targetValueLong = Long.valueOf(valid.targetValue());
                    if (numberLong.compareTo(targetValueLong) == 0) {
                        return valid.message();
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    Double targetValueDouble = Double.valueOf(valid.targetValue());
                    if (numberDouble.compareTo(targetValueDouble) == 0) {
                        return valid.message();
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    Integer targetValueInteger = Integer.valueOf(valid.targetValue());
                    if (numberInteger.compareTo(targetValueInteger) == 0) {
                        return valid.message();
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }

    public String greaterThan(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            switch (parameterType) {
                case String:
                    if (value.compareTo(valid.targetValue()) <= 0) {
                        return valid.message();
                    }
                    break;
                case Long:
                    Long numberLong = Long.valueOf(value);
                    Long targetValueLong = Long.valueOf(valid.targetValue());
                    if (numberLong.compareTo(targetValueLong) <= 0) {
                        return valid.message();
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    Double targetValueDouble = Double.valueOf(valid.targetValue());
                    if (numberDouble.compareTo(targetValueDouble) <= 0) {
                        return valid.message();
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    Integer targetValueInteger = Integer.valueOf(valid.targetValue());
                    if (numberInteger.compareTo(targetValueInteger) <= 0) {
                        return valid.message();
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }

    public String greaterThanOrEqualTo(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            switch (parameterType) {
                case String:
                    if (value.compareTo(valid.targetValue()) < 0) {
                        return valid.message();
                    }
                    break;
                case Long:
                    Long numberLong = Long.valueOf(value);
                    Long targetValueLong = Long.valueOf(valid.targetValue());
                    if (numberLong.compareTo(targetValueLong) < 0) {
                        return valid.message();
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    Double targetValueDouble = Double.valueOf(valid.targetValue());
                    if (numberDouble.compareTo(targetValueDouble) < 0) {
                        return valid.message();
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    Integer targetValueInteger = Integer.valueOf(valid.targetValue());
                    if (numberInteger.compareTo(targetValueInteger) < 0) {
                        return valid.message();
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }

    public String lessThan(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            switch (parameterType) {
                case String:
                    if (value.compareTo(valid.targetValue()) >= 0) {
                        return valid.message();
                    }
                    break;
                case Long:
                    Long numberLong = Long.valueOf(value);
                    Long targetValueLong = Long.valueOf(valid.targetValue());
                    if (numberLong.compareTo(targetValueLong) >= 0) {
                        return valid.message();
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    Double targetValueDouble = Double.valueOf(valid.targetValue());
                    if (numberDouble.compareTo(targetValueDouble) >= 0) {
                        return valid.message();
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    Integer targetValueInteger = Integer.valueOf(valid.targetValue());
                    if (numberInteger.compareTo(targetValueInteger) >= 0) {
                        return valid.message();
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }

    public String lessThanOrEqualTo(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            switch (parameterType) {
                case String:
                    if (value.compareTo(valid.targetValue()) > 0) {
                        return valid.message();
                    }
                    break;
                case Long:
                    Long numberLong = Long.valueOf(value);
                    Long targetValueLong = Long.valueOf(valid.targetValue());
                    if (numberLong.compareTo(targetValueLong) > 0) {
                        return valid.message();
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    Double targetValueDouble = Double.valueOf(valid.targetValue());
                    if (numberDouble.compareTo(targetValueDouble) > 0) {
                        return valid.message();
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    Integer targetValueInteger = Integer.valueOf(valid.targetValue());
                    if (numberInteger.compareTo(targetValueInteger) > 0) {
                        return valid.message();
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }

    public String in(ValidInfo valid, String key, String value, ParameterType parameterType) {
        try {
            String[] targetValue = valid.targetValue().split(",");
            boolean isValid = true;
            switch (parameterType) {
                case Long:
                    Long numberLong = Long.valueOf(value);
                    for (int i = 0; i < targetValue.length; i++) {
                        Long targetValueLong = Long.valueOf(targetValue[i]);
                        if (numberLong.compareTo(targetValueLong) == 0) {
                            isValid = false;
                            break;
                        }
                    }
                    break;
                case Double:
                    Double numberDouble = Double.valueOf(value);
                    for (int i = 0; i < targetValue.length; i++) {
                        Double targetValueDouble = Double.valueOf(targetValue[i]);
                        if (numberDouble.compareTo(targetValueDouble) == 0) {
                            isValid = false;
                            break;
                        }
                    }
                    break;
                case Integer:
                    Integer numberInteger = Integer.valueOf(value);
                    for (int i = 0; i < targetValue.length; i++) {
                        Integer targetValueInteger = Integer.valueOf(targetValue[i]);
                        if (numberInteger.compareTo(targetValueInteger) == 0) {
                            isValid = false;
                            break;
                        }
                    }
                    break;
            }
            if (isValid) {
                return valid.message();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return key + "的值转换异常";
        }
    }
}

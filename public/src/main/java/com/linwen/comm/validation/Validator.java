package com.linwen.comm.validation;


import com.linwen.comm.validation.enums.ParameterType;

/**
 * Created by linwen on 18-12-23.
 */
public interface Validator {
    /**
     * @param table
     * @param type          1 value校验存在数据库 2 value校验不存在数据库
     * @param key
     * @param columnDB
     * @param value
     * @param parameterType
     * @return 校验成功返回true，校验失败返回false
     */
    boolean isValid(String table, int type, String key, String columnDB, String value, ParameterType parameterType);
}

package com.linwen.comm.converter;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by linwen on 18-12-17.
 */

/**
 * 对象中的字段进行处理
 */
public class StringToNullConverter extends PropertyEditorSupport {
    public String convert(String value) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return value;
    }

    public String getAsText() {
        String value = (String) this.getValue();
        return !StringUtils.isEmpty(value) ? value : null;
    }

    public void setAsText(@Nullable String text) throws IllegalArgumentException {
        this.setValue(convert(text));
    }
}

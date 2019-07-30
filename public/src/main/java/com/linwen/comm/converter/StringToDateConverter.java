package com.linwen.comm.converter;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 自定义spring校验的时间格式转换，如果转换不通过将不做报错处理
 */
public class StringToDateConverter extends PropertyEditorSupport {

    private final DateFormat dateFormat;
    private final boolean allowEmpty;
    private final int exactDateLength;

    public StringToDateConverter(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    public StringToDateConverter(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    public static void main(String[] args) {
    }

    public Date convert(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        value = value.trim();
        Date dtDate = null;
        try {
            if (Pattern.matches("^-?\\d+$", value)) {
                if (value.length() > 11) {
                    dtDate = new Date(Long.valueOf(value));
                } else if (value.length() == 11) {
                    dtDate = new Date(Long.valueOf(value) * 1000);
                }
            } else {
                dtDate = dateFormat.parse(value);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            dtDate = null;
        }
        return dtDate;
    }

    public String getAsText() {
        Date value = (Date) this.getValue();
        return value != null ? this.dateFormat.format(value) : "";
    }

    public void setAsText(@Nullable String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            this.setValue(null);
        } else {
            if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
                throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
            }

            this.setValue(convert(text));
        }

    }

}
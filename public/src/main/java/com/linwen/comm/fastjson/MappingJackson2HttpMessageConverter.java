package com.linwen.comm.fastjson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class MappingJackson2HttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static final Logger LOG = LoggerFactory.getLogger(MappingJackson2HttpMessageConverter.class);

    private boolean encryptFlag = false;

    public void setEncryptFlag(boolean encryptFlag) {
        this.encryptFlag = encryptFlag;
    }

    public MappingJackson2HttpMessageConverter() {
        super(new MediaType("application", "json", Charset.forName("UTF-8")));
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }

    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return mapper.readValue(inputMessage.getBody(), clazz);
    }

    protected void writeInternal(Object d, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        byte[] jsonBytes;
        jsonBytes = ToJsonAsBytes(d);
        OutputStream out = outputMessage.getBody();
        out.write(jsonBytes, 0, jsonBytes.length);
        out.close();
    }

    public static byte[] ToJsonAsBytes(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (Exception var2) {
            LOG.error("ToJsonAsBytes error,Object:{}", value, var2);
            return null;
        }
    }

    public static String ToJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception var2) {
            LOG.error("ToJosn error,Object:{}", value, var2);
            return "";
        }
    }
}
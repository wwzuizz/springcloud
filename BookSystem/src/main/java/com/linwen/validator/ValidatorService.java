package com.linwen.validator;

import com.linwen.comm.base.BaseService;
import com.linwen.comm.validation.Validator;
import com.linwen.comm.validation.enums.ParameterType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class ValidatorService extends BaseService implements Validator {

    @Override
    public boolean isValid(String table, int type, String key, String columnDB, String value, ParameterType parameterType) {
        String mysql = "select count(0) from " + table + " where " + columnDB + "=" + value;
        List<BigInteger> bigIntegers = entityManager.createNativeQuery(mysql).getResultList();
        if (bigIntegers.size() > 0) {
            if (bigIntegers.get(0).intValue() > 0) {
                return type == 1 ? true : false;
            }
        }
        return type == 1 ? false : true;
    }
}

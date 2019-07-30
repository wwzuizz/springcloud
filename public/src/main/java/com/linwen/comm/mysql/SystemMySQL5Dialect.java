package com.linwen.comm.mysql;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class SystemMySQL5Dialect extends MySQL5Dialect {
    public SystemMySQL5Dialect() {
        registerFunction("now", new SQLFunctionTemplate(StandardBasicTypes.STRING, "now()"));
        registerFunction("asin", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "asin(?1)"));
        registerFunction("sqrt", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "sqrt(?1)"));
        registerFunction("pow", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "POW(?1,?2)"));
        registerFunction("sqrt", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "sqrt(?1)"));
        registerFunction("sin", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "sin(?1)"));
        registerFunction("CAST", new SQLFunctionTemplate(StandardBasicTypes.STRING, "CAST(?1)"));
        registerFunction("SYSDATE", new SQLFunctionTemplate(StandardBasicTypes.STRING, "SYSDATE()"));
        registerHibernateType(-1, StandardBasicTypes.TEXT.getName());
    }
}
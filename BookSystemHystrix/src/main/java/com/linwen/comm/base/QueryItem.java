package com.linwen.comm.base;

/**
 * Created by linwen on 18-12-24.
 */
public class QueryItem {

    private String exp;//表达式
    private String value;//值

    public QueryItem() {
    }

    public QueryItem(String exp, String value) {
        this.exp = exp;
        this.value = value;
    }

    public String getExp() {
        return exp;
    }

    public QueryItem setExp(String exp) {
        this.exp = exp;
        return this;
    }

    public String getValue() {
        return value;
    }

    public QueryItem setValue(String value) {
        this.value = value;
        return this;
    }
}

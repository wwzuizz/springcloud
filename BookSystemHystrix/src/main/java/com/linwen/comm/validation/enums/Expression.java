package com.linwen.comm.validation.enums;

/**
 * Created by linwen on 18-12-22.
 */
public enum Expression {
    equal("="),//等于
    notEqual("!="),//不等于
    greaterThan(">"),//大于
    greaterThanOrEqualTo(">="),//大于等于
    lessThan("<"),//小于
    lessThanOrEqualTo("<="),//小于等于
    notNull("notNull"),//不能为空，字符串追加不能为""
    in("in"),//属于
    ;
    String expression;

    Expression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}

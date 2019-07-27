package com.linwen.comm.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseCondition extends BaseBean {

    protected final static Integer PAGE_SHOW_COUNT = 5;
    protected static final long serialVersionUID = 1L;
    protected Map<String, Query> params = new LinkedHashMap();
    protected Map<String, String> order = new LinkedHashMap();
    protected Integer pageNum = 1;
    protected Integer pageSize = 0;
    protected Integer totalCount = 0;
    protected Integer pageCount;
    protected String orderField;
    protected String orderDirection;
    List<BaseCondition> conditionList = new ArrayList<>();
    protected QueryHelp queryHelp;
    String MysqlTable;

    public String getMysqlTable() {
        return MysqlTable;
    }

    public void setMysqlTable(String mysqlTable) {
        MysqlTable = mysqlTable;
    }

    public QueryHelp getQueryHelp() {
        return queryHelp;
    }

    public BaseCondition setQueryHelp(QueryHelp queryHelp) {
        this.queryHelp = queryHelp;
        return this;
    }

    public List<BaseCondition> getConditionList() {
        return conditionList;
    }

    public BaseCondition setConditionList(List<BaseCondition> conditionList) {
        this.conditionList = conditionList;
        return this;
    }

    public Map<String, String> getOrder() {
        return order;
    }

    public void setOrder(Map order) {
        this.order = order;
    }

    public Map<String, Query> getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public BaseCondition addQuery(String queryKey, Query query) {
        if (params.containsKey(queryKey)) {
            Query queryOld = params.get(queryKey);
            queryOld.setTable(query.getTable());
            queryOld.getQueryItemList().addAll(query.getQueryItemList());
        } else {
            params.put(queryKey, query);
        }
        return this;
    }

    public BaseCondition addQuery(String queryKey, List<QueryItem> queryItemList) {
        if (params.containsKey(queryKey)) {
            params.get(queryKey).getQueryItemList().addAll(queryItemList);
        } else {
            params.put(queryKey, new Query(queryItemList));
        }
        return this;
    }

    public BaseCondition addQuery(String queryKey, QueryItem queryItem) {
        if (params.containsKey(queryKey)) {
            params.get(queryKey).getQueryItemList().add(queryItem);
        } else {
            List<QueryItem> queryItemList = new ArrayList<>();
            queryItemList.add(queryItem);
            params.put(queryKey, new Query(queryItemList));
        }
        return this;
    }

    public BaseCondition addOrder(String orderKey, String expression) {
        order.put(orderKey, expression);
        return this;
    }

    public Integer getPageCount() {

        return pageCount;
    }

    public void setPageCount(Integer pageCount) {

        this.pageCount = pageCount;
    }

    public Integer getPageNum() {

        return pageNum != null ? pageNum - 1 : 0;
    }

    public BaseCondition setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Integer getPageSize() {

        return pageSize != null && pageSize > 0 ? pageSize : PAGE_SHOW_COUNT;
    }

    public BaseCondition setPageSize(Integer pageSize) {

        this.pageSize = pageSize;
        return this;
    }

    public String getOrderField() {

        return orderField;
    }

    public BaseCondition setOrderField(String orderField) {

        this.orderField = orderField;
        return this;
    }

    public String getOrderDirection() {

        return orderDirection == null ? "desc" : orderDirection;
    }

    public BaseCondition setOrderDirection(String orderDirection) {

        this.orderDirection = orderDirection;
        return this;
    }

    public Integer getTotalCount() {

        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {

        this.totalCount = totalCount;
        Integer a = totalCount / getPageSize();
        Integer b = totalCount % getPageSize();
        if (b > 0)
            ++a;
        setPageCount(a);// 计算页数
    }

    public Integer getStartIndex() {
        Integer pageNum = this.getPageNum() > 0 ? this.getPageNum() - 1 : 0;
        return pageNum * this.getPageSize();
    }

    public <T extends BaseBean> T getBeseBean() {

        return null;
    }

}

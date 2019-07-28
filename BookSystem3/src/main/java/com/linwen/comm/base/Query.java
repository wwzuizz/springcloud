package com.linwen.comm.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwen on 18-12-24.
 */
public class Query {
    public static final String _EXP = "_exp";
    public static final String EXP = "exp";
    public static final String VALUE = "value";
    public static final String ORDERBY = "orderBy";
    public static final String BY = "by";
    public static final String ORDER = "order";

    public Query() {

    }

    public Query(List<QueryItem> queryItemList) {
        this.queryItemList = queryItemList;
    }

    private String table;
    private List<QueryItem> queryItemList = new ArrayList<>();

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<QueryItem> getQueryItemList() {
        return queryItemList;
    }

    public void setQueryItemList(List<QueryItem> queryItemList) {
        this.queryItemList = queryItemList;
    }
}

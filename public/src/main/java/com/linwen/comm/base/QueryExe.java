package com.linwen.comm.base;

import javax.persistence.criteria.From;
import java.util.HashMap;
import java.util.Map;

public class QueryExe {
    From from;
    String table;
    Map<String, Query> queryMap = new HashMap<>();

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Query> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, Query> queryMap) {
        this.queryMap = queryMap;
    }
}

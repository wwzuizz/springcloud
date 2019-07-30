package com.linwen.comm.base;

import org.hibernate.jpa.QueryHints;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

/**
 * Created by linwen on 19-7-6.
 */
public abstract class QueryHelp implements QueryListener<BaseCondition> {
    @Override
    public void getExpressionPredicate(From root, CriteriaBuilder criteriaBuilder, List<Predicate> predicate, String table, String key, String value, String expression) {

    }

    @Override
    public void groupBy(From from, CriteriaQuery criteriaQuery) {

    }

    @Override
    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        QueryExe queryExe = new QueryExe();
        queryExe.setTable(baseCondition.getMysqlTable());
        queryExe.setFrom(root);
        return queryExe;
    }

    @Override
    public void settingTableWhere(List<QueryExe> queryExeList, Map<String, Query> queryMap) {
        QueryExe queryExe = null;
        Query query = null;
        for (int i = 0; i < queryExeList.size(); i++) {
            for (String key :
                    queryMap.keySet()) {
                query = queryMap.get(key);
                if (query.getTable().equals(queryExe.getTable())) {
                    queryExe = queryExeList.get(i);
                    queryExe.getQueryMap().put(key, queryMap.get(key));
                    queryMap.remove(key);
                }
            }
        }
    }

    public void settingHint(TypedQuery typedQuery, Class clas) {
        typedQuery.setHint(QueryHints.HINT_CACHE_REGION, clas.toString());
        typedQuery.setHint(QueryHints.HINT_CACHEABLE, "true");
    }
}

package com.linwen.comm.base;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

/**
 * Created by linwen on 19-7-6.
 */
public interface QueryListener<D extends BaseCondition> {
    public void srttingTypedQuery(TypedQuery typedQuery);

    public void query(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder, List<Predicate> predicate, D conditon);

    public void settingConditions(List<D> baseConditions, Map<String, Query> queryMap);

    public void getExpressionPredicate(From root, CriteriaBuilder criteriaBuilder, List<Predicate> predicate, String table, String key, String value, String expression);

    public void groupBy(From from, CriteriaQuery criteriaQuery);

    public QueryExe getJoinList(From root, BaseCondition baseConditions);

    public void settingTableWhere(List<QueryExe> queryExeList, Map<String, Query> queryMap);
}

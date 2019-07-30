/**
 *
 */

package com.linwen.comm.base;

import com.linwen.comm.entity.IdGenerator;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @author Guocg
 */
public abstract class BaseQueryService<T extends BaseBean, D extends BaseCondition, ID extends Object> extends BaseService {
    public abstract Class<T> getMysqlBindClass();

    public int install(T baseBean) throws IllegalAccessException {
        return entityManager.createNativeQuery(getInstall(baseBean)).executeUpdate();
    }

    public int installs(List<T> baseBean) throws IllegalAccessException {
        String sql = "";
        for (int i = 0; i < baseBean.size(); i++) {
            sql += getInstall(baseBean.get(i));
        }
        return entityManager.createNativeQuery(sql).executeUpdate();
    }

    public String getInstall(T baseBean) throws IllegalAccessException {
        Table table = baseBean.getClass().getAnnotation(Table.class);
        Field[] fields = baseBean.getClass().getDeclaredFields();
        String sql = "INSERT INTO `" + table.name() + "` ";
        String fieldMysql = "(";
        String fieldValue = "(";
        String temp = "";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            boolean flag = field.isAccessible();
            if (column != null) {
                Object o = field.get(baseBean);
                if (o != null) {
                    fieldMysql += temp + "`" + column.name() + "`";
                    fieldValue += temp + valueFieldConvert(field, o);
                    if ("".equals(temp)) {
                        temp = ",";
                    }
                } else {
                    Id id = field.getAnnotation(Id.class);
                    if (id != null) {
                        Object object = IdGenerator.getGuid();
                        field.set(baseBean, object);
                        fieldMysql += temp + "`" + column.name() + "`";
                        fieldValue += temp + object;
                        if ("".equals(temp)) {
                            temp = ",";
                        }
                    } else {
                        if (!column.nullable()) {
                            fieldMysql += temp + "`" + column.name() + "`";
                            fieldValue += temp + valueFieldConvertDefault(field, o);
                            if ("".equals(temp)) {
                                temp = ",";
                            }
                        }
                    }
                }
            }

            field.setAccessible(flag);
        }
        fieldMysql += ") ";
        fieldValue += ") ";
        sql += fieldMysql + " VALUES " + fieldValue + ";";
        return sql;
    }

    public int update(T baseBean, D baseCondition, boolean isNull) throws IllegalAccessException {
        return entityManager.createNativeQuery(getUpdate(baseBean, baseCondition, isNull)).executeUpdate();
    }

    public int updates(List<T> baseBean, D baseCondition, boolean isNull) throws IllegalAccessException {
        String sql = "";
        for (int i = 0; i < baseBean.size(); i++) {
            sql += getUpdate(baseBean.get(i), baseCondition, isNull);
        }
        return entityManager.createNativeQuery(sql).executeUpdate();
    }

    /**
     * @param baseBean      新的实体
     * @param baseCondition 更新条件（批量用的）
     * @param isNull        是否置空
     * @throws IllegalAccessException
     */
    public String getUpdate(T baseBean, D baseCondition, boolean isNull) throws IllegalAccessException {
        Table table = baseBean.getClass().getAnnotation(Table.class);
        Field[] fields = baseBean.getClass().getDeclaredFields();
        String sql = "UPDATE `" + table.name() + "` SET ";
        String temp = ",";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                Object o = field.get(baseBean);
                if (o != null) {
                    sql += "`" + column.name() + "`=" + valueFieldConvert(field, o) + "";
                    if ("".equals(temp)) {
                        temp = ",";
                    }
                } else if (isNull) {
                    sql += temp + "`" + column.name() + "`=null";
                    if ("".equals(temp)) {
                        temp = ",";
                    }
                }
            }
            field.setAccessible(false);
        }
        sql += " " + getWhereQuery(baseCondition) + ";";
        return sql;
    }


    protected T getEMOne(D conditon) {
        TypedQuery typedQuery = entityManager.createQuery(getCriteriaQuery(conditon));
        if (conditon.getQueryHelp() != null) {
            conditon.getQueryHelp().settingHint(typedQuery, getMysqlBindClass());
        } else {
            defaultHint(typedQuery, getMysqlBindClass());
        }
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(1);
        try {
            return (T) typedQuery.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    protected List<T> getEmListAll(D conditon) {
        Sort sort = getOrders(conditon);
        TypedQuery typedQuery = entityManager.createQuery(getCriteriaQuery(conditon, sort));
        if (conditon.getQueryHelp() != null) {
            conditon.getQueryHelp().settingHint(typedQuery, getMysqlBindClass());
        } else {
            defaultHint(typedQuery, getMysqlBindClass());
        }
        return typedQuery.getResultList();
    }

    protected List<T> getEmList(D conditon) {
        Sort sort = getOrders(conditon);
        PageRequest pageRequest = PageRequest.of(conditon.getPageNum(), conditon.getPageSize(), sort);
        TypedQuery typedQuery = entityManager.createQuery(getCriteriaQuery(conditon, pageRequest));
        if (conditon.getQueryHelp() != null) {
            conditon.getQueryHelp().settingHint(typedQuery, getMysqlBindClass());
        } else {
            defaultHint(typedQuery, getMysqlBindClass());
        }
        typedQuery.setFirstResult((int) pageRequest.getOffset());
        typedQuery.setMaxResults(pageRequest.getPageSize());
        return typedQuery.getResultList();
    }

    protected Page<T> getEMPageList(D conditon) {
        Sort sort = getOrders(conditon);
        PageRequest pageRequest = PageRequest.of(conditon.getPageNum(), conditon.getPageSize(), sort);
        TypedQuery typedQuery = entityManager.createQuery(getCriteriaQuery(conditon, pageRequest));
        if (conditon.getQueryHelp() != null) {
            conditon.getQueryHelp().settingHint(typedQuery, getMysqlBindClass());
        } else {
            defaultHint(typedQuery, getMysqlBindClass());
        }
        typedQuery.setFirstResult((int) pageRequest.getOffset());
        typedQuery.setMaxResults(pageRequest.getPageSize());
        return PageableExecutionUtils.getPage(typedQuery.getResultList(), pageRequest, () -> countPageList(conditon));
    }

    private CriteriaQuery getCriteriaQuery(D conditon, PageRequest pageRequest, Sort sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getMysqlBindClass());//列表查询
        Root<T> root = criteriaQuery.from(getMysqlBindClass());
        List<Predicate> predicate = new ArrayList<>();

        QueryHelp queryHelp = conditon.getQueryHelp();
        whereSql(conditon, criteriaBuilder, criteriaQuery, root, predicate, queryHelp);

        criteriaQuery.where(predicate.toArray(new Predicate[predicate.size()]));
        if (pageRequest != null && pageRequest.getSort().isSorted()) {
            criteriaQuery.orderBy(toOrders(pageRequest.getSort(), root, criteriaBuilder));
        }
        if (sort != null && sort.isSorted()) {
            criteriaQuery.orderBy(toOrders(sort, root, criteriaBuilder));
        }
        return criteriaQuery;
    }

    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        QueryExe queryExe = new QueryExe();
        queryExe.setTable(baseCondition.getMysqlTable());
        queryExe.setFrom(root);
        return queryExe;
    }

    public void settingTableWhere(List<QueryExe> queryExeList, Map<String, Query> queryMap) {
        QueryExe queryExe = null;
        Query query = null;
        for (int i = 0; i < queryExeList.size(); i++) {
            for (String key :
                    queryMap.keySet()) {
                query = queryMap.get(key);
                queryExe = queryExeList.get(i);
                if (query.getTable().equals(queryExe.getTable())) {
                    queryExe.getQueryMap().put(key, queryMap.get(key));
                    queryMap.remove(key);
                }
            }
        }
    }

    public void groupBy(From from, CriteriaQuery criteriaQuery) {

    }

    public void settingConditions(List<BaseCondition> baseConditions, Map<String, Query> queryMap) {

    }

    private CriteriaQuery countCriteriaQuery(D conditon) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Long.class);//列表查询
        Root<T> root = criteriaQuery.from(getMysqlBindClass());
        List<Predicate> predicate = new ArrayList<>();
        QueryHelp queryHelp = conditon.getQueryHelp();
        whereSql(conditon, criteriaBuilder, criteriaQuery, root, predicate, queryHelp);
        criteriaQuery.where(predicate.toArray(new Predicate[predicate.size()]));
        if (criteriaQuery.isDistinct()) {
            criteriaQuery.select(criteriaBuilder.countDistinct(root));
        } else {
            criteriaQuery.select(criteriaBuilder.count(root));
        }
        return criteriaQuery;
    }

    private void whereSql(D conditon, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Root<T> root, List<Predicate> predicate, QueryHelp queryHelp) {
        List<BaseCondition> conditionList = conditon.getConditionList();
        conditionList.add(conditon);
        for (int i = 0; i < conditionList.size(); i++) {
            if (conditionList.get(i).getBeseBean() != null) {
                Table table = conditon.getBeseBean().getClass().getAnnotation(Table.class);
                conditionList.get(i).setMysqlTable(table.name());
                Field[] fields = conditon.getBeseBean().getClass().getDeclaredFields();
                Query query = null;
                List<QueryItem> queryItemList = null;
                QueryItem queryItem = null;
                for (int ii = 0; ii < fields.length; ii++) {
                    query = new Query();
                    query.setTable(table.name());
                    queryItem = new QueryItem();
                    queryItemList = new ArrayList<>();
                    Field field = fields[ii];
                    try {
                        field.setAccessible(true);
                        Object o = field.get(conditon.getBeseBean());
                        if (o != null) {
                            queryItem.setExp("=");
                            queryItem.setValue(o.toString());
                        } else {
                            continue;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                    queryItemList.add(queryItem);
                    query.setQueryItemList(queryItemList);
                    conditon.addQuery(table.name(), query);
                }
            }
        }
        //整理where
        if (queryHelp != null) {
            queryHelp.settingConditions(conditionList, conditon.getParams());
        } else {
            settingConditions(conditionList, conditon.getParams());
        }

        //获得join的表
        List<QueryExe> joinList = new ArrayList<>();
        if (queryHelp != null) {
            for (int i = 0; i < conditionList.size(); i++) {
                joinList.add(queryHelp.getJoinList(root, conditionList.get(i)));
            }
            queryHelp.settingTableWhere(joinList, conditon.getParams());
        } else {
            for (int i = 0; i < conditionList.size(); i++) {
                joinList.add(getJoinList(root, conditionList.get(i)));
            }
            settingTableWhere(joinList, conditon.getParams());
        }
        //处理where的条件
        Map<String, Query> queryMap = null;
        List<QueryItem> queryItemList = null;
        QueryItem queryItem = null;
        for (int i = 0; i < joinList.size(); i++) {
            queryMap = joinList.get(i).getQueryMap();
            for (String key :
                    queryMap.keySet()) {
                queryItemList = queryMap.get(key).getQueryItemList();
                for (int j = 0; j < queryItemList.size(); j++) {
                    queryItem = queryItemList.get(i);
                    if (!"".equals(queryItem.getValue())) {
                        if (queryHelp != null) {
                            queryHelp.getExpressionPredicate(joinList.get(i).getFrom(), criteriaBuilder, predicate, joinList.get(i).getTable(), key, queryItem.getValue(), queryItem.getExp());
                        } else {
                            getExpressionPredicate(joinList.get(i).getFrom(), criteriaBuilder, predicate, joinList.get(i).getTable(), key, queryItem.getValue(), queryItem.getExp());
                        }
                    }
                }
            }
        }
        //处理group
        for (int i = 0; i < joinList.size(); i++) {
            if (queryHelp != null) {
                queryHelp.groupBy(joinList.get(i).getFrom(), criteriaQuery);
            } else {
                groupBy(joinList.get(i).getFrom(), criteriaQuery);
            }
        }
    }


    public long countPageList(D conditon) {
        TypedQuery typedQuery = entityManager.createQuery(countCriteriaQuery(conditon));
        if (conditon.getQueryHelp() != null) {
            conditon.getQueryHelp().settingHint(typedQuery, getMysqlBindClass());
        } else {
            defaultHint(typedQuery, getMysqlBindClass());
        }
        List<Long> totals = typedQuery.getResultList();
        long total = 0L;
        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }

    protected T getEMOne(ID id) {
        return (T) entityManager.find(getMysqlBindClass(), id);
    }


    private CriteriaQuery getCriteriaQuery(D conditon) {
        return getCriteriaQuery(conditon, null, null);
    }

    private CriteriaQuery getCriteriaQuery(D conditon, Sort sort) {

        return getCriteriaQuery(conditon, null, sort);
    }

    private CriteriaQuery getCriteriaQuery(D conditon, PageRequest pageRequest) {

        return getCriteriaQuery(conditon, pageRequest, null);
    }

    public void defaultHint(TypedQuery typedQuery, Class clas) {
        typedQuery.setHint(QueryHints.HINT_CACHE_REGION, clas.toString());
        typedQuery.setHint(QueryHints.HINT_CACHEABLE, "true");
    }


    private String getExpressionPredicate(String key, String value, String expression) {
        if ("like".equals(expression)) {
            return key + " like \"" + "%" + value + "%\"";
        } else if ("==".equals(expression)) {
            return key + " =\"" + value + "\"";
        } else if ("!=".equals(expression)) {
            return key + " !=\"" + value + "\"";
        } else if (">=".equals(expression)) {
            return key + " >=" + value;
        } else if ("<=".equals(expression)) {
            return key + " <=" + value;
        } else if (">".equals(expression)) {
            return key + " >" + value;
        } else if ("<".equals(expression)) {
            return key + " <" + value;
        } else if ("in".equals(expression)) {
            return key + " in (" + value + ")";
        } else if ("notIn".equals(expression)) {
            return key + " not in (" + value + ")";
        } else if ("notNull".equals(expression)) {
            return key + " is not null";
        } else if ("isNull".equals(expression)) {
            return key + " is null";
        }
        return "";
    }

    /**
     * 反射获取属性
     *
     * @param conditon
     * @return
     * @throws IllegalAccessException
     */
    private Map<String, QueryItem> getReflectionConditon(D conditon) throws IllegalAccessException {
        Map<String, QueryItem> queryBeanMap = new HashMap<>();
        Field[] fields = conditon.getClass().getDeclaredFields();
        Object ob = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            ob = field.get(conditon);
            if (ob != null) {
                queryBeanMap.put(field.getName(), new QueryItem("=", valueFieldConvert(field, ob) + ""));
            }
            field.setAccessible(false);
        }
        return queryBeanMap;
    }

    /**
     * 转换值微mysql使用
     *
     * @param field
     * @param ob
     * @return
     */
    private Object valueFieldConvert(Field field, Object ob) {
        if (isBasicDataTypes(field.getType())) {
            return "'" + ob + "'";
        }
        if (field.getType() == Date.class) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return "'" + simpleDateFormat.format(ob) + "'";
        }
        if (field.getType() == String.class) {
            return "\"" + ob + "\"";
        }
        return ob;
    }

    private Object valueFieldConvertDefault(Field field, Object ob) {
        if (isBasicDataTypes(field.getType())) {
            if (ob == null) {
                ob = 0;
            }
            return "'" + ob + "'";
        }
        if (field.getType() == Date.class) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            if (ob == null) {
                ob = simpleDateFormat.format(new Date());
            }
            return "'" + simpleDateFormat.format(ob) + "'";
        }
        if (field.getType() == String.class) {
            if (ob == null) {
                ob = "";
            }
            return "\"" + ob + "\"";
        }
        return ob;
    }

    /**
     * 是不是数值类型
     *
     * @param clazz
     * @return
     */
    private boolean isBasicDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        return classSet.contains(clazz);
    }

    /**
     * 创建动态sql用的
     *
     * @param conditon
     * @return
     */
    protected String getWhereQuery(D conditon) {
        if (conditon == null) {
            return "";
        }
        Map<String, QueryItem> queryBeanMap = null;
        try {
            queryBeanMap = getReflectionConditon(conditon);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Map<String, Query> stringStringMap = conditon.getParams();
        if (queryBeanMap != null)
            for (String key :
                    queryBeanMap.keySet()) {
                if (stringStringMap.containsKey(key)) {
                    stringStringMap.get(key).getQueryItemList().add(queryBeanMap.get(key));
                } else {
                    List<QueryItem> queryItems = new ArrayList<>();
                    queryItems.add(queryBeanMap.get(key));
                    stringStringMap.put(key, new Query(queryItems));
                }
            }
        String whereQuery = "";
        for (String key :
                stringStringMap.keySet()) {
            try {
                List<QueryItem> queryItemList = stringStringMap.get(key).getQueryItemList();
                QueryItem queryItem = null;
                for (int i = 0; i < queryItemList.size(); i++) {
                    if ("".equals(whereQuery)) {
                        whereQuery = " where ";
                    }
                    queryItem = queryItemList.get(i);
                    String expression = queryItem.getExp();
                    String value = queryItem.getValue();
                    if (!"".equals(value)) {
                        whereQuery += " and " + getExpressionPredicate(key, value, expression) + "  ";
                    }
                }
            } catch (Exception e) {
                return "";
            }
        }
        return whereQuery;
    }


    private void getExpressionPredicate(From root, CriteriaBuilder criteriaBuilder, List<Predicate> predicate, String table, String key, String value, String expression) {
        if ("like".equals(expression)) {
            predicate.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
        } else if ("==".equals(expression)) {
            predicate.add(criteriaBuilder.equal(root.get(key), value));
        } else if ("!=".equals(expression)) {
            predicate.add(criteriaBuilder.notEqual(root.get(key), value));
        } else if (">=".equals(expression)) {
            predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get(key), value));
        } else if ("<=".equals(expression)) {
            predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get(key), value));
        } else if (">".equals(expression)) {
            predicate.add(criteriaBuilder.greaterThan(root.get(key), value));
        } else if ("<".equals(expression)) {
            predicate.add(criteriaBuilder.lessThan(root.get(key), value));
        } else if ("in".equals(expression)) {
            CriteriaBuilder.In<Object> idIn = criteriaBuilder.in(root.get(key));
            boolean is_in = false;
            String[] ids = value.split(",");
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            Class entityClass = (Class) params[2];
            if (entityClass != null)
                for (int i = 0; i < ids.length; i++) {
                    if (entityClass.getName().equals("java.lang.Long")) {
                        idIn.value(Long.valueOf(ids[i]));
                        is_in = true;
                    } else if (entityClass.getName().equals("java.lang.Integer")) {
                        idIn.value(Integer.valueOf(ids[i]));
                        is_in = true;
                    }
                }
            if (is_in) {
                predicate.add(idIn);
            }
        } else if ("notNull".equals(expression)) {
            predicate.add(criteriaBuilder.isNotNull(root.get(key)));
        } else if ("isNull".equals(expression)) {
            predicate.add(criteriaBuilder.isNull(root.get(key)));
        }
    }
}

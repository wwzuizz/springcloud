/**
 *
 */

package com.linwen.comm.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Guocg
 */
public abstract class BaseService {
    @Resource
    protected PlatformTransactionManager platformTransactionManager;
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    @PersistenceContext
    protected EntityManager entityManager;


    public static void settingJsonObject(JSONObject object, String key, Object value) {
        if (value == null) {
            object.put(key, "");
        } else {
            if (value instanceof Date) {
                object.put(key, (((Date) value).getTime() + ""));
            }
            if (value instanceof JSONArray || value instanceof JSONObject) {
                object.put(key, value);
            } else {
                object.put(key, (value + ""));
            }
        }
    }

    /**
     * 开始事务
     *
     * @return
     */
    public TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return platformTransactionManager.getTransaction(def);
    }

    /**
     * 失败的
     *
     * @param Status
     */
    public void rollback(TransactionStatus Status) {
        platformTransactionManager.rollback(Status);
    }

    /**
     * 成功的
     *
     * @param Status
     */
    public void commit(TransactionStatus Status) {
        platformTransactionManager.commit(Status);
    }

    protected Sort getOrders(BaseCondition conditon) {

        if (conditon.getOrder().size() > 0) {
            if (conditon.getOrderField() != null && !"".equals(conditon.getOrderField())) {
                conditon.addOrder(conditon.getOrderField(), "desc");
            }
            if (conditon.getOrderDirection() != null && "asc".equals(conditon.getOrderDirection())) {
                conditon.addOrder(conditon.getOrderField(), conditon.getOrderDirection());
            }
            List<Sort.Order> orders = new ArrayList<>();
            Map<String, String> map = conditon.getOrder();
            for (String key :
                    map.keySet()) {
                if ("asc".equals(map.get(key))) {
                    Sort.Order order = new Sort.Order(Sort.Direction.ASC, key);
                    orders.add(order);
                } else if ("desc".equals(map.get(key))) {
                    Sort.Order order = new Sort.Order(Sort.Direction.DESC, key);
                    orders.add(order);
                }
            }
            return Sort.by(orders);
        } else {
            Sort sort = new Sort(Sort.Direction.DESC, conditon.getOrderField() != null && !"".equals(conditon.getOrderField()) ? conditon.getOrderField() : settingOrderDefaultValue()); //创建时间降序排序
            if (conditon.getOrderDirection() != null && !"".equals(conditon.getOrderDirection())) {
                if (conditon.getOrderDirection().equals("asc")) {
                    sort = new Sort(Sort.Direction.ASC, conditon.getOrderField() != null && !"".equals(conditon.getOrderField()) ? conditon.getOrderField() : settingOrderDefaultValue());
                }
            }
            return sort;
        }
    }

    protected String getOrdersString(BaseCondition conditon) {
        if (conditon.getOrder().size() > 0) {
            if (conditon.getOrderField() != null && !"".equals(conditon.getOrderField())) {
                conditon.addOrder(conditon.getOrderField(), "desc");
            }
            if (conditon.getOrderDirection() != null && "asc".equals(conditon.getOrderDirection())) {
                conditon.addOrder(conditon.getOrderField(), conditon.getOrderDirection());
            }
            Map<String, String> map = conditon.getOrder();
            String orderBy = " ORDER BY ";
            for (String key :
                    map.keySet()) {
                if ("asc".equals(map.get(key))) {
                    orderBy += key + " asc,";
                } else if ("desc".equals(map.get(key))) {
                    orderBy += key + " desc,";
                }
            }
            return orderBy.substring(0, orderBy.length() - 1);
        } else {
            String orderBy = " ORDER BY " + (conditon.getOrderField() != null && !"".equals(conditon.getOrderField()) ? conditon.getOrderField() : settingOrderDefaultValue());

            if (conditon.getOrderDirection() != null && !"".equals(conditon.getOrderDirection())) {
                if (conditon.getOrderDirection().equals("asc")) {
                    orderBy += " asc ";
                } else {
                    orderBy += " desc ";
                }
            }
            return orderBy;
        }
    }

    public String settingOrderDefaultValue() {
        return "id";
    }

    public JSONObject getPageInfo(JSONObject returnJson, Page page) {
        returnJson.put("totalPages", page.getTotalPages());
        returnJson.put("numberOfElements", page.getTotalElements());
        returnJson.put("pageSize", page.getSize());
        returnJson.put("pageNum", page.getNumber() + 1);
        return returnJson;
    }
}

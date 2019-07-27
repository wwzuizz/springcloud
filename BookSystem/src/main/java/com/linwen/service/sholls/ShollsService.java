
package com.linwen.service.sholls;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseQueryService;
import com.linwen.comm.base.QueryExe;
import com.linwen.model.sholls.bean.Sholls;
import com.linwen.model.sholls.condition.ShollsCondition;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.From;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:51
 */
@Service
public class ShollsService extends BaseQueryService<Sholls, ShollsCondition, Long> {
    public final static String Sholls_TABLE = "sholls";

    @Transactional(rollbackFor = Exception.class)
    public int installSholls(Sholls sholls) {
        try {
            return install(sholls);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int installSholls(List<Sholls> shollsList) {
        try {
            return installs(shollsList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateSholls(Sholls sholls) {
        return updateSholls(sholls, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateSholls(Sholls sholls, boolean isNull) {
        try {
            return update(sholls, null, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateSholls(Sholls sholls, ShollsCondition condition, boolean isNull) {
        try {
            return update(sholls, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateSholls(List<Sholls> shollsList) {
        return updateSholls(shollsList, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateSholls(List<Sholls> shollsList, boolean isNull) {
        return updateSholls(shollsList, null, isNull);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateSholls(List<Sholls> shollsList, ShollsCondition condition, boolean isNull) {
        try {
            return updates(shollsList, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletes(String[] str) throws Exception {
        Sholls sholls = null;
        for (int i = 0; i < str.length; i++) {
            Long id = Long.valueOf(str[i]);
            sholls = getEMShollsOne(id);
            if (sholls == null) {
                throw new Exception(id + "找不到");
            }
            try {
                entityManager.createNativeQuery("DELETE FROM `sholls` WHERE (`id`='" + id + "');").executeUpdate();
            } catch (Exception e) {
                throw new Exception("数据库报错");
            }
        }
    }

    public Sholls getEMShollsOne(Long id) {
        return getEMOne(id);
    }

    public Sholls getEMSholls(ShollsCondition shollsCondition) {
        return getEMOne(shollsCondition);
    }

    public List<Sholls> getEMShollsList(ShollsCondition shollsCondition) {
        return getEmList(shollsCondition);
    }

    public Page<Sholls> getEMPageShollsList(ShollsCondition shollsCondition) {
        return getEMPageList(shollsCondition);
    }


    public JSONObject getJsonObjectShollsOne(Long id) {
        JSONObject retObject = new JSONObject();
        Sholls sholls = getEMShollsOne(id);
        if (sholls != null) {
            getShollsItem(retObject, sholls);
        }
        return retObject;
    }

    public JSONObject getJsonObjectSholls(ShollsCondition shollsCondition) {
        JSONObject retObject = new JSONObject();
        Sholls sholls = getEMSholls(shollsCondition);
        if (sholls != null) {
            getShollsItem(retObject, sholls);
        }
        return retObject;
    }

    public JSONArray getShollsJsonArray(ShollsCondition shollsCondition) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        List<Sholls> shollsList = getEMShollsList(shollsCondition);
        for (int i = 0; i < shollsList.size(); i++) {
            jsonObject = new JSONObject();
            getShollsItem(jsonObject, shollsList.get(i));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject getShollsPageInfoJsonArray(ShollsCondition shollsCondition) throws IllegalAccessException {
        Page<Sholls> shollsPage = getEMPageShollsList(shollsCondition);
        JSONObject jsonRetObject = new JSONObject();
        getPageInfo(jsonRetObject, shollsPage);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        if (shollsPage.getTotalElements() > 0) {
            List<Sholls> shollsList = shollsPage.getContent();
            for (int i = 0; i < shollsList.size(); i++) {
                jsonItem = new JSONObject();
                getShollsItem(jsonItem, shollsList.get(i));
                jsonArray.add(jsonItem);
            }
        }
        jsonRetObject.put("list", jsonArray);
        return jsonRetObject;
    }


    public Long countEmSholls(ShollsCondition conditon) {
        return countPageList(conditon);
    }

    @Override
    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        if (baseCondition instanceof ShollsCondition) {
            ShollsCondition shollsCondition = (ShollsCondition) baseCondition;
            if (shollsCondition != null) {
                return super.getJoinList(root, baseCondition);
            }
        }
        return null;
    }


    @Override
    public Class<Sholls> getMysqlBindClass() {
        return Sholls.class;
    }

    public static void getShollsItem(JSONObject jsonObject, Sholls sholls) {
        settingJsonObject(jsonObject, "id", sholls.getId());
        settingJsonObject(jsonObject, "name", sholls.getName());
        settingJsonObject(jsonObject, "info", sholls.getInfo());
        settingJsonObject(jsonObject, "status", sholls.getStatus());
        settingJsonObject(jsonObject, "price", sholls.getPrice());
        JSONArray jsonArray = null;
        JSONObject object = null;
    }

}

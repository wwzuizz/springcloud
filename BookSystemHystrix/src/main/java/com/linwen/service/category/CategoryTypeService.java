
package com.linwen.service.category;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseQueryService;
import com.linwen.comm.base.QueryExe;
import com.linwen.model.book.bean.Book;
import com.linwen.model.book.condition.BookCondition;
import com.linwen.model.category.bean.CategoryType;
import com.linwen.model.category.condition.CategoryTypeCondition;
import com.linwen.service.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:49
 */
@Service
public class CategoryTypeService extends BaseQueryService<CategoryType, CategoryTypeCondition, Long> {
    public final static String CategoryType_TABLE = "category_type";

    @Transactional(rollbackFor = Exception.class)
    public int installCategoryType(CategoryType categoryType) {
        try {
            return install(categoryType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int installCategoryType(List<CategoryType> categoryTypeList) {
        try {
            return installs(categoryTypeList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCategoryType(CategoryType categoryType) {
        return updateCategoryType(categoryType, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCategoryType(CategoryType categoryType, boolean isNull) {
        try {
            return update(categoryType, null, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCategoryType(CategoryType categoryType, CategoryTypeCondition condition, boolean isNull) {
        try {
            return update(categoryType, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCategoryType(List<CategoryType> categoryTypeList) {
        return updateCategoryType(categoryTypeList, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCategoryType(List<CategoryType> categoryTypeList, boolean isNull) {
        return updateCategoryType(categoryTypeList, null, isNull);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCategoryType(List<CategoryType> categoryTypeList, CategoryTypeCondition condition, boolean isNull) {
        try {
            return updates(categoryTypeList, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletes(String[] str) throws Exception {
        CategoryType categoryType = null;
        for (int i = 0; i < str.length; i++) {
            Long id = Long.valueOf(str[i]);
            categoryType = getEMCategoryTypeOne(id);
            if (categoryType == null) {
                throw new Exception(id + "找不到");
            }
            try {
                entityManager.createNativeQuery("DELETE FROM `bc_mapped` WHERE (`class_id`='" + id + "');").executeUpdate();
                entityManager.createNativeQuery("DELETE FROM `category_type` WHERE (`id`='" + id + "');").executeUpdate();
            } catch (Exception e) {
                throw new Exception("数据库报错");
            }
        }
    }

    public CategoryType getEMCategoryTypeOne(Long id) {
        return getEMOne(id);
    }

    public CategoryType getEMCategoryType(CategoryTypeCondition categoryTypeCondition) {
        return getEMOne(categoryTypeCondition);
    }

    public List<CategoryType> getEMCategoryTypeList(CategoryTypeCondition categoryTypeCondition) {
        return getEmList(categoryTypeCondition);
    }

    public Page<CategoryType> getEMPageCategoryTypeList(CategoryTypeCondition categoryTypeCondition) {
        return getEMPageList(categoryTypeCondition);
    }


    public JSONObject getJsonObjectCategoryTypeOne(Long id) {
        JSONObject retObject = new JSONObject();
        CategoryType categoryType = getEMCategoryTypeOne(id);
        if (categoryType != null) {
            getCategoryTypeItem(retObject, categoryType);
        }
        return retObject;
    }

    public JSONObject getJsonObjectCategoryType(CategoryTypeCondition categoryTypeCondition) {
        JSONObject retObject = new JSONObject();
        CategoryType categoryType = getEMCategoryType(categoryTypeCondition);
        if (categoryType != null) {
            getCategoryTypeItem(retObject, categoryType);
        }
        return retObject;
    }

    public JSONArray getCategoryTypeJsonArray(CategoryTypeCondition categoryTypeCondition) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        List<CategoryType> categoryTypeList = getEMCategoryTypeList(categoryTypeCondition);
        for (int i = 0; i < categoryTypeList.size(); i++) {
            jsonObject = new JSONObject();
            getCategoryTypeItem(jsonObject, categoryTypeList.get(i));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject getCategoryTypePageInfoJsonArray(CategoryTypeCondition categoryTypeCondition) throws IllegalAccessException {
        Page<CategoryType> categoryTypePage = getEMPageCategoryTypeList(categoryTypeCondition);
        JSONObject jsonRetObject = new JSONObject();
        getPageInfo(jsonRetObject, categoryTypePage);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        if (categoryTypePage.getTotalElements() > 0) {
            List<CategoryType> categoryTypeList = categoryTypePage.getContent();
            for (int i = 0; i < categoryTypeList.size(); i++) {
                jsonItem = new JSONObject();
                getCategoryTypeItem(jsonItem, categoryTypeList.get(i));
                jsonArray.add(jsonItem);
            }
        }
        jsonRetObject.put("list", jsonArray);
        return jsonRetObject;
    }


    public Long countEmCategoryType(CategoryTypeCondition conditon) {
        return countPageList(conditon);
    }

    @Override
    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        if (baseCondition instanceof CategoryTypeCondition) {
            CategoryTypeCondition categoryTypeCondition = (CategoryTypeCondition) baseCondition;
            if (categoryTypeCondition != null) {
                return super.getJoinList(root, baseCondition);
            }
        }
        if (baseCondition instanceof BookCondition) {
            BookCondition bookCondition = (BookCondition) baseCondition;
            if (bookCondition != null) {
                Join<CategoryType, Book> bookJoin = root.joinList("bookList");
                QueryExe queryExe = new QueryExe();
                queryExe.setTable(baseCondition.getMysqlTable());
                queryExe.setFrom(bookJoin);
                return queryExe;
            }
        }
        return null;
    }


    @Override
    public Class<CategoryType> getMysqlBindClass() {
        return CategoryType.class;
    }

    public static void getCategoryTypeItem(JSONObject jsonObject, CategoryType categoryType) {
        settingJsonObject(jsonObject, "id", categoryType.getId());
        settingJsonObject(jsonObject, "name", categoryType.getName());
        JSONArray jsonArray = null;
        JSONObject object = null;
        jsonArray = new JSONArray();
//        if (categoryType.getBookList() != null) {
//            for (int i = 0; i < categoryType.getBookList().size(); i++) {
//                object = new JSONObject();
//                BookService.getBookItem(object, categoryType.getBookList().get(i));
//                jsonArray.add(object);
//            }
//        }
        settingJsonObject(jsonObject, "books", jsonArray);
    }

}

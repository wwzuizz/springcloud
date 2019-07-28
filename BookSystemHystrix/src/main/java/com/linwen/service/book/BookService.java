
package com.linwen.service.book;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseQueryService;
import com.linwen.comm.base.QueryExe;
import com.linwen.model.book.bean.Book;
import com.linwen.model.book.condition.BookCondition;
import com.linwen.model.category.bean.CategoryType;
import com.linwen.model.category.condition.CategoryTypeCondition;
import com.linwen.service.category.CategoryTypeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:47
 */
@Service
public class BookService extends BaseQueryService<Book, BookCondition, Long> {
    public final static String Book_TABLE = "book";

    @Transactional(rollbackFor = Exception.class)
    public int installBook(Book book) {
        try {
            return install(book);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int installBook(List<Book> bookList) {
        try {
            return installs(bookList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBook(Book book) {
        return updateBook(book, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBook(Book book, boolean isNull) {
        try {
            return update(book, null, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBook(Book book, BookCondition condition, boolean isNull) {
        try {
            return update(book, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBook(List<Book> bookList) {
        return updateBook(bookList, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBook(List<Book> bookList, boolean isNull) {
        return updateBook(bookList, null, isNull);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBook(List<Book> bookList, BookCondition condition, boolean isNull) {
        try {
            return updates(bookList, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletes(String[] str) throws Exception {
        Book book = null;
        for (int i = 0; i < str.length; i++) {
            Long id = Long.valueOf(str[i]);
            book = getEMBookOne(id);
            if (book == null) {
                throw new Exception(id + "找不到");
            }
            try {
                entityManager.createNativeQuery("DELETE FROM `bc_mapped` WHERE (`book_id`='" + id + "');").executeUpdate();
                entityManager.createNativeQuery("DELETE FROM `book` WHERE (`id`='" + id + "');").executeUpdate();
            } catch (Exception e) {
                throw new Exception("数据库报错");
            }
        }
    }

    public Book getEMBookOne(Long id) {
        return getEMOne(id);
    }

    public Book getEMBook(BookCondition bookCondition) {
        return getEMOne(bookCondition);
    }

    public List<Book> getEMBookList(BookCondition bookCondition) {
        return getEmList(bookCondition);
    }

    public Page<Book> getEMPageBookList(BookCondition bookCondition) {
        return getEMPageList(bookCondition);
    }


    public JSONObject getJsonObjectBookOne(Long id) {
        JSONObject retObject = new JSONObject();
        Book book = getEMBookOne(id);
        if (book != null) {
            getBookItem(retObject, book);
        }
        return retObject;
    }

    public JSONObject getJsonObjectBook(BookCondition bookCondition) {
        JSONObject retObject = new JSONObject();
        Book book = getEMBook(bookCondition);
        if (book != null) {
            getBookItem(retObject, book);
        }
        return retObject;
    }

    public JSONArray getBookJsonArray(BookCondition bookCondition) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        List<Book> bookList = getEMBookList(bookCondition);
        for (int i = 0; i < bookList.size(); i++) {
            jsonObject = new JSONObject();
            getBookItem(jsonObject, bookList.get(i));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject getBookPageInfoJsonArray(BookCondition bookCondition) throws IllegalAccessException {
        Page<Book> bookPage = getEMPageBookList(bookCondition);
        JSONObject jsonRetObject = new JSONObject();
        getPageInfo(jsonRetObject, bookPage);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        if (bookPage.getTotalElements() > 0) {
            List<Book> bookList = bookPage.getContent();
            for (int i = 0; i < bookList.size(); i++) {
                jsonItem = new JSONObject();
                getBookItem(jsonItem, bookList.get(i));
                jsonArray.add(jsonItem);
            }
        }
        jsonRetObject.put("list", jsonArray);
        jsonRetObject.put("book","BookSystem1");
        return jsonRetObject;
    }


    public Long countEmBook(BookCondition conditon) {
        return countPageList(conditon);
    }

    @Override
    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        if (baseCondition instanceof BookCondition) {
            BookCondition bookCondition = (BookCondition) baseCondition;
            if (bookCondition != null) {
                return super.getJoinList(root, baseCondition);
            }
        }
        if (baseCondition instanceof CategoryTypeCondition) {
            CategoryTypeCondition categoryTypeCondition = (CategoryTypeCondition) baseCondition;
            if (categoryTypeCondition != null) {
                Join<Book, CategoryType> categoryTypeJoin = root.joinList("categoryTypeList");
                QueryExe queryExe = new QueryExe();
                queryExe.setTable(baseCondition.getMysqlTable());
                queryExe.setFrom(categoryTypeJoin);
                return queryExe;
            }
        }
        return null;
    }


    @Override
    public Class<Book> getMysqlBindClass() {
        return Book.class;
    }

    public static void getBookItem(JSONObject jsonObject, Book book) {
        settingJsonObject(jsonObject, "id", book.getId());
        settingJsonObject(jsonObject, "name", book.getName());
        JSONArray jsonArray = null;
        JSONObject object = null;
        jsonArray = new JSONArray();
        if (book.getCategoryTypeList() != null) {
            for (int i = 0; i < book.getCategoryTypeList().size(); i++) {
                object = new JSONObject();
                CategoryTypeService.getCategoryTypeItem(object, book.getCategoryTypeList().get(i));
                jsonArray.add(object);
            }
        }
        settingJsonObject(jsonObject, "categoryTypes", jsonArray);
    }

}



package com.linwen.controller.admin.service;

import com.linwen.comm.base.BaseService;
import com.linwen.model.book.bean.Book;
import com.linwen.model.book.condition.BookCondition;
import com.linwen.model.category.bean.CategoryType;
import com.linwen.model.category.condition.CategoryTypeCondition;
import com.linwen.service.book.BookService;
import com.linwen.service.category.CategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by linwen on 19-7-6.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class BookAndService extends BaseService {
    @Autowired
    CategoryTypeService categoryTypeService;
    @Autowired
    BookService bookService;

    public Book addBook(BookCondition bookCondition) {
        Book book = bookCondition.getBeseBean();
        if (book.getId() != null) {
            Book bookOld = bookService.getEMBookOne(book.getId());
            bookService.updateBook(book);
        } else {
            bookService.installBook(book);
        }
        return book;
    }

    public void addBook(BookCondition bookCondition, String categoryTypeIds) {
        Book book = addBook(bookCondition);
        addCategoryTypesMappedBook(book.getId(), categoryTypeIds);
    }

    public CategoryType addCategoryType(CategoryTypeCondition categoryTypeCondition) {

        CategoryType categoryType = categoryTypeCondition.getBeseBean();
        if (categoryType.getId() != null) {
            CategoryType categoryTypeOld = categoryTypeService.getEMCategoryTypeOne(categoryType.getId());
            categoryTypeService.updateCategoryType(categoryType);
        } else {
            categoryTypeService.installCategoryType(categoryType);
        }
        return categoryType;
    }

    public void addCategoryTypeList(Long bookId, List<CategoryType> categoryTypeList) {
        categoryTypeService.installCategoryType(categoryTypeList);
        String categoryTypeIds = "";
        String temp = "";
        for (int i = 0; i < categoryTypeList.size(); i++) {
            categoryTypeIds = temp + categoryTypeList.get(i).getId();
            if ("".equals(temp)) {
                temp = ",";
            }
        }
        addCategoryTypesMappedBook(bookId, categoryTypeIds);

    }

    public void addCategoryTypesMappedBook(Long bookId, String categoryTypeIds) {
        if (categoryTypeIds != null && !"".equals(categoryTypeIds)) {
            String[] categoryTypeArray = categoryTypeIds.split(",");
            String mappedSql = "";
            for (int i = 0; i < categoryTypeArray.length; i++) {
                mappedSql += "INSERT INTO `bc_mapped` (`book_id`, `class_id`) VALUES ('" + bookId + "', '" + categoryTypeArray[i] + "');\n";
            }
            entityManager.createNativeQuery("DELETE FROM `bc_mapped` WHERE (`book_id`='" + bookId + "');").executeUpdate();
            if (!"".equals(mappedSql)) {
                entityManager.createNativeQuery(mappedSql).executeUpdate();
            }
        }
    }

    public BookService getBookService() {
        return this.bookService;
    }

    public CategoryTypeService getCategoryTypeService() {
        return this.categoryTypeService;
    }


}

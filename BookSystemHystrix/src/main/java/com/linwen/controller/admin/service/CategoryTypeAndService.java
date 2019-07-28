

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
public class CategoryTypeAndService extends BaseService {
    @Autowired
    BookService bookService;
    @Autowired
    CategoryTypeService categoryTypeService;

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

    public void addCategoryType(CategoryTypeCondition categoryTypeCondition, String bookIds) {
        CategoryType categoryType = addCategoryType(categoryTypeCondition);
        addBooksMappedCategoryType(categoryType.getId(), bookIds);
    }

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

    public void addBookList(Long categoryTypeId, List<Book> bookList) {
        bookService.installBook(bookList);
        String bookIds = "";
        String temp = "";
        for (int i = 0; i < bookList.size(); i++) {
            bookIds = temp + bookList.get(i).getId();
            if ("".equals(temp)) {
                temp = ",";
            }
        }
        addBooksMappedCategoryType(categoryTypeId, bookIds);

    }

    public void addBooksMappedCategoryType(Long categoryTypeId, String bookIds) {
        if (bookIds != null && !"".equals(bookIds)) {
            String[] bookArray = bookIds.split(",");
            String mappedSql = "";
            for (int i = 0; i < bookArray.length; i++) {
                mappedSql += "INSERT INTO `bc_mapped` (`categoryType_id`, `book_id`) VALUES ('" + categoryTypeId + "', '" + bookArray[i] + "');\n";
            }
            entityManager.createNativeQuery("DELETE FROM `bc_mapped` WHERE (`categoryType_id`='" + categoryTypeId + "');").executeUpdate();
            if (!"".equals(mappedSql)) {
                entityManager.createNativeQuery(mappedSql).executeUpdate();
            }
        }
    }

    public CategoryTypeService getCategoryTypeService() {
        return this.categoryTypeService;
    }

    public BookService getBookService() {
        return this.bookService;
    }


}

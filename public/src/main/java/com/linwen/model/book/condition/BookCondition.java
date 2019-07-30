

package com.linwen.model.book.condition;

import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.validation.annotation.ParameterValueExistDB;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.model.book.bean.Book;
import com.linwen.validator.ValidatorService;

/**
 * 用于list页面的查询条件
 *
 * @author lin
 * @date 2019-7-27 19:03:47
 */
public class BookCondition extends BaseCondition {


    @ValidParameter(
            name = "书主键id",
            fieldName = "id",
            description = "",

            parameterType = ParameterType.Long,
            ValueExistDB = @ParameterValueExistDB(ValidClass = {ValidatorService.class}, table = "book", column = "id", message = "书主键id不存在"),
            validRanges = {"Edit", "List", "Single"},
            showRanges = {"Edit", "List", "Single"}
    )
    private Long id;  //

    @ValidParameter(
            name = "书的名称",
            fieldName = "name",
            description = "",

            parameterType = ParameterType.String,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private String name;  //

    public BookCondition setId(Long value) {
        this.id = value;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public BookCondition setName(String value) {
        this.name = value;
        return this;
    }

    public String getName() {
        return this.name;
    }


    @Override
    public String toString() {
        return "BookCondition{" +
                "params=" + params +
                ", order=" + order +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", pageCount=" + pageCount +
                ", orderField='" + orderField + '\'' +
                ", orderDirection='" + orderDirection + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public Book getBeseBean() {
        Book book = new Book();
        book.setId(this.getId());
        book.setName(this.getName());
        return book;
    }

}
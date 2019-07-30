

package com.linwen.model.category.condition;

import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.validation.annotation.ParameterValueExistDB;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.model.category.bean.CategoryType;
import com.linwen.validator.ValidatorService;

/**
 * 用于list页面的查询条件
 *
 * @author lin
 * @date 2019-7-27 19:03:49
 */
public class CategoryTypeCondition extends BaseCondition {


    @ValidParameter(
            name = "id",
            fieldName = "id",
            description = "",

            parameterType = ParameterType.Long,
            ValueExistDB = @ParameterValueExistDB(ValidClass = {ValidatorService.class}, table = "categoryType", column = "id", message = "id不存在"),
            validRanges = {"Edit", "List", "Single"},
            showRanges = {"Edit", "List", "Single"}
    )
    private Long id;  //

    @ValidParameter(
            name = "name",
            fieldName = "name",
            description = "",

            parameterType = ParameterType.String,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private String name;  //

    public CategoryTypeCondition setId(Long value) {
        this.id = value;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public CategoryTypeCondition setName(String value) {
        this.name = value;
        return this;
    }

    public String getName() {
        return this.name;
    }


    @Override
    public String toString() {
        return "CategoryTypeCondition{" +
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
    public CategoryType getBeseBean() {
        CategoryType categoryType = new CategoryType();
        categoryType.setId(this.getId());
        categoryType.setName(this.getName());
        return categoryType;
    }

}
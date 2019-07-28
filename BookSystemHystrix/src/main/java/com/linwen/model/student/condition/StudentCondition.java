

package com.linwen.model.student.condition;

import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.validation.annotation.ParameterValueExistDB;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.model.student.bean.Student;
import com.linwen.validator.ValidatorService;

/**
 * 用于list页面的查询条件
 *
 * @author lin
 * @date 2019-7-27 19:03:52
 */
public class StudentCondition extends BaseCondition {


    @ValidParameter(
            name = "id",
            fieldName = "id",
            description = "",

            parameterType = ParameterType.Long,
            ValueExistDB = @ParameterValueExistDB(ValidClass = {ValidatorService.class}, table = "student", column = "id", message = "id不存在"),
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

    @ValidParameter(
            name = "infoId",
            fieldName = "infoId",
            description = "",

            parameterType = ParameterType.Long,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private Long infoId;  //

    public StudentCondition setId(Long value) {
        this.id = value;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public StudentCondition setName(String value) {
        this.name = value;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public StudentCondition setInfoId(Long value) {
        this.infoId = value;
        return this;
    }

    public Long getInfoId() {
        return this.infoId;
    }


    @Override
    public String toString() {
        return "StudentCondition{" +
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
                ", infoId=" + infoId +
                '}';
    }

    @Override
    public Student getBeseBean() {
        Student student = new Student();
        student.setId(this.getId());
        student.setName(this.getName());
        student.setInfoId(this.getInfoId());
        return student;
    }

}
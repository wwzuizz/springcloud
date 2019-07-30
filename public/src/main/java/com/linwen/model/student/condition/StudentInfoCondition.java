

package com.linwen.model.student.condition;

import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.validation.annotation.ParameterValueExistDB;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.model.student.bean.StudentInfo;
import com.linwen.validator.ValidatorService;

/**
 * 用于list页面的查询条件
 *
 * @author lin
 * @date 2019-7-27 19:03:53
 */
public class StudentInfoCondition extends BaseCondition {


    @ValidParameter(
            name = "id",
            fieldName = "id",
            description = "",

            parameterType = ParameterType.Long,
            ValueExistDB = @ParameterValueExistDB(ValidClass = {ValidatorService.class}, table = "studentInfo", column = "id", message = "id不存在"),
            validRanges = {"Edit", "List", "Single"},
            showRanges = {"Edit", "List", "Single"}
    )
    private Long id;  //

    @ValidParameter(
            name = "info",
            fieldName = "info",
            description = "",

            parameterType = ParameterType.String,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private String info;  //

    @ValidParameter(
            name = "studentId",
            fieldName = "studentId",
            description = "",

            parameterType = ParameterType.Long,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private Long studentId;  //

    public StudentInfoCondition setId(Long value) {
        this.id = value;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public StudentInfoCondition setInfo(String value) {
        this.info = value;
        return this;
    }

    public String getInfo() {
        return this.info;
    }

    public StudentInfoCondition setStudentId(Long value) {
        this.studentId = value;
        return this;
    }

    public Long getStudentId() {
        return this.studentId;
    }


    @Override
    public String toString() {
        return "StudentInfoCondition{" +
                "params=" + params +
                ", order=" + order +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", pageCount=" + pageCount +
                ", orderField='" + orderField + '\'' +
                ", orderDirection='" + orderDirection + '\'' +
                ", id=" + id +
                ", info='" + info + '\'' +
                ", studentId=" + studentId +
                '}';
    }

    @Override
    public StudentInfo getBeseBean() {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setId(this.getId());
        studentInfo.setInfo(this.getInfo());
        studentInfo.setStudentId(this.getStudentId());
        return studentInfo;
    }

}
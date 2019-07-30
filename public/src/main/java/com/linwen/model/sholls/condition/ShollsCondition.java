

package com.linwen.model.sholls.condition;

import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.validation.annotation.NotNull;
import com.linwen.comm.validation.annotation.ParameterValueExistDB;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.model.sholls.bean.Sholls;
import com.linwen.validator.ValidatorService;

import java.math.BigDecimal;

/**
 * 用于list页面的查询条件
 *
 * @author lin
 * @date 2019-7-27 19:03:51
 */
public class ShollsCondition extends BaseCondition {


    @ValidParameter(
            name = "id",
            fieldName = "id",
            description = "",

            parameterType = ParameterType.Long,
            ValueExistDB = @ParameterValueExistDB(ValidClass = {ValidatorService.class}, table = "sholls", column = "id", message = "id不存在"),
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
            name = "价格",
            fieldName = "info",
            description = "我是注释说明",
            notNull = @NotNull(),
            parameterType = ParameterType.String,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private String info;  //

    @ValidParameter(
            name = "状态",
            fieldName = "status",
            description = "1=添加,2=失败我是注释说明",

            parameterType = ParameterType.Integer,

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private Integer status;  //

    @ValidParameter(
            name = "价格",
            fieldName = "price",
            description = "我是注释说明",
            notNull = @NotNull(),

            validRanges = {"Edit", "List", "Single", "Add"},
            showRanges = {"Edit", "List", "Single", "Add"}
    )
    private BigDecimal price;  //

    public ShollsCondition setId(Long value) {
        this.id = value;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public ShollsCondition setName(String value) {
        this.name = value;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ShollsCondition setInfo(String value) {
        this.info = value;
        return this;
    }

    public String getInfo() {
        return this.info;
    }

    public ShollsCondition setStatus(Integer value) {
        this.status = value;
        return this;
    }

    public Integer getStatus() {
        return this.status;
    }

    public ShollsCondition setPrice(BigDecimal value) {
        this.price = value;
        return this;
    }

    public BigDecimal getPrice() {
        return this.price;
    }


    @Override
    public String toString() {
        return "ShollsCondition{" +
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
                ", info='" + info + '\'' +
                ", status=" + status +
                ", price=" + price +
                '}';
    }

    @Override
    public Sholls getBeseBean() {
        Sholls sholls = new Sholls();
        sholls.setId(this.getId());
        sholls.setName(this.getName());
        sholls.setInfo(this.getInfo());
        sholls.setStatus(this.getStatus());
        sholls.setPrice(this.getPrice());
        return sholls;
    }

}



package com.linwen.controller.admin.category;

import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.converter.multirequestbody.MultiRequestBody;
import com.linwen.comm.validation.annotation.OAuthRequired;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.annotation.ValidationRequest;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.controller.admin.service.CategoryTypeAndService;
import com.linwen.model.book.condition.BookCondition;
import com.linwen.model.category.condition.CategoryTypeCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author lin
 * @date 2019-7-27 19:03:49
 */
@Controller
@RequestMapping("/admin/categoryType")
public class AdminCategoryTypeController extends BaseController {
    @Autowired
    CategoryTypeAndService categoryTypeAndService;

    @RequestMapping("/categoryTypeList")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "List", category = "", method = "POST", description = "", name = "列表")
    public JSONObject categoryTypeList(Model model,
                                       @MultiRequestBody CategoryTypeCondition vo,
                                       @MultiRequestBody BookCondition bookVo,
                                       HttpSession session) {
        try {
            vo.getConditionList().add(bookVo);
            return JsonSuccessResult(session.getId(), categoryTypeAndService.getCategoryTypeService().getCategoryTypePageInfoJsonArray(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/getCategoryTypeSingle")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Single", category = "", method = "POST", description = "", name = "详情")
    public JSONObject categoryTypeSingle(Model model, @MultiRequestBody CategoryTypeCondition vo, HttpSession session) {
        try {
            return JsonSuccessResult(session.getId(), categoryTypeAndService.getCategoryTypeService().getJsonObjectCategoryType(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/categoryTypeAdd")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Add", category = "", method = "POST", description = "", name = "添加")
    public JSONObject categoryTypeAdd(Model model, @MultiRequestBody CategoryTypeCondition vo,
                                      @MultiRequestBody @ValidParameter(
                                              name = "Ids",
                                              description = "",
                                              parameterType = ParameterType.String,
                                              validRanges = {"Add"},
                                              showRanges = {"Add"}
                                      ) String bookIds,
                                      HttpSession session) {
        try {
            categoryTypeAndService.addCategoryType(vo
                    , bookIds
            );
            return JsonSuccessResult(session.getId(), "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/categoryTypeEdit")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Edit", category = "", method = "POST", description = "", name = "编辑")
    public JSONObject categoryTypeEdit(Model model, @MultiRequestBody CategoryTypeCondition vo,
                                       @MultiRequestBody @ValidParameter(
                                               name = "Ids",
                                               description = "",
                                               parameterType = ParameterType.String,
                                               validRanges = {"Edit"},
                                               showRanges = {"Edit"}
                                       ) String bookIds,
                                       HttpSession session) {
        return categoryTypeAdd(model, vo,
                bookIds,
                session);
    }

    @RequestMapping("/categoryTypeDelete")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Delete", category = "", method = "POST", description = "", name = "删除")
    public JSONObject categoryTypedelete(Model model, @MultiRequestBody @ValidParameter(
            name = "ids",
            description = "",
            parameterType = ParameterType.String,
            validRanges = {"Delete"},
            showRanges = {"Delete"}
    ) String ids, HttpSession session) {
        try {
            String[] id = ids.split(",");
            categoryTypeAndService.getCategoryTypeService().deletes(id);
            return JsonSuccessResult(session.getId(), "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }


}

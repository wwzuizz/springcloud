


package com.linwen.controller.admin.student;

import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.converter.multirequestbody.MultiRequestBody;
import com.linwen.comm.validation.annotation.OAuthRequired;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.annotation.ValidationRequest;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.controller.admin.service.StudentInfoAndService;
import com.linwen.model.student.condition.StudentCondition;
import com.linwen.model.student.condition.StudentInfoCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author lin
 * @date 2019-7-27 19:03:53
 */
@Controller
@RequestMapping("/admin/studentInfo")
public class AdminStudentInfoController extends BaseController {
    @Autowired
    StudentInfoAndService studentInfoAndService;

    @RequestMapping("/studentInfoList")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "List", category = "", method = "POST", description = "", name = "列表")
    public JSONObject studentInfoList(Model model,
                                      @MultiRequestBody StudentInfoCondition vo,
                                      @MultiRequestBody StudentCondition studentVo,
                                      HttpSession session) {
        try {
            vo.getConditionList().add(studentVo);
            return JsonSuccessResult(session.getId(), studentInfoAndService.getStudentInfoService().getStudentInfoPageInfoJsonArray(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/getStudentInfoSingle")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Single", category = "", method = "POST", description = "", name = "详情")
    public JSONObject studentInfoSingle(Model model, @MultiRequestBody StudentInfoCondition vo, HttpSession session) {
        try {
            return JsonSuccessResult(session.getId(), studentInfoAndService.getStudentInfoService().getJsonObjectStudentInfo(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/studentInfoAdd")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Add", category = "", method = "POST", description = "", name = "添加")
    public JSONObject studentInfoAdd(Model model, @MultiRequestBody StudentInfoCondition vo,
                                     @MultiRequestBody @ValidParameter(
                                             name = "Ids",
                                             description = "",
                                             parameterType = ParameterType.String,
                                             validRanges = {"Add"},
                                             showRanges = {"Add"}
                                     ) String studentIds,
                                     HttpSession session) {
        try {
            studentInfoAndService.addStudentInfo(vo
                    , studentIds
            );
            return JsonSuccessResult(session.getId(), "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/studentInfoEdit")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Edit", category = "", method = "POST", description = "", name = "编辑")
    public JSONObject studentInfoEdit(Model model, @MultiRequestBody StudentInfoCondition vo,
                                      @MultiRequestBody @ValidParameter(
                                              name = "Ids",
                                              description = "",
                                              parameterType = ParameterType.String,
                                              validRanges = {"Edit"},
                                              showRanges = {"Edit"}
                                      ) String studentIds,
                                      HttpSession session) {
        return studentInfoAdd(model, vo,
                studentIds,
                session);
    }

    @RequestMapping("/studentInfoDelete")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Delete", category = "", method = "POST", description = "", name = "删除")
    public JSONObject studentInfodelete(Model model, @MultiRequestBody @ValidParameter(
            name = "ids",
            description = "",
            parameterType = ParameterType.String,
            validRanges = {"Delete"},
            showRanges = {"Delete"}
    ) String ids, HttpSession session) {
        try {
            String[] id = ids.split(",");
            studentInfoAndService.getStudentInfoService().deletes(id);
            return JsonSuccessResult(session.getId(), "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }


}

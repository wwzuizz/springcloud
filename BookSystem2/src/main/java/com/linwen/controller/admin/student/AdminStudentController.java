


package com.linwen.controller.admin.student;

import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.converter.multirequestbody.MultiRequestBody;
import com.linwen.comm.validation.annotation.OAuthRequired;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.annotation.ValidationRequest;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.controller.admin.service.StudentAndService;
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
 * @date 2019-7-27 19:03:52
 */
@Controller
@RequestMapping("/admin/student")
public class AdminStudentController extends BaseController {
    @Autowired
    StudentAndService studentAndService;

    @RequestMapping("/studentList")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "List", category = "", method = "POST", description = "", name = "列表")
    public JSONObject studentList(Model model,
                                  @MultiRequestBody StudentCondition vo,
                                  @MultiRequestBody StudentInfoCondition studentInfoVo,
                                  HttpSession session) {
        try {
            vo.getConditionList().add(studentInfoVo);
            return JsonSuccessResult(session.getId(), studentAndService.getStudentService().getStudentPageInfoJsonArray(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/getStudentSingle")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Single", category = "", method = "POST", description = "", name = "详情")
    public JSONObject studentSingle(Model model, @MultiRequestBody StudentCondition vo, HttpSession session) {
        try {
            return JsonSuccessResult(session.getId(), studentAndService.getStudentService().getJsonObjectStudent(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/studentAdd")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Add", category = "", method = "POST", description = "", name = "添加")
    public JSONObject studentAdd(Model model, @MultiRequestBody StudentCondition vo,
                                 HttpSession session) {
        try {
            studentAndService.addStudent(vo
            );
            return JsonSuccessResult(session.getId(), "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/studentEdit")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Edit", category = "", method = "POST", description = "", name = "编辑")
    public JSONObject studentEdit(Model model, @MultiRequestBody StudentCondition vo,
                                  HttpSession session) {
        return studentAdd(model, vo,
                session);
    }

    @RequestMapping("/studentDelete")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Delete", category = "", method = "POST", description = "", name = "删除")
    public JSONObject studentdelete(Model model, @MultiRequestBody @ValidParameter(
            name = "ids",
            description = "",
            parameterType = ParameterType.String,
            validRanges = {"Delete"},
            showRanges = {"Delete"}
    ) String ids, HttpSession session) {
        try {
            String[] id = ids.split(",");
            studentAndService.getStudentService().deletes(id);
            return JsonSuccessResult(session.getId(), "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }


}

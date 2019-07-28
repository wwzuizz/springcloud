


package com.linwen.controller.admin.sholls;

import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.converter.multirequestbody.MultiRequestBody;
import com.linwen.comm.validation.annotation.OAuthRequired;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.annotation.ValidationRequest;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.model.sholls.bean.Sholls;
import com.linwen.model.sholls.condition.ShollsCondition;
import com.linwen.service.sholls.ShollsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

/**
 * @author lin
 * @date 2019-7-27 19:03:50
 */
@Controller
@RequestMapping("/admin/sholls")
public class AdminShollsController extends BaseController {
    @Autowired
    ShollsService shollsService;

    @RequestMapping("/shollsList")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "List", category = "", method = "POST", description = "", name = "列表")
    public JSONObject shollsList(Model model,
                                 @MultiRequestBody ShollsCondition vo,
                                 HttpSession session) {
        try {
            return JsonSuccessResult(session.getId(), shollsService.getShollsPageInfoJsonArray(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/getShollsSingle")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Single", category = "", method = "POST", description = "", name = "详情")
    public JSONObject shollsSingle(Model model, @MultiRequestBody ShollsCondition vo, HttpSession session) {
        try {
            return JsonSuccessResult(session.getId(), shollsService.getJsonObjectSholls(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/shollsAdd")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Add", category = "", method = "POST", description = "", name = "添加")
    public JSONObject shollsAdd(Model model, @MultiRequestBody ShollsCondition vo,
                                HttpSession session) {
        try {
            Sholls sholls = vo.getBeseBean();
            if (vo.getId() != null) {
                Sholls shollsold = shollsService.getEMShollsOne(vo.getId());
                if (shollsold.getName() != null && sholls.getName() != null) {
                    if (shollsold.getName().equals(sholls.getName())) {
                        sholls.setName(null);
                    }
                } else if (sholls.getName() == null) {
                    sholls.setName("");
                }
                if (shollsold.getInfo() != null && sholls.getInfo() != null) {
                    if (shollsold.getInfo().equals(sholls.getInfo())) {
                        sholls.setInfo(null);
                    }
                } else if (sholls.getInfo() == null) {
                    sholls.setInfo("linwen");
                }
                if (shollsold.getStatus() != null && sholls.getStatus() != null) {
                    if (shollsold.getStatus().equals(sholls.getStatus())) {
                        sholls.setStatus(null);
                    }
                } else if (sholls.getStatus() == null) {
                    sholls.setStatus(1);
                }
                if (shollsold.getPrice() != null && sholls.getPrice() != null) {
                    if (shollsold.getPrice().equals(sholls.getPrice())) {
                        sholls.setPrice(null);
                    }
                } else if (sholls.getPrice() == null) {
                    sholls.setPrice(new BigDecimal(0));
                }
                shollsService.updateSholls(sholls);
            } else {
                if (sholls.getName() == null) {
                    sholls.setName("");
                }
                if (sholls.getInfo() == null) {
                    sholls.setInfo("linwen");
                }
                if (sholls.getStatus() == null) {
                    sholls.setStatus(1);
                }
                if (sholls.getPrice() == null) {
                    sholls.setPrice(new BigDecimal(0));
                }
                shollsService.installSholls(sholls);
            }
            return JsonSuccessResult(session.getId(), "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/shollsEdit")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Edit", category = "", method = "POST", description = "", name = "编辑")
    public JSONObject shollsEdit(Model model, @MultiRequestBody ShollsCondition vo,
                                 HttpSession session) {
        return shollsAdd(model, vo,
                session);
    }

    @RequestMapping("/shollsDelete")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Delete", category = "", method = "POST", description = "", name = "删除")
    public JSONObject shollsdelete(Model model, @MultiRequestBody @ValidParameter(
            name = "ids",
            description = "",
            parameterType = ParameterType.String,
            validRanges = {"Delete"},
            showRanges = {"Delete"}
    ) String ids, HttpSession session) {
        try {
            String[] id = ids.split(",");
            shollsService.deletes(id);
            return JsonSuccessResult(session.getId(), "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }


}

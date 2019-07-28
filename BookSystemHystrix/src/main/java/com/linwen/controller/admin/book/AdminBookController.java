


package com.linwen.controller.admin.book;

import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseController;
import com.linwen.comm.converter.multirequestbody.MultiRequestBody;
import com.linwen.comm.validation.annotation.OAuthRequired;
import com.linwen.comm.validation.annotation.ValidParameter;
import com.linwen.comm.validation.annotation.ValidationRequest;
import com.linwen.comm.validation.enums.ParameterType;
import com.linwen.controller.admin.service.BookAndService;
import com.linwen.model.book.condition.BookCondition;
import com.linwen.model.category.condition.CategoryTypeCondition;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:46
 */
@Controller
@RequestMapping("/admin/book")
public class AdminBookController extends BaseController {
    @Autowired
    BookAndService bookAndService;
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 服务发现，有个屁用？
     * @return
     */
    @RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
    public Object discovery()
    {
        List<String> list = discoveryClient.getServices();
        System.out.println("**********" + list);

        List<ServiceInstance> srvList = discoveryClient.getInstances("BOOKSYSTEM");
        for (ServiceInstance element : srvList) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.discoveryClient;
    }
    public JSONObject processHystrix_Get(Model model,
                                         @MultiRequestBody BookCondition vo,
                                         @MultiRequestBody CategoryTypeCondition categoryTypeVo,
                                         HttpSession session)
    {
        JSONObject ret= new JSONObject();
        ret.put("126","123123");
        return ret;
    }
    @RequestMapping("/bookList")
    @ResponseBody
    @OAuthRequired
    //一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
//    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    @ValidationRequest(validRange = "List", category = "", method = "POST", description = "", name = "列表")
    public JSONObject bookList(Model model,
                               @MultiRequestBody BookCondition vo,
                               @MultiRequestBody CategoryTypeCondition categoryTypeVo,
                               HttpSession session) throws Exception {
        throw new Exception("asdasdasd");
    }

    @RequestMapping("/getBookSingle")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Single", category = "", method = "POST", description = "", name = "详情")
    public JSONObject bookSingle(Model model, @MultiRequestBody BookCondition vo, HttpSession session) {
        try {
            return JsonSuccessResult(session.getId(), bookAndService.getBookService().getJsonObjectBook(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/bookAdd")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Add", category = "", method = "POST", description = "", name = "添加")
    public JSONObject bookAdd(Model model, @MultiRequestBody BookCondition vo,
                              @MultiRequestBody @ValidParameter(
                                      name = "Ids",
                                      description = "",
                                      parameterType = ParameterType.String,
                                      validRanges = {"Add"},
                                      showRanges = {"Add"}
                              ) String categoryTypeIds,
                              HttpSession session) {
        try {
            bookAndService.addBook(vo
                    , categoryTypeIds
            );
            return JsonSuccessResult(session.getId(), "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }

    @RequestMapping("/bookEdit")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Edit", category = "", method = "POST", description = "", name = "编辑")
    public JSONObject bookEdit(Model model, @MultiRequestBody BookCondition vo,
                               @MultiRequestBody @ValidParameter(
                                       name = "Ids",
                                       description = "",
                                       parameterType = ParameterType.String,
                                       validRanges = {"Edit"},
                                       showRanges = {"Edit"}
                               ) String categoryTypeIds,
                               HttpSession session) {
        return bookAdd(model, vo,
                categoryTypeIds,
                session);
    }

    @RequestMapping("/bookDelete")
    @ResponseBody
    @OAuthRequired
    @ValidationRequest(validRange = "Delete", category = "", method = "POST", description = "", name = "删除")
    public JSONObject bookdelete(Model model, @MultiRequestBody @ValidParameter(
            name = "ids",
            description = "",
            parameterType = ParameterType.String,
            validRanges = {"Delete"},
            showRanges = {"Delete"}
    ) String ids, HttpSession session) {
        try {
            String[] id = ids.split(",");
            bookAndService.getBookService().deletes(id);
            return JsonSuccessResult(session.getId(), "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonFailResult(session.getId(), 1,
                    e.getMessage());
        }
    }


}

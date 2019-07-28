package com.linwen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
@Controller
public class HTTPController {
    @Autowired
    private HttpService httpService;
    @RequestMapping(value = "/consumer/dept/get")
    @ResponseBody
    public String get()
    {
        return httpService.get();
    }
}

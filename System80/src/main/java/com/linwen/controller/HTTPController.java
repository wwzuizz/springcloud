package com.linwen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
@Controller
public class HTTPController {
    private static final String REST_URL_PREFIX = "http://localhost:8383";
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping(value = "/consumer/dept/get")
    @ResponseBody
    public String get()
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/admin/book/bookList" , String.class);
    }
}

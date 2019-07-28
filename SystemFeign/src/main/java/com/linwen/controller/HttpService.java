package com.linwen.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "BOOKSYSTEM")
public interface HttpService {
    @RequestMapping(value = "/admin/book/bookList", method = RequestMethod.GET)
    @ResponseBody
    public String get();

}

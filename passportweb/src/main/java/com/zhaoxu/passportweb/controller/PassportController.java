package com.zhaoxu.passportweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PassportController {

    @RequestMapping(method = RequestMethod.GET,value = "/index")
    public String index() {
        return "index";
    }

}

package com.zhaoxu.passportweb.controller;

import com.zhaoxu.bean.UmsMember;
import com.zhaoxu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PassportController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "verify")
    @ResponseBody
    public String verify(String token) {
        return "success";
    }

    @RequestMapping(value = "login")
    @ResponseBody
    public String login(UmsMember umsMember) {

        UmsMember member = userService.login(umsMember);

        if (member != null) {

        } else {

        }

        return "token";
    }


    @RequestMapping(value = "index")
    public String index(String ReturnUrl, ModelMap modelMap) {
        modelMap.put("ReturnUrl", ReturnUrl);

        return "index";
    }

}

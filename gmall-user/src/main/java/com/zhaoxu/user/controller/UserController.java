package com.zhaoxu.user.controller;

import com.zhaoxu.user.bean.UmsMember;
import com.zhaoxu.user.bean.UmsMemberReceiveAddress;
import com.zhaoxu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/addresses")
    public UmsMemberReceiveAddress getReceiveAddressByMemberId(@RequestParam("member_id") String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddress;
    }


    @GetMapping("/index")
    public String index() {
        return "Hello user";
    }

}

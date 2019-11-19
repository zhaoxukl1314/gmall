package com.zhaoxu.user.service.impl;

import com.zhaoxu.user.bean.UmsMember;
import com.zhaoxu.user.bean.UmsMemberReceiveAddress;
import com.zhaoxu.user.mapper.UmsMemberReceiveAddressMapper;
import com.zhaoxu.user.mapper.UserMapper;
import com.zhaoxu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {
//        List<UmsMember> umsMemberList = userMapper.selectAllUser();
        List<UmsMember> umsMemberList = userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressByMemberId(String memberId) {
        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().andEqualTo("memberId",memberId);
        umsMemberReceiveAddressMapper.selectByExample(e);
    }

}

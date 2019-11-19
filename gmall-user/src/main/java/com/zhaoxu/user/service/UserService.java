package com.zhaoxu.user.service;

import com.zhaoxu.user.bean.UmsMember;
import com.zhaoxu.user.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    UmsMemberReceiveAddress getReceiveAddressByMemberId(String memberId);
}

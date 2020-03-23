package com.zhaoxu.service;


import com.zhaoxu.bean.UmsMember;
import com.zhaoxu.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, String memberId);

    UmsMember addOauthUser(UmsMember umsMember);

    UmsMember checkOauthUser(UmsMember umsCheck);

    UmsMember getOauthUser(UmsMember umsMemberCheck);

    UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId);
}

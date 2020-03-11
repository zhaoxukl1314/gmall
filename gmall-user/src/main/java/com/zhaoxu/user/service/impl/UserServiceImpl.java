package com.zhaoxu.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhaoxu.bean.UmsMember;
import com.zhaoxu.bean.UmsMemberReceiveAddress;
import com.zhaoxu.service.UserService;
import com.zhaoxu.user.mapper.UmsMemberReceiveAddressMapper;
import com.zhaoxu.user.mapper.UserMapper;
import com.zhaoxu.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {
//        List<UmsMember> umsMemberList = userMapper.selectAllUser();
        List<UmsMember> umsMemberList = userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().andEqualTo("memberId",memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(e);
        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        UmsMember result = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                String member = jedis.get("user:" + umsMember.getPassword() + ":info");
                if (StringUtils.isNotBlank(member)) {
                    result = JSON.parseObject(member, UmsMember.class);
                    return result;
                }
            }
            result = loginFromDb(umsMember);
            if (result != null) {
                jedis.setex("user:" + umsMember.getPassword() + ":info", 60 * 60 * 24, JSON.toJSONString(result));
            }
            return result;
        } finally {
            jedis.close();
        }
    }

    private UmsMember loginFromDb(UmsMember umsMember) {
        List<UmsMember> umsMembers = userMapper.select(umsMember);
        if (umsMembers != null) {
            return umsMembers.get(0);
        }
        return null;
    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();

        jedis.setex("user:"+memberId+":token",60*60*2,token);

        jedis.close();
    }
}

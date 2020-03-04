package com.zhaoxu.cartservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.zhaoxu.bean.OmsCartItem;
import com.zhaoxu.cartservice.mapper.OmsCartItemMapper;
import com.zhaoxu.service.CartService;
import com.zhaoxu.util.RedisUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if (!TextUtils.isEmpty(omsCartItem.getMemberId())) {
            omsCartItemMapper.insert(omsCartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id", omsCartItemFromDb.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb, example);
    }

    @Override
    public void flushCartCache(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);

        Jedis jedis = redisUtil.getJedis();

        Map<String, String> map = new HashMap<>();
        for (OmsCartItem cartItem : omsCartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }
        jedis.del("user:" + memberId + ":cart");
        jedis.hmset("user:" + memberId + ":cart",map);

        jedis.close();

    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        Jedis jedis = redisUtil.getJedis();
        List<String> hvals = jedis.hvals("user:" + userId + ":cart");

        for (String hval : hvals) {
            OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
            omsCartItems.add(omsCartItem);
        }

        jedis.close();
        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem,e);
    }
}

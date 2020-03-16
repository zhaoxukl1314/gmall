package com.zhaoxu.cartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zhaoxu.annotations.LoginRequired;
import com.zhaoxu.bean.OmsCartItem;
import com.zhaoxu.bean.PmsSkuInfo;
import com.zhaoxu.service.CartService;
import com.zhaoxu.service.SkuService;
import com.zhaoxu.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping("toTrade")
    @LoginRequired(loginSuccess = true)
    public String toTrade() {
        return "toTrade";
    }

    @RequestMapping("checkCart")
    @LoginRequired(loginSuccess = false)
    public String checkCart(String isChecked, String skuId, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setIsChecked(isChecked);
        omsCartItem.setProductSkuId(skuId);
        cartService.checkCart(omsCartItem);

        List<OmsCartItem> omsCartItems = cartService.cartList(skuId);
        modelMap.put("cartList", omsCartItems);

        return "cartListInner";
    }


    @RequestMapping("cartList")
    @LoginRequired(loginSuccess = false)
    public String cartList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        if (StringUtils.isBlank(userid)) {
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        } else {
            omsCartItems = cartService.cartList(userid);
        }

        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }

        modelMap.put("cartList", omsCartItems);
        BigDecimal totalAccount = getTotalAccount(omsCartItems);
        modelMap.put("totalAmount", totalAccount);
        return "cartList";
    }

    private BigDecimal getTotalAccount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAccount = new BigDecimal("0");
        Optional<BigDecimal> reduce = omsCartItems.stream().filter(item -> item.getIsChecked().equalsIgnoreCase("1")).map(OmsCartItem::getTotalPrice).reduce((a, b) -> a.add(b));
        return reduce.orElse(new BigDecimal("0"));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addToCart")
    @LoginRequired(loginSuccess = false)
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response) {

        PmsSkuInfo skuInfo = skuService.getSkuById(skuId, "");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setProductSkuCode("1111111111");
        omsCartItem.setQuantity(new BigDecimal(quantity));


        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        if (StringUtils.isBlank(memberId)) {
            List<OmsCartItem> omsCartItems = new ArrayList<>();
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)) {
                omsCartItems.add(omsCartItem);
            } else {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                boolean exist = isCookieValueExist(omsCartItems, omsCartItem);

                if (exist) {
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equalsIgnoreCase(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
                        }
                    }
                } else {
                    omsCartItems.add(omsCartItem);
                }
            }

            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            List<OmsCartItem> omsCartItems = new ArrayList<>();
            OmsCartItem omsCartItemFromDB = cartService.ifCartExistByUser(memberId, skuId);

            if (omsCartItemFromDB == null) {
                omsCartItem.setMemberId(memberId);
                omsCartItem.setMemberNickname("zhaoxu");
                omsCartItem.setQuantity(new BigDecimal(quantity));
                cartService.addCart(omsCartItem);
            } else {
                omsCartItemFromDB.setQuantity(omsCartItemFromDB.getQuantity().add(omsCartItem.getQuantity()));
                cartService.updateCart(omsCartItemFromDB);
            }

            cartService.flushCartCache(memberId);
        }

        return "redirect:/success.html";
    }

    private boolean isCookieValueExist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        Optional<OmsCartItem> first = omsCartItems.stream().filter(item -> item.getProductSkuId().equalsIgnoreCase(omsCartItem.getProductSkuId())).findFirst();
        return first.isPresent();
    }
}

package com.iot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.DO.UserCartItem;
import com.iot.DO.UserDetails;
import com.iot.service.CartItemService;

@Controller
public class CartController {

	@Autowired
	private CartItemService cartItemService;
	
	@RequestMapping(value = "/getCartItems", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCartItems(@RequestBody UserDetails userDetails) {
		Map<String, Object> returnMap  = null;
		if(userDetails.getUserId() != null) {
			returnMap = cartItemService.getCartItems(userDetails.getUserId());
		}
		return returnMap;
	}
	
	@RequestMapping(value = "/checkoutCartItems", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkoutCartItems(@RequestBody UserDetails userDetails) {
		Map<String, Object> returnMap  = null;
		if(userDetails.getUserId() != null) {
			returnMap = cartItemService.checkoutCartItems(userDetails.getUserId());
		}
		return returnMap;
	}
	
	@RequestMapping(value = "/removeCartItem", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> removeCartItem(@RequestBody UserCartItem userCartItem) {
		Map<String, Object> returnMap  = null;
		if(userCartItem.getUserCartItemId() != null) {
			returnMap = cartItemService.removeCartItem(userCartItem);
		}
		return returnMap;
	}
	
}

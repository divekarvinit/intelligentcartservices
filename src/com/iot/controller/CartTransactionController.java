package com.iot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.DO.UserDetails;
import com.iot.service.CartTranscationService;

@Controller
public class CartTransactionController {
	
	@Autowired
	CartTranscationService cartTransactionService;
	
	@RequestMapping(value = "/getTransactionHistory", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getTransactionHistory(@RequestBody UserDetails userDetails) {
		return cartTransactionService.getTransactionHistory(userDetails);
	}

}

package com.iot.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.DO.CartItem;
import com.iot.DO.Checklist;
import com.iot.DO.ImageMaster;
import com.iot.DO.ItemMaster;
import com.iot.DO.Login;
import com.iot.DO.UserCartItem;
import com.iot.service.CartItemService;
import com.iot.service.ChecklistService;
import com.iot.service.LoginService;
import com.iot.service.OffersService;

@Controller
public class WebServicesController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ChecklistService checklistService;	
	
	@Autowired
	private OffersService offersService;		

	@Autowired
	private CartItemService cartItemService;
	
	@RequestMapping(value = "login", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String login(@RequestBody Login login)
			throws UnsupportedEncodingException, JSONException {
		System.out.println("In login");
		try {

//			String strJson = new String(byteJson, "UTF-8");
//			strJson = strJson.replaceAll("\"", "'");
//
//			JSONObject object = new JSONObject(strJson);
//
//			Login objLogin = new Login();
//
//			objLogin.setUserName(object.getString("userName"));
//			objLogin.setPassword(object.getString("password"));

			String jsonResponse = loginService.login(login);
			
			return jsonResponse;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	@RequestMapping(value = "/checklist", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getChecklist(@RequestParam(value="user_id") String userId) {
		try {

			JSONObject jsonObject = new JSONObject();
			
			String jsonResponse = jsonObject.put("Checklist",checklistService.getChecklist(userId)).toString();		
			return jsonResponse;

		} catch (Exception ex) {
			System.out.println("Internal Server Error");
			return null;
		}
	}	
	
	@RequestMapping(value = "/checklistAdd", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String addChecklist(@RequestBody Checklist checklist)
			throws UnsupportedEncodingException, JSONException {
		System.out.println("In Checklist Add ::::::");
		try {

//			String strJson = new String(byteJson, "UTF-8");
//			strJson = strJson.replaceAll("\"", "'");
//
//			JSONObject object = new JSONObject(strJson);

			String jsonResponse = checklistService.addChecklist(checklist);
			System.out.println("Checklist Exit ::::::");
			return jsonResponse;
		} catch (SQLException e) {
			e.printStackTrace();
			return "Internal Error";
		}
	}
	

	@RequestMapping(value = "/checklistUpdate", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateChecklist(@RequestBody Checklist checklist) {
		try {

			/*String strJson = new String(byteJson, "UTF-8");
			strJson = strJson.replaceAll("\"", "'");

			
			JSONObject object = new JSONObject(strJson);
			Checklist checklist = new Checklist(object);*/
			String jsonResponse = checklistService.updateChecklist(checklist);			
			return jsonResponse;

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Internal Server Error";
		}
	}
	
	@RequestMapping(value = "/offers", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getOfferList(@RequestParam(value="user_id") String userId) {
		try {
			JSONObject jsonObject = new JSONObject();

			String jsonResponse = jsonObject.put("OfferList",offersService.getOffersList(userId)).toString();		
			return jsonResponse;

		} catch (Exception ex) {
			System.out.println("Internal Server Error");
			return null;
		}
	}
	
	@RequestMapping(value = "/searchItems", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody Map<String, Object> getItemList(@RequestParam(value="searched_string") String searchedString) {
		try {

			Map<String, Object> returnMap = checklistService.getSearchedItems(searchedString);			
			return returnMap;

		} catch (Exception ex) {
			System.out.println("Internal Server Error");
			return null;
		}
	}	
	
	
	@RequestMapping(value = "/cartItemAdd", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String addCartItem(@RequestBody UserCartItem userCartItem)
			throws UnsupportedEncodingException, JSONException {

		try {

//			String strJson = new String(byteJson, "UTF-8");
//			strJson = strJson.replaceAll("\"", "'");
//
//			JSONObject object = new JSONObject(strJson);

			String jsonResponse = cartItemService.addCartItem(userCartItem);
			
			return jsonResponse;
		} catch (SQLException e) {
			//e.printStackTrace();
			return "Internal Server Error";
		}
	}	
	
	
	@RequestMapping(value = "/getItemRecommendationForIndividualItem", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getItemRecommendationForIndividualItem(@RequestBody ItemMaster itemMaster){
		
		return checklistService.getItemRecommendationForIndividualItem(itemMaster);
	}
	
	
	@RequestMapping(value = "/getItems", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getItems(@RequestBody Login login){
		
		return checklistService.getItemFromItemMaster(login.getUserName());
	}
	
	@RequestMapping(value = "/getImage", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getImage(@RequestBody ImageMaster imageMaster){
		
		return checklistService.getImage(imageMaster);
	}
}

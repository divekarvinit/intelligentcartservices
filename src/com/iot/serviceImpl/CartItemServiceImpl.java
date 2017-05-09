package com.iot.serviceImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.DO.UserCartItem;
import com.iot.DO.UserDetails;
import com.iot.service.CartItemService;
import com.iot.service.ChecklistService;
import com.iot.utility.DbService;
import com.iot.vo.ChecklistVO;
//import com.sendgrid.SendGrid;
@Service
public class CartItemServiceImpl implements CartItemService{

	@Autowired
	ChecklistService checklistService;
	
	private static final String SQL_INSERT = "insert into smart_cart.user_cart_item (user_checklist_id, user_id, item_id, quantity, cart_item_cost, cost_after_discount, is_checked_out) VALUES (?, ?, ?, ?, ?, ?,'N')";
	private static final String SENDGRID_API_KEY = "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";
	
	
	@Override
	public String addCartItem(UserCartItem cartItem) throws SQLException, JSONException{
//		CartItem cartItem = (new CartItem(jsonObj)).getCartItem();		
		JSONObject jsonObject = new JSONObject();
		String result=null;	
		try {
			
//			cartItem.setUserChecklistId(jsonObj.getLong("userChecklistId"));
//			cartItem.setUserId(jsonObj.getString("userId"));
//			cartItem.setItemId(jsonObj.getLong("itemId"));
//			cartItem.setQuantity(jsonObj.getDouble("quantity"));
//			cartItem.setCartItemCost(jsonObj.getDouble("cartItemCost"));
//			cartItem.setCostAfterDiscount(jsonObj.getDouble("costAfterDiscount"));
//			cartItem.setIsCheckedOut("N");
			
			result = create(cartItem);
			
			
			
		} catch (JSONException e) {
			
			result = jsonObject.put("Error","Adding item to cart failed").toString();
			return result;
			
		}
		return result;

		
	}
	
	@SuppressWarnings("unchecked")
	public String create(UserCartItem userCartItem) throws SQLException, JSONException {
		
		Connection con = null;
		Statement itemMasterStatement = null;
		PreparedStatement statement = null;
		Statement userChecklistStatement = null;
		Integer imageId = null;
		ResultSet itemMasterResultSet = null;
		ResultSet generatedKeys = null;
		BigDecimal cartItemCost = new BigDecimal(0);
		String itemName = null;
		BigDecimal costAfterDiscount = new BigDecimal(0);
		DbService dbService = new DbService();
		JSONObject jsonObject = new JSONObject();
		String uomCode = null;
		UserDetails userDetails = null;
	    try{
	    	con = dbService.getConnection();
	    	con.setAutoCommit(false);
	    	
	        statement = con.prepareStatement(SQL_INSERT,
	                                      Statement.RETURN_GENERATED_KEYS);
	        
	        statement.setObject(1, userCartItem.getUserCheckListId());
	        statement.setObject(2, userCartItem.getUserId());
	        statement.setObject(3, userCartItem.getItemId());
	        statement.setObject(4, userCartItem.getQuantity());
	       
	        userDetails = getUserDetails(userCartItem.getUserId()); 
	        
	        if(userCartItem.getItemId() != null) {
	        	
	        	itemMasterStatement = con.createStatement();
		        String itemMasterQuery = "SELECT * FROM item_master where item_master_id = '" + userCartItem.getItemId().toString() + "'";
		        itemMasterResultSet = itemMasterStatement.executeQuery(itemMasterQuery);
		        
		        while(itemMasterResultSet.next()){
		        	cartItemCost = (BigDecimal) itemMasterResultSet.getObject("mrp");
		        	costAfterDiscount = (BigDecimal) itemMasterResultSet.getObject("price_after_discount");
		        	itemName = (String) itemMasterResultSet.getObject("item_name");
		        	imageId = (Integer) itemMasterResultSet.getObject("image_id");
		        	uomCode = (String) itemMasterResultSet.getObject("uom_code");
		        	
		        	if(imageId != null){
		        		Map<String, Object> responseMap = checklistService.getImageFromImageId(imageId);
		        		jsonObject.put("imageFileName", (String) responseMap.get("imageFileName"));
		        		jsonObject.put("imagePath", (String) responseMap.get("imagePath"));
		        	}
		        }
	        }
	         
	        statement.setObject(5, cartItemCost);
	        statement.setObject(6, costAfterDiscount);

	        int affectedRows = statement.executeUpdate();

	        if (affectedRows == 0) {
	        	jsonObject.put("Error","Adding item to Cart failed failed");
	        }

//	        try {
        	generatedKeys = statement.getGeneratedKeys();
        		
        	
            if (generatedKeys.next()) {
            	userCartItem.setUserCartItemId((int) (long) generatedKeys.getObject(1));
            	
            }
            else {
            	jsonObject.put("Error","Adding item to Cart failed failed").toString();
            }
//	        }catch(Exception e){
//	        	e.printStackTrace();
//	        	return jsonObject.put("Error","Adding item to Cart failed failed").toString();
//		    }
	    	        
	        // Update user_checklist_item to set flag that product has been added to cart.
	        
	        if(userCartItem.getUserCheckListId() != null){
	        	userChecklistStatement = con.createStatement();
		        String userChecklistUpdateQuery = "UPDATE user_checklist set is_active = 'N' where user_checklist_id = " + userCartItem.getUserCheckListId();
		        userChecklistStatement.executeUpdate(userChecklistUpdateQuery);
	        }
	        
	        con.commit();
	        
	        Map<String, Object> cartMap = getCartItems(userDetails.getUserId());
			Map<String, Object> cartDetailsMap = (Map<String, Object>) cartMap.get("cartItems");
		    
		    
		    jsonObject.put("userCartItemId", userCartItem.getUserCartItemId());
		    jsonObject.put("userCheckListId", userCartItem.getUserCheckListId());
		    jsonObject.put("userId", userCartItem.getUserId());
		    jsonObject.put("itemId", userCartItem.getItemId());
		    jsonObject.put("itemName", itemName);
		    jsonObject.put("quantity", userCartItem.getQuantity());
		    jsonObject.put("cartItemCost", cartItemCost);
		    jsonObject.put("costAfterDiscount", costAfterDiscount);
		    jsonObject.put("isCheckedOut", userCartItem.getIsCheckedOut());
		    jsonObject.put("uomCode", uomCode);
		    if(cartDetailsMap.get("totalCartSaving") != null){
		    	jsonObject.put("totalCartSaving", cartDetailsMap.get("totalCartSaving").toString());
		    }
		    if(cartDetailsMap.get("totalCartSaving") != null){
		    	jsonObject.put("totalCartValue", cartDetailsMap.get("totalCartValue").toString());
		    }
		    if(cartDetailsMap.get("totalCartSaving") != null){
		    	jsonObject.put("walletBalance", cartDetailsMap.get("walletBalance").toString());
		    }
	        
		    itemMasterResultSet.close();
	        itemMasterStatement.close();
	        statement.close();
	        con.close();
	        
	    }catch(Exception e){
	    	
	    	try {
	    		if(con != null){
	    			con.rollback();
	    			con.close();
		    	}
	    	}catch(Exception innerException){
	    		innerException.printStackTrace();
	    	} 
	    	e.printStackTrace();
	    	return jsonObject.put("Error","Adding item to Cart failed failed").toString();
	    }
	    System.out.println("After Cart Addition:::::: " + jsonObject);
	    return jsonObject.toString();

	}

    @Override
    public Map<String, Object> getCartItems(String userName) {
 
        Connection con = null;
        Integer rowCount = new Integer(0);
        BigDecimal totalCartCost = new BigDecimal(0);
        BigDecimal totalOfMrps = new BigDecimal(0);
        DbService dbService = new DbService();
        Map<String, Object> innerMap = new HashMap<String, Object>();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<UserCartItem> cartItemsList = new ArrayList<UserCartItem>();
        con = dbService.getConnection();
        Statement userCartStatement = null;
        
        try{
            userCartStatement = con.createStatement();
            con.setAutoCommit(false);
            String userCartQuery = "SELECT * FROM user_cart_item where user_id ="+userName + " AND is_checked_out = 'N'";
            ResultSet userCartItemResultSet = userCartStatement.executeQuery(userCartQuery);
            
            while(userCartItemResultSet.next()){
                rowCount++;
                UserCartItem userCartItem = new UserCartItem();
                userCartItem.setUserCartItemId((Integer) userCartItemResultSet.getObject("user_cart_item_id"));
                userCartItem.setUserCheckListId((Integer) userCartItemResultSet.getObject("user_checklist_id"));
                userCartItem.setUserId((String) userCartItemResultSet.getObject("user_id"));
                userCartItem.setItemId((Integer) userCartItemResultSet.getObject("item_id"));
                userCartItem.setQuantity((BigDecimal) userCartItemResultSet.getObject("quantity"));
                userCartItem.setCartItemCost((BigDecimal) userCartItemResultSet.getObject("cart_item_cost"));
                userCartItem.setCostAfterDiscount((BigDecimal) userCartItemResultSet.getObject("cost_after_discount"));
                cartItemsList.add(userCartItem);
                
                if((BigDecimal) userCartItemResultSet.getObject("cost_after_discount") != null) {
                    totalCartCost = totalCartCost.add((BigDecimal) userCartItemResultSet.getObject("cost_after_discount"));
                }
                
                if((BigDecimal) userCartItemResultSet.getObject("cart_item_cost") != null){
                    totalOfMrps = totalOfMrps.add((BigDecimal) userCartItemResultSet.getObject("cart_item_cost"));
                }
            }
            
            innerMap.put("cartItems", cartItemsList);
            innerMap.put("totalCartValue", totalCartCost);
            innerMap.put("totalCartSaving", totalOfMrps.subtract(totalCartCost));
            innerMap.put("walletBalance", getUserDetails(userName).getWalletBalance());
            
            returnMap.put("cartItems", innerMap);
            returnMap.put("success", "Yes");
            if(rowCount > 0) {
                innerMap.put("cartItems", cartItemsList);
            } else {
                innerMap.put("cartItems", "No Data to Display");
            }
            
            userCartItemResultSet.close();
            userCartStatement.close();
            con.close();
            
        } catch(Exception e){
            e.printStackTrace();
            returnMap.put("cartDetails", innerMap);
        }
        
        return returnMap;
    }
 
    @Override
    public UserDetails getUserDetails(String userId) throws SQLException {
        UserDetails userDetails = new UserDetails();
        
        Connection con = null;
        DbService dbService = new DbService();
        
        Statement userCartStatement = null;
        
        con = dbService.getConnection();
        
        userCartStatement = con.createStatement();
        
        String userMasterQuery = "SELECT * FROM user_master where user_id ="+ userId;
        ResultSet userMasterResultSet = userCartStatement.executeQuery(userMasterQuery);
        
        while(userMasterResultSet.next()){
            userDetails.setUserId((String) userMasterResultSet.getObject("user_id"));
            userDetails.setPassword((String) userMasterResultSet.getObject("password"));
            userDetails.setFirstName((String) userMasterResultSet.getObject("first_name"));
            userDetails.setFirstName((String) userMasterResultSet.getObject("middle_name"));
            userDetails.setFirstName((String) userMasterResultSet.getObject("last_name"));
            userDetails.setEmailId((String) userMasterResultSet.getObject("email_id"));
            userDetails.setImagePath((Integer) userMasterResultSet.getObject("image_id"));
            userDetails.setWalletBalance(userMasterResultSet.getDouble("wallet_balance"));
        }
        return userDetails;
    }
 
    @Override
    public Map<String, Object> checkoutCartItems(String userId) {
 
        
        Connection con = null;
        Statement userCartStatement = null;
        Statement userChecklistStatement = null;
        ResultSet userCartItemResultSet = null;
        Double currentWalletBalance = new Double(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Double totalCartCost = new Double(0);
        DbService dbService = new DbService();
        con = dbService.getConnection();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        
        try{
            
            con.setAutoCommit(false);
            userCartStatement = con.createStatement();
            
            // get total cart value that will be deleted from user's wallet balance
            
            String userCartQuery = "SELECT * FROM user_cart_item where user_id = '" + userId + "' AND is_checked_out = 'N'";
            userCartItemResultSet = userCartStatement.executeQuery(userCartQuery);
            
            while(userCartItemResultSet.next()){
            	
            	// Set is checklist item as purchased and set purchase date
            	
            	Integer userChecklistId = (Integer) userCartItemResultSet.getObject("user_checklist_id");
            	
            	if(userChecklistId != null){
            		String userChecklistQuery = "UPDATE user_checklist SET purchase_date = now(), is_purchased = 'Y' where user_checklist_id = " + userChecklistId.toString();
            		userChecklistStatement = con.createStatement();
            		userChecklistStatement.executeUpdate(userChecklistQuery);
            	}
                totalCartCost += (Double) userCartItemResultSet.getDouble("cost_after_discount");
            }
            
            // get current wallet balance of user
            
            currentWalletBalance = getUserDetails(userId).getWalletBalance();
            
            // Update cart records. Set is_checked_out as Yes
            
            userCartQuery = "UPDATE user_cart_item SET is_checked_out='Y' where user_id = '" + userId + "' AND is_checked_out = 'N'";
            Statement userCartStatementObj = con.createStatement();
            userCartStatementObj.executeUpdate(userCartQuery);
            
            // Insert into user transaction table
            
            String userTransactionQuery = "INSERT INTO user_transaction_history (user_id, transaction_amount, transaction_date) VALUES ('" + userId +"'," + totalCartCost + ",'" + format.format(new Date()) + "')";
            Statement userTransactionStatement = con.createStatement();
            userTransactionStatement.executeUpdate(userTransactionQuery);
            
            // Update the user's wallet balance after transaction
            
            String userMasterQuery = "UPDATE user_master SET wallet_balance = " + (currentWalletBalance - totalCartCost) + " where user_id = '" + userId +"'";
            Statement userMasterStatement = con.createStatement();
            userMasterStatement.executeUpdate(userMasterQuery);
            
            userCartItemResultSet.close();
            userCartStatement.close();
            
            
            returnMap.put("message", "Successfully Checked out");
            returnMap.put("walletBalance", currentWalletBalance - totalCartCost);
            
            // Send email to the user after checkout.
            
//            generateEmail(totalCartCost, currentWalletBalance - totalCartCost);
            
            con.commit();
            
            // Get user Checklist 
            
             List<ChecklistVO> checkList = checklistService.getChecklist(userId) ; 
            
            returnMap.put("Checklist", checkList);
            con.commit();
            con.close();
        } catch(Exception e){
            
            e.printStackTrace();
            returnMap.put("message", "Can not checkout your items");
            returnMap.put("walletBalance", "");
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return returnMap;
 
    }

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> removeCartItem(UserCartItem userCartItem) {

		Connection con = null;
        DbService dbService = new DbService();
        Statement userCartStatement = null;
        Map<String, Object> returnmap = new HashMap<String, Object>();
        try{
        	
        	con = dbService.getConnection();
        	con.setAutoCommit(false);
	        
	        userCartStatement = con.createStatement();
	        
	        String userMasterQuery = "DELETE FROM  user_cart_item where user_cart_item_id ="+ userCartItem.getUserCartItemId();
	        userCartStatement.executeUpdate(userMasterQuery);
	        
	        // Update user_checklist_item to set flag that product has been added to cart.
	        
	        if(userCartItem.getUserCheckListId() != null){
	        	Statement userChecklistStatement = con.createStatement();
		        String userChecklistUpdateQuery = "UPDATE user_checklist set is_active = 'Y' where user_checklist_id = " + userCartItem.getUserCheckListId();
		        userChecklistStatement.executeUpdate(userChecklistUpdateQuery);
	        }
	        
	        con.commit();
	        
	        if(userCartItem.getUserId() != null){
	        	
	        	Map<String, Object> cartMap = getCartItems(userCartItem.getUserId());
	    		Map<String, Object> cartDetailsMap = (Map<String, Object>) cartMap.get("cartItems");
	    		
	    		 if(cartDetailsMap.get("totalCartSaving") != null){
	    			 returnmap.put("totalCartSaving", cartDetailsMap.get("totalCartSaving").toString());
	    		    }
	    		    if(cartDetailsMap.get("totalCartSaving") != null){
	    		    	returnmap.put("totalCartValue", cartDetailsMap.get("totalCartValue").toString());
	    		    }
	    		    if(cartDetailsMap.get("totalCartSaving") != null){
	    		    	returnmap.put("walletBalance", cartDetailsMap.get("walletBalance").toString());
	    		    }
	        }
	        
	        userCartStatement.close();
	        
            con.close();
	        returnmap.put("message", "Item removed from cart");
        } catch(Exception e){
        	e.printStackTrace();
        	returnmap.put("message", "item cannot be removed from cart at the moment");
        	 try {
                 con.rollback();
             } catch (SQLException e1) {
                 e1.printStackTrace();
             }
        }
        
		return returnmap;
	}	
	
	/*@SuppressWarnings("unused")
	private void generateEmail(Double invoiceAmount, Double walletBalance){
		
		SendGrid sendgrid = new SendGrid(SENDGRID_API_KEY);
		String emailContent = "Thank you for shopping. Total invoice amount is Rs." + invoiceAmount.toString() +" . Now your wallet balance is Rs. " + walletBalance.toString() +".";
	    String emailSubject = "Invoice";
		SendGrid.Email email = new SendGrid.Email();
	    email.addTo("vinit.divekar@lntinfotech.com");
	    email.setFrom("vinit.divekar@lntinfotech.com");
	    email.setSubject(emailSubject);
	    email.setText(emailContent);

	    try {
	      SendGrid.Response response = sendgrid.send(email);
	      System.out.println(response.getMessage());
	    }
	    catch (Exception e) {
	      System.err.println(e);
	    }
	}*/
}

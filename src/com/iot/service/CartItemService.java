package com.iot.service;
 
import java.sql.SQLException;
import java.util.Map;
 
import org.json.JSONException;
import org.json.JSONObject;

import com.iot.DO.CartItem;
import com.iot.DO.UserCartItem;
import com.iot.DO.UserDetails;
 
public interface CartItemService {
    
    public String addCartItem(UserCartItem userCartItem) throws SQLException, JSONException;
    
    public Map<String, Object> getCartItems(String userName);
    
    public UserDetails getUserDetails(String userId) throws SQLException;
 
    public Map<String, Object> checkoutCartItems(String user_id);

	public Map<String, Object> removeCartItem(UserCartItem userCartItem);
}

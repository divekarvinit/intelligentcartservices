package com.iot.DO;

import org.json.JSONException;
import org.json.JSONObject;

public class CartItem {
	private Long userCartItemId;
	private Long userChecklistId;
	private String userId;	
	private Long itemId;
	private Double quantity;	
	private Double cartItemCost;	
	private Double costAfterDiscount;
	private String isCheckedOut;
	
	private CartItem cartItem;
	
	public Long getUserCartItemId() {
		return userCartItemId;
	}
	public void setUserCartItemId(Long userCartItemId) {
		this.userCartItemId = userCartItemId;
	}
	public Long getUserChecklistId() {
		return userChecklistId;
	}
	public void setUserChecklistId(Long userChecklistId) {
		this.userChecklistId = userChecklistId;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getCartItemCost() {
		return cartItemCost;
	}
	public void setCartItemCost(Double cartItemCost) {
		this.cartItemCost = cartItemCost;
	}
	public Double getCostAfterDiscount() {
		return costAfterDiscount;
	}
	public void setCostAfterDiscount(Double costAfterDiscount) {
		this.costAfterDiscount = costAfterDiscount;
	}
	
	public CartItem getCartItem() {
		return cartItem;
	}
	public void setCartItem(CartItem cartItem) {
		this.cartItem = cartItem;
	}
	
	public CartItem (){}
	public CartItem (JSONObject jsonObj){
		this.cartItem = setCartItemValue(jsonObj);		
		
	}
	
	private CartItem setCartItemValue(JSONObject jsonObj){
		CartItem cartItem = new CartItem();
		try {
			//cartItem.setUser_checklist_id(jsonObj.getLong("user_checklist_id"));
			cartItem.setUserId(jsonObj.getString("userId"));
			cartItem.setItemId(jsonObj.getLong("itemId"));
			cartItem.setQuantity(jsonObj.getDouble("quantity"));
			cartItem.setCartItemCost(jsonObj.getDouble("cartItemCost"));
			cartItem.setCostAfterDiscount(jsonObj.getDouble("costAfterDiscount"));
			
		} catch (JSONException e) {
			return cartItem;
		}
		return cartItem;
				
	}	
	@Override
	public String toString() {
		return "CartItem [user_cart_item_id=" + userCartItemId + ", user_checklist_id=" + userChecklistId
				+ ", user_id=" + userId + ", item_id=" + itemId + ", quantity=" + quantity + ", cart_itemCost="
				+ cartItemCost + ", cost_after_discount=" + costAfterDiscount + "]";
	}
	public String getIsCheckedOut() {
		return isCheckedOut;
	}
	public void setIsCheckedOut(String isCheckedOut) {
		this.isCheckedOut = isCheckedOut;
	}

	
}

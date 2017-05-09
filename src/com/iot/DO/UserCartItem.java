package com.iot.DO;

import java.math.BigDecimal;

public class UserCartItem {

	private Integer userCartItemId;
	private Integer userCheckListId;
	private String userId;
	private Integer itemId;
	private BigDecimal quantity;
	private BigDecimal cartItemCost;
	private BigDecimal costAfterDiscount;
	private String isCheckedOut;
	
	public UserCartItem() {
		super();

	}
	
	
	@Override
	public String toString() {
		return "UserCartItem [userCartItemId=" + userCartItemId + ", userCheckListId=" + userCheckListId + ", userId="
				+ userId + ", itemId=" + itemId + ", quantity=" + quantity + ", cartItemCost=" + cartItemCost
				+ ", costAfterDiscount=" + costAfterDiscount + "]";
	}


	public UserCartItem(Integer userCartItemId, Integer userCheckListId, String userId, Integer itemId,
			BigDecimal quantity, BigDecimal cartItemCost, BigDecimal costAfterDiscount, String isCheckedOut) {
		super();
		this.userCartItemId = userCartItemId;
		this.userCheckListId = userCheckListId;
		this.userId = userId;
		this.itemId = itemId;
		this.quantity = quantity;
		this.cartItemCost = cartItemCost;
		this.costAfterDiscount = costAfterDiscount;
		this.isCheckedOut = isCheckedOut;
	}
	public Integer getUserCartItemId() {
		return userCartItemId;
	}
	public void setUserCartItemId(Integer userCartItemId) {
		this.userCartItemId = userCartItemId;
	}
	public Integer getUserCheckListId() {
		return userCheckListId;
	}
	public void setUserCheckListId(Integer userCheckListId) {
		this.userCheckListId = userCheckListId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getCartItemCost() {
		return cartItemCost;
	}
	public void setCartItemCost(BigDecimal cartItemCost) {
		this.cartItemCost = cartItemCost;
	}
	public BigDecimal getCostAfterDiscount() {
		return costAfterDiscount;
	}
	public void setCostAfterDiscount(BigDecimal costAfterDiscount) {
		this.costAfterDiscount = costAfterDiscount;
	}


	public String getIsCheckedOut() {
		return isCheckedOut;
	}


	public void setIsCheckedOut(String isCheckedOut) {
		this.isCheckedOut = isCheckedOut;
	}
}

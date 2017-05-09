package com.iot.vo;

import java.math.BigDecimal;

public class ChecklistVO {

	private Integer userCheckListId;
	private String userId;
	private Integer itemId;
	private String itemName;
	private String tableType;
	private BigDecimal quantity;
	private String isActive;
	private String isPurchased;
	private String purchaseDate;
	private String createdDate;
	private String modifiedDate;
	private String uomCode;
	private String imageFileName;
	
	
	public String getUomCode() {
		return uomCode;
	}
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getIsPurchased() {
		return isPurchased;
	}
	public void setIsPurchased(String isPurchased) {
		this.isPurchased = isPurchased;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Integer getUserCheckListId() {
		return userCheckListId;
	}
	public void setUserCheckListId(Integer userCheckListId) {
		this.userCheckListId = userCheckListId;
	}
}

package com.iot.DO;

import java.math.BigDecimal;

public class ItemMaster {
	
	private Integer itemMasterId;
	private String itemName;
	private String itemDescription;
	private Integer categoryId;
	private Integer imageId;
	private BigDecimal mrp;
	private BigDecimal baseQuantity;
	private BigDecimal priceAfterDescount;
	private String uomCode;
	
	
	public ItemMaster() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getItemMasterId() {
		return itemMasterId;
	}
	public void setItemMasterId(Integer itemMasterId) {
		this.itemMasterId = itemMasterId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public BigDecimal getMrp() {
		return mrp;
	}
	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}
	public BigDecimal getBaseQuantity() {
		return baseQuantity;
	}
	public void setBaseQuantity(BigDecimal baseQuantity) {
		this.baseQuantity = baseQuantity;
	}
	public BigDecimal getPriceAfterDescount() {
		return priceAfterDescount;
	}
	public void setPriceAfterDescount(BigDecimal priceAfterDescount) {
		this.priceAfterDescount = priceAfterDescount;
	}
	public String getUomCode() {
		return uomCode;
	}
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}
	public ItemMaster(Integer itemMasterId, String itemName, String itemDescription, Integer categoryId,
			Integer imageId, BigDecimal mrp, BigDecimal baseQuantity, BigDecimal priceAfterDescount, String uomCode) {
		super();
		this.itemMasterId = itemMasterId;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.categoryId = categoryId;
		this.setImageId(imageId);
		this.mrp = mrp;
		this.baseQuantity = baseQuantity;
		this.priceAfterDescount = priceAfterDescount;
		this.uomCode = uomCode;
	}
	public Integer getImageId() {
		return imageId;
	}
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	
	

}

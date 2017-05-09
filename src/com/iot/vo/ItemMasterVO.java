package com.iot.vo;

import java.math.BigDecimal;

public class ItemMasterVO {
	
	private Integer itemMasterId;
	private String itemName;
	private String itemDescription;
	private Integer categoryId;
	private String imagePath;
	private BigDecimal mrp;
	private BigDecimal baseQuantity;
	private BigDecimal priceAfterDescount;
	private String uomCode;
	private String rfidString;
	private String barcodeString;
	private String imageFileName;
	
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
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
	public String getRfidString() {
		return rfidString;
	}
	public void setRfidString(String rfidString) {
		this.rfidString = rfidString;
	}
	public String getBarcodeString() {
		return barcodeString;
	}
	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
}

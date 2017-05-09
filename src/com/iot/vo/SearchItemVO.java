package com.iot.vo;

import java.util.List;
import java.util.Map;

public class SearchItemVO {

	private Integer itemMasterId;
	private String itemName;
	private String imagePath;
	private String uomCode;
	private Map<String, List<OfferMasterVO>> offerMap;

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
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getUomCode() {
		return uomCode;
	}
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}
	public Map<String, List<OfferMasterVO>> getOfferMap() {
		return offerMap;
	}
	public void setOfferMap(Map<String, List<OfferMasterVO>> offerMap) {
		this.offerMap = offerMap;
	}
	
	
}

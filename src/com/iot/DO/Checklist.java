package com.iot.DO;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Checklist {

	private long userChecklistId;
	private String userId;
	private long itemId;
	private String itemName;
	private String tableType;
	private double quantity;
	private String isActive;
	private String isPurchased;
	private Date purchaseDate;
	private Date createdDate;
	private Date modifiedDate;

	private Checklist checklist;
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public long getUserChecklistId() {
		return userChecklistId;
	}
	public void setUserChecklistId(long userChecklistId) {
		this.userChecklistId = userChecklistId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
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
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Checklist() {
		
	}	

	public Checklist(JSONObject jsonObj) {
		
		this.checklist = setChecklistValue(jsonObj);
		
	}
	
	public Checklist getChecklist() {
		return this.checklist;
	}
	public void setChklist(Checklist checklist) {
		this.checklist = checklist;
	}
	private Checklist setChecklistValue(JSONObject jsonObj){
		Checklist checklist = new Checklist();
		try {
			checklist.setUserChecklistId(jsonObj.getLong("userChecklistId"));
			checklist.setUserId(jsonObj.getString("userId"));
			checklist.setIsActive(jsonObj.getString("isActive"));
			checklist.setIsPurchased(jsonObj.getString("isPurchased"));
			checklist.setTableType(jsonObj.getString("tableType"));
			
			checklist.setItemId(jsonObj.getLong("itemTd"));
			checklist.setQuantity(jsonObj.getDouble("quantity"));
/*			if(jsonObj.getString("purchase_date")!=null){
				String dateInString = (String)jsonObj.getString("purchase_date");

				try {

				       SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd H:m:s");
				        java.util.Date parsed = format.parse(dateInString);
				        java.sql.Date sql = new java.sql.Date(parsed.getTime());
				        chklist.setPurchase_date(sql);
				} catch (ParseException e) {
					chklist.setPurchase_date(null);				
					System.out.println("No proper date found");
					return;
				}			
			}*/

			
		} catch (JSONException e) {
			return checklist;
		}
		return checklist;
				
	}
	@Override
	public String toString() {
		return "Checklist [user_checklist_id=" + userChecklistId + ", user_id=" + userId + ", itemId=" + itemId
				+ ", tableType=" + tableType + ", quantity=" + quantity + ", isActive=" + isActive
				+ ", isPurchased=" + isPurchased + ", purchaseDate=" + purchaseDate + "]";
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}

package com.iot.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
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

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.iot.DO.Checklist;
import com.iot.DO.ImageMaster;
import com.iot.DO.ItemMaster;
import com.iot.DO.ItemRfidBarcode;
import com.iot.service.ChecklistService;
import com.iot.utility.DbService;
import com.iot.vo.ChecklistVO;
import com.iot.vo.ItemMasterVO;
import com.iot.vo.OfferMasterVO;
import com.iot.vo.SearchItemVO;
@Service
public class ChecklistServiceImpl implements ChecklistService {
	
	private static final String SQL_INSERT = "INSERT INTO smart_cart.user_checklist (user_id, item_id, table_type, quantity, is_active, is_purchased, purchase_date, created_date, modified_date) VALUES (?, ?, ?, ?, ?, ?, null, sysdate(), sysdate())";
	private static final String SQL_INSERT_FOR_CUSTOM_ITEM_MASTER = "INSERT INTO smart_cart.custom_item_master (item_master_id, item_name, item_description, category_id, image_id, offer_id, base_quantity, mrp, price_after_discount, uom_code) VALUES (?, ?, ?, ?, ?, ?, null, sysdate(), sysdate())";
	
	private static final String SQL_UPDATE = "UPDATE smart_cart.user_checklist set user_id = ?, item_id = ?, table_type=?, quantity=?, is_active=?, is_purchased=?, purchase_date=sysdate() where user_checklist_id=?";	
//	private static final String SQL_SELECT = "SELECT * FROM category_offer_mapping where category_id = ";
//	private static final String INDIVIDUAL_ITEM_BASED_RECOMMENDATION_API = "Bearer qe2TqsYsYV3RRxxNUQWe5xcONsohf7FmcohFNUpo0A1YYC4dFJEQkBcBWCKj7gYSDAqIb37QrU5ESGc2OsxgYA==";	
	private static final String INDIVIDUAL_ITEM_BASED_RECOMMENDATION_API = "Bearer vRX8M8DER9/gx/UX7C+qUXrRenFFGWImNIMta77GHDtFliEKu96KzNwlkMRKmJvThbG44TjKZSr0NLh7/7lw6g==";
//	private static final String INDIVIDUAL_ITEM_BASED_RECOMMENDATION_URL = "https://ussouthcentral.services.azureml.net/workspaces/14000d6d7471481c80c703989bd8e907/services/9453a31c27684ee6bba04560e121a7d6/execute?api-version=2.0&details=true";
	private static final String INDIVIDUAL_ITEM_BASED_RECOMMENDATION_URL = "https://ussouthcentral.services.azureml.net/workspaces/14000d6d7471481c80c703989bd8e907/services/d4d10e3fd9a344af8bc0f6422cadcf10/execute?api-version=2.0&details=true";
	
	@Override	
	public List<ChecklistVO> getChecklist(String user_id)throws SQLException{
		
			Connection con = null;
			DbService dbService = new DbService();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Statement stmtChecklist = null;
			List<ChecklistVO> checklistItems = new ArrayList<ChecklistVO>();
			try {


				con = dbService.getConnection();
						stmtChecklist = con.createStatement();
						String queryForChecklist = "SELECT * FROM user_checklist where user_id= '"+user_id + "' and is_purchased='N'";
						ResultSet checklistResultSet = stmtChecklist.executeQuery(queryForChecklist);

							while (checklistResultSet.next()) {
								
								String itemName = null;
								Statement itemMasterStatement = con.createStatement();
								Integer itemId = (Integer) checklistResultSet.getObject("item_id");
								
								ChecklistVO checklistVO  = new ChecklistVO();
								checklistVO.setUserCheckListId((Integer) checklistResultSet.getObject("user_checklist_id"));
								checklistVO.setUserId((String) checklistResultSet.getObject("user_id"));
								checklistVO.setItemId(itemId);
								checklistVO.setTableType((String) checklistResultSet.getObject("table_type"));
								checklistVO.setQuantity((BigDecimal) checklistResultSet.getObject("quantity"));
								checklistVO.setIsActive((String)checklistResultSet.getObject("is_active"));
								checklistVO.setIsPurchased((String)checklistResultSet.getObject("is_purchased"));
								
								if(checklistResultSet.getObject("purchase_date") != null){
									checklistVO.setPurchaseDate(format.format((Date)checklistResultSet.getObject("purchase_date")));
								}
								if((Date)checklistResultSet.getObject("created_date") != null){
									checklistVO.setCreatedDate(format.format((Date)checklistResultSet.getObject("created_date")));
								}
								if((Date)checklistResultSet.getObject("modified_date") != null){
									checklistVO.setModifiedDate(format.format((Date)checklistResultSet.getObject("modified_date")));
								}
								Statement imageMasterStatement = null;
								Integer imageId = null;
								
								if(itemId != null){
									
									String queryForItemMaster = "SELECT * FROM item_master where item_master_id= "+ itemId;
									ResultSet itemMasterResultSet = itemMasterStatement.executeQuery(queryForItemMaster);
									
									while(itemMasterResultSet.next()){
										checklistVO.setItemName((String) itemMasterResultSet.getObject("item_name"));
										imageId = (Integer) itemMasterResultSet.getObject("image_id");
										checklistVO.setUomCode((String) itemMasterResultSet.getObject("uom_code"));
										if(imageId != null){
											imageMasterStatement = con.createStatement();
											String queryForImageMaster = "SELECT * FROM image_master where image_master_id = "+ String.valueOf(imageId);
											ResultSet imageMasterResultSet = imageMasterStatement.executeQuery(queryForImageMaster);
											
											while(imageMasterResultSet.next()){
												checklistVO.setImageFileName((String) imageMasterResultSet.getObject("image_file_name"));
											}
										}
									}
								}
								
								
								checklistItems.add(checklistVO);
								
//								JSONObject jsonObjectArrayObj = new JSONObject();
//								long userCheckListId = (Long)checklistResultSet.getLong("user_checklist_id")!=null?checklistResultSet.getLong("user_checklist_id"):null;
//								String userId = checklistResultSet.getString("user_id");
//								long itemId = (Long)checklistResultSet.getLong("item_id")!=null?checklistResultSet.getLong("item_id"):null;
//								String tableType = checklistResultSet.getString("table_type");
//								String itemName = null;
//								Statement itemMasterStatement = con.createStatement();
//								if(Long.valueOf(itemId) != null){
//									String queryForItemMaster = "SELECT * FROM item_master where item_master_id= "+ String.valueOf(itemId);
//									ResultSet itemMasterResultSet = itemMasterStatement.executeQuery(queryForItemMaster);
//									
//									while(itemMasterResultSet.next()){
//										itemName = (String) itemMasterResultSet.getObject("item_name");
//									}
//									
//									if(itemName != null){
//										jsonObjectArrayObj.put("itemName", itemName);
//									} else{
//										jsonObjectArrayObj.put("itemName", "");
//									}
//								}
//								
//								double quantity = (Double)checklistResultSet.getDouble("quantity")!=null?checklistResultSet.getDouble("quantity"):null;
//								String isActive = checklistResultSet.getString("is_Active");							
//								String isPurchased = checklistResultSet.getString("is_purchased");
//								Date purchaseDate = (Date) checklistResultSet.getObject("purchase_date");
//								Date createdDate = (Date) checklistResultSet.getObject("created_date");
//								Date modifiedDate = (Date) checklistResultSet.getObject("modified_date");
//								
//								jsonObjectArrayObj.put("userChecklistId",userCheckListId);
//								jsonObjectArrayObj.put("userId",userId!=null?userId:"");
//								jsonObjectArrayObj.put("itemId",itemId);
//								jsonObjectArrayObj.put("tableType",tableType!=null?tableType:"");
//								jsonObjectArrayObj.put("quantity",quantity);
//								jsonObjectArrayObj.put("isActive",isActive!=null?isActive:"");							
//								jsonObjectArrayObj.put("isPurchased",isPurchased!=null?isPurchased:"");
//								
//								if(purchaseDate != null){
//									jsonObjectArrayObj.put("purchaseDate",format.format(purchaseDate));
//								} else {
//									jsonObjectArrayObj.put("purchaseDate","");
//								}
//								if(createdDate != null){
//									jsonObjectArrayObj.put("createdDate",format.format(createdDate));
//								} else {
//									jsonObjectArrayObj.put("createdDate","");
//								}
//								if(modifiedDate != null){
//									jsonObjectArrayObj.put("modifiedDate",format.format(modifiedDate));
//								} else {
//									jsonObjectArrayObj.put("modifiedDate","");
//								}
//								checklistArr.put(jsonObjectArrayObj);							
														
							}
						
						checklistResultSet = null;
						stmtChecklist = null;										
						
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("Connection failed");
					return null;
				} finally {
					try {
						con.close();
					} catch (SQLException e) {
						System.out.println("Connection failed");
						return null;
					}
				}
				return checklistItems;

		}
	
	@Override
	public String addChecklist(Checklist checklist) throws SQLException, JSONException{
//		Checklist chklist = (new Checklist(jsonObj)).getChecklist();		
		JSONObject jsonObject = new JSONObject();
		String result=null;	
		try {
			
//			chklist.setUserId(jsonObj.getString("userId"));
//			chklist.setIsActive(jsonObj.getString("isActive"));
//			chklist.setIsPurchased(jsonObj.getString("isPurchased"));
//
//			chklist.setTableType(jsonObj.getString("tableType"));
//			chklist.setItemId(jsonObj.getLong("itemId"));
/*			if(jsonObj.get("purchase_date")==null){
				chklist.setPurchase_date((Date)jsonObj.get("purchase_date"));
			}*/			
//			chklist.setQuantity(jsonObj.getDouble("quantity"));
			result = create(checklist);
			
			
			
		} catch (JSONException e) {
			
			result = jsonObject.put("Error","Creating checklist failed").toString();
			return result;
			
		}
		return result;

		
	}
	
	public String create(Checklist chklist) throws SQLException, JSONException {
		System.out.println("in the create");
		Connection con = null;
		DbService dbService = new DbService();
		JSONObject jsonObject = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		chklist.setCreatedDate(new Date());
		chklist.setModifiedDate(new Date());
		String itemName = null;
		Integer imageId = null;
		String uomCode = null;
		String imageFileName = null;
	    try{
	    	con = dbService.getConnection();
	        PreparedStatement statement = con.prepareStatement(SQL_INSERT,
	                                      Statement.RETURN_GENERATED_KEYS);
	        
	        statement.setString(1, chklist.getUserId());
	        statement.setLong(2, chklist.getItemId());
	        statement.setString(3, chklist.getTableType());
	        statement.setDouble(4, chklist.getQuantity());
	        statement.setString(5, chklist.getIsActive());
	        statement.setString(6, chklist.getIsPurchased());
	        
	        if(Long.valueOf(chklist.getItemId()) == null){
	        	
	        	PreparedStatement statementForCustomItemMaster = con.prepareStatement(SQL_INSERT_FOR_CUSTOM_ITEM_MASTER,
                        Statement.RETURN_GENERATED_KEYS);
	        }

	        int affectedRows = statement.executeUpdate();

	        if (affectedRows == 0) {
	        	jsonObject.put("Error","Creating checklist failed");
	        }

	        try {
	        	ResultSet generatedKeys = statement.getGeneratedKeys();
	        		
	        	
	            if (generatedKeys.next()) {
	            	chklist.setUserChecklistId(generatedKeys.getLong(1));
	            	
	            	if(Long.valueOf(chklist.getItemId()) != null) {
	            		Statement itemMasterStatement = con.createStatement();
	            		String queryForItemMaster = "SELECT * FROM item_master where item_master_id= "+ chklist.getItemId();
						ResultSet itemMasterResultSet = itemMasterStatement.executeQuery(queryForItemMaster);
						
						Statement imageMasterStatement = null;
						
						while(itemMasterResultSet.next()){
							itemName =  (String) itemMasterResultSet.getObject("item_name");
							imageId = (Integer) itemMasterResultSet.getObject("image_id");
							uomCode = (String) itemMasterResultSet.getObject("uom_code");
							if(imageId != null){
								imageMasterStatement = con.createStatement();
								String queryForImageMaster = "SELECT * FROM image_master where image_master_id = "+ String.valueOf(imageId);
								ResultSet imageMasterResultSet = imageMasterStatement.executeQuery(queryForImageMaster);
								
								while(imageMasterResultSet.next()){
									imageFileName = (String) imageMasterResultSet.getObject("image_file_name");
								}
							}
						}
						
	            	}
	            	
	            }
	            else {
	            	jsonObject.put("Error","Creating checklist failed").toString();
	            }
	        }catch(Exception e){
	        	return jsonObject.put("Error","Creating checklist failed").toString();
		    }
	    	        
	    }catch(Exception e){
	    	 con.close();
	    	e.printStackTrace();
	    	return jsonObject.put("Error","Creating checklist failed").toString();
	    } finally {
	    	if(con != null){
	    		con.close();
	    	}
	    }
	    con.close();

	    jsonObject.put("userCheckListId", chklist.getUserChecklistId());
	    jsonObject.put("itemId", chklist.getItemId());
	    jsonObject.put("userId", chklist.getUserId());
	    jsonObject.put("tableType", chklist.getTableType());
	    jsonObject.put("quantity", chklist.getQuantity());
	    jsonObject.put("isActive", "Y");
	    jsonObject.put("isPurchased",chklist.getIsPurchased());
	    jsonObject.put("purchaseDate","");
	    jsonObject.put("createdDate",format.format(chklist.getCreatedDate()));
	    jsonObject.put("modifiedDate",format.format(chklist.getModifiedDate()));
	    jsonObject.put("itemName", itemName);
	    jsonObject.put("uomCode", uomCode);
	    jsonObject.put("imageFileName", imageFileName);
	    
	    System.out.println("JSON after add to checklist ::::: " + jsonObject);
	    System.out.println("Exiting the create");
	    return jsonObject.toString();

	}

    @Override
    public Map<String, Object> getSearchedItems(String searchedString) {
        
        Connection con = null;
        Integer rowCount = new Integer(0);
        DbService dbService = new DbService();
        con = dbService.getConnection();
        Statement itemMasterStatement = null;
        List<SearchItemVO> searchItemList = new ArrayList<SearchItemVO>();
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        
        
        try {
            
            // Search Items amtching the pattern
            
            itemMasterStatement = con.createStatement();
            String queryForItemMaster = "SELECT * FROM item_master where item_name like '%"+searchedString +"%'";
            ResultSet itemMasterResultSet = itemMasterStatement.executeQuery(queryForItemMaster);
            
            while(itemMasterResultSet.next()) {
 
                
                SearchItemVO searchItemVO = new SearchItemVO();
                Map<String, List<OfferMasterVO>> offerMap = new HashMap<String, List<OfferMasterVO>>();
                List<OfferMasterVO> offerListForItem = new ArrayList<OfferMasterVO>();
                
                rowCount ++;
                
                // Get category of the item. As we want to find offer related to the category of the item.
                
                if((Integer) itemMasterResultSet.getObject("category_id") != null) {
                    
                    Integer categoryId = (Integer) itemMasterResultSet.getObject("category_id") ;
                    
                    // Get offers associated with the category
                    
                    Statement catrgoryOfferMappingStatement = con.createStatement();
                    String queryForCatrgoryOfferMapping = "SELECT * FROM category_offer_mapping where category_id = "+ categoryId.toString();
                    ResultSet categoryOfferMappingResultSet = catrgoryOfferMappingStatement.executeQuery(queryForCatrgoryOfferMapping);
                    
                    while(categoryOfferMappingResultSet.next()){
                        if((Integer) categoryOfferMappingResultSet.getObject("offer_id") != null) {
                            
                            OfferMasterVO offerMasterVO = new OfferMasterVO();
                            
                            Integer offerId = (Integer) categoryOfferMappingResultSet.getObject("offer_id");
                            
                            // Get details of the offer
                            
                            Statement offerMasterStatement = con.createStatement();
                            String queryForOfferMaster = "SELECT * FROM offer_master where offer_master_id = "+ offerId.toString();
                            ResultSet offerMasterResultSet = offerMasterStatement.executeQuery(queryForOfferMaster);
                            
                            if(offerMasterResultSet.next()){
                                
                                offerMasterVO.setOfferId((Integer)offerMasterResultSet.getObject("offer_master_id"));
                                offerMasterVO.setOfferName((String)offerMasterResultSet.getObject("offer_name"));
                                offerMasterVO.setImagePath((String)offerMasterResultSet.getObject("offer_image_path"));
                            }
 
                            offerListForItem.add(offerMasterVO);
                        }
                    }
                    
                    // Add the offers to the offers json object.
                    
                    offerMap.put("offers", offerListForItem);
                }
                
                searchItemVO.setItemMasterId((Integer)itemMasterResultSet.getObject("item_master_id"));
                searchItemVO.setItemName((String)itemMasterResultSet.getObject("item_name"));
                searchItemVO.setImagePath((String)itemMasterResultSet.getObject("image_path"));
                searchItemVO.setUomCode((String)itemMasterResultSet.getObject("uom_code"));
                searchItemVO.setOfferMap(offerMap);
 
                searchItemList.add(searchItemVO);
            }
 
            returnMap.put("success", "Yes");
            if(rowCount > 0) {
                returnMap.put("itemList", searchItemList);
            } else {
                returnMap.put("itemList", "No data to Display");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            
            returnMap.put("success", "No");
            returnMap.put("message", "Cannot Show the List now");
            
        }
        return returnMap;
    }

	@Override
	public String updateChecklist(Checklist chklist) throws SQLException {
		
		Connection con = null;
		DbService dbService = new DbService();
		String result="";
	    try{
	    	con = dbService.getConnection();
	        PreparedStatement statement = con.prepareStatement(SQL_UPDATE);
	        
	        statement.setString(1, chklist.getUserId());
	        statement.setLong(2, chklist.getItemId());
	        statement.setString(3, chklist.getTableType());
	        statement.setDouble(4, chklist.getQuantity());
	        statement.setString(5, chklist.getIsActive());
	        statement.setString(6, chklist.getIsPurchased());
	        statement.setLong(7, chklist.getUserChecklistId());
	        

	        int affectedRows = statement.executeUpdate();

	        if (affectedRows >= 1) {
	        	result = "Success: Updated checklist";
	        	
	        }else{
	        	result = "Error: Updating checklist failed";
	        }
    	        
	    }catch(Exception e){
	    	con.close();
	    	e.printStackTrace();
	    	return result = "Error: Updating checklist failed";
	    }
	    con.close();
	    return result = "Success: Updated checklist";

	}

	@Override
	public Map<String, Object> deleteChecklistItem(Checklist checklist) {
		
		// hard delete.
		
		Connection con = null;
		DbService dbService = new DbService();
		Statement deleteChecklistItemStatement = null;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		 try{
		    con = dbService.getConnection();
		    deleteChecklistItemStatement = con.createStatement();
		    String deleteChecklistQuery = "DELETE FROM user_checklist where user_checklist_id = " + checklist.getUserChecklistId();
		    deleteChecklistItemStatement.executeUpdate(deleteChecklistQuery);
		    
		    deleteChecklistItemStatement.close();
		    deleteChecklistItemStatement.close();
		    con.close();
		    
		    returnMap.put("success", "Y");
		    returnMap.put("message", "Records deleted successfully");
		 } catch(Exception e) {
			 try {
				con.close();
				e.printStackTrace();
				returnMap.put("success", "N");
				returnMap.put("message", "Cannot delete the Record at the moment");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			 
		 }
		 
		return returnMap;
	}

	@Override
	public Map<String, Object> getItemRecommendationForIndividualItem(ItemMaster itemMaster) {

		System.out.println("In to get reco ::::::");
		Connection con = null;
		DbService dbService = new DbService();
		Statement statement = null;
		List<String> recommendedItemsName = new ArrayList<String>();
		List<ItemMasterVO> recommendedItemsList = new ArrayList<ItemMasterVO>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			con = dbService.getConnection();
			statement = con.createStatement();
			URL url = new URL(INDIVIDUAL_ITEM_BASED_RECOMMENDATION_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", INDIVIDUAL_ITEM_BASED_RECOMMENDATION_API);
			String data = createJson(itemMaster.getItemMasterId().toString()).toString();
			
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String strTemp = "";
			String line;
			while ((line = br.readLine()) != null) {
				strTemp += line + "\n";
			}
			
			JSONObject obj = new JSONObject(strTemp);
			
			obj = obj.getJSONObject("Results");
			obj = obj.getJSONObject("output1");
			obj = obj.getJSONObject("value");
			JSONArray objArray = obj.getJSONArray("Values");
			
			System.out.println(objArray);
			
			if(objArray!=null && objArray.length()>0){
                for (int i = 0; i < objArray.length(); i++) {
                    JSONArray childJsonArray=objArray.optJSONArray(i);
                    if(childJsonArray!=null && childJsonArray.length()>0){
                        for (int j = 0; j < childJsonArray.length(); j++) {
                        	if(childJsonArray != null && childJsonArray.optString(j) != null) {
	                            int firstIndex = childJsonArray.optString(j).indexOf("{");
	                            int lastIndex = childJsonArray.optString(j).indexOf("}");
	                            String itemName = childJsonArray.optString(j).substring(firstIndex + 1, lastIndex);
	                            recommendedItemsName.add(itemName);                        		
	                        }
                        }
                    }
                }
            }
			
			for (String itemName : recommendedItemsName) {
				String itemQuery = "SELECT * FROM item_master where item_name = '" + itemName +"'";
				ResultSet recoItemResultSet = statement.executeQuery(itemQuery);
				
				while(recoItemResultSet.next()){
					
					ItemMasterVO itemMasterVO = new ItemMasterVO();
					Integer imageId = (Integer) recoItemResultSet.getObject("image_id");
					
					
					itemMasterVO.setItemMasterId( (Integer)recoItemResultSet.getObject("item_master_id"));
					itemMasterVO.setItemName((String)recoItemResultSet.getObject("item_name"));
					itemMasterVO.setItemDescription((String)recoItemResultSet.getObject("item_description"));
					itemMasterVO.setCategoryId((Integer)recoItemResultSet.getObject("category_id"));
					
					itemMasterVO.setBaseQuantity((BigDecimal)recoItemResultSet.getObject("base_quantity"));
					itemMasterVO.setMrp((BigDecimal)recoItemResultSet.getObject("mrp"));
					itemMasterVO.setPriceAfterDescount((BigDecimal)recoItemResultSet.getObject("price_after_discount"));
					itemMasterVO.setUomCode((String)recoItemResultSet.getObject("uom_code"));
					
					if(imageId != null){
						
						Map<String, Object> responseMap = new HashMap<String, Object>();
						responseMap = getImageFromImageId(imageId);
						
						itemMasterVO.setImagePath((String)responseMap.get("imagePath"));
						itemMasterVO.setImageFileName((String)responseMap.get("imageFileName"));
					}
					
					recommendedItemsList.add(itemMasterVO);
				}
			}
			returnMap.put("recommendedItems", recommendedItemsList);
		} catch (Exception e){
			e.printStackTrace();
			returnMap.put("message", "Could not recommend at the moment");
		}
		System.out.println("Exiting get reco ::::::");
		return returnMap;
	}

	private JSONObject createJson(String integer) throws JSONException {

		JSONObject jsonColumnNames = new JSONObject();
		ArrayList<ArrayList<String>> arra = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> innerArraylist = new ArrayList<String>();
		
		ArrayList<String> item1 = new ArrayList<String>();
		item1.add("item_master_id");
		
		innerArraylist.add(integer);
		arra.add(innerArraylist);
		jsonColumnNames.put("ColumnNames", item1);
		jsonColumnNames.put("Values", arra);
		
		JSONObject jsonInput1 = new JSONObject();
		jsonInput1.put("input1", jsonColumnNames);
		
		JSONObject jsonInputs = new JSONObject();
		jsonInputs.put("Inputs", jsonInput1);
		return jsonInputs;
	}

	@Override
	public Map<String, Object> getItemFromItemMaster(String userName) {

		System.out.println("In get Items service :::::::");
		Connection connection = null;
		Date lastLoggedIn = null;
		DbService dbService = new DbService();
		Statement statement = null;
		String itemMasterQuery = null;
		List<ItemMasterVO> items = new ArrayList<ItemMasterVO>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<ItemRfidBarcode> rfidbarcodeList = new ArrayList<ItemRfidBarcode>();
		
		try{
			
			// get last log in time of the user
			connection = dbService.getConnection();
			if(userName != null) {
				statement = connection.createStatement();
				String userMasterQuery = "SELECT last_log_in FROM user_master where user_id = '" + userName +"'";
				ResultSet resultSet = statement.executeQuery(userMasterQuery);
				while(resultSet.next()){
					lastLoggedIn = (Date) resultSet.getObject("last_log_in");
				}
			}
			// get all items
			
			
			Statement itemmasterStatement = connection.createStatement();
			
			if(lastLoggedIn != null){
				itemMasterQuery = "SELECT * FROM item_master where modified_date > '" + lastLoggedIn + "'";
			} else {
				itemMasterQuery = "SELECT * FROM item_master";
			}
			
			ResultSet itemmasterResultSet = itemmasterStatement.executeQuery(itemMasterQuery);
			
			while(itemmasterResultSet.next()){
				ItemMasterVO itemMasterVo = new ItemMasterVO();
				Integer imageId = (Integer) itemmasterResultSet.getObject("image_id");
				itemMasterVo.setItemMasterId((Integer) itemmasterResultSet.getObject("item_master_id"));
				itemMasterVo.setItemName((String) itemmasterResultSet.getObject("item_name"));
				itemMasterVo.setItemDescription((String) itemmasterResultSet.getObject("item_description"));
				itemMasterVo.setCategoryId((Integer) itemmasterResultSet.getObject("category_id"));
				itemMasterVo.setBaseQuantity((BigDecimal) itemmasterResultSet.getObject("base_quantity"));
				itemMasterVo.setMrp((BigDecimal) itemmasterResultSet.getObject("mrp"));
				itemMasterVo.setPriceAfterDescount((BigDecimal) itemmasterResultSet.getObject("price_after_discount"));
				itemMasterVo.setUomCode((String)itemmasterResultSet.getObject("uom_code"));
				
				if(imageId != null){
					
					Map<String, Object> responseMap = new HashMap<String, Object>();
					responseMap = getImageFromImageId(imageId);
					
					itemMasterVo.setImagePath((String)responseMap.get("imagePath"));
					itemMasterVo.setImageFileName((String)responseMap.get("imageFileName"));
				}
				items.add(itemMasterVo);
			}
			
			itemmasterResultSet.close();
			itemmasterStatement.close();
			
			// Get all rfids and barcodes.
			
			Statement statementObj = connection.createStatement();
			String rfidBarcodeQuery = "SELECT * FROM item_rfid_barcode";
			ResultSet resultSetObj = statementObj.executeQuery(rfidBarcodeQuery);
			
			while(resultSetObj.next()){
				
				ItemRfidBarcode itemRfidBarcode = new ItemRfidBarcode();
				
				itemRfidBarcode.setItemId((Integer)resultSetObj.getObject("item_id"));
				itemRfidBarcode.setRfidString((String)resultSetObj.getObject("rfid_string"));
				itemRfidBarcode.setBarcodeString((String)resultSetObj.getObject("barcode_string"));
				rfidbarcodeList.add(itemRfidBarcode);
			}
			returnMap.put("itemList", items);
			returnMap.put("itemDetails", rfidbarcodeList);
			
			resultSetObj.close();
			statementObj.close();
			connection.close();
			
		} catch(Exception e){
			e.printStackTrace();
			returnMap.put("itemList", null);
			returnMap.put("itemDetails", null);
			returnMap.put("message", "No items to display");
		} 
		System.out.println("Exiting get Items service :::::::");
		return returnMap;
	}

	@Override
	public Map<String, Object> getImageFromImageId(Integer imageId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Connection con = null;
		DbService db = new DbService();
		con = db.getConnection();
		ResultSet imageMasterResultSet = null;
		Statement imageMasterStatement = null;
		
		try {
			
		
		if(imageId != null){
			
			imageMasterStatement = con.createStatement();
			String imageMasterQuery = "SELECT * FROM image_master where image_master_id = " + imageId;
			imageMasterResultSet = imageMasterStatement.executeQuery(imageMasterQuery);
			
			while(imageMasterResultSet.next()){
				responseMap.put("imagePath", (String)imageMasterResultSet.getObject("image_path"));
				responseMap.put("imageFileName",(String)imageMasterResultSet.getObject("image_file_name"));
			}
		}
		imageMasterResultSet.close();
		imageMasterStatement.close();
		con.close();
		} catch(Exception e){
			 e.printStackTrace();
		} finally {
			try {
				if(imageMasterStatement != null){
					imageMasterStatement.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			
			try {
				if(con != null){
					con.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getImage(ImageMaster imageMaster) {
		
		System.out.println("Get Image starts ::::::");
		Connection connection = null;
		DbService dbService = new DbService();
		Statement statement = null;
		byte[] imageFile = null;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			
			connection = dbService.getConnection();
			statement = connection.createStatement();
			String imageMasterQuery = "SELECT * FROM image_master where image_file_name = '" + imageMaster.getImageFileName() + "'";
			ResultSet imageMasterResultSet = statement.executeQuery(imageMasterQuery);
			
			while(imageMasterResultSet.next()){
				imageFile = (byte[]) imageMasterResultSet.getObject("image_file");
			}
			
			if(imageFile != null){
				returnMap.put("image", imageFile);
			} else {
				returnMap.put("message", "Cannot display image at the moment");
			}
			
		} catch(Exception e){
			
			e.printStackTrace();
			returnMap.put("message", "Cannot display image at the moment");
		}
		System.out.println("Get Image ends ::::::");
		return returnMap;
	}
	
}

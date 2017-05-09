package com.iot.daoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.iot.DO.Login;
import com.iot.dao.LoginDao;
import com.iot.utility.Constants;
import com.iot.utility.DbService;

@Repository("login")
public class LoginDaoImpl implements LoginDao {

	JSONObject jsonObject = new JSONObject();

	private static final String USER_BASED_RECOMMENDATION_URL = "https://ussouthcentral.services.azureml.net/workspaces/14000d6d7471481c80c703989bd8e907/services/8b49ac7bcafc4d2e89fb2ab61c1113b7/execute?api-version=2.0&details=true"; 
	private static final String USER_BASED_RECOMMENDATION_API = "Bearer F99tQDxEVoMj/FGB/b9sh6tst9AQKYyVJutb8nn0h4lad3kQHS+0qKp2g8/QnzEeknxjZ3Z/EGlLRrVLm7fKoQ==";
//	private static final String USER_BASED_OFFER_RECOMMENDATION_URL = "https://ussouthcentral.services.azureml.net/workspaces/14000d6d7471481c80c703989bd8e907/services/0da541844947421190d34c58ff2f3df7/execute?api-version=2.0&details=true";
//	private static final String USER_BASED_OFFER_RECOMMENDATION_API = "Bearer r68yAG/0Fv4slPD2e2aIlcctAW4MQRfh0bKHC0ksLOVwo01ZumT0FEeHygYITkenQmFCzrB5YLjz28kBWK+1dw==";
	private static final String USER_BASED_OFFER_RECOMMENDATION_API = "Bearer d4adHnWevKj7mpI6qwF1o3fxS/QtMEpKzgEl4zVtnhWlqOAji9P965auibsuI6Cw+rom2kSac+wXi5uZ8STevA==";
	private static final String USER_BASED_OFFER_RECOMMENDATION_URL = "https://ussouthcentral.services.azureml.net/workspaces/14000d6d7471481c80c703989bd8e907/services/f953367abe764702bbe03e03becc236e/execute?api-version=2.0&details=true";
	
	@Override
	public String login(Login objLogin) {
		
		System.out.println("In the Login Service");
		JSONObject jsonObject = new JSONObject();
	
		Connection con = null;
		Integer imageId = null;
		Statement stmt = null;
		String itemImageFileName = null;
		Statement imageMasterStatement = null;
		DbService dbService = new DbService();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement stmtOffer = null;
		Statement stmtChecklist = null;
		try {

			String jsonResponse = null;

			con = dbService.getConnection();

			String userName = objLogin.getUserName();
			String password = objLogin.getPassword();

			stmt = con.createStatement();

			String queryForLogin = "SELECT * FROM user_master WHERE user_id = '"
					+ userName + "' AND password = '" + password + "'";

			ResultSet resultSet = stmt.executeQuery(queryForLogin);
			JSONArray offerArr = new JSONArray();
			JSONArray checklistArr = new JSONArray();
			if (!resultSet.next()){
				jsonObject.put("status", Constants.RESPONSE_CODE_FAILURE);
				jsonObject.put("message", Constants.INVALID_USERNAME_PASSWORD);
				jsonResponse = jsonObject.toString();
				System.out.println("Response = " + jsonResponse);
				return jsonResponse;
			} else {
				ArrayList<Long> itemIdList = new ArrayList<Long>();
				  //display results
				do {
					
					// Update the user_master table and set last log in = current time
					
					Statement userMasterStatement = con.createStatement();
					String userMasterUpdateQuery = "UPDATE user_master SET last_log_in = '" + format.format(new Date()) +"' where user_id = '" + userName + "'";
//					userMasterStatement.executeUpdate(userMasterUpdateQuery);
					
					String user_id = resultSet.getString("user_id");
					String firstName = resultSet.getString("first_name");
					String middleName =  resultSet.getString("middle_name");
					String lastName = resultSet.getString("last_name");
					String emailId = resultSet.getString("email_id");
					Integer imagePath = (Integer)resultSet.getObject("image_id");
					String walletBalance = resultSet.getString("wallet_balance");
					
					JSONObject innerJsonObject = new JSONObject();
					
					
					
					innerJsonObject.put("userId", user_id);						
					innerJsonObject.put("firstName", firstName!=null?firstName:"");						
					innerJsonObject.put("middleName",middleName!=null?middleName:"");					
					innerJsonObject.put("lastName", lastName!=null?lastName:"");					
					innerJsonObject.put("emailId", emailId!=null?emailId:"");
					innerJsonObject.put("imagePath", imagePath!=null?imagePath:"");
					innerJsonObject.put("walletBalance", walletBalance!=null?walletBalance:"");
					

					
					/*stmtOffer = con.createStatement();
					String queryForOfferList = "SELECT * FROM offer_master ";
					ResultSet offerResultSet = stmtOffer.executeQuery(queryForOfferList);
	
					while (offerResultSet.next()) {
						JSONObject jsonObjectArrayObj = new JSONObject();
						String offerId = offerResultSet.getString("offer_master_id");
						String offerName = offerResultSet.getString("offer_name");
						Integer offerImageId = (Integer) offerResultSet.getObject("image_id");
						
						String imageFileName = getImageFileName(offerImageId);
//						if(offerImageId != null){
//							Statement stmtOfferImage = con.createStatement();
//							String queryForOfferImage = "SELECT * FROM image_master where image_master_id = '" + offerImageId + "'";
//							ResultSet offerImageSet = stmtOfferImage.executeQuery(queryForOfferImage);
//							while(offerImageSet.next()){
//								imageName = (String) offerImageSet.getObject("image_file_name");
//							}
//						}
						
						jsonObjectArrayObj.put("offerId",offerId!=null?offerId:"");
						jsonObjectArrayObj.put("offerName",offerName!=null?offerName:"");
						jsonObjectArrayObj.put("imageFileName",imageFileName);
						offerArr.put(jsonObjectArrayObj);
						
					}*/
					
					
					stmtOffer = null;
//					offerResultSet = null;
//					jsonObject.put("Offers", offerArr);					
					
					stmtChecklist = con.createStatement();
					
					
					// Recommend Offers  depending upon the  user 
					
					// Get latest added item from userChecklist
					
					String getOneItemFromChecklist = "select * from user_checklist where user_id = " + userName + " ORDER BY (user_checklist_id) ASC  LIMIT 1";
					ResultSet oneItemResultSet = stmtChecklist.executeQuery(getOneItemFromChecklist);
					Integer oneItemId = null;
					while(oneItemResultSet.next()){
						oneItemId = (Integer) oneItemResultSet.getObject("item_id");
					}
					JSONArray offersArray = null;
					if(oneItemId != null){
						offersArray = getUserBasedOffers(userName, oneItemId.toString());
					} else {
						offersArray = getUserBasedOffers(userName, String.valueOf(17));
					}
					 
					
					// Show item in the checklist if the item is active and it is not purchased yet or it is purchased but purchased today only.
					// That means if an item is purchased, we will show that item in the checklist only for the day on which it is purchased
					
					String queryForChecklist = "SELECT * FROM user_checklist where user_id= "+user_id + " and is_purchased='N'";
					ResultSet checklistResultSet = stmtChecklist.executeQuery(queryForChecklist);
					
					

						while (checklistResultSet.next()) {
							JSONObject jsonObjectArrayObj = new JSONObject();
							long userCheckListId = (Long)checklistResultSet.getLong("user_checklist_id")!=null?checklistResultSet.getLong("user_checklist_id"):null;
							String userId = checklistResultSet.getString("user_id");
							long itemId = (Long)checklistResultSet.getLong("item_id")!=null?checklistResultSet.getLong("item_id"):null;
							String tableType = checklistResultSet.getString("table_type");
							String itemName = null;
							String unit = null;
							Statement itemMasterStatement = con.createStatement();
							if(Long.valueOf(itemId) != null){
								String queryForItemMaster = "SELECT * FROM item_master where item_master_id= "+ String.valueOf(itemId);
								ResultSet itemMasterResultSet = itemMasterStatement.executeQuery(queryForItemMaster);
								
								while(itemMasterResultSet.next()){
									itemName = (String) itemMasterResultSet.getObject("item_name");
									imageId = (Integer) itemMasterResultSet.getObject("image_id");
									unit = (String) itemMasterResultSet.getObject("uom_code");
									if(imageId != null){
										imageMasterStatement = con.createStatement();
										String queryForImageMaster = "SELECT * FROM image_master where image_master_id = "+ String.valueOf(imageId);
										ResultSet imageMasterResultSet = imageMasterStatement.executeQuery(queryForImageMaster);
										
										while(imageMasterResultSet.next()){
											itemImageFileName = (String) imageMasterResultSet.getObject("image_file_name");
										}
									}
								}
								
								if(itemName != null){
									jsonObjectArrayObj.put("itemName", itemName);
								} else{
									jsonObjectArrayObj.put("itemName", "");
								}
								
								if(itemImageFileName != null){
									jsonObjectArrayObj.put("imageFileName", itemImageFileName);
								} else{
									jsonObjectArrayObj.put("imageFileName", "");
								}
								
								if(unit != null){
									jsonObjectArrayObj.put("uomCode", unit);
								} else{
									jsonObjectArrayObj.put("uomCode", "");
								}
							}
							
							double quantity = (Double)checklistResultSet.getDouble("quantity")!=null?checklistResultSet.getDouble("quantity"):null;
							String isActive = checklistResultSet.getString("is_Active");							
							String isPurchased = checklistResultSet.getString("is_purchased");
							Date purchaseDate = (Date) checklistResultSet.getObject("purchase_date");
							Date createdDate = (Date) checklistResultSet.getObject("created_date");
							Date modifiedDate = (Date) checklistResultSet.getObject("modified_date");
							
							jsonObjectArrayObj.put("userCheckListId",userCheckListId);
							jsonObjectArrayObj.put("userId",userId!=null?userId:"");
							jsonObjectArrayObj.put("itemId",itemId);
							jsonObjectArrayObj.put("tableType",tableType!=null?tableType:"");
							jsonObjectArrayObj.put("quantity",quantity);
							jsonObjectArrayObj.put("isActive",isActive!=null?isActive:"");							
							jsonObjectArrayObj.put("isPurchased",isPurchased!=null?isPurchased:"");
							
							if(purchaseDate != null){
								jsonObjectArrayObj.put("purchaseDate",format.format(purchaseDate));
							} else {
								jsonObjectArrayObj.put("purchaseDate","");
							}
							if(createdDate != null){
								jsonObjectArrayObj.put("createdDate",format.format(createdDate));
							} else {
								jsonObjectArrayObj.put("createdDate","");
							}
							if(modifiedDate != null){
								jsonObjectArrayObj.put("modifiedDate",format.format(modifiedDate));
							} else {
								jsonObjectArrayObj.put("modifiedDate","");
							}
							checklistArr.put(jsonObjectArrayObj);							
						}
					
						// Get the entire user checklist for item recommendation
						
						String queryForCheckForRecolist = "SELECT * FROM user_checklist where user_id= "+user_id;
						ResultSet checklistForRecoResultSet = stmtChecklist.executeQuery(queryForCheckForRecolist);
						
						while (checklistForRecoResultSet.next()) {
							long itemId = (Long)checklistForRecoResultSet.getLong("item_id")!=null?checklistForRecoResultSet.getLong("item_id"):null;
							itemIdList.add(itemId);
						}
						
//						JSONArray recoItemArrray  = getRecommendedItemsUsingMl(userName, itemIdList);	
					
					checklistResultSet = null;
					stmtChecklist = null;
					jsonObject.put("Checklist", checklistArr);
					jsonObject.put("Userdetails", innerJsonObject); 
//					jsonObject.put("RecommendedItems", recoItemArrray);
					stmtOffer = null;
//					offerResultSet = null;
					jsonObject.put("Offers", offersArray);	
					
					
				}while (resultSet.next());
				jsonObject.put("status", Constants.RESPONSE_CODE_SUCCESS);
				resultSet=null;
				stmt=null;
			}

		//	StreamUtility.objReadWriteLock.readLock().lock();
			
			
			
			
			
		//	StreamUtility.objReadWriteLock.readLock().unlock();

			System.out.println("Json Obj: " + jsonObject);
		} catch (Exception ex) {
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
		
		System.out.println("Exiting the Login Service");
		return jsonObject.toString();

	}

	private String getImageFileName(Integer imageMasterId) throws Exception{

		DbService dbService = new DbService();
		String imageFileName = null;
		Statement stmtOfferImage = null;
		ResultSet offerImageSet = null;
		Connection con = null;
		try{
			con = dbService.getConnection();
			
			if(imageMasterId != null){
				stmtOfferImage = con.createStatement();
				String queryForOfferImage = "SELECT * FROM image_master where image_master_id = '" + imageMasterId + "'";
				offerImageSet = stmtOfferImage.executeQuery(queryForOfferImage);
				while(offerImageSet.next()){
					imageFileName = (String) offerImageSet.getObject("image_file_name");
				}
			}
			
			offerImageSet.close();
			stmtOfferImage.close();
			con.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return imageFileName;
	}

	private JSONArray getUserBasedOffers(String userName, String itemId) throws IOException, JSONException {


		Connection con = null;
		DbService dbService = new DbService();
		JSONArray array = new JSONArray();
		try{
			
			con = dbService.getConnection();
//			
			String data = createJsonForUserBasedOffers(userName, itemId).toString();
			ArrayList<String> categoryIds = new ArrayList<String>();
			URL url = new URL(USER_BASED_OFFER_RECOMMENDATION_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", USER_BASED_OFFER_RECOMMENDATION_API);
			
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
//			String strTemp = new String ("{\"Results\":{\"output1\":{\"type\":\"table\",\"value\":{\"ColumnNames\":[\"category_id\",\"cat4\",\"cat1\",\"cat2\",\"cat3\"],\"ColumnTypes\":[\"Int32\",\"Int32\",\"Int32\",\"Int32\",\"Int32\"],\"Values\":[[\"3\",\"8\",\"5\",\"9\",\"13\"]]}}}}");
			String strTemp = "";
			String line;
			while ((line = br.readLine()) != null) {
				strTemp += line + "\n";
			}
			
			JSONObject obj = new JSONObject(strTemp);
			
			System.out.println(strTemp);
			
			obj = obj.getJSONObject("Results");
			obj = obj.getJSONObject("output1");
			obj = obj.getJSONObject("value");
			JSONArray objArray = obj.getJSONArray("Values");
			
			if(objArray!=null && objArray.length()>0){
	            for (int i = 0; i < objArray.length(); i++) {
	                JSONArray childJsonArray=objArray.optJSONArray(i);
	                if(childJsonArray!=null && childJsonArray.length()>0){
	                    for (int j = 0; j < childJsonArray.length(); j++) {
	                    	if(childJsonArray != null && childJsonArray.optString(j) != null) {
	//                            int firstIndex = childJsonArray.optString(j).indexOf("{");
	//                            int lastIndex = childJsonArray.optString(j).indexOf("}");
	//                            String itemName = childJsonArray.optString(j).substring(firstIndex + 1, lastIndex);
	//                            System.out.println(itemName);
	//                    		recommendedItems.add((String) childJsonArray.get(1));
//	                    		if(!categoryIds.contains(childJsonArray.optString(j))){
	                    			categoryIds.add(childJsonArray.optString(j));
//	                    		}
	                        }
	                    }
	                }
	            }
	        }
			
			// get Offer List related to the categories
			for (String categoryId : categoryIds) {
				
			
				if(categoryId != null) {
		            
		            // Get offers associated with the category
		            
		            Statement catrgoryOfferMappingStatement = con.createStatement();
		            String queryForCatrgoryOfferMapping = "SELECT * FROM category_offer_mapping where category_id = "+ categoryId;
		            ResultSet categoryOfferMappingResultSet = catrgoryOfferMappingStatement.executeQuery(queryForCatrgoryOfferMapping);
		            
		            
		            while(categoryOfferMappingResultSet.next()){
		                if((Integer) categoryOfferMappingResultSet.getObject("offer_id") != null) {
		                    
		                    JSONObject jsonObj = new JSONObject();
		                    
		                    Integer offerId = (Integer) categoryOfferMappingResultSet.getObject("offer_id");
		                    
		                    // Get details of the offer
		                    
		                    Statement offerMasterStatement = con.createStatement();
		                    String queryForOfferMaster = "SELECT * FROM offer_master where offer_master_id = "+ offerId.toString();
		                    ResultSet offerMasterResultSet = offerMasterStatement.executeQuery(queryForOfferMaster);
		                    
		                    if(offerMasterResultSet.next()){
		                    	jsonObj.put("offerId", (Integer)offerMasterResultSet.getObject("offer_master_id"));
		                    	jsonObj.put("offerName", (String)offerMasterResultSet.getObject("offer_name"));
		                    	jsonObj.put("imageFileName", getImageFileName((Integer) offerMasterResultSet.getObject("image_id")));
		                    }
		
		                    array.put(jsonObj);
		                }
		            }
		        }
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	private JSONObject createJsonForUserBasedOffers(String userName, String itemId) throws JSONException {

		JSONObject jsonColumnNames = new JSONObject();
		ArrayList<ArrayList<String>> arra = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("user_id");
		columnNames.add("item_id");
		
		ArrayList<String> innerArraylist = new ArrayList<String>();
		innerArraylist.add(userName);
		innerArraylist.add(itemId);
		arra.add(innerArraylist);
		
		jsonColumnNames.put("ColumnNames", columnNames);
		jsonColumnNames.put("Values", arra);
		
		JSONObject jsonInput1 = new JSONObject();
		jsonInput1.put("input1", jsonColumnNames);
		
		JSONObject jsonInputs = new JSONObject();
		jsonInputs.put("Inputs", jsonInput1);
		
		return jsonInputs;
	}

	@SuppressWarnings("unused")
	private JSONArray getRecommendedItemsUsingMl(String userName, ArrayList<Long> itemIdList) {
		
		Connection con = null;
		Statement stmt = null;
		JSONArray recommendedItemsArray = new JSONArray();
		ArrayList<String> itemNames = new ArrayList<String>();
		ArrayList<String> recommendedItems = new ArrayList<String>();
		DbService dbService = new DbService();
		try{
			
			for (Long itemId : itemIdList) {
				con = dbService.getConnection();
				stmt = con.createStatement();
				String itemQuery = "SELECT * FROM item_master where item_master_id = " + itemId;
				ResultSet rs = stmt.executeQuery(itemQuery);
				
				while(rs.next()){
					if((String)rs.getObject("item_name") != null){
						itemNames.add((String)rs.getObject("item_name"));
					}
				}
			}
			
			String data = createJson(userName, itemIdList).toString();

			URL url = new URL(USER_BASED_RECOMMENDATION_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", USER_BASED_RECOMMENDATION_API);
			
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
//	                            int firstIndex = childJsonArray.optString(j).indexOf("{");
//	                            int lastIndex = childJsonArray.optString(j).indexOf("}");
//	                            String itemName = childJsonArray.optString(j).substring(firstIndex + 1, lastIndex);
//	                            System.out.println(itemName);
                        		recommendedItems.add((String) childJsonArray.get(1));                        		
	                        }
                        }
                    }
                }
            }
			
			for (String itemId : recommendedItems) {
				String itemQuery = "SELECT * FROM item_master where item_master_id = " + itemId;
				ResultSet recoItemResultSet = stmt.executeQuery(itemQuery);
				
				while(recoItemResultSet.next()){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("itemMasterId", (Integer)recoItemResultSet.getObject("item_master_id"));
					jsonObj.put("itemName", (String)recoItemResultSet.getObject("item_name"));
					jsonObj.put("itemDescription", (String)recoItemResultSet.getObject("item_description"));
					jsonObj.put("categoryId", (Integer)recoItemResultSet.getObject("category_id"));
					jsonObj.put("imagePath", (String)recoItemResultSet.getObject("image_path"));
					jsonObj.put("baseQuantity", (BigDecimal)recoItemResultSet.getObject("base_quantity"));
					jsonObj.put("mrp", (BigDecimal)recoItemResultSet.getObject("mrp"));
					jsonObj.put("priceAfterDiscount", (BigDecimal)recoItemResultSet.getObject("price_after_discount"));
					jsonObj.put("uomCode", (String)recoItemResultSet.getObject("uom_code"));
					recommendedItemsArray.put(jsonObj);
				}
			}
		} catch(Exception e){
			
		}
		return recommendedItemsArray;
	}

	private JSONObject createJson(String userName,ArrayList<Long> itemIdList) throws JSONException {
		JSONObject jsonColumnNames = new JSONObject();
		ArrayList<ArrayList<Object>> arra = new ArrayList<ArrayList<Object>>();
		
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("UserId");
		columnNames.add("MovieId");
		columnNames.add("Rating");
		columnNames.add("Timestamp");
		
		for (Long itemId : itemIdList) {
			ArrayList<Object> innerArraylist = new ArrayList<Object>();
			innerArraylist.add(userName);
			innerArraylist.add(itemId.toString());
			innerArraylist.add(new Integer(0).toString());
			innerArraylist.add(new Integer(0).toString());
			arra.add(innerArraylist);
		}
		
		jsonColumnNames.put("ColumnNames", columnNames);
		jsonColumnNames.put("Values", arra);
		
		JSONObject jsonInput1 = new JSONObject();
		jsonInput1.put("input1", jsonColumnNames);
		
		JSONObject jsonInputs = new JSONObject();
		jsonInputs.put("Inputs", jsonInput1);
		
		return jsonInputs;
	}
}

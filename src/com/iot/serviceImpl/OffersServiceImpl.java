package com.iot.serviceImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.iot.service.OffersService;
import com.iot.utility.DbService;

@Service
public class OffersServiceImpl implements OffersService{
	private static final String SQL_SELECT = " SELECT distinct a.* FROM smart_cart.offer_master a, smart_cart.category_master b, "+ 
			" smart_cart.item_master c, smart_cart.category_offer_mapping d, smart_cart.user_checklist e "+
			" where a.offer_master_id = d.offer_id and "+
			" b.category_master_id = c.category_id and "+ 
			" c.category_id = d.category_id and "+
			" b.category_master_id = d.category_id and "+
			" e.user_id= ";
	@Override
	public JSONArray getOffersList(String user_id) throws SQLException {
		JSONArray offerslistArr = new JSONArray();
		Connection con = null;
		DbService dbService = new DbService();

		Statement stmtOffer = null;
		try {


			con = dbService.getConnection();
			stmtOffer = con.createStatement();
					String queryForOfferList = SQL_SELECT + user_id;
					
					
					ResultSet offerResultSet = stmtOffer.executeQuery(queryForOfferList);

						while (offerResultSet.next()) {
							JSONObject jsonObjectArrayObj = new JSONObject();
							jsonObjectArrayObj.put("offerId",offerResultSet.getString("offer_master_id"));
							jsonObjectArrayObj.put("offerName",offerResultSet.getString("offer_name"));
							jsonObjectArrayObj.put("offerImagePath",offerResultSet.getString("offer_image_path"));
							offerslistArr.put(jsonObjectArrayObj);							
						}
					
					offerResultSet = null;
					stmtOffer = null;											
					
			}catch (Exception ex) {
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
			return offerslistArr;
	}

}

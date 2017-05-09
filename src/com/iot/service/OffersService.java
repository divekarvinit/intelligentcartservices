package com.iot.service;

import java.sql.SQLException;

import org.json.JSONArray;

public interface OffersService {
	public JSONArray getOffersList(String user_id)throws SQLException;
}

package com.iot.service;

import java.util.Map;

import com.iot.DO.UserDetails;

public interface CartTranscationService {

	public Map<String, Object> getTransactionHistory(UserDetails userDetails);

}

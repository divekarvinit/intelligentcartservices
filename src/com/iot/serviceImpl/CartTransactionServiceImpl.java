package com.iot.serviceImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.iot.DO.UserDetails;
import com.iot.service.CartTranscationService;
import com.iot.utility.DbService;
import com.iot.vo.UserTransactionHistoryVO;

@Service
public class CartTransactionServiceImpl implements CartTranscationService {

	@Override
	public Map<String, Object> getTransactionHistory(UserDetails userDetails) {

		Connection con = null;
        DbService dbService = new DbService();
        List<UserTransactionHistoryVO> transactionHistory = new ArrayList<UserTransactionHistoryVO>();
        Map<String, Object> returnmap = new HashMap<String, Object>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try{
	        con = dbService.getConnection();
	        
	        Statement userCartStatement = con.createStatement();
	        
	        // get transaction history
	        
	        String userTransactionHistoryQuery = "SELECT * FROM user_transaction_history where user_id = '"+ userDetails.getUserId() +"' LIMIT 5";
	        ResultSet transactionResultSet = userCartStatement.executeQuery(userTransactionHistoryQuery);
	        
	        while(transactionResultSet.next()){
	        	UserTransactionHistoryVO userTransactionHistoryVO = new UserTransactionHistoryVO();
	        	Date transactionDate = (Date) transactionResultSet.getObject("transaction_date");
	        	
	        	userTransactionHistoryVO.setUserTransactionHistoryId((Integer) transactionResultSet.getObject("user_transaction_history_id"));
	        	userTransactionHistoryVO.setUserId((String) transactionResultSet.getObject("user_id"));
	        	userTransactionHistoryVO.setTransactionAmount((BigDecimal) transactionResultSet.getObject("transaction_amount"));
	        	if(transactionDate != null){
	        		userTransactionHistoryVO.setTransactionDate(format.format(transactionDate));
	        	}
	        	
	        	transactionHistory.add(userTransactionHistoryVO);
	        }
	        
	        // Get wallet balance
	        
	        Statement userDetailsStatement = con.createStatement();
	        String userMasterQuery = "SELECT * FROM user_master where user_id = '"+ userDetails.getUserId() +"'";
	        ResultSet userDetailsResultSet = userDetailsStatement.executeQuery(userMasterQuery);
	        
	        while(userDetailsResultSet.next()){
	        	returnmap.put("walletBalance", (BigDecimal) userDetailsResultSet.getObject("wallet_balance"));
	        }
	        
	        returnmap.put("transactionHistory", transactionHistory);
        } catch(Exception e){
        	
        	e.printStackTrace();
        	returnmap.put("message", "Transaction history cannot be fetched at the moment");
        }
        
		return returnmap;
	}

}

package com.iot.vo;

import java.math.BigDecimal;

public class UserTransactionHistoryVO {

	private Integer userTransactionHistoryId;
	private String userId;
	private BigDecimal transactionAmount;
	private String transactionDate;
	
	public Integer getUserTransactionHistoryId() {
		return userTransactionHistoryId;
	}
	public void setUserTransactionHistoryId(Integer userTransactionHistoryId) {
		this.userTransactionHistoryId = userTransactionHistoryId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}

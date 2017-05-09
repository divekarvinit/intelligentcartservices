package com.iot.DO;

import java.math.BigDecimal;
import java.util.Date;

public class UserTransactionHistory {

	private Integer userTransactionHistoryId;
	private Integer userId;
	private BigDecimal transactionAmount;
	private Date transactionDate;
	
	public UserTransactionHistory() {
		super();
	}
	public UserTransactionHistory(Integer userTransactionHistoryId, Integer userId, BigDecimal transactionAmount,
			Date transactionDate) {
		super();
		this.userTransactionHistoryId = userTransactionHistoryId;
		this.userId = userId;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
	}
	public Integer getUserTransactionHistoryId() {
		return userTransactionHistoryId;
	}
	public void setUserTransactionHistoryId(Integer userTransactionHistoryId) {
		this.userTransactionHistoryId = userTransactionHistoryId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}

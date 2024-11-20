package com.ssafy.cafe.model.dto;

import java.util.Date;

public class Order {
	private Long orderId;
	private Long userId;
	private int totalPrice;
	private String orderStatus; // 'Pending', 'Completed', 'Canceled'
	private Date createdAt;

	public Order(Long orderId, Long userId, int totalPrice, String orderStatus, Date createdAt) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", userId=" + userId + ", totalPrice=" + totalPrice + ", orderStatus="
				+ orderStatus + ", createdAt=" + createdAt + "]";
	}

}

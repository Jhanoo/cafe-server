package com.ssafy.cafe.controller.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.User;
import com.ssafy.cafe.model.service.OrderDetailService;
import com.ssafy.cafe.model.service.OrderService;
import com.ssafy.cafe.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
	private UserService userService;

	@PostMapping
	@Operation(summary = "order 객체를 저장하고 추가된 Order의 id를 반환한다."
		, description = "<pre>아래 형태로 입력하면 주문이 입력된다. \r\n"
				+ "{\r\n" 
				+ "  \"userId\": \"ssaf\",\r\n" 
				+ "  \"totalPrice\": \"4000\",\r\n"
				+ "  \"orderStatus\": \"Pending\",\r\n" 
				+ "} " 
				+ "</pre>")
	public ResponseEntity<Long> insertOrder(@RequestBody Order order) {
		logger.debug("insertOrder", order);
		orderService.insertOrder(order);
		
		int stamps = userService.getStamps(order.getUserId());
		int points = userService.getPoints(order.getUserId());
		
		List<OrderDetail> details = order.getDetails();
		for(OrderDetail detail : details) {
			stamps += detail.getQuantity();
		}
		
		points += (stamps / 10) * 2000;
		stamps = stamps % 10;
		
		userService.updatePoints(order.getUserId(), points);
		userService.updateStamps(order.getUserId(), stamps);
		
		return ResponseEntity.ok(order.getOrderId());
	}

	@GetMapping
	public ResponseEntity<List<Order>> getOrdersByUserId(Long userId) {
		List<Order> orders = orderService.getOrdersByUserId(userId);

		if (orders != null) {
			for (Order order : orders) {
				List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(order.getOrderId());
				order.setDetails(details);
			}
		}

		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
		Order order = orderService.getOrderById(orderId);

		if (order != null) {
			List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(order.getOrderId());
			order.setDetails(details);
		}

		return ResponseEntity.ok(order);
	}

	@PutMapping("/{orderId}/status")
	public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, String status) {
		orderService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{orderId}/detail")
	public ResponseEntity<List<OrderDetail>> getOrderDetailByOrderId(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
	}

	@PostMapping("/{orderId}/detail")
	public ResponseEntity<Void> insertOrderDetail(@RequestBody OrderDetail orderDetail) {
		orderDetailService.insertOrderDetail(orderDetail);
		return ResponseEntity.ok().build();
	}

}

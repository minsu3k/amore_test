package com.amore.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amore.test.dto.request.OrderRequest;
import com.amore.test.dto.response.OrderResponse;
import com.amore.test.exception.OrderError;
import com.amore.test.service.OrderService;
import com.amore.test.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = {"주문 관리 API"})
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping(value = "/receipt")
	@ResponseBody
	@ApiOperation(value = "주문 접수", notes = "주문을 접수한다.")
	public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
		OrderResponse orderResponse;
		orderResponse = request.validate();
		if(ObjectUtils.isEmpty(orderResponse.getError())) {
			orderResponse = orderService.createOrder(request);//TODO 작업해야됨
		}
		log.info("order receipt");
		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/check")
	@ResponseBody
	@ApiOperation(value = "주문 상태 조회", notes = "주문 상태를 조회한다.")
	public ResponseEntity<OrderResponse> checkOrderStatus(@RequestBody OrderRequest request) {
		log.info("order status check");
		OrderResponse orderResponse;
		if(CommonUtil.isNullOrEmpty(request.getOrderNumber())){
			orderResponse = OrderResponse.builder()
										 .error(OrderError.NOT_EXIST_ORDER_NO.getMessage())
										 .build();
		}else {
			orderResponse = orderService.getOrder(request);
		}
		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/cancel")
	@ResponseBody
	@ApiOperation(value = "주문 취소", notes = "주문을 취소한다.")
	public ResponseEntity<OrderResponse> orderCancel(@RequestBody OrderRequest request) {
		log.info("order cancel");
		OrderResponse orderResponse;
		if(CommonUtil.isNullOrEmpty(request.getOrderNumber())){
			orderResponse = OrderResponse.builder()
										 .error(OrderError.NOT_EXIST_ORDER_NO.getMessage())
										 .build();
		}else {
			orderResponse = orderService.cancelOrder(request);
		}
		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
	}

}

package com.amore.test.service;

import com.amore.test.dto.request.OrderRequest;
import com.amore.test.dto.response.OrderResponse;

public interface OrderService {

	OrderResponse createOrder(OrderRequest request);

	OrderResponse getOrder(OrderRequest request);

	OrderResponse cancelOrder(OrderRequest request);

	void update(OrderRequest orderRequest);

	void sendProduct();

	void setReadyToSend();

}

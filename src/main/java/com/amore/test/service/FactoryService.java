package com.amore.test.service;

import com.amore.test.dto.request.OrderRequest;

public interface FactoryService {

	void materialSupplement(String type);

	void makeProduct(OrderRequest orderRequest);
	//void makeProduct(String orderNumber);

	boolean checkMaterialAmount(String orderCode);

}

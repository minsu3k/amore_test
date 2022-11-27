package com.amore.test.service;

import java.util.List;

import com.amore.test.dto.request.StockRequest;
import com.amore.test.dto.response.StockResponse;
import com.amore.test.entity.Stock;

public interface StockService {

	List<Stock> checkStockStatus();

	StockResponse setStock(StockRequest stockRequest);

}

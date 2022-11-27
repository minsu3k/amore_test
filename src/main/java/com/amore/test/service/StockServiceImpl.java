package com.amore.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amore.test.dto.request.StockRequest;
import com.amore.test.dto.response.StockResponse;
import com.amore.test.entity.Stock;
import com.amore.test.mapper.StockMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

	@Autowired
	private StockMapper stockMapper;
	
	@Override
	public List<Stock> checkStockStatus() {
		return stockMapper.getStatus();
	}

	@Override
	public StockResponse setStock(StockRequest stockRequest) {
		Stock stockInfo = stockRequest.toStock();
		log.info(stockInfo.toString());
		stockMapper.update(stockInfo);
		Stock stock = stockMapper.getByType(stockRequest.getType());
		
		return StockResponse.builder()
							.message("stock updated")
							.type(stock.getType())
							.quantity(stock.getQuantity())
							.build();
	}

}

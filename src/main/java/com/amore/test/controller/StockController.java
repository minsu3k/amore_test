package com.amore.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amore.test.dto.request.StockRequest;
import com.amore.test.dto.response.StockResponse;
import com.amore.test.entity.Stock;
import com.amore.test.service.StockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/stock")
@Api(tags = {"재고 관리 API"})
public class StockController {
	
	@Autowired
	private StockService stockService;
	
	@GetMapping(value = "/check")
	@ApiOperation(value = "전체 재고 상태 조회", notes = "전체 재고 상태를 조회한다.")
	@ResponseBody
	public ResponseEntity<List<Stock>> checkStockStatus() {
		log.info("check stock status");
		return new ResponseEntity<List<Stock>>(stockService.checkStockStatus(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/add")
	@ApiOperation(value = "재고 입력", notes = "재고 보유량을 입력한다.")
	@ResponseBody
	public ResponseEntity<StockResponse> setStock(@RequestBody StockRequest stockRequest) {
		log.info("set Stock : {}", stockRequest.toString());
		StockResponse stockResponse = stockRequest.validate();
		if(ObjectUtils.isEmpty(stockResponse.getError())) {
			stockResponse = stockService.setStock(stockRequest);
		}
		return new ResponseEntity<StockResponse>(stockResponse, HttpStatus.OK);
	}
}

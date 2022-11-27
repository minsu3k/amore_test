package com.amore.test.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amore.test.dto.request.OrderRequest;
import com.amore.test.dto.response.OrderResponse;
import com.amore.test.entity.Material;
import com.amore.test.entity.Order;
import com.amore.test.entity.Stock;
import com.amore.test.mapper.MaterialMapper;
import com.amore.test.mapper.OrderMapper;
import com.amore.test.mapper.StockMapper;
import com.amore.test.model.FactoryStatus;
import com.amore.test.model.OrderStatus;
import com.amore.test.util.CommonUtil;
import com.amore.test.util.FactoryUtil;
import com.amore.test.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FactoryServiceImpl implements FactoryService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private MaterialMapper materialMapper;
	
	@Autowired
	private StockMapper stockMapper;
	
	@Override
	public void materialSupplement(String type) {
		Stock stock = stockMapper.getByType(type);
		int supplementQuantity = stock.getQuantity() > 200 ? 200 : stock.getQuantity();
		if(stock.getQuantity()== 0) {
			log.info("{} 재고 없음. 재고 보충 필요", type);
		}else {
			stock.setQuantity(stock.getQuantity() - supplementQuantity);
			stockMapper.update(stock);
		}
		
		Material material = materialMapper.getByType(type);
		material.setQuantity(supplementQuantity);
		materialMapper.update(material);
		TimeUtil.plusMinutes(40);
		FactoryUtil.setFactoryStatus(FactoryStatus.READY);
	}
	
	@Override
	public void makeProduct(OrderRequest orderRequest) {
		List<Material> materialList = new ArrayList<Material>();
		if(orderRequest.getOrderCode().lastIndexOf("10") == -1) {
			orderRequest.setEfficacyType1(orderRequest.getOrderCode().substring(0, 1));
			orderRequest.setProportion1(orderRequest.getOrderCode().substring(1, 2));
			orderRequest.setEfficacyType2(orderRequest.getOrderCode().substring(2, 3));
			orderRequest.setProportion2(orderRequest.getOrderCode().substring(3,4));
		}else {
			orderRequest.setEfficacyType1(orderRequest.getOrderCode().substring(0, 1));
			orderRequest.setProportion1(orderRequest.getOrderCode().substring(1, 3));
		}
		
		Material firstMaterial = materialMapper.getByType(orderRequest.getEfficacyType1());
		firstMaterial.setQuantity(firstMaterial.getQuantity() - Integer.parseInt(orderRequest.getProportion1()));
		materialList.add(firstMaterial);
		if(CommonUtil.isNotNullAndEmpty(orderRequest.getEfficacyType2())) {
			Material secondMaterial = materialMapper.getByType(orderRequest.getEfficacyType2());
			secondMaterial.setQuantity(secondMaterial.getQuantity() - Integer.parseInt(orderRequest.getProportion2()));
			materialList.add(secondMaterial);
		}
		log.info("materialList size : {}", materialList.size());
		for(Material material : materialList) {
			materialMapper.update(material);
		}
	}
/*
	@Override
	public void makeProduct(String orderNumber) {
	//public void makeProduct(OrderRequest request) {
		List<Material> materialList = new ArrayList<Material>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderNumber(orderNumber);
		log.info("orderNumber : {}", orderNumber);
		Order order = ordermapper.getOrder(orderNumber);
		log.info(order.toString());
		String orderCode = order.getOrderCode();
		if(orderCode.lastIndexOf("10") == -1) {
			orderRequest.setEfficacyType1(orderCode.substring(0, 1));
			orderRequest.setProportion1(orderCode.substring(1, 2));
			orderRequest.setEfficacyType2(orderCode.substring(2, 3));
			orderRequest.setProportion2(orderCode.substring(3,4));
		}else {
			orderRequest.setEfficacyType1(orderCode.substring(0, 1));
			orderRequest.setProportion1(orderCode.substring(1, 3));
		}
		log.info(orderRequest.toString());
		
		Material firstMaterial = materialMapper.getByType(orderRequest.getEfficacyType1());
		log.info("before firstMaterial info : {}", firstMaterial.toString());
		firstMaterial.setQuantity(firstMaterial.getQuantity() - Integer.parseInt(orderRequest.getProportion1()));
		materialList.add(firstMaterial);
		log.info("after firstMaterial info : {}", firstMaterial.toString());
		if(CommonUtil.isNotNullAndEmpty(orderRequest.getEfficacyType2())) {
			Material secondMaterial = materialMapper.getByType(orderRequest.getEfficacyType2());
			log.info("before secondMaterial info : {}", secondMaterial.toString());
			secondMaterial.setQuantity(secondMaterial.getQuantity() - Integer.parseInt(orderRequest.getProportion2()));
			materialList.add(secondMaterial);
			log.info("after secondMaterial info : {}", secondMaterial.toString());
		}
		log.info("materialList size : {}", materialList.size());
		for(Material material : materialList) {
			materialMapper.update(material);
		}
		//FactoryUtil.addTodayCount();
	}
*/

	@Override
	public boolean checkMaterialAmount(String orderCode) {
		boolean availableToMake = true;
		OrderRequest orderRequest = new OrderRequest();
		if(orderCode.lastIndexOf("10") == -1) {
			orderRequest.setEfficacyType1(orderCode.substring(0, 1));
			orderRequest.setProportion1(orderCode.substring(1, 2));
			orderRequest.setEfficacyType2(orderCode.substring(2, 3));
			orderRequest.setProportion2(orderCode.substring(3,4));
			Material secondMaterial = materialMapper.getByType(orderRequest.getEfficacyType1());
			if(secondMaterial.getQuantity() < Integer.parseInt(orderRequest.getProportion2())) {
				//원료 충전
				log.info("원료 {}의 잔량이 부족하여 충전이 필요합니다.", secondMaterial.getType());
				materialSupplement(secondMaterial.getType());
				availableToMake = false;
			}
		}else {
			orderRequest.setEfficacyType1(orderCode.substring(0, 1));
			orderRequest.setProportion1(orderCode.substring(1, 3));
		}
		Material firstMaterial = materialMapper.getByType(orderRequest.getEfficacyType1());
		if(firstMaterial.getQuantity() < Integer.parseInt(orderRequest.getProportion1())) {
			//원료 충전
			log.info("원료 {}의 잔량이 부족하여 충전이 필요합니다.", firstMaterial.getType());
			/*if(FactoryUtil.getFactoryStatus().equals(FactoryStatus.READY)) {
				FactoryUtil.setFactoryStatus(FactoryStatus.OPERATING);
			}*/
			materialSupplement(firstMaterial.getType());
			availableToMake = false;
		}
		return availableToMake;
	}
	
}

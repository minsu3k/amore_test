package com.amore.test.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.amore.test.entity.Material;
import com.amore.test.entity.Order;
import com.amore.test.mapper.MaterialMapper;
import com.amore.test.mapper.OrderMapper;
import com.amore.test.util.FactoryUtil;
import com.amore.test.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Component
public class FactoryScheduler {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private MaterialMapper materialMapper;
	
	@Scheduled(fixedRate = 10000, initialDelay = 1000)
	//@Scheduled(fixedDelay = 30000, initialDelay = 1000)
	public void FactoryStatus() {
		log.info("현재 시간 : {}", ObjectUtils.isEmpty(TimeUtil.currentDateTime)?  LocalDateTime.of(2022, 8, 1, 9, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : TimeUtil.currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		log.info("금일 생산량 : {}", FactoryUtil.getTodayCount());
		log.info("생산 설비 상태 : {}", FactoryUtil.getFactoryStatus());
		Order order = orderMapper.getProducingOrder();
		log.info("생산중인 주문 번호 : {}", ObjectUtils.isEmpty(order) ? "" : order.getOrderNumber());
		log.info("대기중인 주문 목록 : {}", FactoryUtil.getQueue().toString());
		List<Material> materialList = materialMapper.getAll();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		materialList.forEach(material -> {
			sb.append("{원료 : ")
			  .append(material.getType())
			  .append(", 잔량 : ")
			  .append(material.getQuantity())
			  .append("}, ");
		});
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		log.info("원료 잔량 : {}", sb.toString());
	}
}

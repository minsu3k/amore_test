package com.amore.test.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.stereotype.Component;
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
import com.amore.test.service.FactoryService;
import com.amore.test.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 생산 설비 관리 유틸
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FactoryUtil implements Runnable {
	
	private final FactoryService factoryService;
	
	private final OrderService orderService;
	
	/*
	private final OrderMapper orderMapper;
	
	private final MaterialMapper materialMapper;

	private final StockMapper stockMapper;
	*/
	private static final int DAILY_LIMIT = 30; //일 생산 가능 수량

	private static LinkedBlockingDeque<Order> queue = new LinkedBlockingDeque<Order>();

	private static int TODAY_COUNT = 0; //당일 생산 수량

	private static FactoryStatus FACTORY_STATUS = FactoryStatus.READY; //생산 설비 상태

	public static LinkedBlockingDeque<Order> getQueue() {
		return queue;
	}

	public static int getQueueSize() {
		return queue.size();
	}

	public static boolean isAvailableMakingProduct() {
		boolean isAvailableMakingProduct = false;
		if (TimeUtil.isWorkingHour() 
				&& TODAY_COUNT <= DAILY_LIMIT 
				&& FACTORY_STATUS.equals(FactoryStatus.READY)
				&& queue.size() > 0) {
			isAvailableMakingProduct = true;
		}

		return isAvailableMakingProduct;
	}

	public static void addTodayCount() {
		TODAY_COUNT++;
	}

	public static int getTodayCount() {
		return TODAY_COUNT;
	}

	public static void initializeCount() {
		TODAY_COUNT = 0;
	}

	public static FactoryStatus getFactoryStatus() {
		return FACTORY_STATUS;
	}

	public static void setFactoryStatus(FactoryStatus factoryStatus) {
		FACTORY_STATUS = factoryStatus;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				//log.info("isWorkingHour? : {}",TimeUtil.isWorkingHour());
				//log.info("TODAY_COUNT? : {}", TODAY_COUNT);
				//log.info("FACTORY_STATUS? : {}",FACTORY_STATUS);
				if (isAvailableMakingProduct()) {
					//log.info("isAvailableMakingProduct");
					log.info("current Hour : {}", TimeUtil.currentDateTime.plusSeconds(8*60*60/30/60).getHour());
					if(TimeUtil.currentDateTime.plusSeconds(8*60*60/30/60).getHour() < 17) { //작업 완료 예정시간이 업무시간 내일 경우만 생산
						//log.info("isAvalibeToComplete");
						Order order = queue.poll();
						log.info("생산중인 주문 번호 : {}", order.getOrderNumber());
						OrderRequest orderRequest = new OrderRequest();
						orderRequest.setOrderNumber(order.getOrderNumber());
						orderRequest.setOrderCode(order.getOrderCode());
						//factoryService.makeProduct(productOrderNumber);
						if(!factoryService.checkMaterialAmount(order.getOrderCode())) {//원료 보충
							FACTORY_STATUS = FactoryStatus.OPERATING;
							Thread.sleep(40000);
							FACTORY_STATUS = FactoryStatus.READY;
						} 
						factoryService.makeProduct(orderRequest);
						FactoryUtil.addTodayCount();
						log.info("대기중인 주문 목록 : {}", queue.toString());
						FACTORY_STATUS = FactoryStatus.OPERATING;
						orderRequest.setStepCode(OrderStatus.PRODUCING.getCode());
						orderService.update(orderRequest);
						Thread.sleep(8*60*60/30/60*1000); //1개 생산시간 : 8시간/30개로 계산
						FACTORY_STATUS = FactoryStatus.READY;
						orderRequest.setStepCode(OrderStatus.PRODUCE_COMPLETE.getCode());
						orderService.update(orderRequest);
					}
				}else if(TimeUtil.currentDateTime.getHour() == 17 && TimeUtil.currentDateTime.getMinute() == 0) {
					if(FACTORY_STATUS != FactoryStatus.OPERATION_END) {
						orderService.setReadyToSend(); //발송 준비 상태 변경
						orderService.sendProduct(); //일괄 발송
						FACTORY_STATUS = FactoryStatus.OPERATION_END;
					}
					//factoryService.checkSendDate();
				}
			} catch (InterruptedException e) {
				log.error("공장 중단. [{}]", e.getMessage());
			}
		}
	}
	
	public static String getSendDate(int count) {
		int productingTime = 8*60/30;
		String sendDate = "";
		//대기중인 주문 개수 * 제품 한개 생산 소요시간 (8시간*60분 / 30개)
		Long todayRemainingTime = ChronoUnit.MINUTES.between(TimeUtil.currentDateTime, TimeUtil.currentDateTime.withHour(17));
		log.info("todayRemainingTime : {}", todayRemainingTime);
		int todayAvailableCount = (int)Math.floor(todayRemainingTime.doubleValue() / productingTime) ;
		log.info("todayAvailableCount : {}", todayAvailableCount);
		LocalDateTime sendDateTime = TimeUtil.currentDateTime;
		if(todayAvailableCount <= count) {
			sendDateTime = TimeUtil.currentDateTime.plusDays((count-todayAvailableCount)/30);
		}
		sendDate = sendDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		log.info("발송 예정일 : {}", sendDate);
		return sendDate;
	}
	
}

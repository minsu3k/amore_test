package com.amore.test.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.amore.test.dto.request.OrderRequest;
import com.amore.test.dto.response.OrderResponse;
import com.amore.test.entity.Material;
import com.amore.test.entity.Order;
import com.amore.test.exception.OrderError;
import com.amore.test.mapper.MaterialMapper;
import com.amore.test.mapper.OrderMapper;
import com.amore.test.model.FactoryStatus;
import com.amore.test.model.OrderStatus;
import com.amore.test.util.CommonUtil;
import com.amore.test.util.FactoryUtil;
import com.amore.test.util.OrderUtil;
import com.amore.test.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private FactoryService factoryService;
	
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private MaterialMapper materialMapper;
	
	@Override
	@Transactional
	public OrderResponse createOrder(OrderRequest request) {
		OrderResponse orderResponse;
		Material firstMaterial = materialMapper.getByType(request.getEfficacyType1());
		Material secondMaterial = new Material();
		boolean availableToMake = true;
		//첫번째 원료 단종 여부 체크
		if(!ObjectUtils.isEmpty(firstMaterial)) {
			log.info("첫번째 원료 : {}, 잔량 : {}", firstMaterial.getType(), firstMaterial.getQuantity());
			Order order = new Order();
			//혼합 원료일 경우
			if(CommonUtil.isNotNullAndEmpty(request.getEfficacyType2())) {
				secondMaterial = materialMapper.getByType(request.getEfficacyType2());
				//두번째 원료 단종 여부 체크
				if(!ObjectUtils.isEmpty(secondMaterial)) {
					log.info("두번째 원료 : {}, 잔량 : {}", secondMaterial.getType(), secondMaterial.getQuantity());
					/*
					if(firstMaterial.getQuantity() < Integer.parseInt(request.getProportion1())) {
						//연료 충전
						log.info("원료 {}의 잔량이 부족하여 충전을 시작합니다.", firstMaterial.getType());
						if(FactoryUtil.getFactoryStatus().equals(FactoryStatus.READY)) {
							FactoryUtil.setFactoryStatus(FactoryStatus.OPERATING);
						}
						factoryService.materialSupplement(firstMaterial.getType());
						availableToMake = false;
					}
					if(secondMaterial.getQuantity() < Integer.parseInt(request.getProportion2())) {
						//연료 충전
						log.info("원료 {}의 잔량이 부족하여 충전을 시작합니다.", firstMaterial.getType());
						factoryService.materialSupplement(secondMaterial.getType());
						availableToMake = false;
					}
					*/
					
					//오더 생성 및 발송 예정일 세팅 
					String orderCode = OrderUtil.makeOrderCode(request);
					String orderNumber = orderMapper.createOrderNumber();
					//log.info("orderNumber : {}", orderNumber);
					order.setOrderNumber(orderNumber);
					order.setOrderCode(orderCode);
					order.setStepCode(OrderStatus.RECEIPT.getCode());
					order.setOrderDate(TimeUtil.currentDateTime.format(DateTimeFormatter.ofPattern("yyMMdd")));
					order.setSendDate(FactoryUtil.getSendDate(getReadyStepOrderList().size()));
					if(checkAvailableSendDate(order.getOrderDate(), order.getSendDate())) {
						orderMapper.insert(order);
						request.setOrderNumber(orderNumber);
						request.setOrderCode(orderCode);
						request.setStepCode(order.getStepCode());
						request.setOrderDate(order.getOrderDate());
						request.setSendDate(order.getSendDate());
						//생산
						if(availableToMake) {
							//request.setOrderNumber(order.getOrderNumber());
							//FactoryUtil.getQueue().add(order.getOrderNumber());
							FactoryUtil.getQueue().add(order);
							log.info("queue size : {}",FactoryUtil.getQueueSize());
						}
						orderResponse = getOrder(request);
					}else {
						log.info("주문 지연으로 인한 취소 처리");
						orderResponse = OrderResponse.builder()
								 .orderNumber(order.getOrderNumber())
								 .orderCode(order.getOrderCode())
								 .stepCode(OrderStatus.CANCEL.getCode())
								 .orderDate(order.getOrderDate())
								 .sendDate(order.getSendDate())
								 .error(request.getOrderNumber() + " " + OrderError.DELAYED_CANCEL.getMessage())
								 .build();
					}
				}else {
					//throw new CommonException(CommonError.DISCONTINUED, request.getEfficacyType2() + "효능 단종");
					log.info("{} 효능 단종", request.getEfficacyType2());
					orderResponse = OrderResponse.builder()
							 .error(request.getEfficacyType2() + " " + OrderError.DISCONTINUED.getMessage())
							 .build();
				}
			// 단일 원료일 경우
			}else {
				/*
				if(firstMaterial.getQuantity() < Integer.parseInt(request.getProportion1())) {
					//연료 충전
					log.info("원료 {}의 잔량이 부족하여 충전을 시작합니다.", firstMaterial.getType());
					if(FactoryUtil.getFactoryStatus().equals(FactoryStatus.READY)) {
						FactoryUtil.setFactoryStatus(FactoryStatus.OPERATING);
					}
					factoryService.materialSupplement(firstMaterial.getType());
					availableToMake = false;
				}
				*/
				//오더 생성 및 발송 예정일 세팅 
				String orderCode = OrderUtil.makeOrderCode(request);
				String orderNumber = orderMapper.createOrderNumber();
				//log.info("orderNumber : {}", orderNumber);
				order.setOrderNumber(orderNumber);
				order.setOrderCode(orderCode);
				order.setStepCode(OrderStatus.RECEIPT.getCode());
				order.setOrderDate(TimeUtil.currentDateTime.format(DateTimeFormatter.ofPattern("yyMMdd")));
				order.setSendDate(FactoryUtil.getSendDate(getReadyStepOrderList().size()));
				if(checkAvailableSendDate(order.getOrderDate(), order.getSendDate())) {
					orderMapper.insert(order);
					request.setOrderNumber(orderNumber);
					request.setOrderCode(orderCode);
					request.setStepCode(order.getStepCode());
					request.setOrderDate(order.getOrderDate());
					request.setSendDate(order.getSendDate());
					//생산
					if(availableToMake) {
						//request.setOrderNumber(order.getOrderNumber());
						//FactoryUtil.getQueue().add(order.getOrderNumber());
						FactoryUtil.getQueue().add(order);
						log.info("queue size : {}",FactoryUtil.getQueueSize());
					}
					orderResponse = getOrder(request);	
				}else {
					log.info("주문 지연으로 인한 취소 처리");
					orderResponse = OrderResponse.builder()
							 .orderNumber(order.getOrderNumber())
							 .orderCode(order.getOrderCode())
							 .stepCode(OrderStatus.CANCEL.getCode())
							 .orderDate(order.getOrderDate())
							 .sendDate(order.getSendDate())
							 .error(request.getOrderNumber() + " " + OrderError.DELAYED_CANCEL.getMessage())
							 .build();
				}
			}
		}else {
			log.info("{} 효능 단종", request.getEfficacyType1());
			orderResponse = OrderResponse.builder()
					 .error(request.getEfficacyType1() + " " + OrderError.DISCONTINUED.getMessage())
					 .build();
			//throw new CommonException(CommonError.DISCONTINUED, request.getEfficacyType1() + "효능 단종");
		}
				
		return orderResponse;
	}

	private boolean checkAvailableSendDate(String orderDate, String sendDate) {
		boolean availableSendDate = true;
		List<Order> readyStepOrderList = orderMapper.getReadyStepOrderList();
		for(Order order : readyStepOrderList) {
			LocalDate sendDateTime = LocalDate.parse(order.getSendDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			LocalDate orderDateTime = LocalDate.parse(order.getOrderDate(), DateTimeFormatter.ofPattern("yyMMdd"));
			log.info("sendDateTime : {}", sendDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			log.info("orderDateTime : {}", orderDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			if(sendDateTime.isAfter(orderDateTime.plusDays(6))) {
				availableSendDate = false;
			}
		}
		return availableSendDate;
	}

	@Override
	public OrderResponse getOrder(OrderRequest request) {
		OrderResponse orderResponse;
		Order order = orderMapper.getOrder(request.getOrderNumber());
		if(ObjectUtils.isEmpty(order)) {
			log.info("주문 번호 오류");
			orderResponse = OrderResponse.builder()
										 .error(OrderError.INVALID_ORDER_NO.getMessage() + "[주문 번호 : " + request.getOrderNumber() + "]")
										 .build();
		}else {
			order.setStepCode(OrderStatus.getNamefromCode(order.getStepCode()));
			orderResponse =OrderResponse.builder()
					.orderNumber(order.getOrderNumber())
					.orderCode(order.getOrderCode())
					.stepCode(order.getStepCode())
					.orderDate(order.getOrderDate())
					.sendDate(order.getSendDate())
					.build();
		}
		return orderResponse;
	}
	
	public List<Order> getReadyStepOrderList(){
		return orderMapper.getReadyStepOrderList(); 
	}

	@Override
	public OrderResponse cancelOrder(OrderRequest request) {
		OrderResponse orderResponse;
		Order order = orderMapper.getOrder(request.getOrderNumber());
		if(ObjectUtils.isEmpty(order)) {
			log.info("주문 번호 오류");
			orderResponse = OrderResponse.builder()
										 .error(OrderError.INVALID_ORDER_NO.getMessage() + "[주문 번호 : " + request.getOrderNumber() + "]")
										 .build();
		}
		if(OrderStatus.RECEIPT.getCode().equals(order.getStepCode())) {
			order.setStepCode(OrderStatus.CANCEL.getCode());
			orderMapper.update(order);
			orderResponse = OrderResponse.builder()
						 .orderNumber(order.getOrderNumber())
						 .orderCode(order.getOrderCode())
						 .stepCode(order.getStepCode())
						 .orderDate(order.getOrderDate())
						 .sendDate(order.getSendDate())
						 .message("주문 취소 처리 완료")
						 .build();
			log.info("주문 취소 처리 완료");
		}else {
			log.info("환불 불가");
			orderResponse = OrderResponse.builder()
										 .error(OrderError.INVALID_CANCEL_STEP.getMessage() + "[주문 상품 진행 상태 : " + order.getStepCode() +"]")
										 .build();
		}
		return orderResponse;
	}

	@Override
	public void update(OrderRequest orderRequest) {
		Order order = Order.builder()
						   .orderNumber(orderRequest.getOrderNumber())
						   .stepCode(orderRequest.getStepCode())
						   .build();
		log.info("order update : {}", order.toString());
		orderMapper.update(order);
	}

	@Override
	public void setReadyToSend() {
		List<Order> produceCompleteOrderList = orderMapper.getOrderByStepCode(OrderStatus.PRODUCE_COMPLETE.getCode());
		StringBuilder sb = new StringBuilder();
		sb.append("발송 대기 주문 목록 : [");
		for(Order order : produceCompleteOrderList) {
			order.setStepCode(OrderStatus.SEND_READY.getCode());
			orderMapper.update(order);
			sb.append(order.getOrderNumber()).append(", ");
		}
		sb.append("]");
		sb.delete(sb.length()-2, sb.length());
		log.info(sb.toString());
	}

	@Override
	public void sendProduct() {
		orderMapper.updateStepCodeToSendComplete();
		
	}


}

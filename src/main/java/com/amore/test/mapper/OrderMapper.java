package com.amore.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.amore.test.entity.Order;

@Mapper
public interface OrderMapper {

	void insert(Order order);

	Order getOrder(@Param("orderNumber") String orderNumber);

	String createOrderNumber();

	List<Order> getReadyStepOrderList();

	void update(Order order);

	Order getProducingOrder();

	List<Order> getOrderByStepCode(@Param("stepCode") String stepCode);

	void updateStepCodeToSendComplete();

}

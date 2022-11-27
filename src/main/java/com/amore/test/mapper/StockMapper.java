package com.amore.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.amore.test.entity.Stock;

@Mapper
public interface StockMapper {

	Stock getByType(@Param("type") String type);

	void update(Stock stock);

	List<Stock> getStatus();

}

package com.amore.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.amore.test.entity.Material;

@Mapper
public interface MaterialMapper {

	Material getByType(@Param("type") String type);

	void update(Material material);

	int getTotalCount();

	boolean isNewMaterial(@Param("type") String type);

	void insert(Material material);

	void remove(@Param("type") String type);

	List<Material> getAll();

}

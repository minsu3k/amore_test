package com.amore.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amore.test.dto.request.MaterialRequest;
import com.amore.test.dto.response.MaterialResponse;
import com.amore.test.exception.CommonException;
import com.amore.test.mapper.MaterialMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

	@Autowired
	private MaterialMapper materialMapper;
	
	@Override
	@Transactional
	public MaterialResponse addMaterial(MaterialRequest request) {
		MaterialResponse materialResponse;
		if(!isNewMaterial(request.getType())) {
			log.info("It already exists.");
			materialResponse = MaterialResponse.builder()
											   .type(request.getType())
											   .quantity(request.getQuantity())
											   .error("이미 존재하는 원료.")
											   .build();
		} else if(!availableToAdd()) {
			log.info("It already have 10 materials.");
			materialResponse = MaterialResponse.builder()
											   .type(request.getType())
											   .quantity(request.getQuantity())
											   .error("보유원료 10개 초과")
											   .build();
		} else {
			materialMapper.insert(request.toMaterial());
			materialResponse = MaterialResponse.builder()
					   .type(request.getType())
					   .quantity(request.getQuantity())
					   .message("원료 추가 완료")
					   .build();
		}
		return materialResponse;
	}

	@Override
	@Transactional
	public MaterialResponse removeMaterial(MaterialRequest request) {
		MaterialResponse materialResponse;
		if(isNewMaterial(request.getType())) {
			log.info("It does not exists");
			materialResponse = MaterialResponse.builder()
											   .type(request.getType())
											   .quantity(request.getQuantity())
											   .error("존재하지 않는 원료.")
											   .build();
		}else {
			materialMapper.remove(request.getType());
			materialResponse = MaterialResponse.builder()
					   .type(request.getType())
					   .quantity(request.getQuantity())
					   .message("원료 삭제 완료")
					   .build();
		}
		return materialResponse;
	}

	private boolean isNewMaterial(String type) {
		return materialMapper.isNewMaterial(type);
	}

	private boolean availableToAdd() {
		boolean result = false;
		if(materialMapper.getTotalCount() < 10) {
			result = true;
		}
		return result;
	}
}

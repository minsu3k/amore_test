package com.amore.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amore.test.dto.request.MaterialRequest;
import com.amore.test.dto.response.MaterialResponse;
import com.amore.test.entity.Material;
import com.amore.test.service.MaterialService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/material")
@Api(tags = {"효능 원료 관리 API"})
public class MaterialController {
	
	@Autowired
	private MaterialService materialService;

	@PostMapping(value = "/add")
	@ApiOperation(value = "효능 원료 추가", notes = "새로운 효능 원료를 추가한다.")
	@ResponseBody
	public ResponseEntity<MaterialResponse> addMaterial(@RequestBody MaterialRequest request) {
		request.validate();
		log.info("add material");
		return new ResponseEntity<MaterialResponse>(materialService.addMaterial(request), HttpStatus.OK);
	}
	
	@PostMapping(value = "/delete")
	@ApiOperation(value = "효능 원료 삭제", notes = "효능 원료를 삭제한다.")
	@ResponseBody
	public ResponseEntity<MaterialResponse> removeMaterial(@RequestBody MaterialRequest request) {
		request.validate();
		log.info("remove material");
		return new ResponseEntity<MaterialResponse>(materialService.removeMaterial(request), HttpStatus.OK);
	}
	
}


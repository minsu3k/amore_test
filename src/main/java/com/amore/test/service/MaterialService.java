package com.amore.test.service;

import com.amore.test.dto.request.MaterialRequest;
import com.amore.test.dto.response.MaterialResponse;

public interface MaterialService {

	MaterialResponse addMaterial(MaterialRequest request);

	MaterialResponse removeMaterial(MaterialRequest request);

}

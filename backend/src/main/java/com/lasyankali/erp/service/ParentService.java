package com.lasyankali.erp.service;

import java.util.List;

import com.lasyankali.erp.dto.CreateParentDTO;
import com.lasyankali.erp.dto.ParentResponseDTO;
import com.lasyankali.erp.dto.UpdateParentDTO;

public interface ParentService {
	void createParent(CreateParentDTO createParentDTO);

	ParentResponseDTO getParentById(Long parentId);

	ParentResponseDTO updateParent(Long parentId, UpdateParentDTO updateParentDTO);

	void deleteParent(Long parentId);

	List<ParentResponseDTO> searchParents(String keyword);
}

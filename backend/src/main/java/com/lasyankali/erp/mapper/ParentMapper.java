package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.ParentResponseDTO;
import com.lasyankali.erp.entity.Parents;

@Component
public class ParentMapper {
	public ParentResponseDTO mapToResponse(Parents parent) {

        ParentResponseDTO dto = new ParentResponseDTO();

        dto.setParentId(parent.getParentId());
        dto.setFirstName(parent.getUser().getFirstName());
        dto.setLastName(parent.getUser().getLastName());
        dto.setUsername(parent.getUser().getUserName());
        dto.setEmail(parent.getUser().getEmail());
        dto.setMobileNumber(parent.getUser().getMobileNumber());
        dto.setOccupation(parent.getOccupation());
        dto.setAddress(parent.getAddress());

        return dto;
    }
}

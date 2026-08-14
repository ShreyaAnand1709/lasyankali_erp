package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.CreateParentDTO;
import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.dto.ParentResponseDTO;
import com.lasyankali.erp.dto.UpdateParentDTO;
import com.lasyankali.erp.entity.Parents;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.mapper.ParentMapper;
import com.lasyankali.erp.repository.ParentRepository;
import com.lasyankali.erp.service.ParentService;
import com.lasyankali.erp.service.UserService;

@Service
public class ParentServiceImpl implements ParentService{
	private final ParentRepository repo;
	private final UserService userService;
	private final ParentMapper mapper;
	
	public ParentServiceImpl(ParentRepository repo, UserService userService, ParentMapper mapper) {
		super();
		this.repo = repo;
		this.userService = userService;
		this.mapper = mapper;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void createParent(CreateParentDTO createParentDTO) {
		CreateUserDTO user = new CreateUserDTO();
		user.setEmail(createParentDTO.getEmail());
		user.setFirstName(createParentDTO.getFirstName());
		user.setLastName(createParentDTO.getLastName());
		user.setMobileNumber(createParentDTO.getMobileNumber());
		user.setPassword(createParentDTO.getPassword());
		user.setRoleName("PARENT");
		user.setUsername(createParentDTO.getUsername());
		User savedUser = userService.createUser(user);
		
		Parents parent = new Parents();
		parent.setUser(savedUser);
		parent.setAddress(createParentDTO.getAddress());
		parent.setOccupation(createParentDTO.getOccupation());
		parent.setCreatedAt(LocalDateTime.now());
		parent.setUpdatedAt(LocalDateTime.now());	
		repo.save(parent);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','PARENT')")
	public ParentResponseDTO getParentById(Long parentId) {
		Parents parent = repo.findById(parentId)
	            .orElseThrow(() -> new RuntimeException("Parent not found"));

	    return mapper.mapToResponse(parent);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public ParentResponseDTO updateParent(Long parentId, UpdateParentDTO updateParentDTO) {
		Parents parent = repo.findById(parentId)
	            .orElseThrow(() -> new RuntimeException("Parent not found"));

	    User user = parent.getUser();

	    user.setFirstName(updateParentDTO.getFirstName());
	    user.setLastName(updateParentDTO.getLastName());
	    user.setEmail(updateParentDTO.getEmail());
	    user.setMobileNumber(updateParentDTO.getMobileNumber());

	    parent.setOccupation(updateParentDTO.getOccupation());
	    parent.setAddress(updateParentDTO.getAddress());
	    parent.setUpdatedAt(LocalDateTime.now());

	    repo.save(parent);

	    return mapper.mapToResponse(parent);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteParent(Long parentId) {
		Parents parent = repo.findById(parentId)
	            .orElseThrow(() -> new RuntimeException("Parent not found"));

	    repo.delete(parent);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<ParentResponseDTO> searchParents(String keyword) {
		List<Parents> parents = repo.searchParents(keyword);

	    return parents.stream()
	            .map(mapper::mapToResponse)
	            .toList();
	}

}

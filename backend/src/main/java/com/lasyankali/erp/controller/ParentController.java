package com.lasyankali.erp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.CreateParentDTO;
import com.lasyankali.erp.dto.ParentResponseDTO;
import com.lasyankali.erp.dto.UpdateParentDTO;
import com.lasyankali.erp.service.ParentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/parents")
public class ParentController {
	private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    // Create Parent
    @PostMapping
    public void createParent(@Valid @RequestBody CreateParentDTO createParentDTO) {
        parentService.createParent(createParentDTO);
    }

    // Get Parent Profile
    @GetMapping("/{parentId}")
    public ParentResponseDTO getParentById(@PathVariable Long parentId) {
        return parentService.getParentById(parentId);
    }

    // Update Parent
    @PutMapping("/{parentId}")
    public ParentResponseDTO updateParent(
            @PathVariable Long parentId,
            @RequestBody UpdateParentDTO updateParentDTO) {

        return parentService.updateParent(parentId, updateParentDTO);
    }
    

    // Delete Parent
    @DeleteMapping("/{parentId}")
    public void deleteParent(@PathVariable Long parentId) {
        parentService.deleteParent(parentId);
    }

    // Search Parent
    @GetMapping("/search")
    public List<ParentResponseDTO> searchParents(
            @RequestParam String keyword) {

        return parentService.searchParents(keyword);
    }
}

package com.lasyankali.erp.dto;

import java.time.LocalDate;

public record BatchMemberResponseDTO(Long memberId,
        String firstName,
        String lastName,
        String username,
        String profileStatus,
        String enrollmentStatus,
        LocalDate assignmentDate) {

}

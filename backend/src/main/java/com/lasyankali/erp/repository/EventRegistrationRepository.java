package com.lasyankali.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.EventRegister;

public interface EventRegistrationRepository extends JpaRepository<EventRegister,Long>{
	boolean existsByEvent_EventIdAndStudent_StudentId(Long eventId, Long studentId);
	Optional<EventRegister>
	findByEvent_EventIdAndStudent_StudentId(
	        Long eventId,
	        Long studentId);
	List<EventRegister> findByEvent_EventId(Long eventId);
}

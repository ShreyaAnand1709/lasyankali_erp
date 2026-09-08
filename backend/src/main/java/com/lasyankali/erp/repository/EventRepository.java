package com.lasyankali.erp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lasyankali.erp.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>{
	boolean existsByEventCode(String eventCode);
	Optional<Event> findByEventCode(String eventCode);
	List<Event> findByEventDate(LocalDate date);
	@Query("""
		    SELECT e
		    FROM Event e
		    WHERE LOWER(e.eventName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(e.venue) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    """)
		List<Event> searchByNameOrVenue(@Param("keyword") String keyword);
}

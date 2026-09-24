package com.lasyankali.erp.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lasyankali.erp.service.FeeService;

@Component
public class FeeScheduler {
	private FeeService feeService;

	public FeeScheduler(FeeService feeService) {
		this.feeService = feeService;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(FeeScheduler.class);
	
	@Scheduled (cron = "0 5 0 1 * *", zone="Asia/Kolkata")
	public void generateFeeScheduler() {
		ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
		LocalDate billingMonth = LocalDate.now(indiaZone).withDayOfMonth(1);
		int generatedCount = feeService.generateMonthlyFees(billingMonth);
		logger.info(
				"Fee created: {}",
				billingMonth, generatedCount);
	}
	
	@Scheduled (cron = "0 15 0 * * *", zone="Asia/Kolkata")
	public void markOverdueFeesScheduler() {
		ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
		LocalDate currentDate = LocalDate.now(indiaZone);
		int updatedCount = feeService.markOverDueFees(currentDate);
		logger.info(
				"Daily overdue fee check completed for {}. Fees updated: {}", 
				currentDate, updatedCount);
	}
}

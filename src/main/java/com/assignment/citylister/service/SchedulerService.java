package com.assignment.citylister.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
@Slf4j
public class SchedulerService {

	@Autowired



	@Scheduled(initialDelay = 1000, fixedRate = 10000)
	public void run() {
		log.info("Current time is :: " + Calendar.getInstance().getTime());
	}
}

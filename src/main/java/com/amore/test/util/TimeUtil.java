package com.amore.test.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.amore.test.model.FactoryStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * 시간 관리 유틸
 */
@Slf4j
public class TimeUtil {

	public static LocalDateTime currentDateTime;
	
	private static Thread thread;
	
	private static boolean keepGoing = true;
	
	public static void init() {
        log.info("init Date : {}", currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		thread = new Thread() {
           public void run() {
        	   while(keepGoing) {
        		   try {
        			   //log.info("current Date Time : {}", currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	        		   if(isWorkingHour()) { //근무시간 8시간은 1분을 1초로 간주
	        			   if(currentDateTime.getHour() == 9 && currentDateTime.getMinute() == 0) { // 근무 시작 시간은 9시의 경우 생산설비 카운트 초기화
	        				   log.info("factory is working now");
	        				   FactoryUtil.initializeCount();
	        				   FactoryUtil.setFactoryStatus(FactoryStatus.READY);
	        			   }
	        			   //if(FactoryUtil.isAvailableMakingProduct())
        				   Thread.sleep(1000);
        				   currentDateTime = currentDateTime.plusMinutes(1);
	        		   } else { // 근무 외 시간 16시간은 30초로 간주
	        			   if(currentDateTime.getHour() == 17 && currentDateTime.getMinute() == 0) {
	        				   log.info("factory is closing now");
	        			   }
	        			   Thread.sleep(30000);
	        			   currentDateTime = currentDateTime.plusHours(16);
	        		   }
        		   } catch (InterruptedException e) {
					   log.error(e.getMessage());
					   keepGoing = false;
				   }
        	   }
       		}
        };
        thread.start();
	}
	
	// 근무 시간 체크 09~17시
	public static boolean isWorkingHour() {
		boolean isWorkingHour = false;
		if(currentDateTime.getHour() >= 9 && currentDateTime.getHour() < 17){
			isWorkingHour = true;
		}
		return isWorkingHour;
	}

	//원료 보충시 시간 추가
	public static void plusMinutes(int minutes) {
		currentDateTime.plusMinutes(minutes);
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}
	
	
}

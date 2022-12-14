package dev.linkedlogics.jdbc.process;

import java.time.OffsetDateTime;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.jdbc.service.JdbcServiceConfigurer;
import dev.linkedlogics.service.ServiceLocator;
import dev.linkedlogics.service.SchedulerService.Schedule;
import dev.linkedlogics.service.SchedulerService.ScheduleType;

public class Main {
	public static void main(String[] args) {
		LinkedLogics.configure(new JdbcServiceConfigurer());
		
		System.out.println(OffsetDateTime.now());
		ServiceLocator.getInstance().getSchedulerService().schedule(new Schedule("id", "LOGIC_3sec", "1", OffsetDateTime.now().plusSeconds(3), ScheduleType.DELAY));
		ServiceLocator.getInstance().getSchedulerService().schedule(new Schedule("id", "LOGIC_10sec", "1", OffsetDateTime.now().plusSeconds(10), ScheduleType.DELAY));
		ServiceLocator.getInstance().getSchedulerService().schedule(new Schedule("id", "LOGIC_40sec", "1", OffsetDateTime.now().plusSeconds(40), ScheduleType.DELAY));
		ServiceLocator.getInstance().getSchedulerService().schedule(new Schedule("id", "LOGIC_1min_10sec", "1", OffsetDateTime.now().plusSeconds(70), ScheduleType.DELAY));
	}
}

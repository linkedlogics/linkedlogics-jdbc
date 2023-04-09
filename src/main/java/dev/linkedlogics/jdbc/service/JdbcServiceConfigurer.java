package dev.linkedlogics.jdbc.service;

import dev.linkedlogics.service.ServiceConfigurer;
import dev.linkedlogics.service.common.QueueSchedulerService;

public class JdbcServiceConfigurer extends ServiceConfigurer {
	public JdbcServiceConfigurer() {
		configure(new JdbcProcessService());
		configure(new JdbcContextService());
		configure(new JdbcQueueService());
		configure(new JdbcTopicService());
		configure(new QueueSchedulerService());
		configure(new JdbcTriggerService());
	}
}

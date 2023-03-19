package dev.linkedlogics.jdbc.service;

import dev.linkedlogics.service.ServiceConfigurer;
import dev.linkedlogics.service.local.QueueSchedulerService;

public class JdbcServiceConfigurer extends ServiceConfigurer {
	public JdbcServiceConfigurer() {
		configure(new JdbcContextService());
		configure(new JdbcQueueService());
		configure(new JdbcTopicService());
		configure(new QueueSchedulerService());
		configure(new JdbcTriggerService());
	}
}

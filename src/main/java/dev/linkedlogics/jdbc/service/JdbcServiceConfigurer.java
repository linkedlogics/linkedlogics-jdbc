package dev.linkedlogics.jdbc.service;

import dev.linkedlogics.service.ServiceConfigurer;

public class JdbcServiceConfigurer extends ServiceConfigurer {
	public JdbcServiceConfigurer() {
		configure(new JdbcContextService());
		configure(new JdbcQueueService());
		configure(new JdbcTopicService());
		configure(new JdbcConsumerService());
		configure(new JdbcPublisherService());
		configure(new JdbcSchedulerService());
		configure(new JdbcTriggerService());
	}
}

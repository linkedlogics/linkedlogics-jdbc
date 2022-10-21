package dev.linkedlogics.jdbc.service;

import dev.linkedlogics.service.ServiceConfigurer;

public class DbServiceConfigurer extends ServiceConfigurer {
	public DbServiceConfigurer() {
		configure(new DbQueueService());
		configure(new DbTopicService());
		configure(new DbConsumerService());
		configure(new DbPublisherService());
	}
}

package dev.linkedlogics.jdbc;

import java.util.List;

import dev.linkedlogics.jdbc.service.JdbcContextService;
import dev.linkedlogics.jdbc.service.JdbcQueueService;
import dev.linkedlogics.jdbc.service.JdbcTopicService;
import dev.linkedlogics.jdbc.service.JdbcTriggerService;
import dev.linkedlogics.service.LinkedLogicsService;
import dev.linkedlogics.service.ServiceProvider;
import dev.linkedlogics.service.local.LocalConsumerService;
import dev.linkedlogics.service.local.LocalPublisherService;
import dev.linkedlogics.service.local.QueueSchedulerService;

public class JdbcServices extends ServiceProvider {
	@Override
	public List<LinkedLogicsService> getMessagingServices() {
		return List.of(new JdbcQueueService(), new JdbcTopicService(), new LocalConsumerService(), new LocalPublisherService());
	}

	@Override
	public List<LinkedLogicsService> getSchedulingServices() {
		return List.of(new QueueSchedulerService());
	}

	@Override
	public List<LinkedLogicsService> getStoringServices() {
		return List.of(new JdbcContextService(), new JdbcTriggerService());
	}
}

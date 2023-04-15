package io.linkedlogics.jdbc;

import java.util.List;

import io.linkedlogics.service.LinkedLogicsService;
import io.linkedlogics.service.ServiceProvider;
import io.linkedlogics.service.common.QueueCallbackService;
import io.linkedlogics.service.common.QueueSchedulerService;
import io.linkedlogics.service.local.LocalConsumerService;
import io.linkedlogics.service.local.LocalPublisherService;
import io.linkedlogics.jdbc.service.JdbcContextService;
import io.linkedlogics.jdbc.service.JdbcProcessService;
import io.linkedlogics.jdbc.service.JdbcQueueService;
import io.linkedlogics.jdbc.service.JdbcTopicService;
import io.linkedlogics.jdbc.service.JdbcTriggerService;

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
		return List.of(new JdbcContextService(), new JdbcTriggerService(), new JdbcProcessService());
	}

	@Override
	public List<LinkedLogicsService> getProcessingServices() {
		return List.of(new QueueCallbackService());
	}
	
	
}

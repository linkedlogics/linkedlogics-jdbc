package io.linkedlogics.jdbc.service;

import io.linkedlogics.service.ServiceConfigurer;
import io.linkedlogics.service.common.QueueCallbackService;
import io.linkedlogics.service.common.QueueSchedulerService;
import io.linkedlogics.service.local.LocalLogicService;

public class JdbcServiceConfigurer extends ServiceConfigurer {
	public JdbcServiceConfigurer() {
		configure(new JdbcProcessService());
		configure(new JdbcContextService());
		configure(new JdbcQueueService());
		configure(new JdbcTopicService());
		configure(new QueueSchedulerService());
//		configure(new QueueCallbackService());
		configure(new JdbcTriggerService());
	}
}

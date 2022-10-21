package dev.linkedlogics.jdbc.service;

import dev.linkedlogics.context.LogicContext;
import dev.linkedlogics.service.PublisherService;
import dev.linkedlogics.service.ServiceLocator;

public class DbPublisherService implements PublisherService {

	@Override
	public void publish(LogicContext context) {
		QueueService queueService = ServiceLocator.getInstance().getService(QueueService.class);
		queueService.offer(context.getApplication(), ServiceLocator.getInstance().getMapperService().mapTo(context));
	}
}

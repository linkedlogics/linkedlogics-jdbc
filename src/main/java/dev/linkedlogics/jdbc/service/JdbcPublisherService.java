package dev.linkedlogics.jdbc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.linkedlogics.context.Context;
import dev.linkedlogics.service.PublisherService;
import dev.linkedlogics.service.ServiceLocator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcPublisherService implements PublisherService {

	@Override
	public void publish(Context context) {
		QueueService queueService = ServiceLocator.getInstance().getService(QueueService.class);
		ObjectMapper mapper = ServiceLocator.getInstance().getMapperService().getMapper();
		try {
			queueService.offer(context.getApplication(), mapper.writeValueAsString(context));
		} catch (JsonProcessingException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
}

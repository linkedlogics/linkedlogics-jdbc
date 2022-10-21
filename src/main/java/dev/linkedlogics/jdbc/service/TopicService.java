package dev.linkedlogics.jdbc.service;

import java.util.Optional;

import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.service.LinkedLogicsService;

public interface TopicService extends LinkedLogicsService {
	void offer(String queue, String payload);
	
	Optional<Message> poll(String queue);
}

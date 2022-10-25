package dev.linkedlogics.jdbc.service;

import java.time.OffsetDateTime;
import java.util.Optional;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.jdbc.repository.QueueRepository;

public class JdbcQueueService implements QueueService {
	private QueueRepository repository;
	
	public JdbcQueueService() {
		this.repository = new QueueRepository();
	}
	
	public void offer(String queue, String payload) {
		repository.set(Message.builder().queue(queue).payload(payload).createdAt(OffsetDateTime.now()).build());
	}
	
	public Optional<Message> poll(String queue) {
		return repository.get(queue, LinkedLogics.getApplicationName());
	}
}

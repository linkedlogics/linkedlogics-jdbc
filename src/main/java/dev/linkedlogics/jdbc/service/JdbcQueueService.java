package dev.linkedlogics.jdbc.service;

import java.util.Optional;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.jdbc.repository.QueueRepository;
import dev.linkedlogics.service.QueueService;

public class JdbcQueueService implements QueueService {
	private QueueRepository repository;
	
	public JdbcQueueService() {
		this.repository = new QueueRepository();
	}
	
	public void offer(String queue, String payload) {
		repository.set(queue, payload);
	}
	
	public Optional<String> poll(String queue) {
		return repository.get(queue, LinkedLogics.getApplicationName());
	}
}

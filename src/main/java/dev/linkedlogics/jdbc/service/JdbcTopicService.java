package dev.linkedlogics.jdbc.service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.jdbc.repository.TopicRepository;
import dev.linkedlogics.service.TopicService;

public class JdbcTopicService implements TopicService {
	private String consumerId = UUID.randomUUID().toString();
	private TopicRepository repository;
	private ScheduledExecutorService executor;
	
	@Override
	public void start() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				repository.clear(OffsetDateTime.now());
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	@Override
	public void stop() {
		executor.shutdownNow();
	}

	public JdbcTopicService() {
		this.repository = new TopicRepository();
	}
	
	public void offer(String topic, String payload) {
		repository.set(topic, payload);
	}
	
	public Optional<String> poll(String topic) {
		return repository.get(topic, consumerId);
	}
}

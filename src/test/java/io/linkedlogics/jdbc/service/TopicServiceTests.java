package io.linkedlogics.jdbc.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.linkedlogics.LinkedLogics;
import io.linkedlogics.service.ServiceLocator;
import io.linkedlogics.service.TopicService;
import io.linkedlogics.jdbc.entity.Message;
import io.linkedlogics.jdbc.repository.TopicRepository;
import io.linkedlogics.jdbc.service.JdbcServiceConfigurer;

public class TopicServiceTests {
	private static final String TOPIC = "t1";
	
	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new JdbcServiceConfigurer());
		LinkedLogics.launch();
	}
	
	@Test
	public void shouldOfferAndConsume() {
		TopicService topicService = ServiceLocator.getInstance().getService(TopicService.class);
		
		topicService.offer(TOPIC, "hello");
		
		Optional<String> message = topicService.poll(TOPIC);
		assertThat(message).isPresent();
		assertThat(message.get()).isEqualTo("hello");
		
		message = topicService.poll(TOPIC);
		assertThat(message).isEmpty();
		
		TopicRepository repository = new TopicRepository(new JdbcConnectionService().getDataSource());
		message = repository.get(TOPIC, "other_consumer_id");
		assertThat(message).isPresent();
	}
}

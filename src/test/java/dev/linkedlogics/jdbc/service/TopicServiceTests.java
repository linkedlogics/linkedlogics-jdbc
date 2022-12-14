package dev.linkedlogics.jdbc.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.jdbc.repository.TopicRepository;
import dev.linkedlogics.service.ServiceLocator;

public class TopicServiceTests {
	private static final String TOPIC = "t1";
	
	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new JdbcServiceConfigurer());
	}
	
	@Test
	public void shouldOfferAndConsume() {
		TopicService topicService = ServiceLocator.getInstance().getService(TopicService.class);
		
		topicService.offer(TOPIC, "hello");
		
		Optional<Message> message = topicService.poll(TOPIC);
		assertThat(message).isPresent();
		assertThat(message.get().getPayload()).isEqualTo("hello");
		
		message = topicService.poll(TOPIC);
		assertThat(message).isEmpty();
		
		TopicRepository repository = new TopicRepository();
		message = repository.get(TOPIC, "other_consumer_id");
		assertThat(message).isPresent();
	}
}

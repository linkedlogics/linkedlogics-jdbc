package dev.linkedlogics.jdbc.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.service.ServiceLocator;

public class QueueServiceTests {
	private static final String QUEUE = "q1";
	
	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new DbServiceConfigurer());
	}
	
	@Test
	public void shouldOfferAndConsume() {
		QueueService queueService = ServiceLocator.getInstance().getService(QueueService.class);
		
		queueService.offer(QUEUE, "hello");
		
		Optional<Message> message = queueService.poll(QUEUE);
		assertThat(message).isPresent();
		assertThat(message.get().getPayload()).isEqualTo("hello");
		
		message = queueService.poll(QUEUE);
		assertThat(message).isEmpty();
	}
}

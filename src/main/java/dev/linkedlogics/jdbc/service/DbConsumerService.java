package dev.linkedlogics.jdbc.service;

import java.util.Optional;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.context.LogicContext;
import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.service.ConsumerService;
import dev.linkedlogics.service.ServiceLocator;
import dev.linkedlogics.service.task.ProcessorTask;

public class DbConsumerService implements ConsumerService, Runnable {
	private Thread consumer;
	private boolean isRunning;
	
	@Override
	public void start() {
		consumer = new Thread(this);
		consumer.start();
	}

	@Override
	public void stop() {
		isRunning = false;
		if (consumer != null) {
			consumer.interrupt();
		}
	}

	@Override
	public void run() {
		isRunning = true;
		
		while (isRunning) {
			try {
				QueueService queueService = ServiceLocator.getInstance().getService(QueueService.class);
				Optional<Message> message = queueService.poll(LinkedLogics.getApplicationName());
				
				if (message.isPresent()) {
					consume(ServiceLocator.getInstance().getMapperService().mapFrom(message.get().getPayload(), LogicContext.class));
				} else {
					Thread.sleep(1);
				}
				
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void consume(LogicContext context) {
		ServiceLocator.getInstance().getProcessorService().process(new ProcessorTask(context));
	}
}

package dev.linkedlogics.jdbc.service;

import java.util.List;

import dev.linkedlogics.jdbc.repository.TriggerRepository;
import dev.linkedlogics.service.TriggerService;

public class JdbcTriggerService implements TriggerService {
	private TriggerRepository repository;
	
	public JdbcTriggerService() {
		repository = new TriggerRepository();
	}
	
	@Override
	public List<Trigger> get(String contextİd) {
		return repository.get(contextİd);
	}

	@Override
	public void set(String contextİd, Trigger trigger) {
		repository.create(contextİd, trigger);
	}
}

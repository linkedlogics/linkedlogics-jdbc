package io.linkedlogics.jdbc.service;

import java.util.List;

import io.linkedlogics.service.TriggerService;
import io.linkedlogics.jdbc.repository.TriggerRepository;

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

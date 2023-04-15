package io.linkedlogics.jdbc.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.linkedlogics.model.ProcessDefinition;
import io.linkedlogics.model.ProcessDefinitionReader;
import io.linkedlogics.model.ProcessDefinitionWriter;
import io.linkedlogics.service.local.LocalProcessService;
import io.linkedlogics.jdbc.repository.ProcessRepository;
import io.linkedlogics.jdbc.repository.ProcessRepository.ProcessEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcProcessService extends LocalProcessService {
	private ScheduledExecutorService service;
	private ProcessRepository repository;

	public JdbcProcessService() {
		this.repository = new ProcessRepository();
	}

	@Override
	public void start() {
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				refreshProcesses();
			}
		}, 5, 5, TimeUnit.MINUTES);
	}

	@Override
	public void stop() {
		if (service != null) {
			service.shutdownNow();
		}
	}

	@Override
	public Optional<ProcessDefinition> getProcess(String processId, int processVersion) {
		try {
			int version;
			if (processVersion == LATEST_VERSION) {
				version = repository.getMaxVersion(processId).map(Function.identity()).orElseThrow(() -> new RuntimeException());
			} else {
				version = processVersion;
			}

			Optional<ProcessDefinition> process = Optional.ofNullable(definitions.get(getProcessKey(processId, version)));

			if (process.isEmpty()) {
				Optional<ProcessEntity> entity = repository.getProcess(processId, version);
				if (entity.isPresent()) {
					ProcessDefinition newProcess = new ProcessDefinitionReader(entity.get().getBuilder()).read();
					super.addProcess(newProcess);
					return Optional.of(newProcess);
				}
			} else {
				return process;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return Optional.empty();
	}

	@Override
	protected void addProcess(ProcessDefinition process) {
		super.addProcess(process);

		try {
			String builder = new ProcessDefinitionWriter(process).write();

			if (repository.getProcess(process.getId(), process.getVersion()).isPresent()) {
				repository.update(new ProcessEntity(process.getId(), process.getVersion(), builder));
			} else {
				repository.createProcess(new ProcessEntity(process.getId(), process.getVersion(), builder));
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("unable to store process %s[%d] in db", process.getId(), process.getVersion()));
		}
	}

	public void refreshProcesses() {
		try {
			List<ProcessEntity> entities = repository.getProcesses();

			for (ProcessEntity entity : entities) {
				ProcessDefinitionReader reader = new ProcessDefinitionReader(entity.getBuilder());
				try {
					ProcessDefinition process = reader.read();
					JdbcProcessService.super.addProcess(process);
				} catch (Exception e) {
					log.error(String.format("unable to read process %s:%d", entity.getId(), entity.getVersion()), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
}

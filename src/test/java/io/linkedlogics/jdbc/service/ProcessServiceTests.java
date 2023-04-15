package io.linkedlogics.jdbc.service;

import java.util.ServiceLoader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.linkedlogics.LinkedLogics;
import io.linkedlogics.jdbc.process.SimpleProcess1Tests;
import io.linkedlogics.service.ServiceLocator;
import io.linkedlogics.jdbc.service.JdbcProcessService;
import io.linkedlogics.jdbc.service.JdbcServiceConfigurer;


public class ProcessServiceTests {
	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new JdbcServiceConfigurer());
		LinkedLogics.registerLogic(SimpleProcess1Tests.class);
		LinkedLogics.registerProcess(SimpleProcess1Tests.class);
		LinkedLogics.launch();
	}
	
	@Test
	public void shouldRefreshProcesses() {
		JdbcProcessService service = (JdbcProcessService) ServiceLocator.getInstance().getProcessService();
		service.refreshProcesses();
	}
}

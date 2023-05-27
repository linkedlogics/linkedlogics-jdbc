package io.linkedlogics.jdbc.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.linkedlogics.LinkedLogics;
import io.linkedlogics.jdbc.process.JdbcProcess1Tests;
import io.linkedlogics.service.ServiceLocator;


public class ProcessServiceTests {
	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new JdbcServiceConfigurer());
		LinkedLogics.registerLogic(JdbcProcess1Tests.class);
		LinkedLogics.registerProcess(JdbcProcess1Tests.class);
		LinkedLogics.launch();
	}
	
	@Test
	public void shouldRefreshProcesses() {
		JdbcProcessService service = (JdbcProcessService) ServiceLocator.getInstance().getProcessService();
		service.refreshProcesses();
	}
}

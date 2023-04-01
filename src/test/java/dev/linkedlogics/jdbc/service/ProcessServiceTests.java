package dev.linkedlogics.jdbc.service;

import java.util.ServiceLoader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.jdbc.process.SimpleProcess1Tests;
import dev.linkedlogics.service.ServiceLocator;


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

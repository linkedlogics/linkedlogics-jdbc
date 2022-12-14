package dev.linkedlogics.jdbc.process;

import static dev.linkedlogics.LinkedLogicsBuilder.expr;
import static dev.linkedlogics.LinkedLogicsBuilder.logic;
import static dev.linkedlogics.jdbc.process.ProcessTestHelper.waitUntil;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.linkedlogics.LinkedLogics;
import dev.linkedlogics.LinkedLogicsBuilder;
import dev.linkedlogics.LinkedLogicsCallback;
import dev.linkedlogics.annotation.Input;
import dev.linkedlogics.annotation.Logic;
import dev.linkedlogics.context.Context;
import dev.linkedlogics.context.ContextError;
import dev.linkedlogics.context.Status;
import dev.linkedlogics.jdbc.service.JdbcServiceConfigurer;
import dev.linkedlogics.model.process.ProcessDefinition;

public class SimpleProcess1Tests {
	
	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new JdbcServiceConfigurer());
		LinkedLogics.registerLogic(SimpleProcess1Tests.class);
		LinkedLogics.registerProcess(SimpleProcess1Tests.class);
	}

	@Test
	public void testScenario1() {
		AtomicBoolean result = new AtomicBoolean();
		String contextId = LinkedLogics.start("SIMPLE_SCENARIO_1", new HashMap<>() {{ put("s", "hello");}},
				new LinkedLogicsCallback() {
					
					@Override
					public void onTimeout() {
						
					}
					
					@Override
					public void onSuccess(Context context) {
						String s = (String) context.getParams().get("s");
						result.set(s.equals("ZZZYYYXXXHELLO"));						
					}
					
					@Override
					public void onFailure(Context context, ContextError error) {
						
					}
				});
		assertThat(waitUntil(contextId, Status.FINISHED)).isTrue();
		assertThat(result.get()).isTrue();
	}
	
	@Logic(id = "UPPER", returnAs = "s")
	public static String makeUpper(@Input("s") String s) {
		return s.toUpperCase();
	}
	
	@Logic(id = "PREFIX", returnAs = "s")
	public static String addPrefix(@Input("s") String s, @Input("p") String p) {
		return p + s;
	}
	
	public static ProcessDefinition simple() {
		return LinkedLogicsBuilder.createProcess("SIMPLE_SCENARIO_1")
				.add(logic("UPPER").application("test").input("s", expr("s")).build())
				.add(logic("PREFIX").application("test").input("s", expr("s")).input("p", "XXX").build())
				.add(logic("UPPER").application("test").input("s", expr("s")).build())
				.add(logic("PREFIX").application("test").input("s", expr("s")).input("p", "yyy").build())
				.add(logic("UPPER").application("test").input("s", expr("s")).build())
				.add(logic("PREFIX").application("test").input("s", expr("s")).input("p", "ZZZ").build())
				.build();
	}
}

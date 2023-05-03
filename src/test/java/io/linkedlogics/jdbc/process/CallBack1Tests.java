package io.linkedlogics.jdbc.process;

import static io.linkedlogics.LinkedLogicsBuilder.createProcess;
import static io.linkedlogics.LinkedLogicsBuilder.expr;
import static io.linkedlogics.LinkedLogicsBuilder.logic;
import static io.linkedlogics.LinkedLogicsBuilder.verify;
import static io.linkedlogics.jdbc.process.ProcessTestHelper.waitUntil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.linkedlogics.LinkedLogics;
import io.linkedlogics.LinkedLogicsCallback;
import io.linkedlogics.annotation.Input;
import io.linkedlogics.annotation.Logic;
import io.linkedlogics.context.Context;
import io.linkedlogics.context.ContextBuilder;
import io.linkedlogics.context.ContextError;
import io.linkedlogics.context.Status;
import io.linkedlogics.model.ProcessDefinition;
import io.linkedlogics.jdbc.service.JdbcServiceConfigurer;

public class CallBack1Tests {

	@BeforeAll
	public static void setUp() {
		LinkedLogics.configure(new JdbcServiceConfigurer());
		LinkedLogics.registerLogic(CallBack1Tests.class);
		LinkedLogics.registerProcess(CallBack1Tests.class);
		LinkedLogics.launch();
	}

	@Test
	public void testScenario1() {
		AtomicBoolean result = new AtomicBoolean();
		String contextId = LinkedLogics.start(ContextBuilder.process("CALLBACK_SCENARIO_1").params("s", "hello").build(),
				new LinkedLogicsCallback() {
					
					@Override
					public void onTimeout() {
						
					}
					
					@Override
					public void onSuccess(Context context) {
 						String s = (String) context.getParams().get("s");
						result.set(s.equals("HELLO"));	
						result.notifyAll();
					}
					
					@Override
					public void onFailure(Context context, ContextError error) {
						
					}
				});
		assertThat(waitUntil(contextId, Status.FINISHED)).isTrue();
		try {
			result.wait();
		} catch (InterruptedException e) {}
		assertThat(result.get()).isTrue();
	}

	public static ProcessDefinition scenario1() {
		return createProcess("CALLBACK_SCENARIO_1", 0)
				.add(logic("STRING_UPPER").input("s", expr("s")).returnAs("s").build())
				.build();
	}
	
	@Test
	public void testScenario2() {
		AtomicBoolean result = new AtomicBoolean();
		String contextId = LinkedLogics.start(ContextBuilder.process("CALLBACK_SCENARIO_2").params("s", "hello").build(),
				new LinkedLogicsCallback() {
					
					@Override
					public void onTimeout() {
						
					}
					
					@Override
					public void onSuccess(Context context) {

					}
					
					@Override
					public void onFailure(Context context, ContextError error) {
						result.set(true);
						result.notifyAll();
					}
				});
		assertThat(waitUntil(contextId, Status.FAILED)).isTrue();
		try {
			result.wait();
		} catch (InterruptedException e) {}
		assertThat(result.get()).isTrue();
	}

	public static ProcessDefinition scenario2() {
		return createProcess("CALLBACK_SCENARIO_2", 0)
				.add(logic("STRING_UPPER").input("s", expr("s")).returnAs("s").build())
				.add(verify(expr("false")).build())
				.build();
	}

	@Logic(id = "STRING_UPPER")
	public static String upper(@Input("s") String s) {
		return s.toUpperCase();
	}
	
	@Logic(id = "STRING_LOWER")
	public static String lower(@Input("s") String s) {
		return s.toLowerCase();
	}
}

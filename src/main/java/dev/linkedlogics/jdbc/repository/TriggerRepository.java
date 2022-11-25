package dev.linkedlogics.jdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dev.linkedlogics.jdbc.service.JdbcDataSource;
import dev.linkedlogics.service.TriggerService.Trigger;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TriggerRepository {
	public static final String TABLE = "ll_trigger";

	private static final String INSERT = "INSERT INTO " + TABLE + " (context_id, waiting_context_id, waiting_position, created_at) "
			+ "VALUES(?, ?, ?, ?)";

	private static final String SELECT = "SELECT id, waiting_context_id, waiting_position FROM " + TABLE + " WHERE context_id = ?";
	private static final String DELETE = "DELETE FROM " + TABLE + " WHERE id = ?";

	protected DataSourceTransactionManager transactionManager;
	protected JdbcTemplate jdbcTemplate;

	public TriggerRepository() {
		this.jdbcTemplate = new JdbcTemplate(JdbcDataSource.getDataSource());
		this.transactionManager = new DataSourceTransactionManager(JdbcDataSource.getDataSource());
	}
	
	public void create(String contextId, Trigger trigger) {
		int result = jdbcTemplate.update(INSERT, 
				new Object[]{contextId, trigger.getContextId(), trigger.getPosition(), OffsetDateTime.now()},
				new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP});
		if (result == 0) {
			throw new RuntimeException();
		}
	}
	
	public List<Trigger> get(String contextId) {
		TransactionStatus txStatus = transactionManager.getTransaction(getTransactionDefinition());
		try {
			List<TriggerEntity> list = jdbcTemplate.query(SELECT, new Object[] {contextId}, new int[] {Types.VARCHAR}, new TriggerRowMapper());
			
			List<Trigger> triggerList = list.stream().map(t -> new Trigger(t.getWaitingContextId(), t.getWaitingPosition())).collect(Collectors.toList());
			
			list.forEach(t -> {
				jdbcTemplate.update(DELETE, new Object[] {t.getId()});
			});
			
			transactionManager.commit(txStatus);
			return triggerList;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			transactionManager.rollback(txStatus);
		}
		return List.of();
	}
	
	protected DefaultTransactionDefinition getTransactionDefinition() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		definition.setTimeout(3);
		return definition;
	}
	
	protected static class TriggerRowMapper implements RowMapper<TriggerEntity> {
		@Override
		public TriggerEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			TriggerEntity trigger = new TriggerEntity();
			trigger.setId(rs.getLong(1));
			trigger.setWaitingContextId(rs.getString(2));
			trigger.setWaitingPosition(rs.getString(3));
			return trigger;
		}
	}
	
	@NoArgsConstructor
	@Data
	protected static class TriggerEntity {
		private long id;
		private String waitingContextId;
		private String waitingPosition;
	}
}

package dev.linkedlogics.jdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.jdbc.service.JdbcDataSource;

public abstract class MessageRepository {
	protected DataSourceTransactionManager transactionManager;
	protected JdbcTemplate jdbcTemplate;
	
	public MessageRepository() {
		this.jdbcTemplate = new JdbcTemplate(JdbcDataSource.getDataSource());
		this.transactionManager = new DataSourceTransactionManager(JdbcDataSource.getDataSource());
	}
	
	public abstract void set(Message message);

	public abstract Optional<Message> get(String queue, String consumer);
	
	protected DefaultTransactionDefinition getTransactionDefinition() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		definition.setTimeout(3);
		return definition;
	}

	protected static class MessageRowMapper implements RowMapper<Message> {
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			Message message = new Message();
			message.setId(rs.getLong(1));
			message.setQueue(rs.getString(2));
			message.setPayload(rs.getString(3));
			message.setCreatedAt(OffsetDateTime.ofInstant(Instant.ofEpochMilli(rs.getTimestamp(4).getTime()), ZoneId.of("UTC")));
			message.setConsumedBy(rs.getString(5));
			return message;
		}
	}
}

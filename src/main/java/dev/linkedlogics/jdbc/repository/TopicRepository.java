package dev.linkedlogics.jdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dev.linkedlogics.jdbc.entity.Message;
import dev.linkedlogics.jdbc.service.DbDataSource;

public class TopicRepository extends MessageRepository {
	private static final String TABLE = "ll_topic";
	
	private static final String INSERT = "INSERT INTO " + TABLE + " (queue, payload, created_at) VALUES(?, ?, ?)";
	private static final String UPDATE = "INSERT INTO " + TABLE + "_consumed (id, created_at, consumed_by, consumed_at) VALUES(?, ?, ?, ?)"; 
	private static final String SELECT = "SELECT id, queue, payload, created_at, consumed_by FROM " + TABLE + 
			" WHERE queue = ? AND id NOT IN (SELECT id FROM " + TABLE + "_consumed WHERE consumed_by = ?) LIMIT 1";
	
	private static final String DELETE = "DELETE FROM "+ TABLE + " WHERE created_at < ?";
	private static final String DELETE_CONSUMED = "DELETE FROM "+ TABLE + "_consumed WHERE created_at < ?";

	private DataSourceTransactionManager transactionManager;
	private JdbcTemplate jdbcTemplate;

	public TopicRepository() {
		this.jdbcTemplate = new JdbcTemplate(DbDataSource.getDataSource());
		transactionManager = new DataSourceTransactionManager(DbDataSource.getDataSource());
	}

	public void set(Message message) {
		int result = jdbcTemplate.update(INSERT, 
				new Object[]{message.getQueue(), message.getPayload(), message.getCreatedAt()},
				new int[]{Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP});
		if (result == 0) {
			throw new RuntimeException();
		}
	}

	public Optional<Message> get(String queue, String consumer) {
		Message message = null;
		TransactionStatus txStatus = transactionManager.getTransaction(getTransactionDefinition());
		try {
			List<Message> list = jdbcTemplate.query(SELECT, new Object[] {queue, consumer}, new int[] {Types.VARCHAR, Types.VARCHAR}, new MessageRowMapper());
			if (list.size() > 0) {
				message = list.get(0);
				jdbcTemplate.update(UPDATE, new Object[] {message.getId(), message.getCreatedAt(), consumer, OffsetDateTime.now()}, new int[] {Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR, Types.TIMESTAMP});
			}
			transactionManager.commit(txStatus);
			return Optional.ofNullable(message);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(txStatus);
		}
		return Optional.empty();
	}
	
	public Optional<Message> clear(OffsetDateTime expiredAt) {
		Message message = null;
		TransactionStatus txStatus = transactionManager.getTransaction(getTransactionDefinition());
		try {
			int result = jdbcTemplate.update(DELETE, new Object[] {expiredAt});
			jdbcTemplate.update(DELETE_CONSUMED, new Object[] {expiredAt});
			transactionManager.commit(txStatus);
			System.out.println("CLEARED = " + result);
			return Optional.ofNullable(message);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(txStatus);
		}
		return Optional.empty();
	}
}

package io.linkedlogics.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.linkedlogics.jdbc.service.config.JdbcConnectionServiceConfig;
import io.linkedlogics.service.ConfigurableService;
import io.linkedlogics.service.LinkedLogicsService;

public class JdbcConnectionService extends ConfigurableService<JdbcConnectionServiceConfig> implements LinkedLogicsService {
	private static HikariDataSource dataSource;

	public JdbcConnectionService() {
		super(JdbcConnectionServiceConfig.class);
	}

	public DataSource getDataSource() {
		if (dataSource != null) {
			return dataSource;
		} else {
			synchronized (JdbcConnectionService.class) {
				if (dataSource != null) {
					return dataSource;
				} else {
					dataSource = initDataSource();
					return dataSource;
				}
			}
		}
	}
	
	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	protected HikariDataSource initDataSource() {
		HikariConfig config = new HikariConfig();

		config.setJdbcUrl(getConfig().getUrl());
		config.setUsername(getConfig().getUsername());
		config.setPassword(getConfig().getPassword());

		getConfig().getPoolMin().ifPresent(config::setMinimumIdle);
		getConfig().getPoolMax().ifPresent(config::setMaximumPoolSize);

		config.addDataSourceProperty("cachePrepStmts" , "true");
		config.addDataSourceProperty("prepStmtCacheSize" , "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
		config.setDriverClassName(getConfig().getDriver());
		return new HikariDataSource(config);
	}

}

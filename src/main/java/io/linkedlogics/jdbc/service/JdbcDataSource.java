package io.linkedlogics.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.linkedlogics.config.LinkedLogicsConfiguration;

public class JdbcDataSource {
	public static final String DB = "datasource";

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(getDbConfig("url").map(c -> c.toString()).orElseThrow(() -> new IllegalArgumentException("missing configuration " + DB + ".url")));
        config.setUsername(getDbConfig("username").map(c -> c.toString()).orElseThrow(() -> new IllegalArgumentException("missing configuration " + DB + ".username")));
        config.setPassword(getDbConfig("password").map(c -> c.toString()).orElseThrow(() -> new IllegalArgumentException("missing configuration " + DB + ".password")));
        getDbConfig("pool.min").ifPresent(c -> {
        	config.setMinimumIdle((Integer) c);
        });
        getDbConfig("pool.max").ifPresent(c -> {
        	config.setMaximumPoolSize((Integer) c);
        });
        config.addDataSourceProperty("cachePrepStmts" , "true");
        config.addDataSourceProperty("prepStmtCacheSize" , "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        config.setDriverClassName("org.h2.Driver");
        ds = new HikariDataSource(config);
    }

    private JdbcDataSource() {
    	
    }

    public static DataSource getDataSource() {
    	return ds;
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    private static Optional<Object> getDbConfig(String config) {
    	return LinkedLogicsConfiguration.getConfig(DB + "." + config);
    }
}
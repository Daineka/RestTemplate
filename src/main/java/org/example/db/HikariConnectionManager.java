package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionManager {

    private static final String URL_KEY = "jdbcUrl";
    private static final String USER_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String DRIVER_KEY = "driverClassName";

    private static HikariDataSource dataSource;

    private HikariConnectionManager() {
    }

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setUsername(PropertiesUtil.get(USER_KEY));
        config.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        config.setDriverClassName(PropertiesUtil.get(DRIVER_KEY));
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(2000000);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);

    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        dataSource = null;
    }
}

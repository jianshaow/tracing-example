package com.test.tracing.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(@Value("jdbc/test.ds") String jndiName) {
        return new JndiDataSourceLookup().getDataSource(jndiName);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

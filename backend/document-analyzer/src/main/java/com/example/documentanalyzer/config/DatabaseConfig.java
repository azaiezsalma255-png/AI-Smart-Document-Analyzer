package com.example.documentanalyzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/document_analyzer");
        ds.setUsername("document_user");
        ds.setPassword("0000");
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }
}
package com.kyu0.jungo.system.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("db.properties")
@Configuration
public class DbConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER_CLASS_NAME;
    @Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.username}")
    private String USER_NAME;
    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
            .driverClassName(DRIVER_CLASS_NAME)
            .url(URL)
            .username(USER_NAME)
            .password(PASSWORD)
        .build();
    }
}

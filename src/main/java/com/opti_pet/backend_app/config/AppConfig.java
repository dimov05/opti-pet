package com.opti_pet.backend_app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opti_pet.backend_app.persistence.repository.UserRepository;
import com.opti_pet.backend_app.service.CustomUserDetailsService;
import com.opti_pet.backend_app.service.UserService;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
public class AppConfig {

    @Value("${DBConnectionString}")
    private String connectionString;

    @Bean
    public DataSource getDataSource() {
        final DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(connectionString);

        return dataSourceBuilder.build();
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        final DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(connectionString);

        return Flyway.configure().dataSource(dataSourceBuilder.build()).schemas("opti-pet").outOfOrder(true).group(false).load();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        return mapper;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }
}

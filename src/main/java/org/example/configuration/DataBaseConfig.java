package org.example.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
public class DataBaseConfig {

    private static final int DEFAULT_TIMEOUT = 300;

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        jpaTransactionManager.setDefaultTimeout(DEFAULT_TIMEOUT);
        return jpaTransactionManager;
    }
}
package com.example.demo.student;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.demo.student",
        entityManagerFactoryRef = "studentEntityManagerFactory",
        transactionManagerRef = "studentTransactionManager"
)
public class StudentDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties studentDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource studentDataSource() {
        return studentDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "studentEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean studentEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(studentDataSource())
                .packages("com.example.demo.student")
                .persistenceUnit("student")
                .build();
    }

    @Bean(name = "studentTransactionManager")
    @Primary
    public PlatformTransactionManager studentTransactionManager(
            @Qualifier("studentEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}

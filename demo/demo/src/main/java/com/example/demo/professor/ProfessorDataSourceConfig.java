package com.example.demo.professor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.demo.professor",
        entityManagerFactoryRef = "professorEntityManagerFactory",
        transactionManagerRef = "professorTransactionManager"
)
public class ProfessorDataSourceConfig {

    @Bean
    @ConfigurationProperties("professor.datasource")
    public DataSourceProperties professorDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource professorDataSource() {
        return professorDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "professorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean professorEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(professorDataSource())
                .packages("com.example.demo.professor")
                .persistenceUnit("professor")
                .build();
    }

    @Bean(name = "professorTransactionManager")
    public PlatformTransactionManager professorTransactionManager(
            final @Qualifier("professorEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}

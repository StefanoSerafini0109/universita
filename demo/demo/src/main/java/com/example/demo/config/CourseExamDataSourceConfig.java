
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.example.demo.degreecourse", "com.example.demo.exam"},
        entityManagerFactoryRef = "courseExamEntityManagerFactory",
        transactionManagerRef = "courseExamTransactionManager"
)
public class CourseExamDataSourceConfig {

    @Bean
    @ConfigurationProperties("course.datasource")
    public DataSourceProperties courseDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource courseDataSource() {
        return courseDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "courseExamEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean courseExamEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(courseDataSource())
                .packages("com.example.demo.degreecourse", "com.example.demo.exam")
                .persistenceUnit("courseExam")
                .build();
    }

    @Bean(name = "courseExamTransactionManager")
    public PlatformTransactionManager courseExamTransactionManager(
            @Qualifier("courseExamEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}
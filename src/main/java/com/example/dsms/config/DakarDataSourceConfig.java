package com.example.dsms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.dsms.dakar.repository",
        entityManagerFactoryRef = "dakarEntityManagerFactory",
        transactionManagerRef = "dakarTransactionManager"
)
public class DakarDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.dakar")
    public DataSourceProperties dakarDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dakarDataSource() {
        return dakarDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean dakarEntityManagerFactory(
            @Qualifier("dakarDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.dsms.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(new HashMap<>() {{
            put("hibernate.hbm2ddl.auto", "update");
            put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        }});
        return em;
    }

    @Bean
    public PlatformTransactionManager dakarTransactionManager(
            @Qualifier("dakarEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}

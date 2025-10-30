// ==========================================
// 2. StlDataSourceConfig.java
// ==========================================
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
        basePackages = "com.example.dsms.stl.repository",
        entityManagerFactoryRef = "stlEntityManagerFactory",
        transactionManagerRef = "stlTransactionManager"
)
public class StlDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.stl")
    public DataSourceProperties stlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource stlDataSource() {
        return stlDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean stlEntityManagerFactory(
            @Qualifier("stlDataSource") DataSource dataSource) {

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
    public PlatformTransactionManager stlTransactionManager(
            @Qualifier("stlEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}
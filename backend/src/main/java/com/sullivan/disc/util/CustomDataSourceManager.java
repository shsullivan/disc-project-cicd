package com.sullivan.disc.util;

/**
 * <h1>Class: CustomDataSourceManager</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 18, 2025
 *  Normally Spring autocaches user login information from Windows system settings on boot and uses them to immediately
 *  scan for repositories and entities and begin mananging them, but the DMS Project requires that the user provide
 *  their own database login credentials to initialize the database. To accomplish that, Springs data source
 *  connectivity manager, entity manager, and transaction manager must be disabled and initialized manually after the
 *  user has provided their login credentials. The CustomDataSourceManager does that.
 */

import com.sullivan.disc.dto.DbLoginRequestDTO;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class CustomDataSourceManager {

    // Attributes
    private DataSource dataSource;
    private EntityManagerFactory entityManagerFactory;
    private PlatformTransactionManager transactionManager;

    /**
     * Method takes a DBLoginRequest DTO collected from the UI and uses that info to initialize
     * the data source for the App (MySQL Database)
     * @param request is a data transfer object generated from form post information sent from the web UI
     * containing database host and login information
     * @throws IllegalStateException if the EntityManagerFactory fails to initialize for any reason.
     */
    public void initDataSource(DbLoginRequestDTO request) throws SQLException {
        String jdbcUrl = "jdbc:mysql://" + request.host + ":" + request.port + "/disc_system";

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(request.username);
        ds.setPassword(request.password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Test the connection
        try (Connection conn = ds.getConnection()) {
            this.dataSource = ds;
        }

        // Set up JPA EntityManagerFactory so that Spring will begin entity management scanning after DB login
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(ds);
        factory.setPackagesToScan("com.sullivan.disc.model");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Set up JPA hibernate functionality after DB login info has been provided
        // This is normally autoconfigured by Spring on boot.
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaProperties.put("hibernate.show_sql", true);
        factory.setJpaPropertyMap(jpaProperties);

        factory.afterPropertiesSet();
        EntityManagerFactory emf = factory.getObject();
        if (emf == null) {
            throw new IllegalStateException("Could not get EntityManagerFactory");
        }

        this.entityManagerFactory = emf;
        this.transactionManager = new JpaTransactionManager(this.entityManagerFactory);
    }
}
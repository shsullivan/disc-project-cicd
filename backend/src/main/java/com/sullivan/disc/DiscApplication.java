package com.sullivan.disc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * <h1>D.I.S.C. Application</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 18, 2025
 * The DiscApplication program implements a DBMS application that mstores and manages information about
 * found disc golf discs in a dedicated database. A Spring Boot Rest API manages all interactions between the
 * React web based front end and the database.
 */
@SpringBootApplication
public class DiscApplication {

	/**
	 * This is the main method that initializes the Spring Boot Rest API that then connects to the DB backend
	 * and serves the web-based front end.
	 * @param args unused
     */
	public static void main(String[] args) {
		SpringApplication.run(DiscApplication.class, args);
	}

}

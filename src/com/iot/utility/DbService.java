package com.iot.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Class exposes database services to the application.
 * 
 */
public class DbService {
	private String drive = "";
	private String dburl = "";
	private String database = "";
	private String dbUsername = "";
	private String dbPassword = "";
	private String propFileName = "configuration.properties";
	
	public DbService(){
		Properties prop = new Properties();
		InputStream is = null;
				
		try {
			is = getClass().getClassLoader().getResourceAsStream(propFileName);
			prop.load(is);
			this.drive = prop.getProperty("driver.class.name");
			this.dburl = prop.getProperty("database.url");

			this.database = prop.getProperty("database.name");			
			this.dbUsername = prop.getProperty("database.username");
			this.dbPassword = prop.getProperty("database.password");
		} catch (FileNotFoundException e) {
			
			System.out.println("Class::"+DbService.class+" method::readAppProperties"+"An erorr has occurred."+ e);
		} catch (IOException e) {
			System.out.println("Class::"+DbService.class+" method::readAppProperties"+"An erorr has occurred."+ e);
		}		
		
	}
	public Connection getConnection() {
		Connection connection = null;		
		try {
			System.out.println("DB Url: "+dburl+ " database: "+database+" user name: "+dbUsername+" password: "+dbPassword);
			Class.forName(drive);
			connection = DriverManager.getConnection(
					dburl+database, dbUsername, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
}

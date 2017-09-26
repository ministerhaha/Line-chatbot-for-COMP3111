package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		/*
		Connection c = getConnection();
		Statement stmt = null;
		String result = null;
		ResultSet rs = stmt.executeQuery( "SELECT * FROM chatbotdb;" );
		try {
			stmt = c.createStatement();
			while(rs.next()) {
				String kw = rs.getString("keyword");
				String rp = rs.getString("response");
				if (text.toLowerCase().equals(kw.toLowerCase())) {
					result = rp;
				}
			}
		}catch(Exception e) {
			log.info("Exception while reading database: {}", e.toString());
		}finally {
			try {
				rs.close();
		        stmt.close();
		        c.close();
			} catch (Exception ex) {
				log.info("Exception while closing database: {}", ex.toString());
			}
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
		
		return null;*/
		String result = null;
		try {
			Connection c = this.getConnection();
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM chatbotdb;");
			ResultSet rs = stmt.executeQuery();
			String kw = null;
			String rp = null;
			while(rs.next()) {
				kw = rs.getString(1);
				rp = rs.getString(2);
				if (text.toLowerCase().equals(kw.toLowerCase())) {
					result = rp;
				}
			}
			rs.close();
			stmt.close();
			c.close(); 
		}catch(Exception e) {
			log.info("Exception while reading database: {}", e.toString());
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}

/**
 * Manage the communication with the ViSH database for only READ commands.
 */
package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import clustering.Canopy;

import entities.LearningObject;
import entities.UserProfile;

/**
 * @author Daniel Gallego Vico
 *
 */
public class VishDatabaseDriver {
	
	private Logger dbLogger = Logger.getLogger("VishDatabaseDriverLog");
	
	// to control the database connection
	private Statement statement;
	
	// database information
	private final String DB_DRIVER = "org.postgresql.Driver";
	private final String DB_NAME = "vish_production";
	private final String DB_USER = "recsys";
	private final String DB_PASS = "DaniRecommend";
	private final String DB_URL = "jdbc:postgresql://vishub.global.dit.upm.es/" + DB_NAME;
	
	/**
	 * Constructor 
	 */
	public VishDatabaseDriver() {
		
	}
	
	/**
	 * Connect with the database
	 */
	public void connect() {
		try {	
			// Register the JDBC driver for PostgreSQL
			Class.forName(DB_DRIVER);
		}
		catch (ClassNotFoundException e) {
			dbLogger.log(Level.WARNING, "Error while registering the JDBC driver for PostgreSQL");
			e.printStackTrace();
		}
		try {	
			// establish the connection
			Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			dbLogger.log(Level.INFO, "Connected to " + DB_URL);
			// save the statement
			statement = dbConnection.createStatement();
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while connecting to the database " + DB_NAME);
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the connection with the database
	 */
	public void close() {
		try {
			statement.close();
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while closing the connection with the database " + DB_NAME);
			e.printStackTrace();
		}
	}
	
	/*
	 ***************************************************************************
	 * USER PROFILE MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * READ
	 * 
	 * Extract from the database the source points 
	 * representing the users profiles
	 * 
	 * @return a list of user profiles
	 */
	public List<UserProfile> getUserProfiles() {
		// query that returns one row for every user as follows:
		// id (integer) | language (String) | array_agg (Strings[] subjects) | age_min (integer) | age_max (integer)
		String selectQuery = "SELECT users.id,users.language,array_agg(tags.name),activity_objects.age_min,activity_objects.age_max " +
				"FROM actors INNER JOIN users ON users.actor_id=actors.id INNER JOIN " +
				"profiles ON profiles.actor_id=actors.id INNER JOIN activity_objects ON " +
				"activity_objects.id=actors.activity_object_id LEFT OUTER JOIN taggings " +
				"ON taggings.taggable_type='ActivityObject' AND " +
				"taggings.taggable_id=activity_objects.id LEFT OUTER JOIN tags ON " + 
				"tags.id=taggings.tag_id WHERE actors.subject_type='User' GROUP BY " +
				"users.id,users.language,activity_objects.age_min,activity_objects.age_max"; 
				
		List<UserProfile> users = new ArrayList<UserProfile>();
		try {
			ResultSet result = statement.executeQuery(selectQuery);
			while(result.next()) {
				// user id
				int id = result.getInt("id");
				
				// might be multiple languages (TODO currently only one)
				String language = result.getString("language");
				List <String> languagesList = new ArrayList<String>();
				if(language != null) languagesList.add(language);
				
				// might be multiple subjects
				List <String> subjectsList = new ArrayList<String>();
				Array sqlArray = result.getArray("array_agg");
				String[] textArray = (String[])sqlArray.getArray();
				if(textArray[0] != null) subjectsList = new ArrayList<String>(Arrays.asList(textArray));
				
				// target students' age 
				int minAge = result.getInt("age_min");
				int maxAge = result.getInt("age_max");
				
				// add the user to the list
				UserProfile u = new UserProfile(id, subjectsList, languagesList, minAge, maxAge);
				users.add(u);
			}
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while fetching user profiles");
			e.printStackTrace();
		}
		return users;
	}
	
}

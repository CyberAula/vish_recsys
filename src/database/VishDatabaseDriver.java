/**
 * Manage the communication with the ViSH database for only READ commands.
 */
package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.security.User;

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
	 * representing the users profiles.
	 * 
	 * The users are ordered by their followers due to
	 * select as cluster centers first the "hub users".
	 * 
	 * 
	 * @return a list of user profiles
	 */
	public List<UserProfile> getUserProfiles() {
		// query that returns one row for every user (ordered by followers) as follows:
		// id (integer) | language (String) | array_agg (Strings[] subjects) | age_min (integer) | age_max (integer)
		String selectQuery = "SELECT actors.id,users.language,array_agg(tags.name),activity_objects.age_min,activity_objects.age_max,activity_objects.follower_count " +
				"FROM actors INNER JOIN users ON users.actor_id=actors.id INNER JOIN " +
				"profiles ON profiles.actor_id=actors.id INNER JOIN activity_objects ON " +
				"activity_objects.id=actors.activity_object_id LEFT OUTER JOIN taggings " +
				"ON taggings.taggable_type='ActivityObject' AND " +
				"taggings.taggable_id=activity_objects.id LEFT OUTER JOIN tags ON " +
				"tags.id=taggings.tag_id WHERE actors.subject_type='User' GROUP BY " +
				"actors.id,users.language,activity_objects.age_min,activity_objects.age_max,activity_objects.follower_count " +
				"ORDER BY activity_objects.follower_count DESC";
		
		List<UserProfile> users = new ArrayList<UserProfile>();
		try {
			ResultSet result = statement.executeQuery(selectQuery);
			while(result.next()) {
				// user id
				int id = result.getInt("id");
				
				// TODO might be multiple languages (currently only one)
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
			dbLogger.log(Level.WARNING, "Error while fetching user profiles from " + DB_NAME + " database");
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * READ
	 * 
	 * Extract from the database the profile corresponding 
	 * to the user with the id given 
	 * 
	 * @param userId
	 * @return
	 */
	public UserProfile getUserProfile(int userId) {
		UserProfile user = new UserProfile(userId);
		
		String selectQuery = "SELECT actors.id,users.language,array_agg(tags.name),activity_objects.age_min,activity_objects.age_max,activity_objects.follower_count " +
				"FROM actors INNER JOIN users ON users.actor_id=actors.id INNER JOIN " +
				"profiles ON profiles.actor_id=actors.id INNER JOIN activity_objects ON " +
				"activity_objects.id=actors.activity_object_id LEFT OUTER JOIN taggings " +
				"ON taggings.taggable_type='ActivityObject' AND " +
				"taggings.taggable_id=activity_objects.id LEFT OUTER JOIN tags ON tags.id=taggings.tag_id " +
				"WHERE actors.subject_type='User' AND actors.id=" + userId +
				" GROUP BY actors.id,users.language,activity_objects.age_min,activity_objects.age_max,activity_objects.follower_count";
		try {
			ResultSet result = statement.executeQuery(selectQuery);
			if(result.next()) {
				// user id
				int id = result.getInt("id");
				
				// TODO might be multiple languages (currently only one)
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
				
				user = new UserProfile(id, subjectsList, languagesList, minAge, maxAge);
			}
		}
		catch(SQLException e) {
			dbLogger.log(Level.WARNING, "Error while fetching the user profile whit id " + userId  + "from " + DB_NAME + " database");
			e.printStackTrace();
		}
		
		return user;
	}
	
	/*
	 ***************************************************************************
	 * LEARNING OBJECTS MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * Extract from the database the learning objects
	 * related to the user provided 
	 * 
	 * @param userId
	 * @return the list of LOs corresponding to the userId
	 */
	public List<LearningObject> getLOfromUser(UserProfile user) {
		List<LearningObject> LOs = new ArrayList<LearningObject>();
		// query that returns one row for every LO related to the user provided as follows:
		//  id (integer) | object_type (String) | array_agg (Strings[] subjects) | visit_count (integer)
		String selectQuery = "SELECT activity_objects.id,activity_objects.object_type,array_agg(tags.name),activity_objects.visit_count " +
				"FROM activity_objects INNER JOIN activity_object_activities ON " +
				"activity_object_activities.activity_object_id=activity_objects.id " +
				"INNER JOIN activities ON " +
				"activities.id=activity_object_activities.activity_id LEFT OUTER JOIN " +
				"taggings ON taggings.taggable_type='ActivityObject' AND " +
				"taggings.taggable_id=activity_objects.id LEFT OUTER JOIN tags ON " +
				"tags.id=taggings.tag_id WHERE activity_objects.object_type<>'Actor' " +
				"AND activities.author_id="+ user.getId() + " GROUP BY " +
				"activity_objects.id,activity_objects.object_type";
		try {
			ResultSet result = statement.executeQuery(selectQuery);
			while(result.next()) {
				// LO id
				int id = result.getInt("id");
				
				// LO type
				String type = result.getString("object_type");
				
				// TODO might be multiple languages (currently only one)
				//String language = result.getString("language");
				List <String> languagesList = new ArrayList<String>();
				//if(language != null) languagesList.add(language);
				
				// might be multiple subjects
				List <String> subjectsList = new ArrayList<String>();
				Array sqlArray = result.getArray("array_agg");
				String[] textArray = (String[])sqlArray.getArray();
				if(textArray[0] != null) subjectsList = new ArrayList<String>(Arrays.asList(textArray));
				
				// target students' age 
				int minAge = 4; //result.getInt("age_min");
				int maxAge = 30; //result.getInt("age_max");
				
				// number of times the LO has been used by ViSH users
				int timesUsed = result.getInt("visit_count");
				
				// add the LO to the list
				LearningObject lo = new LearningObject(id, type, subjectsList, languagesList, minAge, maxAge, timesUsed);
				LOs.add(lo);
			}
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while fetching learning objects from " + DB_NAME + " database");
			e.printStackTrace();
		}
		
		return LOs;
	}
	
	/**
	 * Query the top subjects in ViSH
	 * 
	 * @return topSubjects 
	 */
	public List<String> getTopSubjects(int top) {
		List<String> topSubjects = new ArrayList<String>();
		
		String query = "SELECT tags.name,COUNT(taggings.id) " +
					"FROM tags LEFT JOIN taggings ON taggings.tag_id=tags.id GROUP BY tags.id ORDER BY COUNT(taggings.id) DESC " +
					"LIMIT " + top;
		try {
			ResultSet result = statement.executeQuery(query);
			while(result.next()) {
				String subject = result.getString("name");
				topSubjects.add(subject);
			}
		}
		catch(SQLException e) {
			dbLogger.log(Level.WARNING, "Error while fetching top subjects from " + DB_NAME + " database");
			e.printStackTrace();
		}
		
		return topSubjects;
	}
	
}

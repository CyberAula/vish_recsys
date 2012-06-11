/**
 * Manage the database
 */
package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import clustering.Canopy;

import entities.LearningObject;
import entities.UserProfile;

/**
 * @author Daniel Gallego Vico
 *
 */
public class DatabaseDriver {
	
	private Logger dbLogger = Logger.getLogger("DatabaseDriverLog");
	
	// to control the database connection
	private Statement statement;
	
	// application that is going to use the database
	private final String APP_NAME = "vish_recsys";
	
	// database information
	private final String DB_NAME = "test";
	private final String DB_USER = "thanos";
	private final String DB_PASS = "duckbo0y";
	
	// database tables
	private final String CLUSTERS_TABLE = "clusters";
	private final String USER_PROFILE_TABLE = "users";
	private final String LEARNING_OBJECTS_TABLE = "";
	
	/**
	 * Constructor 
	 */
	public DatabaseDriver() {
		
	}
	
	/**
	 * Initialize the connection with the database
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void connect() throws ClassNotFoundException, SQLException {
		try {	
			// Register the JDBC driver for PostgreSQL
			Class.forName("org.postgresql.Driver");
			
			// URL of the database server
			String url = "jdbc:postgresql://localhost/" + DB_NAME;
			Properties props = new Properties();
			// the database user on whose behalf the connection is being made.
			props.setProperty("user", DB_USER);
			// the database user's password.
			props.setProperty("password", DB_PASS);
			// Connect using SSL. The driver must have been compiled with SSL support. 
			// This property does not need a value associated with it. 
			// The mere presence of it specifies a SSL connection. 
			// However, for compatibility with future versions, the value "true" is preferred.
			// TODO props.setProperty("ssl","true");
			// Specifies the name of the application that is using the connection. 
			props.setProperty("ApplicationName", APP_NAME);
			
			// stablish the connection
			Connection dbConnnection = DriverManager.getConnection(url, props);
			// save the statement
			statement = dbConnnection.createStatement();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException("Error while registering the JDBC driver for PostgreSQL");
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error while connecting to the database " + DB_NAME);
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
	public List<UserProfile> getUserProfiles() throws SQLException {
		String selectQuery = "SELECT * FROM " + USER_PROFILE_TABLE;
		ResultSet result = statement.executeQuery(selectQuery);
		List<UserProfile> users = new ArrayList<UserProfile>();
		try {
			while(result.next()) {
				int id = result.getInt(0);
				String s = result.getString(1);
				List <String> subjects = new ArrayList<String>();
				subjects.add(s);
				String l = result.getString(2);
				List<String> languages = new ArrayList<String>();
				languages.add(l);
				UserProfile user = new UserProfile(id, subjects, languages);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException ("Error while fetching user profiles: not found in DB " + USER_PROFILE_TABLE);
		}
		return users;
	}
	
	
	/*
	 ***************************************************************************
	 * SOCIAL CLUSTER MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * CREATE
	 * 
	 */
	public void createCluster (Canopy canopy) throws SQLException {
		// check if it exists
		String selectQuery = "SELECT * FROM " + CLUSTERS_TABLE + " WHERE " + " id  = " + canopy.getCanopyId();
		ResultSet result = statement.executeQuery(selectQuery);
		if(result.next()) {
			throw new SQLException("Cannot insert duplicated cluster with id: " + canopy.getCanopyId());
		}
	}
	
	/**
	 * READ 
	 * 
	 * @param canopy
	 */
	public Canopy getCluster(int canopyId) {
		Canopy canopy = new Canopy();
		
		return canopy;
	}
	
	/**
	 * READ (all)
	 * 
	 * @return
	 */
	public List<Canopy> getAllClusters() {
		List<Canopy> clusters = new ArrayList<Canopy>();
		
		return clusters;
	}
	
	/**
	 * UPDATE
	 * 
	 * @param canopy
	 */
	public void updateCluster (Canopy canopy) {
		
	}
	
	/**
	 * DELETE
	 * 
	 * @param canopy
	 */
	public void deleteCluster (Canopy canopy) {
		
	}
	
	
	/*
	 ***************************************************************************
	 * LEARNING OBJECTS MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * READ
	 * 
	 * @param userId
	 * @return
	 */
	public List<LearningObject> getLOUsedByUser(int userId) {
		List<LearningObject> lObjects = new ArrayList<LearningObject>();
		
		return lObjects;
	}
	
	
}

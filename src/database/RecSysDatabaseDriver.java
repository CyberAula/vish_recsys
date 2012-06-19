/**
 * Manage the communication with the ViSH RecSys database.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
public class RecSysDatabaseDriver {
	
private Logger dbLogger = Logger.getLogger("VishDatabaseDriverLog");
	
	// to control the database connection
	private Statement statement;
	
	// database information
	private final String DB_DRIVER = "org.postgresql.Driver";
	private final String DB_NAME = "vish_recsys_production";
	private final String DB_USER = "recsys";
	private final String DB_PASS = "DaniRecommend";
	private final String DB_URL = "jdbc:postgresql://vishub.global.dit.upm.es/" + DB_NAME;
	
	// USERS table
	// id (integer), clusterId (integer), position (integer)
	private final String TABLE_USERS = "users";
	private final String TABLE_USERS_ID = "id";
	private final String TABLE_USERS_CLUSTER_ID = "clusterId";
	private final String TABLE_USERS_POSITION = "position";
	
	// CLUSTERS table
	// id (integer), centerId (integer)
	private final String TABLE_CLUSTERS = "clusters";
	private final String TABLE_CLUSTERS_ID = "id";
	private final String TABLE_CLUSTERS_CENTER_ID = "centerId";
	private final String TABLE_CLUSTERS_SIZE = "size";
	 
	// LEARNING_OBJECTS table
	// id (integer), clusterId (integer), type (integer), position (integer)
	private final String TABLE_LO = "learning_objects";
	private final String TABLE_LO_ID = "id";
	private final String TABLE_LO_CLUSTER_ID = "clusterId";
	private final String TABLE_LO_TYPE = "type";
	private final String TABLE_LO_POSITION = "position";
	
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
	
	/**
	 * Create the tables after destroying the old data
	 */
	public void upgradeTables() {
		try {
			dbLogger.log(Level.INFO, "Upgrading the database " + DB_NAME + ", which will destroy all old data.");
			
			dbLogger.log(Level.INFO, "Droping the old version and creating the new table: " + TABLE_USERS);
			statement.execute("DROP TABLE IF EXISTS " + TABLE_USERS);
			String createUsersTableCmd = "CREATE TABLE " + TABLE_USERS + " (" +
													TABLE_USERS_ID + " integer, " +
													TABLE_USERS_CLUSTER_ID + " integer, " +
													TABLE_USERS_POSITION + " integer)";
			statement.execute(createUsersTableCmd);
			
			dbLogger.log(Level.INFO, "Droping the old version and creating the new table: " + TABLE_CLUSTERS);
			statement.execute("DROP TABLE IF EXISTS " + TABLE_CLUSTERS);
			String createClustersTableCmd = "CREATE TABLE " + TABLE_CLUSTERS + " (" + 
													TABLE_CLUSTERS_ID + " integer, " +
													TABLE_CLUSTERS_CENTER_ID + " integer, " +
													TABLE_CLUSTERS_SIZE + " integer)";
			statement.execute(createClustersTableCmd);
			
			dbLogger.log(Level.INFO, "Droping the old version and creating the new table: " + TABLE_LO);
			statement.execute("DROP TABLE IF EXISTS " + TABLE_LO);
			String createLOTableCmd = "CREATE TABLE " + TABLE_LO + " (" + 
													TABLE_LO_ID + " integer, " +
													TABLE_LO_CLUSTER_ID + " integer, " +
													TABLE_LO_TYPE + " varchar(255), " +
													TABLE_LO_POSITION + " integer)";
			statement.execute(createLOTableCmd);
		}
		catch(SQLException e) {
			dbLogger.log(Level.WARNING, "Error while upgrading the tables");
			e.printStackTrace();
		}
	}
	
	/*
	 ***************************************************************************
	 * SOCIAL CLUSTER MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * CREATE
	 * 
	 * Manage the creation of clusters and the users 
	 * related to them in the database 
	 * 
	 * @param canopy
	 */
	public void createCluster (Canopy canopy) {
		try {
			// check if the canopy exists in the database
			String selectQuery = "SELECT * FROM " + TABLE_CLUSTERS + " WHERE " + " id  = " + canopy.getCanopyId();
			ResultSet result = statement.executeQuery(selectQuery);
			if(result.next()) {
				dbLogger.log(Level.WARNING, "Cannot insert duplicated cluster with id: " + canopy.getCanopyId());
			}
			// create the cluster in the database and the users related to it
			else {
				statement.executeUpdate("INSERT INTO " + TABLE_CLUSTERS + " VALUES (" + 
										canopy.getCanopyId() + ", " +
										canopy.getCenter().getId() + ", " + 
										(canopy.getUsers().size()+1) +  ")");
				// the first user in the cluster is the center
				int position = 1;
				UserProfile center = canopy.getCenter();
				statement.executeUpdate("INSERT INTO " + TABLE_USERS + " VALUES (" +
									center.getId() + ", " + canopy.getCanopyId() + ", " + position + ")"); 
				// then the rest of the users are stored by their distance to the center
				Iterator<UserProfile> iter = canopy.getUsers().iterator();
				while(iter.hasNext()) {
					position++;
					UserProfile u = iter.next();
					statement.executeUpdate("INSERT INTO " + TABLE_USERS + " VALUES (" +
									u.getId() + ", " + canopy.getCanopyId() + ", " + position + ")");
				}
			}
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while creating a new cluster or user");
			e.printStackTrace();
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
		String query = "SELECT * FROM " + TABLE_CLUSTERS;
		try {
			ResultSet result = statement.executeQuery(query);
			while(result.next()) {
				// we only need the id
				int id = result.getInt(TABLE_CLUSTERS_ID);
				Canopy c = new Canopy(id);
				clusters.add(c);
			}
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while querying the table " + TABLE_CLUSTERS);
			e.printStackTrace();
		}
		
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
	 * USERS MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * READ
	 * 
	 * @param clusterId
	 * @return the list of users into the cluster
	 */
	public List<UserProfile> getUsersIntoCluster(int clusterId) {
		List<UserProfile> users = new ArrayList<UserProfile>();
		try {
			String usersIntoClusterQuery = "SELECT * FROM " + TABLE_USERS +  " WHERE " + TABLE_USERS_CLUSTER_ID + "=" + clusterId;
			ResultSet result =  statement.executeQuery(usersIntoClusterQuery);
			while(result.next()) {
				int userId = result.getInt(TABLE_USERS_ID);
				UserProfile u = new UserProfile(userId);
				users.add(u);
			}
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while retrieving the users into a cluster");
			e.printStackTrace();
		}
		return users;
	}
	
	
	/*
	 ***************************************************************************
	 * LEARNING OBJECTS MANAGEMENT
	 ***************************************************************************
	 */
	
	/**
	 * CREATE
	 * 
	 * Manage the creation of learning objects in the database
	 * 
	 * @param lo
	 * @param position
	 * @param cluster
	 */
	public void createLearningObject(LearningObject lo, int position, Canopy cluster) {
		try {
			statement.executeUpdate("INSERT INTO " + TABLE_LO + " VALUES (" + 
									lo.getId() + ", " + 
									cluster.getCanopyId() + ", " +
									"'" + lo.getType() + "', " +
									position + ")");
		}
		catch (SQLException e) {
			dbLogger.log(Level.WARNING, "Error while creating a new learning object");
			e.printStackTrace();
		}
		
	}
	
}

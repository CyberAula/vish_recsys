package manager;
/**
 * Class in charge of generating the social clusters in which the
 * users are gathered by similarity.
 * 
 * The clustering process is based on Canopy algorithm.
 * 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import clustering.Canopy;
import clustering.CanopyClusterer;


import database.RecSysDatabaseDriver;
import database.VishDatabaseDriver;
import distance.ViSHDistance;
import entities.UserProfile;

/**
 * @author Daniel Gallego Vico
 *
 */
public class SocialContextManager {

	// Canopy thresholds T1 > T2
	private final double T1 = 8;
	private final double T2 = 4;
	
	// Social clusters
	private List<Canopy> clusters;
	
	private Logger log = Logger.getLogger("SocialContextManagerLog");
	
	/**
	 * Executes the user profile clustering process
	 */
	public void doUserProfileClustering() {
		log.log(Level.INFO, "User profile clustering started");
		
		// connecting to the database ViSH database
		VishDatabaseDriver vishDB = new VishDatabaseDriver();
		vishDB.connect();
		// configure the Canopy clusterer
		ViSHDistance measure = new ViSHDistance();
		CanopyClusterer clusterer = new CanopyClusterer(measure, T1, T2);
		// create the social clusters based on the user profiles stored in the ViSH database
		List <UserProfile> source = vishDB.getUserProfiles();
		clusters = clusterer.createCanopies(source);
		// close the connection with the ViSH database
		vishDB.close();
		
		// connecting to the database RecSys database
		RecSysDatabaseDriver recsysDB = new RecSysDatabaseDriver();
		recsysDB.connect();
		// upgrade the tables related to clusters, users and learning objects
		recsysDB.upgradeTables();
		// save the social clusters in the RecSys database
		Iterator<Canopy> iter = clusters.iterator();
		while(iter.hasNext()) {
			Canopy c = iter.next();
			recsysDB.createCluster(c);
		}
		// close the connection with the RecSys database
		recsysDB.close();
		
		log.log(Level.INFO, "User profile clustering finished");
	}
	
	/**
	 * Assigns the set of Learning Objects (LO) related to the users
	 * belonging to that cluster
	 */
	public void doLOAssignment(Canopy canopy) {
		// connecting to the database
		VishDatabaseDriver db = new VishDatabaseDriver();
		db.connect();
		
		// close the database connection
		db.close();
	}
	
	/**
	 * Identifies the closest cluster to the target user
	 * 
	 * @param targetUser
	 * @return the id of the closest cluster
	 */
	public int discoverUserCluster(UserProfile targetUser) {
		int closestCanopyId = -1;
		
		// connecting to the RecSys database
		RecSysDatabaseDriver db = new RecSysDatabaseDriver();
		db.connect();
		// retrieve the clusters
		List<Canopy> clusters = db.getAllClusters();
		ViSHDistance measure = new ViSHDistance();
		// iterate over all the clusters to find the closest to the target user
		Iterator<Canopy> iter = clusters.iterator();
		double minDist = 1000;
		while(iter.hasNext()) {
			Canopy c = iter.next();
			double distance = measure.calculateDistance(c.getCenter(), targetUser);
			if(distance < minDist) {
				distance = minDist;
				closestCanopyId = c.getCanopyId();
			}
		}
		// close the database connection
		db.close();
				
		return closestCanopyId;
	}

}

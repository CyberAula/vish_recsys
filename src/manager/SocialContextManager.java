package manager;
/**
 * Class in charge of generating the social clusters in which the
 * users are gathered by similarity.
 * 
 * The clustering process is based on Canopy algorithm.
 * 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import clustering.Canopy;
import clustering.CanopyClusterer;


import database.RecSysDatabaseDriver;
import database.VishDatabaseDriver;
import distance.ViSHDistance;
import entities.LearningObject;
import entities.UserProfile;

/**
 * @author Daniel Gallego Vico
 *
 */
public class SocialContextManager {

	// Canopy thresholds T1 > T2
	private final double T1 = 3;
	private final double T2 = 1;
	
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
	 * Assigns the Learning Objects (LO) related to every cluster
	 */
	public void doLOAssignment() {
		log.log(Level.INFO, "Learning Object Assignment started");
		
		// connecting to the ViSH database
		VishDatabaseDriver vishDb = new VishDatabaseDriver();
		vishDb.connect();
		// connecting to the RecSys database
		RecSysDatabaseDriver recsysDb = new RecSysDatabaseDriver();
		recsysDb.connect();
		
		// iterate over all the clusters
		List<Canopy> clusters = recsysDb.getAllClusters();
		Iterator<Canopy> iter = clusters.iterator();
		while(iter.hasNext()) {
			// the list of LOs related to the cluster
			List<LearningObject> clusterLOs = new ArrayList<LearningObject>();
			Canopy c = iter.next();
			// get the users into the cluster
			List<UserProfile> users = recsysDb.getUsersIntoCluster(c.getCanopyId());
			
			// Iterate over all the users into a cluster to get their LOs related
			Iterator<UserProfile> usersIte = users.iterator();
			while(usersIte.hasNext()) {
				UserProfile u = usersIte.next();
				// add the LOs to the list
				clusterLOs.addAll(vishDb.getLOfromUser(u));				
			}
			
			// TODO sort the LOs into the cluster by their distance to the cluster center
			// currently they are sorted taking into account that user's order

			
			// iterate over all the LOs to add them to the RecSys database
			Iterator<LearningObject> LOiter = clusterLOs.iterator();
			int position = 0;
			while(LOiter.hasNext()) {
				LearningObject lo = LOiter.next();
				recsysDb.createLearningObject(lo, position++, c);
			}
		}
		
		// close the database connections
		vishDb.close();
		recsysDb.close();
		
		log.log(Level.INFO, "Learning Object Assignment finished");
	}
	
	/**
	 * Identifies the closest cluster to the target user
	 * 
	 * @param targetUser
	 * @return the id of the closest cluster
	 */
	public int discoverUserCluster(UserProfile targetUser) {
		log.log(Level.INFO, "Discovering the closest cluster to the user with id: " + targetUser.getId());
		
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
	
	/**
	 * 
	 * @return detailed information about the clusters generated
	 */
	public String getClustersInformation() {
		String info = "\n";
		
		// connecting to the RecSys database
		RecSysDatabaseDriver recsysDb = new RecSysDatabaseDriver();
		recsysDb.connect();
		
		// Information about number of clusters
		List <Canopy> clusters = recsysDb.getAllClusters();
		int numberOfClusters = clusters.size();
		info = info + "Number of social clusters created: " + numberOfClusters + "\n";
		
		// Information about users in every cluster
		Iterator<Canopy> clusterIter = clusters.iterator();
		while(clusterIter.hasNext()) {
			Canopy cluster = clusterIter.next();
			List<UserProfile> usersIntoCluster = recsysDb.getUsersIntoCluster(cluster.getCanopyId());
			info = info + "Cluster " + cluster.getCanopyId() + " : " + usersIntoCluster.size() + " users\n";
		}
		
		// close the database connections
		recsysDb.close();
		
		info = info + "\n";
		return info;
	}

}

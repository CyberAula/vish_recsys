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
	private final double T1 = 4;
	private final double T2 = 2;
	
	// Number of top subjects used in ViSH
	private final int TOP_SUBJECTS = 5;
	
	// Social clusters
	private List<Canopy> clusters;
	// User profiles
	private List<UserProfile> users;
	
	// Logger
	private Logger log = Logger.getLogger("SocialContextManagerLog");
	
	/**
	 * Generates the social context related to the ViSH users by carrying on:
	 * - the user profile clustering
	 * - the learning objects assignment
	 */
	public void generateSocialContext() {
		// connecting to the ViSH database
		VishDatabaseDriver vishDB = new VishDatabaseDriver();
		vishDB.connect();
		// connecting to the RecSys database
		RecSysDatabaseDriver recsysDB = new RecSysDatabaseDriver();
		recsysDB.connect();
		
		doUserProfileClustering(vishDB, recsysDB);
		doLOAssignment(vishDB, recsysDB);
		
		// close the connection with the ViSH database
		vishDB.close();
		// close the connection with the RecSys database
		recsysDB.close();
	}

	/**
	 * Executes the user profile clustering process
	 */
	private void doUserProfileClustering(VishDatabaseDriver vishDB, RecSysDatabaseDriver recsysDB) {
		log.log(Level.INFO, "User profile clustering started");
		
		// configure the Canopy clusterer
		ViSHDistance measure = new ViSHDistance();
		CanopyClusterer clusterer = new CanopyClusterer(measure, T1, T2);
		// create the social clusters based on 
		// the user profiles stored in the ViSH database
		// and the top subjects used in ViSH
		users = vishDB.getUserProfiles();
		List <String> topSubjects = vishDB.getTopSubjects(TOP_SUBJECTS);
		clusters = clusterer.createCanopies(users, topSubjects);
		
		// upgrade the tables related to clusters, users and learning objects
		recsysDB.upgradeTables();
		// save the social clusters in the RecSys database
		Iterator<Canopy> iter = clusters.iterator();
		while(iter.hasNext()) {
			Canopy c = iter.next();
			recsysDB.createCluster(c);
		}
		
		log.log(Level.INFO, "User profile clustering finished");
	}
	
	/**
	 * Assigns the Learning Objects related to every cluster
	 */
	private void doLOAssignment(VishDatabaseDriver vishDB, RecSysDatabaseDriver recsysDB) {
		log.log(Level.INFO, "Learning Object Assignment started");
		
		// iterate over all the clusters
		Iterator<Canopy> clusterIter = clusters.iterator();
		while(clusterIter.hasNext()) {
			// the list of LOs related to the cluster
			List<LearningObject> clusterLOs = new ArrayList<LearningObject>();
			Canopy cluster = clusterIter.next();
			// get the users into the cluster
			List<UserProfile> users = recsysDB.getUsersIntoCluster(cluster.getCanopyId());
			
			// Iterate over all the users into a cluster to get their LOs related
			Iterator<UserProfile> usersIte = users.iterator();
			while(usersIte.hasNext()) {
				UserProfile u = usersIte.next();
				// add the LOs to the list
				clusterLOs.addAll(vishDB.getLOfromUser(u));				
			}
			// TODO sort the LOs into the cluster by their distance to the cluster center
			// currently they are sorted taking into account that user's order
			
			// iterate over all the LOs to add them to the RecSys database
			Iterator<LearningObject> LOiter = clusterLOs.iterator();
			int position = 1;
			while(LOiter.hasNext()) {
				LearningObject lo = LOiter.next();
				recsysDB.createLearningObject(lo, position++, cluster);
			}
		}
		
		log.log(Level.INFO, "Learning Object Assignment finished");
	}
	
	/**
	 * Identifies the closest cluster to the target user
	 * 
	 * @param targetUser
	 * @return the id of the closest cluster
	 */
	public int discoverUserCluster(int targetUserId) {
		log.log(Level.INFO, "Discovering the closest cluster to the user with id: " + targetUserId);
		
		int closestCanopyId = -1;
		
		// connecting to the ViSH database
		VishDatabaseDriver vishDB = new VishDatabaseDriver();
		vishDB.connect();
		// connecting to the RecSys database
		RecSysDatabaseDriver recsysDB = new RecSysDatabaseDriver();
		recsysDB.connect();
		
		// if the user is into an existing cluster, return its cluster id
		int userClusterId = recsysDB.getUserClusterId(targetUserId);
		if(userClusterId != -1)  {
			closestCanopyId = userClusterId;
		}
		// if the user is new in ViSH, discover the closest cluster among the existing ones
		else {
			// retrieve the target user profile
			UserProfile targetUser = vishDB.getUserProfile(targetUserId);
			
			// retrieve the social clusters
			List<Canopy> clusters = recsysDB.getAllClusters();
			ViSHDistance measure = new ViSHDistance();
			
			// iterate over all the clusters to find the closest to the target user
			Iterator<Canopy> iter = clusters.iterator();
			double minDist = 1000;
			while(iter.hasNext()) {
				Canopy c = iter.next();
				double distance = measure.calculateDistance(c.getCenter(), targetUser);
				if(distance < minDist) {
					minDist = distance;
					closestCanopyId = c.getCanopyId();
				}
			}
		}
		
		// close the database connections
		vishDB.close();
		recsysDB.close();
				
		return closestCanopyId;
	}
	
	/**
	 * 
	 * @return detailed information about the clusters generated
	 */
	public String getClustersInformation() {
		String info = "\n" + "*****************************************" + "\n";
		
		// connecting to the RecSys database
		RecSysDatabaseDriver recsysDb = new RecSysDatabaseDriver();
		recsysDb.connect();
		
		// Information about number of clusters
		List <Canopy> clusters = recsysDb.getAllClusters();
		int numberOfClusters = clusters.size();
		info = info + "Number of social clusters created: " + numberOfClusters + "\n";
		info = info + "*****************************************" + "\n";
		
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

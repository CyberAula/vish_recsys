import java.util.ArrayList;
import java.util.List;

import clustering.Canopy;
import clustering.CanopyClusterer;


import distance.ViSHDistance;
import entities.UserProfile;


/**
 * Class in charge of generating the social clusters in which the
 * users are gathered by similarity.
 * 
 * The clustering process is based on Canopy algorithm.
 * 
 */

/**
 * @author Daniel Gallego Vico
 *
 */
public class SocialContextManager {

	// Canopy thresholds T1 > T2
	private final double T1 = 8;
	private final double T2 = 4;
	
	/**
	 * Method in charge of executing the user profile clustering process
	 */
	public void doUserProfileClustering() {
		CanopyClusterer clusterer = new CanopyClusterer(new ViSHDistance(), T1, T2);
		List<Canopy> canopies = clusterer.createCanopies(getSourcePoints());
	}
	
	/**
	 * Assign the set of Learning Objects (LO) related to the users
	 * belonging to that cluster
	 */
	public void doLOAssignment(Canopy canopy) {
		
	}
	
	/**
	 * Identifies the cluster a user belongs to
	 * 
	 * @param user
	 * @return the cluster
	 */
	public Canopy discoverUserCluster(UserProfile user) {
		Canopy userCanopy = new Canopy();
		
		return userCanopy;
	}
	
	/**
	 * Extract from the database the source points representing the
	 * users profiles to generate the social clusters
	 * 
	 * @return a list of users profiles
	 */
	private List<UserProfile> getSourcePoints() {
		List<UserProfile> users = new ArrayList<UserProfile>();
		
		// TODO extract users from databse
		
		// u1
		List<String> subjects1 = new ArrayList<String>();
		subjects1.add("biology");
		List<String> languages1 = new ArrayList<String>();
		languages1.add("english");
		UserProfile u1 = new UserProfile(1, subjects1, languages1);
		
		//u2
		List<String> subjects2 = new ArrayList<String>();
		subjects2.add("bilogia");
		List<String> languages2 = new ArrayList<String>();
		languages2.add("spanish");
		UserProfile u2 = new UserProfile(2, subjects2, languages2);
		
		//u3
		List<String> subjects3 = new ArrayList<String>();
		subjects3.add("nanotechnology");
		List<String> languages3 = new ArrayList<String>();
		languages3.add("english");
		UserProfile u3 = new UserProfile(3, subjects3, languages3);
		
		//u4
		List<String> subjects4 = new ArrayList<String>();
		subjects4.add("nanotechnology");
		List<String> languages4 = new ArrayList<String>();
		languages4.add("spanish");
		UserProfile u4 = new UserProfile(4, subjects4, languages4);
		
		
		users.add(u1);
		users.add(u3);
		users.add(u2);
		users.add(u4);
		
		return users;
	}
	

	public static void main(String[] args) {
		SocialContextManager s = new SocialContextManager();
		s.doUserProfileClustering();
		
	}
}

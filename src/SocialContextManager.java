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
	 * Extract from the database the source points representing the
	 * users profiles to generate the social clusters
	 * 
	 * @return a list of users profiles
	 */
	private List<UserProfile> getSourcePoints() {
		List<UserProfile> users = new ArrayList<UserProfile>();
		
		// TODO extract users from databse
		
		UserProfile u1 = new UserProfile(1, "bilogy", "english");
		UserProfile u2 = new UserProfile(2, "bilogia", "spanish");
		UserProfile u3 = new UserProfile(3, "nanotechnology", "english");
		UserProfile u4 = new UserProfile(4, "nanotechnology", "spanish");
		
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

/**
 * Class in charge of carrying on the canopy clustering process
 */
package clustering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import distance.ViSHDistance;
import entities.UserProfile;

/**
 * @author Daniel Gallego Vico
 *
 */
public class CanopyClusterer {
	
	// the T1 distance threshold
	private double t1;

	// the T2 distance threshold
	private double t2;
	
	private ViSHDistance measure;

	/**
	 * Constructor
	 * 
	 * @param measure
	 * @param t1
	 * @param t2
	 */
	public CanopyClusterer(ViSHDistance measure, double t1, double t2) {
		this.t1 = t1;
	    this.t2 = t2;
	    this.measure = measure;
	}
	
	/**
	 * @return the t1
	 */
	public double getT1() {
		return t1;
	}

	/**
	 * @param t1 the t1 to set
	 */
	public void setT1(double t1) {
		this.t1 = t1;
	}

	/**
	 * @return the t2
	 */
	public double getT2() {
		return t2;
	}

	/**
	 * @param t2 the t2 to set
	 */
	public void setT2(double t2) {
		this.t2 = t2;
	}

	/**
	 * @return the measure
	 */
	public ViSHDistance getMeasure() {
		return measure;
	}

	/**
	 * @param measure the measure to set
	 */
	public void setMeasure(ViSHDistance measure) {
		this.measure = measure;
	}


	/**
	 * Calculate if the point is covered by the canopy
	 * @param a point
	 * @return true if the point is covered by the canopy
	 */ 
	public boolean canopyCovers(Canopy canopy, UserProfile point) {
		boolean covered = false;
		UserProfile center = canopy.getCenter();
		double dist = measure.calculateDistance(center, point);
		covered = (dist<t1) ? true : false;
		return covered;
	}
	
	/**
	 ************************** 
	 * FOLK CANOPY 
	 **************************
	 * (This is a modified version of Canopy reference implementation 
	 * adapted to the ViSH use case)
	 * 
	 * The algorithm begins with a set of user profiles sorted by their 
	 * number of followers. This is done to consider first the “social hub users” 
	 * as canopy centers, instead of picking them at random.
	 * 
	 * Therefore, when the first user is taken (and removed from the list), 
	 * it is used to create a “centroid” that will store the most relevant subjects 
	 * related to the folksonomy used in ViSH starting with the first user’s subjects. 
	 * As a result, the centroid is the center to create the canopy, and the 
	 * user is added to the canopy users list.
	 * 
	 * Then, the algorithm iterates through the remainder of the point set. 
	 * At each point, if its distance from the first point is < T1, then the point 
	 * is added to the cluster. If, in addition, the distance is < T2, then the point 
	 * is removed from the set and their subjects are added to the centroid’s subjects. 
	 * It has to be taken into account that only the top subjects (tags) used in ViSH 
	 * are added to the centroid’s subjects. This way points that are very close to the 
	 * centroid will avoid all further processing saving computational time. 
	 * 
	 * The algorithm loops until the initial set is empty, accumulating a set of canopies, 
	 * each containing one or more points. A given point may occur in more than one canopy.
	 * 
	 * ==> NOTE: Taking into account that could exist grey users without 
	 * user profile attributes information, they will be gathered in a  
	 * special canopy called greyUsersCanopy
	 * 
	 * 
	 * @param points, a list<Vector> defining the points to be clustered
	 * @param topSubjects, the feature subjects that are more important in the point space analyzed
	 * @return the List<Canopy> created
	 */
	public List<Canopy> createCanopies(List<UserProfile> points, List<String> topSubjects) {
		List<Canopy> canopies = new ArrayList<Canopy>();

		int nextCanopyId = 0;
		Canopy greyUsersCanopy = new Canopy();
		while (!points.isEmpty()) {
			Iterator<UserProfile> ptIter = points.iterator();
			UserProfile p1 = ptIter.next();
			ptIter.remove();
			
			// condition for grey users (users without subjects of interest)
			if (p1.getSubjects().isEmpty()) {
				// if it is the first user in the "grey" canopy, 
				// it is set as the center and it is added to the canopy
				if(greyUsersCanopy.getCenter() == null ) {
					greyUsersCanopy = new Canopy(p1, nextCanopyId++);
					greyUsersCanopy.addUser(p1);
				}
				else {
					greyUsersCanopy.addUser(p1);
				}
				continue;
			}
			
			// a clone of the point picked becomes the centroid of the new canopy 
			Canopy canopy = new Canopy(p1, nextCanopyId++);
			// the point is added to the canopy with distance to the center 0
			p1.setDistanceToCenter(0);
			canopy.addUser(p1);
			// add the new canopy to the canopy list
			canopies.add(canopy);
			
			while (ptIter.hasNext()) {
				UserProfile p2 = ptIter.next();
				double dist = measure.calculateDistance(canopy.getCenter(), p2);
		        // Put all points that are within distance threshold T1 into the canopy
		        if (dist < t1) {
		        	p2.setDistanceToCenter(dist);
		        	canopy.addUser(p2);
		        }
		        // Remove from the list all points that are within distance threshold T2
		        if (dist < t2) {
		        	ptIter.remove();
		        	// Add those new subjects to the centroid's subjects
		        	// if they belong to the top ViSH subjects 
		        	canopy.getCenter().addTopSubjects(p2.getSubjects(), topSubjects);
		        }
			}
			// sort the users into the canopy by their distance to the centroid
			canopy.sortUsersByDistance();
		}
		// sort the grey users canopy and add it to the canopies list
		greyUsersCanopy.sortUsersByDistance();
		canopies.add(greyUsersCanopy);
		
		return canopies;
	}
	
}


//public List<Canopy> createCanopies(List<UserProfile> points) {
//List<Canopy> canopies = new ArrayList<Canopy>();
///*
// *************************** 
// * Reference Implementation
// ***************************
// * Given a distance metric, one can create canopies as follows: 
// * Start with a list of the data points in any order,
// * and with two distance thresholds, T1 and T2, where T1 > T2.
// * (These thresholds can be set by the user, or selected by cross-validation.) 
// * Pick a point on the list and measure its distance to all other points. 
// * Put all points that are within distance threshold T1 into a canopy. 
// * Remove from the list all points that are within distance threshold T2. 
// * Repeat until the list is empty.
// * 
// * --> MODIFICATIONS: 
// * 1) Taking into account that could exist grey users without 
// * user profile attributes information, they will be gathered in a  
// * special canopy called greyUsersCanopy
// * 
// * 2) The list of users evaluated have been previously sorted by their number  
// * of followers, so as to have as canopy centers the "hub users"
// * 
// */
//int nextCanopyId = 0;
//Canopy greyUsersCanopy = new Canopy();
//while (!points.isEmpty()) {
//	Iterator<UserProfile> ptIter = points.iterator();
//	UserProfile p1 = ptIter.next();
//	ptIter.remove();
//	
//	// condition for grey users (users without subjects of interest)
//	if (p1.getSubjects().isEmpty()) {
//		// if it is the first user in the "grey" canopy, it is set as the center
//		if(greyUsersCanopy.getCenter() == null ) {
//			greyUsersCanopy = new Canopy(p1, nextCanopyId++);
//		}
//		else {
//			greyUsersCanopy.addUser(p1);
//		}
//		continue;
//	}
//	
//	// the point picked is the center of the new canopy 
//	Canopy canopy = new Canopy(p1, nextCanopyId++);
//	canopies.add(canopy);
//	
//	while (ptIter.hasNext()) {
//		UserProfile p2 = ptIter.next();
//		double dist = measure.calculateDistance(p1, p2);
//        // Put all points that are within distance threshold T1 into the canopy
//        if (dist < t1) {
//        	p2.setDistanceToCenter(dist);
//        	canopy.addUser(p2);
//        }
//        // Remove from the list all points that are within distance threshold T2
//        if (dist < t2) {
//        	ptIter.remove();
//        }
//	}
//	// sort the users into the canopy
//	canopy.sortUsersByDistance();
//}
//// sort the grey users canopy and add it to the canopies list
//greyUsersCanopy.sortUsersByDistance();
//canopies.add(greyUsersCanopy);
//
//return canopies;
//}

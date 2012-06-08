/**
 * Class in charge of carrying on the canopy clustering process
 */
package clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	 * Iterate through the points, adding new canopies. Return the list of canopies
	 * 
	 * @param points
	 * 				a list<Vector> defining the points to be clustered
	 * @param measure
	 * 				a DistanceMeasure to use
	 * @param t1
	 * 				the T1 distance threshold
	 * @param t2
	 * 				the T2 distance threshold
	 * @return the List<Canopy> created
	 */
	public List<Canopy> createCanopies(List<UserProfile> points) {
		List<Canopy> canopies = new ArrayList<Canopy>();
		/*
	     *************************** 
	     * Reference Implementation
	     ***************************
	     * Given a distance metric, one can create canopies as follows: 
	     * Start with a list of the data points in any order,
	     * and with two distance thresholds, T1 and T2, where T1 > T2.
	     * (These thresholds can be set by the user, or selected by cross-validation.) 
	     * Pick a point on the list and measure its distance to all other points. 
	     * Put all points that are within distance threshold T1 into a canopy. 
	     * Remove from the list all points that are within distance threshold T2. 
	     * Repeat until the list is empty.
	     */
		int nextCanopyId = 0;
		while (!points.isEmpty()) {
			Iterator<UserProfile> ptIter = points.iterator();
			UserProfile p1 = (UserProfile)ptIter.next();
			ptIter.remove();
			
			Canopy canopy = new Canopy(p1, nextCanopyId++);
			canopies.add(canopy);
			
			while (ptIter.hasNext()) {
				UserProfile p2 = ptIter.next();
				double dist = measure.calculateDistance(p1, p2);
		        // Put all points that are within distance threshold T1 into the canopy
		        if (dist < t1) {
		        	p2.setDistanceToCenter(dist);
		        	canopy.addUser(p2);
		        }
		        // Remove from the list all points that are within distance threshold T2
		        if (dist < t2) {
		        	ptIter.remove();
		        }
			}
			// sort the users into the canopy
			canopy.sortUsersByDistance();
		}
		return canopies;
	}
	
}

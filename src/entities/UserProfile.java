/**
 * 
 */
package entities;

import java.util.List;

/**
 * @author Daniel Gallego Vico
 *
 */
public class UserProfile implements Comparable<UserProfile> {
	
	private int  userId;
	// list of subjects of interest
	private List<String> subjects;
	// list of languages the user is able to understand
	private List<String> languages;
	// the distance between this point and the 
	// center of the canopy it belongs to
	private double distanceToCenter;
	
	/**
	 * Constructor
	 * 
	 * @param subject
	 * @param language
	 */
	public UserProfile(int userId, List<String> subjects, List<String> languages) {
		this.userId = userId;
		this.subjects = subjects;
		this.languages = languages;
	}

	/**
	 * 
	 * @return the userId
	 */
	public int getId() {
		return userId;
	}
	
	/**
	 * @return the subject
	 */
	public List<String> getSubjects() {
		return subjects;
	}

	/**
	 * @return the language
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * @return the distanceToCenter
	 */
	public double getDistanceToCenter() {
		return distanceToCenter;
	}
	
	/**
	 * Set the distance between this point and the 
	 * center of the canopy it belongs to
	 * 
	 * @param dist
	 */
	public void setDistanceToCenter(double dist) {
		this.distanceToCenter = dist;
	}
	
	/**
	 * Compare a given UserProfile with this object.
	 * If distanceToCenter is greater than the received object,
	 * then this object is greater than the other.
	 */
	public int compareTo(UserProfile u) {
		return  (int)(this.distanceToCenter - u.distanceToCenter);
	}
}

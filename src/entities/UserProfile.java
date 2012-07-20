/**
 * Define a user profile in ViSH as follows:
 * U:= <subjects, languages, targetLevel, followers, role, organization, country, city> 
 */
package entities;

import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Gallego Vico
 *
 */
public class UserProfile implements Comparable<UserProfile> {
	
	/********** ATTRIBUTTES **********/
	private int  userId;
	// list of subjects of interest
	private List<String> subjects;
	// list of languages the user is able to understand
	private List<String> languages;
	// target level described by the min and max age the user is focused on
	private int minTargetLevel;
	private int maxTargetLevel;
	// ViSH followers
	private int followers;
	// the context related to the user in his daily activity
	private String role;
	// more personal information
	private String organization;
	private String country;
	private String city;
	
	
	// the distance between this point and the 
	// center of the canopy the user belongs to
	private double distanceToCenter;
	
	/**
	 * Constructor
	 * 
	 * @param userId
	 */
	public UserProfile(int userId) {
		this.userId = userId;
	}
	
	/**
	 * Constructor
	 * 
	 * @param userId
	 * @param subjects
	 * @param languages
	 * @param minAge
	 * @param maxAge
	 */
	public UserProfile(int userId, List<String> subjects, List<String> languages, int minAge, int maxAge) {
		this.userId = userId;
		this.subjects = subjects;
		this.languages = languages;
		this.minTargetLevel = minAge;
		this.maxTargetLevel = maxAge;
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
	
	/**
	 * Add new subjects to the list of user profile subjects
	 * if they belong to the ViSH top subjects
	 * 
	 * @param newSubjects
	 */
	public void addTopSubjects(List<String> newSubjects, List<String> topSubjects) {
		// Iterate over all the new subjects to check if the user profile 
		// has them in the current subjects list
		Iterator<String> newSubIt = newSubjects.iterator();
		while(newSubIt.hasNext()) {
			String newSubject = newSubIt.next();
			// if the current list does not contains a subject, 
			// it is added when it belongs to the top ViSH subjects
			if(!subjects.contains(newSubject) && topSubjects.contains(newSubject)) {
				subjects.add(newSubject);
			}
		}
	}
	
}

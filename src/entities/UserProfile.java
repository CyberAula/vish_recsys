/**
 * 
 */
package entities;

/**
 * @author Daniel Gallego Vico
 *
 */
public class UserProfile implements Comparable<UserProfile> {
	
	private int  userId;
	private String subject;
	private String language;
	
	// the distance between this point and the 
	// center of the canopy it belongs to
	private double distanceToCenter;
	
	/**
	 * Constructor
	 * 
	 * @param subject
	 * @param language
	 */
	public UserProfile(int userId, String subject, String language) {
		this.userId = userId;
		this.subject = subject;
		this.language = language;
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
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
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

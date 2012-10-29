/**
 * Define a learning object (LO) in ViSH.
 * A LO can be a simple resource (image, video...)
 * or a complex pedagogical one, like a virtual excursion
 */
package models;

import java.util.List;

/**
 * @author Daniel Gallego Vico
 *
 */
public class LearningObject implements Comparable<LearningObject> {
	
	/********** ATTRIBUTTES **********/
	private int id;
	// type of LO
	private String type;
	// relevance of the LO in the cluster
	private int position;
	// number of times the LO has been visited
	private int visitCount;
	
	// list of subjects that this LO is related to
	private List<String> subjects;
	// language in which the LO has been generated
	private List<String> languages;
	// target level described by the min and max age recommended
	private int minTargetLevel;
	private int maxTargetLevel;
	
	// determine if the content is adapted to a mobile device
	private boolean mobileAdapted;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param type
	 * @param clusterId
	 */
	public LearningObject(int id, String type, List<String> subjects, List<String> languages, int minAge, int maxAge, int visitCount) {
		this.id = id;
		this.type = type;
		this.subjects = subjects;
		this.languages = languages;
		this.minTargetLevel = minAge;
		this.maxTargetLevel = maxAge;
		this.visitCount = visitCount;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * 
	 * @return visitCount
	 */
	public int getVisitCount() {
		return visitCount;
	}

	/**
	 * Compare a given Learning Object with this object.
	 * If visitCount is greater than the received object,
	 * then this object is greater than the other.
	 */
	@Override
	public int compareTo(LearningObject arg0) {
		return (this.visitCount - arg0.visitCount);
	}

}

/**
 * Define a learning object (LO) in ViSH.
 * A LO can be a simple resource (image, video...)
 * or a complex pedagogical one, like a virtual excursion
 */
package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Gallego Vico
 *
 */
public class LearningObject {
	
	//Constants
	public final int VIRTUAL_EXCURSION = 0;
	public final int RESOURCE = 1;
	
	/********** ATTRIBUTTES **********/
	private int id;
	// type of LO
	private int type;
	// list of subjects that this LO is related to
	private List<String> subjects;
	// language in which the LO has been generated
	private String languages;
	// target level described by the min and max age recommended
	private int minTargetLevel;
	private int maxTargetLevel;
	// number of times the LO has been used by someone
	private int timesUsed;
	// list of users that have used the LO
	private List<UserProfile> usedBy = new ArrayList<UserProfile>();
	// determine if the content is adapted to a mobile device
	private boolean mobileAdapted;

}

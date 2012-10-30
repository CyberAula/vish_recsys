/**
 * Determines whether or not the current situation 
 * warrants a recommendation considering 
 * social, location and user context information
 */
package manager;

import models.LocationContextModel;
import models.SocialContextModel;
import models.UserContextModel;

/**
 * @author Daniel Gallego Vico
 *
 */
public class SituationAssessmentManager {
	
	// score S1 influences threshold T2, if:
	//  - S1 = 0 -> Abort recommendation
	//  - S1 > T1 -> Recommend
	//  - S1 = 1 -> Force recommendation
	private double S1; 
	
	// threshold that determines if the 
	// proactive recommendation is suitable
	private double T1; 
	
	// Each Recommendation Score (RS) is weighted depending on the
	// influence factor relative to the proactive recommendation
	private final double INFLUENCE_SOCIAL = 1/3;
	private final double INFLUENCE_LOCATION = 1/3;
	private final double INFLUENCE_USER = 1/3;
	
	/**
	 * Constructor
	 * 
	 * @param T1, threshold that has to be exceed in order to consider a proactive recommendation
	 * @param jsonValues
	 */
	public SituationAssessmentManager(double T1, String jsonValues) {
		// TODO extract values from ViSH json received
		this.T1 = T1;
	}
	
	/**
	 * 
	 * @return true is the situation is suitable for a proactive recommendation
	 */
	public boolean isSituationSuitable() {
		double totalInfluence = INFLUENCE_SOCIAL + INFLUENCE_LOCATION + INFLUENCE_USER;
		// calculate score S1 from weighed context information to analyze suitability
		S1 = (getSocialRecommendationScore() * INFLUENCE_SOCIAL +
				getLocationRecommendationScore(3,3) * INFLUENCE_LOCATION + 
				getUserRecommendationScore(3,3) * INFLUENCE_SOCIAL)
				/ totalInfluence;
		if(S1 > T1) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the social context
	 * 
	 * @return a value between 0 and 1
	 */
	private double getSocialRecommendationScore() {
		return new SocialContextModel().calculateRecommendationScore();
	}
	
	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the location context
	 * 
	 * @param geoLocationStatus
	 * @param timeStatus
	 * @return a value between 0 and 1
	 */
	private double getLocationRecommendationScore(int geoLocationStatus, int timeStatus) {
		return new LocationContextModel(geoLocationStatus, timeStatus).calculateRecommendationScore();
	}
	
	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the user context
	 * 
	 * @param activityStatus
	 * @param deviceStatus
	 * @return a value between 0 and 1
	 */
	private double getUserRecommendationScore(int activityStatus, int deviceStatus) {
		return new UserContextModel(activityStatus, deviceStatus).calculateRecommendationScore();
	}

}

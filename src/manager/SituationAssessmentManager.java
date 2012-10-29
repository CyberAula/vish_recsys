/**
 * Determines whether or not the current situation 
 * warrants a recommendation considering 
 * social, location and user context information
 */
package manager;

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
	 */
	public SituationAssessmentManager(double T1) {
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
				getLocationRecommendationScore() * INFLUENCE_LOCATION + 
				getUserRecommendationScore() * INFLUENCE_SOCIAL)
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
		// TODO logic
		return 1;
	}
	
	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the location context
	 * 
	 * @return a value between 0 and 1
	 */
	private double getLocationRecommendationScore() {
		// loop over all the feature values to 
		return 1;
	}
	
	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the user context
	 * 
	 * @return a value between 0 and 1
	 */
	private double getUserRecommendationScore() {
		// TODO logic
		return 1;
	}

}

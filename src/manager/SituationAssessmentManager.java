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
	
	// Each contextual attribute is weighed depending on the
	// its relative importance to the proactive recommendation 
	private final double LOCATION_CONTEXT_WEIGHT = 1/3;
	private final double USER_CONTEXT_WEIGHT = 1/3;
	private final double SOCIAL_CONTEXT_WEIGHT = 1/3;
	
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
		// calculate score S1 from weighed context information to analyze suitability
		S1 = locationContextAssessment() * LOCATION_CONTEXT_WEIGHT + 
				userContextAssessment() * USER_CONTEXT_WEIGHT + 
				socialContextAssessment() * SOCIAL_CONTEXT_WEIGHT;
		if(S1 > T1) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the location context
	 * 
	 * @return a value between 0 and 1
	 */
	private double locationContextAssessment() {
		// TODO logic
		return 1;
	}
	
	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the user context
	 * 
	 * @return a value between 0 and 1
	 */
	private double userContextAssessment() {
		// TODO logic
		return 1;
	}
	
	/**
	 * Calculates a value to indicate the suitability of generating a 
	 * proactive recommendation attending to the social context
	 * 
	 * @return a value between 0 and 1
	 */
	private double socialContextAssessment() {
		// TODO logic
		return 1;
	}

}

/**
 * Evaluates the candidate items to be recommended. 
 * If one or more items are considered good enough in the current context, 
 * the recommender system would communicate it. 
 * This process is only executed when the situation assessment indicates a 
 * promising situation to generate a recommendation.
 */
package manager;

/**
 * @author Daniel Gallego Vico
 *
 */
public class ItemAssessmentManager {
	
	// threshold that determines if the an item 
	// is suitable to be recommended
	private double T2;
	
	/**
	 * Constructor: calculates T2 = |1 - S1|
	 * 
	 * @param S1, score to determine the proactivity in the situation assessment
	 */
	public ItemAssessmentManager(double S1) {
		this.T2 = Math.abs(1-S1);
	}

}

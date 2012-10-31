/**
 * Resource which provides a GET representation
 * to generate a forced recommendation
 */
package restserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import manager.ItemAssessmentManager;
import manager.SituationAssessmentManager;
import manager.SocialContextManager;
import models.LearningObject;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * @author Daniel Gallego Vico
 *
 */
public class GenerateForcedRecommendation extends ServerResource {
	
	private Logger log = Logger.getLogger("GenerateForcedRecommendation");
	
	private final String USER_ID = "userid";
	
	/**
	 * URI: /recommendation/proactive
	 * Request method: http GET
	 * Function called: proactive
	 * Parameters: ?userid={id}
	 * 
	 * @return similarClusterId
	 */
	@Get
	public String getPersonalizedRecommendation() {
		// retrieve parameters
		int userId = Integer.valueOf(getQuery().getValues(USER_ID));

		// First, identify the closest cluster to the user given
		SocialContextManager scm = new SocialContextManager();
		int closestClusterId = scm.discoverUserCluster(userId);
		
		// Second, generate the personalized recommendation
		// S1 = 1 as we are forcing the recommendation
		ItemAssessmentManager itemAssessManager = new ItemAssessmentManager(1);
		// generate list of LO to be recommended
		ArrayList<LearningObject> itemList = itemAssessManager.getPersonalizedRecommendation();
		
		log.info("The item ids related to the personalized recommendation are: ");
		return generateResponse(itemList);
	}
	
	/**
	 * 
	 * @return a String containing a list of ids corresponding to the LO recommended
	 */
	private String generateResponse(ArrayList<LearningObject> itemList) {
		String response = "";
		for (Iterator iterator = itemList.iterator(); iterator.hasNext();) {
			LearningObject lo = (LearningObject) iterator.next();
			response += lo.getId() + ",";
		}
		return response;
	}

}

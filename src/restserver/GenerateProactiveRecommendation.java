/**
 * Resource which provides a GET representation
 * to initialize the process to generate a proactive recommendation
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
 * @author thanos
 *
 */
public class GenerateProactiveRecommendation extends ServerResource {
	
	private Logger log = Logger.getLogger("GenerateRecommendationResource");
	
	private final String USER_ID = "userid";
	private final String GEO_LOCATION = "geolocation";
	private final String TIME = "time";
	private final String ACTIVITY = "activity";
	private final String DEVICE = "device";
	
	private final double T1 = 0.5;
	
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
		String geoLocation = getQuery().getValues(GEO_LOCATION);
		String time = getQuery().getValues(TIME);
		String activity = getQuery().getValues(ACTIVITY);
		String device = getQuery().getValues(DEVICE);
		
		// First, identify the closest cluster to the user given
		SocialContextManager scm = new SocialContextManager();
		int closestClusterId = scm.discoverUserCluster(userId);
		
		// Second, determine if the situation is appropriate
		SituationAssessmentManager sitAssessManager = new SituationAssessmentManager(T1, geoLocation, time, activity, device);
		if(sitAssessManager.isSituationSuitable()) {
			log.info("The situation is suitable for a proactive recommendation");
			// Third, generate the personalized recommendation
			ItemAssessmentManager itemAssessManager = new ItemAssessmentManager(sitAssessManager.getS1());
			// generate list of LO to be recommended
			ArrayList<LearningObject> itemList = itemAssessManager.getPersonalizedRecommendation();
			log.info("The item ids related to the personalized recommendation are: ");
			return generateResponse(itemList);
		}
		else {
			log.info("The situation is not suitable for a proactive recommendation");
			return String.valueOf("Situation not appropriate");
		}
		
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

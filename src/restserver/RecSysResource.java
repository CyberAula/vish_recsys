/**
 * Resource which provides a GET representation
 * to initialize the clustering process
 */ 
package restserver;

import manager.SocialContextManager;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * @author Daniel Gallego Vico
 *
 */
public class RecSysResource extends ServerResource {

	@Get
	public String initSocialContextManager() {
		SocialContextManager scm = new SocialContextManager();
		// First the user profile clustering
		scm.doUserProfileClustering();
		// Then, the learning objects assignment
		scm.doLOAssignment();
		
		// Print information about clusters generated
		System.out.println(scm.getClustersInformation());
		
		return "ViSH RecSys launched!";
	}
}

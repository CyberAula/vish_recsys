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
		scm.doUserProfileClustering();
		
		return "ViSH RecSys launched!";
	}
}

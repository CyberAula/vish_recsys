/**
 * Resource which provides a GET representation
 * to initialize the clustering process
 */ 
package restserver;

import java.util.logging.Logger;

import manager.SocialContextManager;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * @author Daniel Gallego Vico
 *
 */
public class RecSysResource extends ServerResource {
	
	private Logger log = Logger.getLogger("RecSysServerResource");

	@Get
	public void initSocialContextManager() {
		SocialContextManager scm = new SocialContextManager();
		scm.generateSocialContext();
		
		// Print information about clusters generated
		log.info(scm.getClustersInformation()); 
	}
}

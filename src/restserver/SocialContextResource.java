/**
 * Resource which provides a GET representation
 * to initialize the social context generation process
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
public class SocialContextResource extends ServerResource {
	
	private Logger log = Logger.getLogger("SocialContextResource");

	@Get
	public String initSocialContextManager() {
		SocialContextManager scm = new SocialContextManager();
		scm.generateSocialContext();
		
		// Print information about clusters generated
		log.info(scm.getClustersInformation());
		
		return "Social context successfully generated!";
		
		// callback to inform about the successful generation
		
	}
	
}

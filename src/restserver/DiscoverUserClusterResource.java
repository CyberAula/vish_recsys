/**
 * Resource which provides a GET representation
 * to initialize user's cluster discovery process
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
public class DiscoverUserClusterResource extends ServerResource {
	
	private Logger log = Logger.getLogger("DiscoverUserClusterResource");

	
	/**
	 * URI: /socialcontext/discoverusercluster
	 * Request method: http GET
	 * Function called: DiscoverUserCluster
	 * Parameters: ?userid={id}
	 * 
	 * @return similarClusterId
	 */
	@Get
	public String userClusterDiscovery() {
		int userId = Integer.valueOf(getQuery().getValues("userid"));
		
		SocialContextManager scm = new SocialContextManager();
		int closestClusterId = scm.discoverUserCluster(userId);
		
		log.info("The user " + userId + " is similar to the users into the cluster " + closestClusterId);
		
		return String.valueOf(closestClusterId);
	}
}

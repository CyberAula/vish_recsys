/**
 * 
 */
package restserver;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * @author Daniel Gallego Vico
 *
 */
public class RecSysApplication extends Application {
	
	/**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of RecSysdResource.
        Router router = new Router(getContext());

        // Route to launch the Social Context generation process  
        router.attach("/socialcontext/generate", SocialContextResource.class);
        // Route to discover the closest user's cluster considering a user id passed as a parameter
        router.attach("/socialcontext/usercluster", DiscoverUserClusterResource.class);

        return router;
    }

}

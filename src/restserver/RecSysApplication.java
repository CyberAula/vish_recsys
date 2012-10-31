/**
 * RESTlet application that defines the routes 
 * related to the recommender engine API
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
        
        // Route to discover the closest user's cluster
        router.attach("/socialcontext/discoverusercluster", DiscoverUserClusterResource.class);
        
        // Route to generate a proactive recommendation
        router.attach("/recommendation/proactive", GenerateProactiveRecommendation.class);
        
        // Route to generate a foced recommendation (non proactive)
        router.attach("/recommendation/forced", GenerateForcedRecommendation.class);
        
        return router;
    }
}

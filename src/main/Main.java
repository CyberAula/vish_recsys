/**
 * Starts the HTTP server corresponding to the Recommender REST API
 */
package main;

import org.restlet.Component;
import org.restlet.data.Protocol;

import restserver.RecSysApplication;

/**
 * @author Daniel Gallego Vico
 *
 */
public class Main {
	
	public static void main(String[] args) throws Exception {  
	    // Create a new Component.  
	    Component component = new Component();  
	  
	    // Add a new HTTP server listening on the port specified
	    int port = 8182;
	    if(args.length != 0) {
	    	port = Integer.valueOf(args[0]);
	    }
	    component.getServers().add(Protocol.HTTP, port);  
	  
	    // Attach the sample application.  
	    component.getDefaultHost().attach("/recsys", new RecSysApplication());  
	  
	    // Start the component.  
	    component.start();  
	}  

}

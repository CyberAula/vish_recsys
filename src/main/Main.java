/**
 * 
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
	  
	    // Add a new HTTP server listening on port 8182.  
	    component.getServers().add(Protocol.HTTP, 8182);  
	  
	    // Attach the sample application.  
	    component.getDefaultHost().attach("/recsys", new RecSysApplication());  
	  
	    // Start the component.  
	    component.start();  
	}  

}

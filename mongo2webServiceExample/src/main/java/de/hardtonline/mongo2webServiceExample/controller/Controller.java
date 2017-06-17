package de.hardtonline.mongo2webServiceExample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hardtonline.mongo2webServiceExample.repository.BatchRepository;
import de.hardtonline.mongo2webServiceExample.repository.SingleRequestRepository;

// TODO: Scheduler für loops einbauen (start & pause & stop über REST realisieren)
// TODO: KOnfigurierbaren ThreadPool aufbauen, der die DB nach Batches im Status "XY" abfragt.
// TODO: "Harter" Stop einbauen
@RestController
public class Controller {
    private static final String template = "Hello, this is a simple REST service looking in a mongo db for things to do";
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SingleRequestRepository srr;
    
    @Autowired
	private BatchRepository br;
    
    private boolean isRunning = false;
    
    /*
     * Example: http://localhost:10051/worker/info
     */
    @RequestMapping("/info")
    public String info() {
    	logger.debug("Started info");
        return String.format(template);
    }
    
    /*
     * Example: http://localhost:10051/worker/startProcess?runOnce=true
     */
    @RequestMapping("/startProcess")
    public String startProcess(@RequestParam(name="runOnce", required=false)boolean onlyOnce) {
    	logger.debug("Started Processing the database");
    	
    	if (onlyOnce) {
    		logger.debug("... and we run only once!");
    	} else {
    		isRunning = true;
    	}
    	
    	String result = "Start successful!";
    	
        return result;
    }
    
    /*
     * Example: http://localhost:10051/worker/stopProcess
     */
    @RequestMapping("/stopProcess")
    public String stopProcess() {
    	logger.debug("Stopping Processing the database");
    	
    	if (!isRunning) {
    		logger.debug("Worker is already stopped!");
    	}
    	
    	String result = "Stop successful!";
    	isRunning = false;
    	
        return result;
    }
    
    /*
     * Example: http://localhost:10051/worker/status
     */
    @RequestMapping("/status")
    public String clearMongoDb() {
    	logger.debug("Started Status information");
    	
    	long countBatch = br.count();
    	long countRequests = srr.count();
    	
    	StringBuilder response = new StringBuilder();
    	response.append("We currently have >" + countBatch + "< Batches and >" + countRequests + "< SingleRequests in the db.");
    	if (isRunning) {
    		response.append("And the worker is running!");
    	} else {
    		response.append("And the worker is not running!");
    	}
    	
        return response.toString();
    }
}

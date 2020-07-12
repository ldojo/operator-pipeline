package com.citi.ocp4.jfrog;

import org.artifactory.md.Properties;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.artifactory.request.Request;
import org.artifactory.repo.RepoPath;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



 abstract class MixIn {
	@JsonIgnore(true) 
	String properties; 
}

public class DownloadEventProcessor {
	static Logger log = Logger.getLogger("stuff");
	
	public void afterDownload(Request request, RepoPath repoPath) {
		log.info("request: " + request.toString());
		log.info("repoPath: " + repoPath.toString());
	}
	
	public static void printInput(Request request, RepoPath repoPath) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.addMixIn(request.getClass(), MixIn.class);
		

		
		Map<String,Object> payload = new HashMap<String,Object>();
		payload.put("repoPath", repoPath);
		payload.put("request", request);
		payload.put("requestProperties", request.getProperties().entries());
		log.severe("payload: " + payload);
	}
}

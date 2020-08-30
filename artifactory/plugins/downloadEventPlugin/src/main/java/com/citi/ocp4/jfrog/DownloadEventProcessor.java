package com.citi.ocp4.jfrog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.entity.ContentType;
import org.artifactory.repo.RepoPath;
import org.artifactory.request.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



 abstract class MixIn {
	@JsonIgnore(true) 
	String properties; 
}

public class DownloadEventProcessor {
	static Logger log = Logger.getLogger("stuff");
	private static final String JFROG_EVENT_PROCESSOR_ENV = "JFROG_EVENT_PROCESSOR";
	

	
	public static void processRemoteDownload(Request request, RepoPath repoPath) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.addMixIn(request.getClass(), MixIn.class);
		
		Map<String,Object> payload = new HashMap<String,Object>();
		payload.put("repoPath", repoPath);
		payload.put("request", request);
		payload.put("requestProperties", request.getProperties().entries());
		String payloadJson = mapper.writeValueAsString(payload);

		String downloadEventProcessorUrl = System.getenv(DownloadEventProcessor.JFROG_EVENT_PROCESSOR_ENV);
		try {
			org.apache.http.client.fluent.Request.Post(downloadEventProcessorUrl).bodyString(payloadJson, ContentType.APPLICATION_JSON).execute().returnContent();
		} catch (IOException e) {
			log.severe("could not send download event payload to " + downloadEventProcessorUrl + ". Exception " + e + ": " + e.getMessage() + ". payload: " + payloadJson);
		}
		
	}


}

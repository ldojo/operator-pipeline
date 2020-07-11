package com.citi.ocp4.jfrog;

import java.util.logging.Logger;

import org.artifactory.request.Request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.artifactory.repo.RepoPath;

public class DownloadEventProcessor {
	static Logger log = Logger.getLogger("stuff");
	
	public void afterDownload(Request request, RepoPath repoPath) {
		log.info("request: " + request.toString());
		log.info("repoPath: " + repoPath.toString());
	}
	
	public static void printInput(Request request, RepoPath repoPath) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ObjectWriter writer = mapper.writer().withoutAttribute("properties");
		

		log.severe("request: " + request.toString() + " isinstanceof: " + (request instanceof Request));
		log.severe("request json: " + writer.writeValueAsString(request));
		log.severe("repoPath: " + repoPath.toString()+ " isinstanceof: " + (repoPath instanceof RepoPath) + " class: " + repoPath.getClass());
		log.severe("repoPath json: " + mapper.writeValueAsString(repoPath));

	}
}

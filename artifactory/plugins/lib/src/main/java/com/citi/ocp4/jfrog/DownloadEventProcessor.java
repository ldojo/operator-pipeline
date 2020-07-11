package com.citi.ocp4.jfrog;

import java.util.logging.Logger;

import org.artifactory.request.Request;
import org.artifactory.repo.RepoPath;

public class DownloadEventProcessor {
	static Logger log = Logger.getLogger("stuff");
	
	public void afterDownload(Request request, RepoPath repoPath) {
		log.info("request: " + request.toString());
		log.info("repoPath: " + repoPath.toString());
	}
	
	public static void printInput(Object request, Object repoPath) {
		log.severe("request: " + request.toString() + " isinstanceof: " + (request instanceof Request));
		log.severe("repoPath: " + repoPath.toString()+ " isinstanceof: " + (request instanceof RepoPath));
	}
}

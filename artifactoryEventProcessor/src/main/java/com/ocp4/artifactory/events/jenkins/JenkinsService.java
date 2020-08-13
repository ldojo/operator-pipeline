package com.ocp4.artifactory.events.jenkins;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface JenkinsService {

	public void invokeScanJob(String artifactoryUrl, String imagePath) throws ClientProtocolException, IOException;
	
}

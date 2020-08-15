package com.ocp4.operators.pipeline.jenkins;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface JenkinsService {

	public void invokeScanJob(String imageUrl) throws ClientProtocolException, IOException;
	
}

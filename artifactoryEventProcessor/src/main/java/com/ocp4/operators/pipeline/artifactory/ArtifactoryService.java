package com.ocp4.operators.pipeline.artifactory;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface ArtifactoryService {
	public enum ImageStatus {
		UNSCANNED
	}
	public void setImageStatus(String artifactoryPath, String repoKey, ImageStatus status) throws ClientProtocolException, IOException;
}

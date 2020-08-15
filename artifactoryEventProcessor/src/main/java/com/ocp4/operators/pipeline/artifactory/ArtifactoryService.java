package com.ocp4.operators.pipeline.artifactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.ClientProtocolException;

public interface ArtifactoryService {
	/**
	 * This enum serves as the Status that will be set as an Artifactory Property to the target image
	 * The idea is that artifactory can be queries for all images that are unscanned by querying a repo for
	 * all images where there is a property where scanStatus=UNSCANNED for example
	 * @author lshulman
	 *
	 */
	public enum ImageStatus {
		UNSCANNED
	}
	public void setImageStatus(String artifactoryPath, String repoKey, ImageStatus status) throws ClientProtocolException, IOException;
	public Optional<List<String>> fetchUnscannedImages() throws ClientProtocolException, IOException;
}

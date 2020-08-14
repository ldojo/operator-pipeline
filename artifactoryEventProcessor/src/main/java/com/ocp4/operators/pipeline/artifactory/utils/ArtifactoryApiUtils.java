package com.ocp4.operators.pipeline.artifactory.utils;

import java.util.Optional;

public class ArtifactoryApiUtils {

	/**
	 * a handy method to construct the Artifactory api HTTP PATCH URL to invoke when 
	 * adding a Property to set the 'scanStatus' for an image
	 * @param artifactoryHost the http(s)://artifactoryhost:port/artifactory URL
	 * @param repoKey the repo 
	 * @param artifactoryPath the path of the image in the repo
	 * @return
	 */
	public static String createSetStatusPropertiesURL(Optional<String> artifactoryHost,  Optional<String> repoKey,Optional<String> artifactoryPath
			) {
		String result = stripEndSlash(artifactoryHost.get()) + "/artifactory/api/metadata/" + stripBeginEndSlashes(repoKey.get()) 
		+ "/" + stripBeginEndSlashes(artifactoryPath.get())  ;
		return result ;
	}

	public static String stripBeginEndSlashes(String artifactoryPath) {
		return stripBeginSlash(stripEndSlash(artifactoryPath));
	}

	public static String stripBeginSlash(String s) {
		return s == null ? null : s.startsWith("/") ? s.substring(1) : s;
	}

	public static String stripEndSlash(String s) {
		return s == null ? null : s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
	}

}

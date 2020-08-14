package com.ocp4.operators.pipeline.artifactory.utils;

import java.util.Optional;

public class ArtifactoryApiUtils {

	public static String createSetStatusPropertiesURL(Optional<String> artifactoryHost,  Optional<String> repoKey,Optional<String> artifactoryPath,
			Optional<String> status) {
		
		return stripEndSlash(artifactoryHost.get()) + "/artifactory/api/storage/" + stripBeginEndSlashes(repoKey.get()) 
		+ "/" + stripBeginEndSlashes(artifactoryPath.get()) + "?properties=status=" + status.get() ;
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

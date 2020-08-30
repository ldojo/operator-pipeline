package com.ocp4.operators.pipeline.artifactory.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.util.UriUtils;

import com.jayway.jsonpath.JsonPath;

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

	public static String createUnscannedImagesSearchUrl(String artifactoryHost) {
		return stripEndSlash(artifactoryHost) + "/artifactory/api/search/prop?scanStatus=UNSCANNED";
	}

	public static List<String> parseManifestUrisFromApiResponseJson(String responseJson) {
		List<String> manifestUris = JsonPath.parse(responseJson).read("$['results'][*]['uri']");
		manifestUris = manifestUris.stream().filter(s -> s.endsWith("manifest.json"))
				.collect(Collectors.toList());

		return manifestUris;
	}
	
	public static String convertArtifactoryManifestJsonURI2Image(String manifestJsonUri) throws MalformedURLException {
		URL url = new URL(manifestJsonUri);
		String host = url.getHost();
		String port = url.getPort() == -1 ? "" : ":" + url.getPort();
		String uri = url.getFile();
		uri = uri.replace("/artifactory/api/storage", "");
		uri = uri.replace("/manifest.json", "").replace("/list.manifest.json", "").replace("-cache/", "/");
		StringBuilder uriBuilder = new StringBuilder(uri);
		uriBuilder.setCharAt(uri.lastIndexOf("/"), ':');
		uri = uriBuilder.toString();
		return (host + port + uri).replace(":sha256__", "@sha256:");
	}

}

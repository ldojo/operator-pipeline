package com.ocp4.operators.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.ocp4.operators.pipeline.artifactory.utils.ArtifactoryApiUtils;

@SpringBootTest

public class ArtifactoryApiUtilsTests {
	
	@Value("classpath:parseManifestUrisFromApiResponseJsonTest.json")
	Resource manifestUrisFromApiResponseJson;
	
	@Test
	void testConvertArtifactoryManifestJsonURI2Image() {
		String testUri = "http://ec2-3-15-23-148.us-east-2.compute.amazonaws.com:8081/artifactory/api/storage/operator-access-redhat-com-remote-cache/amq7/amq-streams-cluster-operator/1.1.0/list.manifest.json";
		try {
			String result = ArtifactoryApiUtils.convertArtifactoryManifestJsonURI2Image(testUri);
			assertEquals(result, "ec2-3-15-23-148.us-east-2.compute.amazonaws.com:8081/operator-access-redhat-com-remote/amq7/amq-streams-cluster-operator:1.1.0");
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
		
		testUri = "http://ec2-3-15-23-148.us-east-2.compute.amazonaws.com:8081/artifactory/api/storage/operator-quay-io-remote-cache/opstree/redis-operator/latest/manifest.json";
		try {
			String result = ArtifactoryApiUtils.convertArtifactoryManifestJsonURI2Image(testUri);
			assertEquals(result, "ec2-3-15-23-148.us-east-2.compute.amazonaws.com:8081/operator-quay-io-remote/opstree/redis-operator:latest");
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
		
		//test with no port
		testUri = "https://ec2-3-15-23-148.us-east-2.compute.amazonaws.com/artifactory/api/storage/operator-quay-io-remote-cache/opstree/redis-operator/latest/manifest.json";
		try {
			String result = ArtifactoryApiUtils.convertArtifactoryManifestJsonURI2Image(testUri);
			assertEquals(result, "ec2-3-15-23-148.us-east-2.compute.amazonaws.com/operator-quay-io-remote/opstree/redis-operator:latest");
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
		
		//test transformation with Sha references
		
		testUri = "https://ec2-3-15-23-148.us-east-2.compute.amazonaws.com:8081/artifactory/api/storage/operator-quay-io-remote-cache/coreos/prometheus-operator/sha256__933cd5bf380cf7db330808ff54f75f26fda0b1501021d499a1766b7d16224188/manifest.json";
		try {
			String result = ArtifactoryApiUtils.convertArtifactoryManifestJsonURI2Image(testUri);
			assertEquals(result, "ec2-3-15-23-148.us-east-2.compute.amazonaws.com:8081/operator-quay-io-remote/coreos/prometheus-operator@sha256:933cd5bf380cf7db330808ff54f75f26fda0b1501021d499a1766b7d16224188");
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	void testParseManifestUrisFromApiResponseJson() {
		try {
			String json = IOUtils.toString(manifestUrisFromApiResponseJson.getInputStream(), StandardCharsets.UTF_8);
			List<String> uris = ArtifactoryApiUtils.parseManifestUrisFromApiResponseJson(json);
			//There are 25 results in the test json file, but only 14 are for manifest.json urls. make sure 
			//results are filtered by manifest.json urls
			assertEquals(14, uris.size());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	
	
	@Test
	void testStatusURLCreation() {

		String host = "http://host:8080/", repoKey = "operator-dev/", path = "path/to/image/";
		String result = ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.of(host), Optional.of(repoKey),
				Optional.of(path));
		assertEquals(result,
				"http://host:8080/artifactory/api/metadata/operator-dev/path/to/image");

		// test with some slashes added/removed to the arguments. result should be the
		// same
		host = "http://host:8080";
		repoKey = "/operator-dev";
		path = "/path/to/image/";
		result = ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.of(host), Optional.of(repoKey),
				Optional.of(path));
		assertEquals(result,
				"http://host:8080/artifactory/api/metadata/operator-dev/path/to/image");

		// calling the method with nulls should throw NPE
		try {
			ArtifactoryApiUtils.createSetStatusPropertiesURL(null, Optional.of("test"), null);
			fail("should have thrown an NPE");
		} catch (NullPointerException e) {

		}
		
		try {
			ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.ofNullable(null), Optional.of("test"), Optional.of("test"));
			fail("should have thrown an NPE");
		} catch (NoSuchElementException e) {

		}
	}
}

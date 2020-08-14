package com.ocp4.operators.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ocp4.operators.pipeline.artifactory.utils.ArtifactoryApiUtils;

@SpringBootTest

public class ArtifactoryApiUtilsTests {
	@Test
	void testStatusURLCreation() {

		String host = "http://host:8080/", repoKey = "operator-dev/", path = "path/to/image/";
		String result = ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.of(host), Optional.of(repoKey),
				Optional.of(path), Optional.of("UNSCANNED"));
		assertEquals(result,
				"http://host:8080/artifactory/api/storage/operator-dev/path/to/image?properties=status=UNSCANNED");

		// test with some slashes added/removed to the arguments. result should be the
		// same
		host = "http://host:8080";
		repoKey = "/operator-dev";
		path = "/path/to/image/";
		result = ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.of(host), Optional.of(repoKey),
				Optional.of(path), Optional.of("UNSCANNED"));
		assertEquals(result,
				"http://host:8080/artifactory/api/storage/operator-dev/path/to/image?properties=status=UNSCANNED");

		// calling the method with nulls should throw NPE
		try {
			ArtifactoryApiUtils.createSetStatusPropertiesURL(null, Optional.of("test"), null, Optional.of("test"));
			fail("should have thrown an NPE");
		} catch (NullPointerException e) {

		}
		
		try {
			ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.ofNullable(null), Optional.of("test"), Optional.of("test"), Optional.of("test"));
			fail("should have thrown an NPE");
		} catch (NoSuchElementException e) {

		}
	}
}

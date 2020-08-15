package com.ocp4.operators.pipeline.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.JsonPath;
import com.ocp4.operators.pipeline.artifactory.ArtifactoryService;
import com.ocp4.operators.pipeline.jenkins.JenkinsService;

@RestController
public class Apis {

	private static Logger log = Logger.getLogger(Apis.class.getName());
	@Autowired
	private JenkinsService jenkinsService;

	@Autowired
	private ArtifactoryService artifactoryService;

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@PostMapping("/downloadEvent")
	public ResponseEntity<String> downloadEvent(@RequestBody String json) {
		log.info("consumed artifactory download event: " + json);
		try {
			String imagePath = JsonPath.parse(json).read("$['request']['repoPath']['path']");
			String repoKey = JsonPath.parse(json).read("$['request']['repoPath']['repoKey']");

			artifactoryService.setImageStatus(repoKey + "-cache", imagePath, ArtifactoryService.ImageStatus.UNSCANNED);
			return ResponseEntity.ok("Artifactory Image Status set to " + ArtifactoryService.ImageStatus.UNSCANNED + " for " + repoKey + " " + imagePath  );
		} catch (IOException | com.jayway.jsonpath.PathNotFoundException e) {
			String err = "could not parse artifactory image download event json. Exception message: " + e.getMessage()
			+ ". The expected payload is the Json representation of https://repo.jfrog.org/artifactory/oss-releases-local/org/artifactory/artifactory-papi/4.14.1/artifactory-papi-4.14.1-javadoc.jar!/org/artifactory/request/Request.html that Artifactory sends";
			log.severe(err);
			return ResponseEntity.badRequest().body(err);
		}
	}
	
	@GetMapping("/unscannedImages")
	public ResponseEntity<?> unscannedImages(){
		try {
			return ResponseEntity.of( artifactoryService.fetchUnscannedImages());
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}

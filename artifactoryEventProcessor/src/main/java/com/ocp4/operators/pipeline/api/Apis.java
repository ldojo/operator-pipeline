package com.ocp4.operators.pipeline.api;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Autowired
	private JenkinsService jenkinsService;

	@Autowired
	private ArtifactoryService artifactoryService;
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@PostMapping("/downloadEvent")
	public void downloadEvent(@RequestBody String json) throws ClientProtocolException, IOException {
		Logger.getLogger(this.getClass().getName()).info("consumed artifactory download even: " + json);
		String imagePath = JsonPath.parse(json).read("$['request']['repoPath']['path']");
		String repoKey = JsonPath.parse(json).read("$['request']['repoPath']['repoKey']");
		artifactoryService.setImageStatus( repoKey + "-cache",imagePath, ArtifactoryService.ImageStatus.UNSCANNED);
		//jenkinsService.invokeScanJob(artifactoryHost, imagePath);
	}
}

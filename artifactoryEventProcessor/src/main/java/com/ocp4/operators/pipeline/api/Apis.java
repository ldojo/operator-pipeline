package com.ocp4.operators.pipeline.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

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
import com.ocp4.operators.pipeline.artifactory.utils.ArtifactoryUtils;
import com.ocp4.operators.pipeline.jenkins.JenkinsService;

import io.swagger.annotations.ApiOperation;

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

	@ApiOperation(produces = "application/json", value="consumes an Artifactory https://repo.jfrog.org/artifactory/oss-releases-local/org/artifactory/artifactory-papi/4.14.1/artifactory-papi-4.14.1-javadoc.jar!/org/artifactory/request/Request.html json represenation, after an image pull even in Artifactory")
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
	
	@ApiOperation(produces = "application/json", value="returns a list of images in artifactory remote repo(s) that have a property scanStatus=UNSCANNED")
	@GetMapping("/unscannedImages")
	
	public ResponseEntity<?> unscannedImages(){
		try {
			return ResponseEntity.of( artifactoryService.fetchUnscannedImages());
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@ApiOperation(produces = "application/json", value="finds all unscanned images in artifactory remote operator image repos, and invokes jenkins scan jobs for them")
	@GetMapping("/scanUnscannedImages")
	public ResponseEntity<?> scanUnscannedImages(){
		List<String> scannedImages = new ArrayList<String>();
		Optional<List<String>> unscannedManifestJsonsUris = Optional.empty();
		try {
			unscannedManifestJsonsUris = artifactoryService.fetchUnscannedManifestJsonUris();
		} catch (IOException e) {
			log.severe("could not fetch unscanned Images from artifactory to scan. Exception: " + e.getMessage());
		}
		
		if(unscannedManifestJsonsUris.isPresent()) {
			for (String manifestJsonUri : unscannedManifestJsonsUris.get()) {
				log.info("invoking jenkins scan for " + manifestJsonUri);
				try {
					artifactoryService.setScanStatus(manifestJsonUri.replace("/storage/", "/metadata/"), ArtifactoryService.ImageStatus.PROCESSING);
				} catch (IOException e1) {
					log.severe("could not set Artifactory Property scanStatus for " + manifestJsonUri + ". Exception: " + e1.getMessage());
				}
				try {
					String image = ArtifactoryUtils.convertArtifactoryManifestJsonURI2Image(manifestJsonUri);
					jenkinsService.invokeScanJob(image);
					scannedImages.add(image);
				} catch (IOException e) {
					log.severe("could not invoke jenkins scan job for " + manifestJsonUri + ". Exception: " + e.getMessage());
				}
			}
		}
		return ResponseEntity.ok(scannedImages);
	}
	
	@ApiOperation(produces = "application/json", value="given an image in an artifactory remote proxy repo, produces the target artifactory higher env repo the image should be promoted to")
	@GetMapping("/imagePromotionTarget/${sourceImage}")
	public ResponseEntity<?> imagePromotionTarget(@PathParam("sourceImage") String sourceImage){
		return ResponseEntity.ok(artifactoryService.imagePromotionTargets(sourceImage));
	}
	
}

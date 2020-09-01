package com.ocp4.operators.pipeline.artifactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.ocp4.operators.pipeline.artifactory.utils.ArtifactoryUtils;

@Service
public class ArtifactoryServiceImpl implements ArtifactoryService{

	@Value("${artifactory.host}")
	private String artifactoryHost;
	@Value("${artifactory.user}")
	private String artifactoryUser;
	@Value("${artifactory.password}")
	private String artifactoryPassword;
	
	@Value("classpath:imagePromotionTargets.json")
	Resource imagePromotionTargetsJson;
	
	@SuppressWarnings("serial")
	@Override
	public void setImageStatus(String repoKey,String artifactoryPath,  ArtifactoryService.ImageStatus status) throws ClientProtocolException, IOException {
		String url = ArtifactoryUtils.createSetStatusPropertiesURL(Optional.of(artifactoryHost), Optional.of(repoKey),Optional.of(artifactoryPath));
		setScanStatus(url, status);
	}


	@Override
	public Optional<List<String>> fetchUnscannedImages() throws ClientProtocolException, IOException {
		Optional<List<String>> manifestUris = fetchUnscannedManifestJsonUris();
		List<String> results = new ArrayList<String>();
		if(manifestUris.isPresent()) {
			for(String s : manifestUris.get()) {
				results.add(ArtifactoryUtils.convertArtifactoryManifestJsonURI2Image(s));
			}
		}
		return Optional.of(results);
	}
	
	@Override
	public Optional<List<String>> fetchUnscannedManifestJsonUris() throws ClientProtocolException, IOException{
		String url = ArtifactoryUtils.createUnscannedImagesSearchUrl(artifactoryHost);
		String responseJson = Request.Get(url)
		.addHeader(HttpHeaders.AUTHORIZATION,
				"Basic " + new String(Base64.encodeBase64((artifactoryUser + ":" + artifactoryPassword).getBytes())))
		.execute().returnContent().asString();
		List<String> manifestUris = ArtifactoryUtils.parseManifestUrisFromApiResponseJson(responseJson);
		return Optional.of(manifestUris);
	}


	@SuppressWarnings("serial")
	@Override
	public void setScanStatus(String url, ImageStatus status) throws ClientProtocolException, IOException {
		Map<String,Map<String,String>> props = new HashMap<String,Map<String,String>>();
		props.put("props", new HashMap<String,String>(){
			{
				put("scanStatus", status.toString());
			}
		});
		Logger.getLogger(this.getClass().getName()).info("Updating artifactory scanStatus property to " + status + " for " + url);
		Request.Patch(url)
		.addHeader(HttpHeaders.AUTHORIZATION,
				"Basic " + new String(Base64.encodeBase64((artifactoryUser + ":" + artifactoryPassword).getBytes())))
		.bodyString(new ObjectMapper().writeValueAsString(props), ContentType.APPLICATION_JSON)
		.execute().returnContent();		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> imagePromotionTargets(String sourceImage) {
		List<String> imagePromotionTargets = new ArrayList<String>();
		try {
			imagePromotionTargets = new ObjectMapper().readValue(imagePromotionTargetsJson.getInputStream(), List.class);
		} catch (IOException e) {
			throw new RuntimeException("could not read imagePromotionTargets.json file from classpath. Exception: " + e.getMessage());
		}
		return ArtifactoryUtils.imagePromotionTarget(sourceImage, imagePromotionTargets);
	}

}

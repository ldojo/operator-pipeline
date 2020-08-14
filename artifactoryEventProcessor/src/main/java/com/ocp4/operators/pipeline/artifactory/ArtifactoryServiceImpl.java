package com.ocp4.operators.pipeline.artifactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.ocp4.operators.pipeline.artifactory.utils.ArtifactoryApiUtils;

@Service
public class ArtifactoryServiceImpl implements ArtifactoryService{

	@Value("${artifactory.host}")
	private String artifactoryHost;
	@Value("${artifactory.user}")
	private String artifactoryUser;
	@Value("${artifactory.password}")
	private String artifactoryPassword;
	
	
	@SuppressWarnings("serial")
	@Override
	public void setImageStatus(String repoKey,String artifactoryPath,  ArtifactoryService.ImageStatus status) throws ClientProtocolException, IOException {
		String url = ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.of(artifactoryHost), Optional.of(repoKey),Optional.of(artifactoryPath));
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

}

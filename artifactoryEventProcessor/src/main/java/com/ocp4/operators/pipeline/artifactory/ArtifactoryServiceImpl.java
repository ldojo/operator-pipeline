package com.ocp4.operators.pipeline.artifactory;

import java.io.IOException;
import java.util.Optional;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.net.HttpHeaders;
import com.ocp4.operators.pipeline.artifactory.utils.ArtifactoryApiUtils;

@Service
public class ArtifactoryServiceImpl implements ArtifactoryService{

	@Value("${artifactory.host}")
	private String artifactoryHost;
	@Value("${artifactory.user}")
	private String artifactoryUser;
	@Value("$artifactory.password}")
	private String artifactoryPassword;
	
	
	@Override
	public void setImageStatus(String artifactoryPath, String repoKey, ArtifactoryService.ImageStatus status) throws ClientProtocolException, IOException {
		String url = ArtifactoryApiUtils.createSetStatusPropertiesURL(Optional.of(artifactoryHost), Optional.of(artifactoryPath), Optional.of(repoKey), Optional.of(status.toString()));
		Request.Put(url)
		.addHeader(HttpHeaders.AUTHORIZATION,
				"Basic " + new String(Base64.encodeBase64((artifactoryUser + ":" + artifactoryPassword).getBytes())))
		.execute().returnContent();
	}

}

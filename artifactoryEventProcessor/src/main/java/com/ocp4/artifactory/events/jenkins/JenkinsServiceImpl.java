package com.ocp4.artifactory.events.jenkins;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.net.HttpHeaders;

@Service
public class JenkinsServiceImpl implements JenkinsService {

	@Value("${jenkins.url}")
	String jenkinsUrl;

	@Value("${jenkins.imageScanJob}")
	String imageScanJobName;

	@Value("${jenkins.user}")
	String jenkinsUser;

	@Value("${jenkins.password}")
	String jenkinsPassword;

	@Override
	public void invokeScanJob(String artifactoryUrl, String imagePath) throws ClientProtocolException, IOException {
		Request.Post(jenkinsUrl + "job/" + imageScanJobName + "/buildWithParameters?imageUrl=" + artifactoryUrl + "/" + imagePath)
		.addHeader(HttpHeaders.AUTHORIZATION,
				"Basic " + new String(Base64.encodeBase64((jenkinsUser + ":" + jenkinsPassword).getBytes())))
		.execute().returnContent();

	}

}

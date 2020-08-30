to deploy a basic Jenkins server with basic auth, run
```
 oc new-app -e JENKINS_PASSWORD=password $JENKINS_IMAGE
 oc expose svc/ose-jenkins
#where $JENKINS_IMAGE is the RH openshift4/ose-jenkins:(v4.3.0) imported image stream
```

To deploy via an Openshift Template, which will enable the Kubernetes plugin and Openshift Auth,
```
oc process -f templates/jenkins-ephemeral-template.json  -p JENKINS_IMAGE_STREAM_TAG=$JENKINS_IMAGE
```

If running with podman, don't forget to supply the environment variable SCAN_UNSCANNED_IMGS_API_URL so that
the Jenkins scanUnscannedImages job knows the api URL to curl to invoke it. Ex:

```
sudo podman run --name jenkins --rm -d -p 8082:8080 --env SCAN_UNSCANNED_IMGS_API_URL=http://operator-pipeline-api:8080/scanUnscannedImages <image-name>
```

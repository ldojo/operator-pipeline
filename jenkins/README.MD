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


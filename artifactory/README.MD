Example for how to run as a template in Openshift:

### Build the image
```
podman build -t artifactory
```

### launch as a template in Openshift:
```
oc process -f artifactory-template.yml -p JFROG_RH_USER=<user> -p JFROG_RH_PASSWD=<password> -p NAME=jfrog -p IMAGE=docker.bintray.io/jfrog/artifactory-pro:6.16.2 | oc create -f -
```

### launch via podman
```
sudo podman run --name artifactory --rm -it -v /path/to/license/artifactory.lic:/artifactory_extra_conf/artifactory.lic:Z -p 8081:8081 --env JFROG_EVENT_PROCESSOR="http://localhost:8080/downloadEvent" --env JFROG_RH_PASSWD=<password> --env JFROG_RH_USER=<user>  artifactory
```

For Artifactory via OperatorHub:
https://dev.to/jfrog/artifactory-xray-on-openshift-via-operatorhub-2cn3

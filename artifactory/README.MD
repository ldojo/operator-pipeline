Example for how to run as a template in Openshift:
```
oc process -f artifactory-template.yml -p NAME=jfrog -p IMAGE=docker.bintray.io/jfrog/artifactory-pro:6.16.2 | oc create -f -
```


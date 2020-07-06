Example for how to run as a template in Openshift:
```
oc process -f artifactory-template.yml -p NAME=jfrog -p IMAGE=docker.bintray.io/jfrog/artifactory-pro:6.16.2 | oc create -f -
```

Then apply the configuration:
```
#set env variables for Redhat container registries login credentials
#these will be set for the redhat remote repos

JFROG_RH_PASSWD=<password>
JFROG_RH_USER=<user>
export JFROG_RH_PASSWD JFROG_RH_USER

cat configs/artifactory.config.xml.template | envsubst  > /tmp/artifactory.config.xml

curl -u admin:password -X POST -H "Content-type:application/xml" --data-binary @/tmp/artifactory.config.xml http://<host>:<port>/artifactory/api/system/configuration

```


For Artifactory via OperatorHub:
https://dev.to/jfrog/artifactory-xray-on-openshift-via-operatorhub-2cn3

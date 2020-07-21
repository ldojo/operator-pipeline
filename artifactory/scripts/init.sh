#!/bin/bash

cat /artifactory_extra_conf/artifactory.config.xml.template | sed s/\$JFROG_RH_PASSWD/"$JFROG_RH_PASSWD"/g | sed s/\$JFROG_RH_USER/"$JFROG_RH_USER"/g > /artifactory_extra_conf/artifactory.config.xml
rm /artifactory_extra_conf/artifactory.config.xml.template

/entrypoint-artifactory.sh


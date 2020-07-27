FROM docker.bintray.io/jfrog/artifactory-pro:6.20.1

#we build the image with pre-defined repos, in the xml.template below
#the init.sh script will perform some needed substitutions to allow access to remote Red Hat repos
#the container must run with environment variables JFROG_RH_USER and JFROG_RH_PASSWD, for the init.sh script below
# to apply. Otherwise, credentials for redhat.io, access.redhat.com, connect.redhat.com won't apply
COPY configs /opt/configs
COPY scripts/init.sh /init.sh
COPY configs/artifactory.config.xml.template /artifactory_extra_conf/artifactory.config.xml.template
COPY plugins/downloadEventPlugin/target/*.jar /artifactory_extra_conf/plugins/lib/
COPY plugins/downloadEventPlugin/lib/* /artifactory_extra_conf/plugins/lib/
COPY plugins/downloadEventPlugin/downloadEventPlugin.groovy /artifactory_extra_conf/plugins/


RUN chmod 775 /init.sh

 
ENTRYPOINT ["/init.sh"]


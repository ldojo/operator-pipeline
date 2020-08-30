FROM docker.io/jenkins/jenkins:latest

ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false

user root
RUN echo "deb http://old-releases.ubuntu.com/ubuntu vivid main universe" >> /etc/apt/sources.list && \
   apt-get update -y && apt-get install jq -y
user jenkins

COPY jenkins/users "$JENKINS_HOME"/users/
COPY jenkins/config.xml "$JENKINS_HOME"/config.xml
COPY jenkins/jobs "$JENKINS_HOME"/jobs
COPY create-catalog.sh /usr/local/bin/


COPY jenkins/plugins/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt


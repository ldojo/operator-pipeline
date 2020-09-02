FROM docker.io/jenkins/jenkins:latest

ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false

user root
RUN apt-get -y -qq update && \
    curl -o /usr/local/bin/jq http://stedolan.github.io/jq/download/linux64/jq && \
    chmod +x /usr/local/bin/jq
user jenkins

COPY jenkins/users "$JENKINS_HOME"/users/
COPY jenkins/config.xml "$JENKINS_HOME"/config.xml
COPY jenkins/jobs "$JENKINS_HOME"/jobs
COPY create-catalog.sh /usr/local/bin/


COPY jenkins/plugins/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt


FROM docker.io/jenkins/jenkins:latest

ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false

COPY users "$JENKINS_HOME"/users/
COPY config.xml "$JENKINS_HOME"/config.xml
COPY jobs "$JENKINS_HOME"/jobs

COPY plugins/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY oc.tar.gz /tmp/

USER root
RUN tar zxf /tmp/oc.tar.gz -C /usr/local/bin/
USER jenkins

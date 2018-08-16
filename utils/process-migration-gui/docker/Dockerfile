# RHPAM Process Instance Migration Docker image
# Version 1

# Pull base image
FROM registry.access.redhat.com/jboss-eap-7/eap71-openshift

# Maintainer
MAINTAINER Calvin Zhu

# deploy app
ADD ROOT.war $JBOSS_HOME/standalone/deployments/

USER root
# In the JBoss EAP container, JBoss EAP runs as the jboss user
# but copied files are brought in as root
RUN chown jboss:jboss $JBOSS_HOME/standalone/deployments/ROOT.war
USER jboss

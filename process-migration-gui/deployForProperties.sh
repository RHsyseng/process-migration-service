mvn clean package
rm -rf /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration*
cp -r /home/czhu/works/git/bpms7_git/calvin_fork/test_rhpam_Client/target/Migration /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.war
touch /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.war.dodeploy
ls -l /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/

ln -s /home/czhu/works/bpms7/configMap/team.properties /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.war/WEB-INF/classes/team.properties
ln -s /home/czhu/works/bpms7/configMap/team2.properties /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.war/WEB-INF/classes/team2.properties
ls -l /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.war/WEB-INF/classes


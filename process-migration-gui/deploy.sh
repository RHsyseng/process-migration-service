mvn clean package
rm -rf /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.*
cp /home/czhu/works/git/bpms7_git/calvin_fork/test_rhpam_Client/target/Migration.war /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/
touch /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/Migration.war.dodeploy
ls -l /home/czhu/jboss/BPMS/rhpam-7.0.0.GA/rhpam-businessCentral_71/standalone/deployments/


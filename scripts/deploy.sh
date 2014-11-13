#/bin/sh

export SERVER="craig@the.interwebs.io"
export PORT_NUMBER="2222"
export SOURCE_FILE="~/clone/test/app/target/angular-grails.war"
export DESTINATION="/web/tomcat/angular-grails/webapps/"
export SCRIPT="/etc/init.d/tomcat-angular"

ssh -p $PORT_NUMBER $SERVER "$SCRIPT stop"
ssh -p $PORT_NUMBER $SERVER "rm -rf $DESTINATION/*"
scp -P $PORT_NUMBER $SOURCE_FILE "$SERVER:$DESTINATION/ROOT.war"
ssh -p $PORT_NUMBER $SERVER "$SCRIPT start"
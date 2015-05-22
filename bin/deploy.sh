#/bin/sh
source ./common.sh

export SERVER="craig@angular-grails.craigburke.com"
export PORT_NUMBER="2222"
export SOURCE_FILE=$DEPLOY_APP_ROOT/build/libs/angular-grails.jar
export DESTINATION="/web/apps/"
export SCRIPT="/etc/init.d/app-angular-grails"

ssh -p $PORT_NUMBER $SERVER "$SCRIPT stop"
ssh -p $PORT_NUMBER $SERVER "rm -rf $DESTINATION/*"
scp -P $PORT_NUMBER $SOURCE_FILE "$SERVER:$DESTINATION/angular-grails.jar"
ssh -p $PORT_NUMBER $SERVER "$SCRIPT start"

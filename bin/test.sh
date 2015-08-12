#/bin/sh

install_lazybones() {
    curl -s get.gvmtool.net | bash
    echo "gvm_auto_answer=true" > ~/.gvm/etc/config
    source ~/.gvm/bin/gvm-init.sh
    gvm install lazybones
    gvm use lazybones
}

test_app() {
    echo "Testing AngularJS $1 application"
    local APP_PATH="./test/apps/$1"
    $APP_PATH/gradlew -p $APP_PATH test
}

install_lazybones

case $CIRCLE_NODE_INDEX in 
    0) test_app "1.2" ;;
    1) test_app "1.3" ;;
    2) test_app "1.4" ;;
esac

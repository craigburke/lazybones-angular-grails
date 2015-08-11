#/bin/sh
set -e

export TEST_APP_ROOT=~/test/apps
export DEPLOY_APP_ROOT=$TEST_APP_ROOT/1.4

install_lazybones() {
	curl -s get.gvmtool.net | bash
	echo "gvm_auto_answer=true" > ~/.gvm/etc/config
	source ~/.gvm/bin/gvm-init.sh
	gvm install lazybones
	gvm use lazybones
}

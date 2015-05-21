#/bin/sh
export PROJECT_ROOT=~/clone
export APP_ROOT=$PROJECT_ROOT/test/apps/1.3/

install_lazybones() {
	curl -s get.gvmtool.net | bash
	echo "gvm_auto_answer=true" > ~/.gvm/etc/config
	source ~/.gvm/bin/gvm-init.sh
	gvm install lazybones
	gvm use lazybones
}

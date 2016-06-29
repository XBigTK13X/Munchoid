#! /bin/bash

script/prepare_package.sh
cd $JENKINS_HOME/tools/conyay
/bin/bash -c "./power.sh Munchoid $WORKSPACE/pkg"
rm -rf $WORKSPACE/artifacts || true
mv ./rap/Munchoid/packages $WORKSPACE/artifacts

push(){
	ZIP=$1
	CHANNEL=$2
	butler push $WORKSPACE/artifacts/$ZIP simplepathstudios/munchoid:$CHANNEL --userversion-file $WORKSPACE/assets/data/version.dat
}

push Munchoid-lin-64.zip linux-stable
push Munchoid-mac.zip osx-stable
push Munchoid-win-32.zip windows-stable
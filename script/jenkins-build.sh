#! /bin/bash

script/prepare_package.sh
cd $JENKINS_HOME/tools/conyay
/bin/bash -c "./power.sh Munchoid $WORKSPACE/pkg"
rm -rf $WORKSPACE/artifacts || true
mv ./rap/Munchoid/packages $WORKSPACE/artifacts
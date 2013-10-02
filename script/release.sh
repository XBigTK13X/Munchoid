#! /bin/bash
git tag -a release/$1 -m '$1 release'
git push origin release/$1

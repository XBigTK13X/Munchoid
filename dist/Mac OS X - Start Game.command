#! /bin/sh
#
cd "$(dirname "$0")"
echo "Launching Game - Check game.log and launcher.log for details."
java -jar launcher.jar > game.log 2>&1

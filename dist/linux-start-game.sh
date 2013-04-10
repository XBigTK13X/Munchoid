#! /bin/sh
#
echo "Launching Game - check game.log and launcher.log for details."
java -jar launcher.jar > game.log 2>&1

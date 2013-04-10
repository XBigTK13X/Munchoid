#! /bin/sh

echo "== Preparing a working directory for the new package: pkg"
rm -rf pkg
rm -rf game/assets/graphics/sprites/.directory
mkdir pkg

echo "== Compiling the game and the launcher"
mvn -q clean package

echo "== Copying resources"
cp -r game/assets ./pkg/assets
cp -r dist/* ./pkg
cp game/target/game-jar-with-dependencies.jar pkg/game.jar
cp launcher/target/launcher-jar-with-dependencies.jar pkg/launcher.jar

git log --max-count=1 > pkg/assets/data/git.txt

echo "== Creating the archive: pkg/game.zip"
cd pkg
zip -rq game.zip ./* -x "*.DS_Store"
cd ..

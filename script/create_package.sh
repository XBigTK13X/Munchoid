#! /bin/sh

echo "== Preparing a working directory for the new package: pkg"
rm -rf pkg
rm -rf assets/graphics/sprites/.directory
mkdir pkg

echo "== Compiling the game and the launcher"
mvn -q clean package

echo "== Copying resources"
cp -r assets pkg/assets
cp -r dist/* pkg
cp target/game-jar-with-dependencies.jar pkg/game.jar

git log --max-count=1 > pkg/assets/data/git.txt


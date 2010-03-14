#!/bin/bash
set -e
set -x

version=0.001

if [ ! -d src ];
then 
    echo 'please run in the jmxxymon directory'
fi

mkdir -p build/java
rm -rf build/dist
mkdir -p build/dist
mkdir -p build/tar
javac -cp lib/antlr-3.2.jar -d build/java $(find src/java -type f -name \*.java)

(cd build/java; jar cf ../jmxxymon.jar ../dist)
cp lib/*.jar build/dist
cp src/scripts/*.sh build/dist
cp README build/dist

(cd build/dist; tar cf ../tar/jmxxymon-$version.tar .)


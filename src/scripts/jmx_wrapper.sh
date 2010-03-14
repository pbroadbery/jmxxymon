#!/bin/bash
# Rather simple (& untested) wrapper for jmx monitor

set -e

if [ ! "$1" = "" ];
then
    cfgfile=$1
else
    echo No config file specified.
    exit 1
fi

wd=$(/bin/pwd)
tmpdir=/tmp/jmxxmon-$$
mkdir $tmpdir
cd $tmpdir
java cp antlr-3.2.jar -jar jmxxymon.jar $BBHOME/etc/$1
for i in *.result; do bb xymonhost $i; done
cd $wd
rm -rf $tmpdir

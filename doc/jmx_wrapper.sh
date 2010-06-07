#!/bin/bash

thisdir=$(dirname $0)
url=$1

tmpdir=/tmp/xymon-$$
mkdir $tmpdir

files=
cfgfile=$tmpdir/jmx.cfg
echo > $cfgfile

$BBHOME/bin/bbhostgrep 'jmx*' | while read L
do
   set $L	# To get one line of output from bbhostgrep
#  echo "+$1+$2+$3+$4+"
   HOSTIP="$1"
   MACHINEDOTS="$2"
   MACHINE=$(echo $2 | $SED -e 's/\./,/g')
   shift;shift;shift
   host=
   files=
   substs="s:abcdef:abcdef:"
   for i in $*;
   do
       case $1 in
	   jmx=*) 
	       files="$files $(echo $1 | $SED -e 's/^.*=//')"
	       ;;
	   jmxhost=*)
	       host="@$(echo $1 | $SED -e 's/^.*=//')"
	       ;;
	   jmxport=*)
	       port="$(echo $1 | $SED -e 's/^.*=//')"
	       substs="$substs;s:@port@:$port:"
	       ;;
	   jmx*=*)
	       var="$(echo $1 | $SED -e 's/^jmx\(.*\).*=.*$/\1/')"
	       val="$(echo $1 | $SED -e 's/^.*=//')"
	       substs="$substs;s:@$var@:$val:"
	       ;;
	   *) ;;
       esac
       shift
       done
   for file in $files;
   do
       cat $BBHOME/etc/$file | grep -v '^#' | $SED -e "s/^/${MACHINE}${host},/" | sed -e $substs >> $cfgfile
   done
done

cp $cfgfile $BBSERVERLOGS/jmx-last-cfg.conf
cd $BBHOME/scripts
wd=$(pwd)
JARS="$wd/jmxxymon.jar:$wd/antlr-3.2.jar"

(cd $tmpdir; date; java -cp $JARS com.pab.jmxmonitor.Main $cfgfile; date)  > $BBSERVERLOGS/jmx-last.log 2>&1
cd $wd


for i in $tmpdir/*.result; 
do
    $BB $BBDISP "$(cat $i)" 
done

rm -rf $tmpdir

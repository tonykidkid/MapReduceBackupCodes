#!/bin/bash
PARAM=$1
PID=`jps | grep -i $PARAM | awk '{print $1}'`
kill -9 $PID
if [ $?=0 ];then
    echo "Success:killed process $PARAM"
else
    echo "kill operation fails!"
fi

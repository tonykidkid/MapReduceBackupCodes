#!/bin/bash
# those hosts which you want to start QuorumPeerMain process
servers="h201 h202 h203"

for s in $servers ; do
  ssh $s "source /etc/profile ; zkServer.sh start"
  if [ $?=0 ]; then
    echo "命令在'$s'执行成功，请查看相关进程是否已存在"
    echo ;
  fi
done

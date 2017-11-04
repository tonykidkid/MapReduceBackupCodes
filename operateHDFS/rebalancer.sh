#!/bin/bash

# It does not allow to run more than 1 Balancer process at the same time on a same hdfs-cluster.
count=0
log_file=/tmp/balancer-10pct-out.log
err_outfile=/tmp/balancer-10pct-debug.log
function my_balancer(){
  if [ ! -f ${log_file} ]; then
    touch ${log_file}
  elif [ ! -f ${err_outfile}]; then
    touch ${err_outfile}
  fi
  jps | grep Balancer 
  if [ $? -ne 0 ]; then
    echo "No other Balancer process, go on running this balancer job."
    sudo -u hdfs nohup hdfs balancer -Ddfs.balancer.movedWinWidth=5400000 -Ddfs.balancer.moverThreads=1000 -Ddfs.balancer.dispatcherThreads=200 -Ddfs.datanode.balance.bandwidthPerSec=100000000 -Ddfs.balancer.max-size-to-move=10737418240 -threshold 10 1>/tmp/balancer-10pct-out.log 2>/tmp/balancer-10pct-debug.log &
    if [ $? -eq 0 ]; then
      echo "successfully started running balancer as Daemon at" `date +'%Y-%m-%d_%H:%M:%S'`
      count=$((${count}+1))
    else
      echo "failed started balancer at" `date +'%Y-%m-%d_%H:%M:%S'`
    fi
  fi

  if [ ${count} -eq 5 ]; then
    exit 0  # to terminate running current bash script.
  fi
}


# run the Balancer for 5 times one by one.
my_balancer
while (true); do
  res=`grep "Balancing took" /tmp/balancer-10pct-out.log`
  if [[ ${res} =~ "Balancing took"  ]]; then
    echo "Current loop finished. Now starting next Balancer! Log file will be renewed."
    my_balancer
  else
    echo "Wait 2 minutes until current Balancer process successfully finished."
    sleep 120
  fi

done

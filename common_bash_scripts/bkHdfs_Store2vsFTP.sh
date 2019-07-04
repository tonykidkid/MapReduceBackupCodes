#!/bin/bash
starttime=`date +'%Y-%m-%d %H:%M:%S'`
start_seconds=$(date --date="$starttime" +%s)

yesterday=`date -d "1 days ago" +"%Y%m%d"`
arg="data_iot_PVS"
biz_domain=${arg}
echo "START BACKUP ${biz_domain} on ${yesterday}"

echo "正在从hdfs拉取数据到本地：/data/data13/hdfs_data_from_kafka/${biz_domain}/"
/usr/bin/hdfs dfs -copyToLocal /user/flume/kafka/iot/${biz_domain}/${yesterday} /data/data13/hdfs_data_from_kafka/${biz_domain}/
dir_res=`du -s /data/data13/hdfs_data_from_kafka/${biz_domain}/* | grep ${yesterday} | awk '{print $1}'`
if [ ${dir_res} -gt 0 ]; then
  echo "Got ${biz_domain} at date ${yesterday} with size of $dir_res (KiloByte)"
else
  echo "failed to get data from hdfs."
  exit -1
fi

# to generate a zip file
cd /data/data13/hdfs_data_from_kafka/${biz_domain}/${yesterday}/ && nohup zip ${biz_domain}_${yesterday}.zip * 2>&1 /dev/null
zip_file=/data/data13/hdfs_data_from_kafka/${biz_domain}/${yesterday}/${biz_domain}_${yesterday}.zip
if [ -f "${zip_file}" ]; then
  echo "Generated zip-file:${zip_file}"
else
  echo "将一堆gz文件压缩成zip失败！";exit -1
fi
src_file=${zip_file}
target_dir="/data/data6/prod_hdfs_backup"
target_abs_path="${target_dir}/${biz_domain}/${yesterday}"

if [ ! -f ${src_file} ];then
  echo "ERROR: The zip file to be uploaded does not exist."
  exit -1
else
 echo "${src_file} will be uploaded to ${target_abs_path} in kvm-host3"
  lftp backup:Y1N6o0T7@10.4.106.133 << ENN
  mkdir -p ${biz_domain}/${yesterday}
  ls -alRh ${biz_domain}/${yesterday}
  put ${src_file} -o ${biz_domain}/${yesterday}/${biz_domain}_${yesterday}_`date +"%H%M%S"`.zip
  ls -alRh ${biz_domain}/${yesterday}
  exit
ENN

fi
echo "END BACKUP ${biz_domain} on ${yesterday} --------"

endtime=`date +'%Y-%m-%d %H:%M:%S'`
end_seconds=$(date --date="$endtime" +%s)
duration=$((end_seconds-start_seconds))
SIZE=`ls -s $zip_file | awk '{print $1}'`
#写入influxdb（back_up） 表名    (db=dev_all,source=mysql_dev） tag     size=${SIZE},dur=${duration}  field
insert_sql="back_up,domain=PVS,source=hdfs  size=${SIZE},dur=${duration}"
curl -i -XPOST 'http://10.39.46.5:8086/write?db=fnw_db' --data-binary "$insert_sql"


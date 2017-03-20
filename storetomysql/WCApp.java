package storetomysql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;

public class WCApp {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJobName("MySQLApp");
        job.setJarByClass(WCApp.class);

        //配置数据库信息
        String driverclass = "com.mysql.jdbc.Driver"; 
		String url = "jdbc:mysql://172.16.112.130:3306/big4";
		String user = "root";
		String password= "hadoop";
        //设置数据库配置
        DBConfiguration.configureDB(job.getConfiguration(),driverclass,url,user,password);
        //设置数据输入内容: 数据来自读取的数据库表
        DBInputFormat.setInput(job,MyDBWritable.class,"select id,name,txt from words","select count(*) from words");
        // 将M-R结果写入到big4.stats表的字段word，c
        DBOutputFormat.setOutput(job,"stats","word","c");

        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);
        job.setNumReduceTasks(3);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.waitForCompletion(true);
    }
}
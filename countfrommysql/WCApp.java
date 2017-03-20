package countfrommysql;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;		// 使作业能操作关系型数据库
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;		// 从数据库中读数据,为map()提供规范的<K,V>数据
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WCApp {
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		Job job;
		try {
			job = Job.getInstance(conf);
			job.setJobName("MySQLApp");
			job.setJarByClass(WCApp.class);

			// 配置数据库相关信息
			String driverclass = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://172.16.112.130:3306/big4"; // 要操作的库名big4
			String user = "root";
			String password = "yourpass";
			// 设置数据库配置
			DBConfiguration.configureDB(job.getConfiguration(), driverclass,
					url, user, password);
			// 取出此表的指定字段，获取总记录条数
			DBInputFormat.setInput(job, MyDBWritable.class,
					"SELECT id,name,txt FROM words",
					"SELECT COUNT(*) FROM words");

			FileOutputFormat.setOutputPath(job, new Path(args[0]));
			job.setMapperClass(WcMapper.class);
			job.setReducerClass(WCReducer.class);
			job.setNumReduceTasks(3);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			job.waitForCompletion(true);
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}

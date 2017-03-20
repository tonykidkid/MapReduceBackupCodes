package tracingmyjob;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TraceApp {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		Job job;
		try {
			job = Job.getInstance(conf);
			job.setJobName("aWordCounterApplication");
			job.setJarByClass(TraceApp.class); // 设置运行jar包的类
			job.setInputFormatClass(TextInputFormat.class); // 设置输入的文件格式
			job.setMapperClass(TraceMapper.class); // 运行的Mapper类
			job.setReducerClass(TraceReducer.class); // 运行的Reducer类
			job.setNumReduceTasks(3); // 设置reduce任务个数
			
			job.setMapOutputKeyClass(Text.class); // map()输出的key数据类型
			job.setMapOutputValueClass(IntWritable.class); // map()输出的value数据类型
			job.setOutputKeyClass(Text.class); // reduce()输出的key数据类型
			job.setOutputValueClass(IntWritable.class); // reduce()输出的value数据类型
			
			FileInputFormat.addInputPath(job, new Path(args[0])); // 传给map()的文件输入路径
			FileOutputFormat.setOutputPath(job, new Path(args[1])); // 输出结果数据到哪个路径
			job.waitForCompletion(false); // 提交此作业
		} catch (IllegalArgumentException | IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

package chain;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 链条式job任务
 */
public class WCChainApp {
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://s201:8020/");
		try {
			Job job = Job.getInstance(conf);
			// 设置job的各种属性
			job.setJobName("WCChainApp"); // 作业名称
			job.setJarByClass(WCChainApp.class); // 搜索类
			job.setInputFormatClass(TextInputFormat.class); // 设置输入文件的格式
			// 添加输入路径
			FileInputFormat.addInputPath(job, new Path("d:/mr/skew"));
			// 设置输出路径
			FileOutputFormat.setOutputPath(job, new Path("d:/mr/skew/out"));
			// 在mapper链条上增加Mapper1
			ChainMapper.addMapper(job, WCMapMapper1.class, LongWritable.class,Text.class, 
					Text.class, IntWritable.class, conf);

			// 在mapper链条上增加Mapper2
			ChainMapper.addMapper(job, WCMapMapper2.class, Text.class,IntWritable.class, 
					Text.class, IntWritable.class, conf);

			// 在reduce链条上设置reduce： 整个job只有一个reduce()
			ChainReducer.setReducer(job, WCReducer.class, Text.class,
					IntWritable.class, Text.class, IntWritable.class, conf);
			// 在reduce链条上增加Mapper2：在执行最终reduce()之前, Reduce端还有一个map()
			ChainReducer.addMapper(job, WCReduceMapper1.class, Text.class,
					IntWritable.class, Text.class, IntWritable.class, conf);

			job.setNumReduceTasks(3);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
		}
	}
}
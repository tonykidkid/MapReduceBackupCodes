package tracingmyjob;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

// 输入的k-v类型是LongWritable - Text  输出的K-V类型是Text - IntWritable
public class TraceMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	// 重写父类的map()方法，实现我们自己的业务逻辑
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, IntWritable>.Context context){
		
		// 创建Text实例和IntWritable实例，分别用作map()输出的k-v
		Text outKey = new Text();	
		IntWritable outValue = new IntWritable();
		
		// 每读入一行就处理一行，摘出我们想要的词
		String line = value.toString();
		String[] strArr = line.split("\t");
		
		for (String s : strArr){
			outKey.set(s);
			outValue.set(1);
			try {
				// 把摘出来的词以 <K, V>对的格式输出，供combiner、shuffle阶段使用
				context.write(outKey, outValue);
				// 跟踪此任务的信息：只要跟踪到的执行期间信息相同，就加一
				context.getCounter("map_side", MyTraceUtil.getSummaryInfo(this, "map()")).increment(1);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

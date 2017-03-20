package tracingmyjob;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

// 输入的k-v类型是Text - IntWritable  输出的K-V类型是Text - IntWritable
public class TraceReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	// 来自map端的<k,v>对已经被shuffle好, 并且把每个key的值放到了迭代器里
	protected void reduce(Text keyIn, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context){
		
		int count = 0;
		// 取出每个key的值(即数字1)，进行累加
		for (IntWritable iw : values){
			count += iw.get();
		}
		try {
			// 把聚合后的结果输出到HDFS
			context.write(keyIn, new IntWritable(count));
			// 跟踪此reduce任务的信息：只要跟踪到的执行期间信息相同，就加一
			context.getCounter("reduce_side", MyTraceUtil.getSummaryInfo(this, "reduce()")).increment(1);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}

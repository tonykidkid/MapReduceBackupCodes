package countfrommysql;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WCReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	@Override
	protected void reduce(Text key, Iterable<IntWritable> intValue, Context ctx){
		int count = 0;
		for (IntWritable i : intValue){
			count += i.get();	// 累加求出单词出现的总次数
		}
		try {
			ctx.write(key, new IntWritable(count));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}

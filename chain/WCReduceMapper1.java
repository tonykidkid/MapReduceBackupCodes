package chain;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Reduce端的map()目的：
 *     对Map端输出的数据再次切割，过滤出指定出现次数的单词
 */
public class WCReduceMapper1 extends Mapper<Text,IntWritable, Text, IntWritable>{
    protected void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
        if(value.get() > 5){
            context.write(key,value);
        }
    }
}

package chain;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 *
 */
public class WCMapMapper2 extends Mapper<Text, IntWritable, Text, IntWritable>{
    // 第二次map()做映射，目的是过滤掉敏感词
    protected void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
        if(!key.toString().equals("falungong")){
            context.write(key,value);
        }
    }
}

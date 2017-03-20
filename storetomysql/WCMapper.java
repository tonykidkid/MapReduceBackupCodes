package storetomysql;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * mapper
 */
public class WCMapper extends Mapper<LongWritable,MyDBWritable,Text,IntWritable> {

    protected void map(LongWritable key, MyDBWritable value, Context context) throws IOException, InterruptedException {
        System.out.println(key);
        String line = value.getTxt();
        System.out.println(value.getId() + "," + value.getName());
        String[] arr = line.split(" ");
        for(String s : arr){
            context.write(new Text(s),new IntWritable(1));
        }
    }
}

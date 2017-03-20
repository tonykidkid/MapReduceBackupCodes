package countfrommysql;

/*
 * mysql> select * from words;
+----+-------+-----------------+
| id 	| name  | txt             |
+----+-------+-----------------+
|  1 | tomas | hello world tom |
|  2 | tomas | hello tom world |
|  3 | tomas | world hello tom |
|  4 | tomas | world tom hello |
 * */
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WcMapper extends Mapper<LongWritable, MyDBWritable, Text, IntWritable>{
	@Override
	protected void map(LongWritable key, MyDBWritable mysqlRecord,	Context context){
		String line = mysqlRecord.getTxt();		// 获取words表中的txt字段值
		String[] arr = line.split(" ");		// 按空格切割txt字段值里的“world hello tom”数组
		for (String s: arr){
			try {
				context.write(new Text(s), new IntWritable(1));	// 输出 word - count
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

package bigdata.twitter.common;

import bigdata.twitter.common.*;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * A simple Map-Reduce count reduce class.
 *
 * All it does is sum the values and emit the (key, sum) pair.
 *
 * Can be used either as Reducer class or Combiner class.
 *
 * @author alemedeiros
 */
public class CountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text key, Iterable<IntWritable> values, Context context)
		throws IOException, InterruptedException {

		IntWritable count = new IntWritable();
		int sum = 0;

		for (IntWritable cur : values)
			sum += cur.get();

		count.set(sum);
		context.write(key, count);
	}
}

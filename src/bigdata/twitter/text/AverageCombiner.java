package bigdata.twitter.text;

import bigdata.twitter.common.*;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * A Map-Reduce reduce class for computing the average length of the tweets in
 * the olympic tweets dataset.
 *
 * Used as a combiner.
 *
 * @author alemedeiros
 */
public class AverageCombiner extends Reducer<NullWritable, IntIntPair,
	   NullWritable, IntIntPair> {

	public void reduce(NullWritable key, Iterable<IntIntPair> values,
			Context context) throws IOException, InterruptedException {

		IntIntPair res;
		int len = 0, sum = 0;

		for (IntIntPair cur : values) {
			len += cur.getFirst().get();
			sum += cur.getSecond().get();
		}

		res = new IntIntPair(len, sum);
		context.write(key, res);
	}
}

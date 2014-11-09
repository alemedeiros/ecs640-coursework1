package bigdata.twitter.text;

import bigdata.twitter.common.*;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * A Map-Reduce mapper class for determining in which bin of the histogram a
 * tweet belongs.
 *
 * @author alemedeiros
 */
public class AverageMapper extends Mapper<LongWritable, Tweet, NullWritable,
	   IntIntPair> {

	NullWritable n = NullWritable.get();

	public void map(LongWritable key, Tweet value, Context context) throws
		IOException, InterruptedException {

		int len;
		IntIntPair res;

		len = value.getTweet().toString().length();

		res = new IntIntPair(len, 1);
		context.write(n, res);
	}
}

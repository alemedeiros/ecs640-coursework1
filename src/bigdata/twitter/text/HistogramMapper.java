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
public class HistogramMapper extends Mapper<LongWritable, Tweet, Text, IntWritable> {

	IntWritable one = new IntWritable(1);

	public void map(LongWritable key, Tweet value, Context context) throws
		IOException, InterruptedException {

		Text bin = new Text();
		int len, cat;

		len = value.getTweet().toString().length();
		cat = (len+1) / 5;

		bin.set(String.format("%03d-%03d", cat*5 + 1, (cat+1) * 5));
		context.write(bin, one);
	}
}

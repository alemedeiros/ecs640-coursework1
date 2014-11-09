package bigdata.twitter.hashtag;

import bigdata.twitter.common.*;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * A Map-Reduce mapper class for counting the occurences of each hashtag.
 *
 * @author alemedeiros
 */
public class HashtagCountMapper extends Mapper<LongWritable, Tweet, Text,
	   IntWritable> {

	IntWritable one = new IntWritable(1);

	public void map(LongWritable key, Tweet value, Context context) throws
		IOException, InterruptedException {

		Text h = new Text();
		String[] hashtags =  value.getHashtags().toString().split(" ");

		for (String ht : hashtags) {
			h.set(ht.toLowerCase());
			context.write(h, one);
		}
	}
}

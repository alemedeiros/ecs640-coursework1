package bigdata.twitter.hashtag;

import bigdata.twitter.common.*;

import java.io.IOException;
import java.util.regex.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * A Map-Reduce mapper class for determining whether a tweet is a support tweet.
 *
 * Uses some regex patterns to find some specific support hashtags and extract
 * the team it is supporting.
 *
 * @author alemedeiros
 */
public class HashtagMapper extends Mapper<LongWritable, Tweet, Text,
	   IntWritable> {

	IntWritable one = new IntWritable(1);
	Pattern ptn[] = { Pattern.compile("^(go)+team(?<t>.*)"),
		Pattern.compile("^(go)+(?<t>.*)"), Pattern.compile("^team(?<t>.*)") };

	public void map(LongWritable key, Tweet value, Context context) throws
		IOException, InterruptedException {

		Text h = new Text();
		String[] hashtags =  value.getHashtags().toString().split(" ");

		for (String ht : hashtags) {

			// Generate Matchers for current hashtag
			Matcher mtch[] = { ptn[0].matcher(ht), ptn[1].matcher(ht),
				ptn[2].matcher(ht) };

			// Check for patterns with preference for the order
			for (Matcher m : mtch) {

				if (m.matches()) {
					h.set(m.group("t").toLowerCase());
					context.write(h, one);

					// Only need to count tweet once
					return;
				}
			}
		}
	}
}

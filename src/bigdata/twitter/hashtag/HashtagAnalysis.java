package bigdata.twitter.hashtag;

import bigdata.twitter.common.*;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * A Map-Reduce job to analyse the number of supporting tweets of the olympic
 * tweets dataset, considering some formats of supporting hashtags.
 *
 * @author alemedeiros
 */
public class HashtagAnalysis {

	public static void runJob(String[] input, String output) throws Exception {

		Configuration conf = new Configuration();

		Job job = new Job(conf);

		// Set classes
		job.setJarByClass(HashtagAnalysis.class);
		job.setReducerClass(CountReducer.class);
		job.setCombinerClass(CountReducer.class);
		job.setMapperClass(HashtagMapper.class);
		job.setInputFormatClass(TweetInputFormat.class);

		// Set types
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// Set # of reduce tasks
		job.setNumReduceTasks(7);

		// Set input and output files
		Path outputPath = new Path(output);
		FileInputFormat.setInputPaths(job, StringUtils.join(input, ","));
		FileOutputFormat.setOutputPath(job, outputPath);
		outputPath.getFileSystem(conf).delete(outputPath,true);

		job.waitForCompletion(true);
	}

	public static void main(String[] args) throws Exception {
		runJob(Arrays.copyOfRange(args, 0, args.length-1), args[args.length-1]);
	}
}

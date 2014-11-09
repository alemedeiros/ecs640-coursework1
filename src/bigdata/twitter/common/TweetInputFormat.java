package bigdata.twitter.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.LineReader;

/**
 * FileInputFormat implementation for a variety of Hadoop Map-Reduce jobs over
 * the olympic tweets dataset.
 *
 * based om StockInputFormat by fcuadrado
 *
 * @author alemedeiros
 */
public class TweetInputFormat extends FileInputFormat<LongWritable,Tweet> {

	public RecordReader<LongWritable, Tweet> createRecordReader(InputSplit
			inputSplit, TaskAttemptContext context) {
		return new TweetRecordReader();
	}

	/**
	 * Modified from StockRecordReader (from fcuadrado)
	 *
	 * @author alemedeiros
	 *
	 */
	public class TweetRecordReader extends RecordReader<LongWritable, Tweet> {

		private CompressionCodecFactory compressionCodecs = null;
		private long start;
		private long pos;
		private long end;
		private LineReader in;
		private int maxLineLength;

		private LongWritable key = null;
		private Tweet value = null;

		private Text line = new Text();

		// Internal fields for daily stock
		private LongWritable id = new LongWritable();
		private LongWritable date = new LongWritable();
		private Text hashtags = new Text();
		private Text tweet = new Text();


		public void initialize(InputSplit genericSplit,
				TaskAttemptContext context) throws IOException {
			FileSplit split = (FileSplit) genericSplit;
			Configuration job = context.getConfiguration();
			this.maxLineLength = job.getInt(
					"mapred.linerecordreader.maxlength", Integer.MAX_VALUE);
			start = split.getStart();
			end = start + split.getLength();
			final Path file = split.getPath();
			compressionCodecs = new CompressionCodecFactory(job);
			final CompressionCodec codec = compressionCodecs.getCodec(file);

			// Open the file and seek to the start of the split
			FileSystem fs = file.getFileSystem(job);
			FSDataInputStream fileIn = fs.open(split.getPath());
			boolean skipFirstLine = false;
			if (codec != null) {
				in = new LineReader(codec.createInputStream(fileIn), job);
				end = Long.MAX_VALUE;
			} else {
				if (start != 0) {
					skipFirstLine = true;
					--start;
					fileIn.seek(start);
				}
				in = new LineReader(fileIn, job);
			}
			if (skipFirstLine) { // Skip first line and re-establish "start".
				start += in.readLine(new Text(), 0,
						(int) Math.min((long) Integer.MAX_VALUE, end - start));
			}
			this.pos = start;
		}

		public boolean nextKeyValue() throws IOException {
			if (key == null) {
				key = new LongWritable();
			}

			if (value == null) {
				value = new Tweet();
			}
			int newSize = 0;

			while (pos < end) {
				newSize = in.readLine(line, maxLineLength, Math.max(
							(int) Math.min(Integer.MAX_VALUE, end - pos),
							maxLineLength));
				if (newSize == 0) {
					break;
				}

				// Fields:
				// tweetId;date;hashtags;tweet
				String[] fields = line.toString().split(";");

				// Data must be correctly formed
				if (fields == null || fields.length != 4) {
					break;
				}
				// Parse key
				// Key is the first field, pointing the stock market
				id.set(Long.decode(fields[0]));
				key.set(id.get());

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss, z");

				// Setting day by parsing from
				try {
					date.set(sdf.parse(fields[1]).getTime());
					hashtags.set(fields[2]); // TODO: build a hashtag list
					tweet.set(fields[3]);

					value.set(id, date, hashtags, tweet);

				} catch (ParseException e) {
					break;

				} catch (NumberFormatException e) {
					break;
				}

				pos += newSize;
				if (newSize < maxLineLength) {
					break;
				}

			}
			if (newSize == 0) {
				key = null;
				value = null;
				return false;
			} else {
				return true;
			}
		}

		@Override
		public LongWritable getCurrentKey() {
			return key;
		}

		@Override
		public Tweet getCurrentValue() {
			return value;
		}

		/**
		 * Get the progress within the split
		 */
		public float getProgress() {
			if (start == end) {
				return 0.0f;
			} else {
				return Math.min(1.0f, (pos - start) / (float) (end - start));
			}
		}

		public synchronized void close() throws IOException {
			if (in != null) {
				in.close();
			}
		}
	}

	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		CompressionCodec codec =
			new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
		return codec == null;
	}

}

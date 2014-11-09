package bigdata.twitter.common;

import java.io.*;
import java.util.Calendar;

import org.apache.hadoop.io.*;

/**
 * A custom Writable class implementation for the Hadoop Map-Reduce jobs over
 * the olympic tweets dataset.
 *
 * @author alemedeiros
 */
public class Tweet implements WritableComparable<Tweet> {

	private LongWritable id;
	private LongWritable date;
	private Text hashtags; // TODO: Save hashtags as a list
	private Text tweet;

	public Tweet() {
		set(new LongWritable(), new LongWritable(), new Text(), new Text());
	}

	public Tweet(long id, Calendar date, String hashtags, String tweet) {
		set(new LongWritable(id), new LongWritable(date.getTimeInMillis()),
				new Text(hashtags), new Text(tweet));
	}

	public Tweet(LongWritable id, LongWritable date, Text hashtags, Text tweet) {
		set(id, date, hashtags, tweet);
	}

	public void set(LongWritable id, LongWritable date, Text hashtags,
			Text tweet) {
		this.id = id;
		this.date = date;
		this.hashtags = hashtags;
		this.tweet = tweet;
	}

	public LongWritable getID() {
		return this.id;
	}

	public LongWritable getDate() {
		return this.date;
	}

	public Text getHashtags() {
		return this.hashtags;
	}

	public Text getTweet() {
		return this.tweet;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.id.write(out);
		this.date.write(out);
		this.hashtags.write(out);
		this.tweet.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.id.readFields(in);
		this.date.readFields(in);
		this.hashtags.readFields(in);
		this.tweet.readFields(in);
	}

	@Override
	public String toString() {
		return id + ";" + date + ";" + hashtags + ";" + tweet + "\n";
	}

	@Override
	public int compareTo(Tweet other) {
		return id.compareTo(other.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tweet) {
			Tweet other = (Tweet) obj;
			return this.id.equals(other.id);
		}
		return false;
	}
}

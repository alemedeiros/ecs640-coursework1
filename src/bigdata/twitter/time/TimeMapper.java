package bigdata.twitter.time;

import bigdata.twitter.common.*;

import java.io.IOException;
import java.util.Calendar;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * A Map-Reduce mapper class for determining the day of a tweet.
 *
 * Converts the date to a string in the format: "YYYY-MM-DD" and use it as
 * output key.
 *
 * @author alemedeiros
 */
public class TimeMapper extends Mapper<LongWritable, Tweet, Text, IntWritable> {

	IntWritable one = new IntWritable(1);

	public void map(LongWritable key, Tweet value, Context context) throws
		IOException, InterruptedException {

		int _day, _month, _year;
		LongWritable date = value.getDate();
		Text day = new Text();
		Calendar _date = Calendar.getInstance();

		// Parse milliseconds to Calendar
		_date.setTimeInMillis(date.get());

		// Get day, month and year
		_day = _date.get(Calendar.DAY_OF_MONTH);
		_month = _date.get(Calendar.MONTH);
		_year = _date.get(Calendar.YEAR);

		day.set(String.format("%04d-%02d-%02d", _year, _month, _day));
		context.write(day, one);
	}
}

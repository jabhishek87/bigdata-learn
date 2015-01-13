package abhishek.poc;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapred.lib.MultipleInputs;

/**
 * Created by abhishekjaiswal on 13/1/15.
 */
public class MaxRating {


    public static class RatingMapper extends Mapper<LongWritable,Text , Text, IntWritable> {
        public void map(LongWritable key , Text value, Context context) throws IOException , InterruptedException {
            String tokens[] = value.toString().split("\t");
            if(tokens.length == 2) {
                context.write(new Text(tokens[1]), new IntWritable(1));

            }
        }

    }

    public static class RatingReducer extends Reducer {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException , InterruptedException {
            int sum = 0;

            for(IntWritable value :values) {
                sum += value.get();

            }
            context.write(key,  new IntWritable(sum));
        }

    }
}

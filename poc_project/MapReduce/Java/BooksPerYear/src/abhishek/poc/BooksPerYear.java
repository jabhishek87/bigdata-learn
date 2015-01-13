package abhishek.poc;

/**
 * Created by abhishekjaiswal on 12/1/15.
 */

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class BooksPerYear {

    public static Integer TryParseInt(String someText) {
        try {
            return Integer.parseInt(someText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }



    public static class FrequencyMapper extends Mapper<LongWritable, Text,Text,IntWritable> {
        public void map(LongWritable key , Text value, Context context) throws IOException , InterruptedException {
            Text word = new Text();
            String delim = "\t";
            Integer year = 0;
            String tokens[] = value.toString().split(delim);
            if (tokens.length >= 4) {
                year = TryParseInt(tokens[3].trim());
                if (year>0) {
                    word = new Text(year.toString());
                    context.write(word, new IntWritable(1));

                }
            }

        }
    }


    public static class FrequencyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for(IntWritable value: values) {
                sum +=  value.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {

        Job job = Job.getInstance(new Configuration());
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(FrequencyMapper.class);
        job.setCombinerClass(FrequencyReducer.class);
        job.setReducerClass(FrequencyReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        //FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileInputFormat.addInputPath(job,new Path(args[0]));
        //FileOutputFormat.setOutputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]+ "_temp"));

        job.setJarByClass(BooksPerYear.class);

        //System.exit(job.waitForCompletion(true) ? 0 : 1);

        int exitCode = job.waitForCompletion(true)?0:1;

        if (exitCode == 0 ) {
            Job SecondJob = Job.getInstance(new Configuration());

            SecondJob.setJarByClass(MaxPubYear.class);

            SecondJob.setOutputKeyClass(Text.class);
            SecondJob.setOutputValueClass(IntWritable.class);

            SecondJob.setMapOutputKeyClass(IntWritable.class);
            SecondJob.setMapOutputValueClass(Text.class);

            SecondJob.setMapperClass(MaxPubYear.MaxPubYearMapper.class);
            SecondJob.setReducerClass(MaxPubYear.MaxPubYearReducer.class);

            FileInputFormat.addInputPath(SecondJob,new Path(args[1]+ "_temp"));
            FileOutputFormat.setOutputPath(SecondJob,new Path(args[1]));
            System.exit(SecondJob.waitForCompletion(true)?0:1);

        }
    }

}

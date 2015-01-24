package abhishek.poc;


import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
//import org.apache.hadoop.mapreduce.Reducer;

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
//import org.apache.hadoop.mapred.lib.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;


public class MaxPublished {

    public static Integer TryParseInt(String someText) {
        try {
            return Integer.parseInt(someText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static class ISBNYearMapper extends Mapper<LongWritable, Text, Text, Text> {

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            Text ISBN = new Text();
            Text JoinTag = new Text();
            String delim = "\t";
            Integer year = 0;
            String tokens[] = value.toString().split(delim);
            if(tokens.length >= 4) {
                year = TryParseInt(tokens[3].trim());
                if(year == 2002) {
                    ISBN = new Text( new Text(tokens[0].trim()));
                    JoinTag = new Text("YEAR\t" + year.toString());
                    context.write(ISBN, JoinTag);
                }
            }
        }

    }

    public static class ISBNRatingMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            Text ISBN = new Text();
            Text JoinTag = new Text();
            String delim = "\t";
            String tokens[] = value.toString().split(delim);
            if(tokens.length == 3)
            {

                ISBN = new Text( new Text(tokens[1].trim()));
                JoinTag = new Text("RATING\t" + tokens[2].toString());
                context.write(ISBN, JoinTag);

            }
        }
    }

    public static class ISBNRatingReducer extends Reducer<Text,Text,Text,Text> {
        String joinTag= "";
        String joinValue = "";
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            String ISBNYear = "";
            String Rating = "";
            for(Text value:values)
            {
                joinTag = value.toString();
                String parts[]  = value.toString().split("\t");
                if(parts.length==2 && parts[0].equals("YEAR")){
                    ISBNYear = parts[1];

                } else if (parts.length==2 && parts[0].equals("RATING")){
                    Rating = parts[1];
                }
            }
            if (ISBNYear != null && !ISBNYear.equals("") && Rating!= null && !Rating.equals(""))
            {
                context.write(key, new Text(Rating)     );

            }
        }

    }


    public static void main(String[] args) throws Exception {

        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(MaxPublished.class);
        job.setReducerClass(ISBNRatingReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        MultipleInputs.addInputPath(job,  new Path(args[0]),TextInputFormat.class, ISBNYearMapper.class);
        MultipleInputs.addInputPath(job,  new Path(args[1]),TextInputFormat.class, ISBNRatingMapper.class);

        Path outputpath = new Path(args[2]+"_temp");
        FileOutputFormat.setOutputPath(job,outputpath);

        int code = job.waitForCompletion(true)?0:1;
        if(code == 0)
        {
            Job SecondJob = Job.getInstance(new Configuration());
            SecondJob.setJarByClass(MaxRating.class);

            SecondJob.setOutputKeyClass(Text.class);
            SecondJob.setOutputValueClass(IntWritable.class);
            SecondJob.setMapOutputKeyClass(Text.class);
            SecondJob.setMapOutputValueClass(IntWritable.class);

            SecondJob.setMapperClass(MaxRating.RatingMapper.class);
            SecondJob.setReducerClass(MaxRating.RatingReducer.class);
            SecondJob.setCombinerClass(MaxRating.RatingReducer.class);

            FileInputFormat.addInputPath(SecondJob,new Path(args[2]+ "_temp"));
            FileOutputFormat.setOutputPath(SecondJob,new Path(args[2]));
            int exitCode = SecondJob.waitForCompletion(true)?0:1;
            //FileSystem.get(conf).delete(new Path(args[2]+"_temp"), true);
            System.exit(exitCode);

        }

    }



}

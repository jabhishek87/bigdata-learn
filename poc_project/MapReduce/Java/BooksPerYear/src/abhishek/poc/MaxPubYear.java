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

public class MaxPubYear {

    public static Integer TryParseInt(String someText) {
        try {
            return Integer.parseInt(someText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static class MaxPubYearMapper extends Mapper<LongWritable , Text, IntWritable,Text> {
        public void map(LongWritable key, Text value , Context context)

                throws IOException, InterruptedException {
            String delim = "\t";
            Text valtosend = new Text();
            String tokens[] = value.toString().split(delim);
            if (tokens.length == 2) {
                valtosend.set(tokens[0] + ";"+ tokens[1]);
                context.write(new IntWritable(1), valtosend);
            }

        }
    }


    public static class MaxPubYearReducer extends Reducer<IntWritable ,Text, Text, IntWritable> {

        public void reduce(IntWritable key, Iterable<Text> values , Context context) throws IOException, InterruptedException {
            int maxiValue = Integer.MIN_VALUE;
            String maxiYear = "";
            for(Text value:values)          {
                String token[] = value.toString().split(";");
                if(token.length == 2 && TryParseInt(token[1]).intValue()> maxiValue) {
                    maxiValue = TryParseInt(token[1]);
                    maxiYear = token[0];
                }
            }
            context.write(new Text(maxiYear), new IntWritable(maxiValue));
        }
    }
}

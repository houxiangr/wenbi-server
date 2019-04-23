package com.houxiang.wenbiserver.itemCF.step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Mapper1 extends Mapper<LongWritable, Text,Text,Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();

    /**
     * key 1,2...
     * value A,1,1;C,3,5;....
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if(value.toString().equals("")){
            return;
        }
        String[] action=value.toString().split(",");
        String userID = action[0];
        String itemID = action[1];
        String score = action[2];
        outKey.set(itemID);
        outValue.set(userID+"_"+score);
        context.write(outKey,outValue);
    }
}

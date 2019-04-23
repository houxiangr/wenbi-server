package com.houxiang.wenbiserver.hadoopTest.step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Mapper1 extends Mapper<LongWritable, Text,Text,Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();

    /**
     * key : 1
     * value : 1_1,2_3,3_-1,4_0
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] rowAndLine = value.toString().split("\t");
        String row = rowAndLine[0];
        System.out.println(value);
        String[] lines = rowAndLine[1].split(",");
        for (String line : lines) {
            String[] oneElement = line.split("_");
            String column = oneElement[0];
            String valueStr = oneElement[1];
            outKey.set(column);
            outValue.set(row + "_" + valueStr);
            context.write(outKey,outValue);
        }
    }
}

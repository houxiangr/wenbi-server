package com.houxiang.wenbiserver.itemCF.step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Mapper2 extends Mapper<LongWritable, Text,Text,Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();
    private static String cache = "/itemCF/step1_output/part-r-00000";

    private DecimalFormat df = new DecimalFormat("0.00");

    private List<String> cacheList = new ArrayList<String>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        //通过输入流将全局缓存变量中的 右侧矩阵 读入List<String>中
        Configuration conf = context.getConfiguration();
        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream in = fs.open(new Path(cache));
        //FileReader fr = new FileReader(itermOccurrenceMatrix);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        //每一行的格式是： 行 tab 列_值,列_值,列_值,列_值
        String line = null;
        while((line = br.readLine())!=null) {
            cacheList.add(line);
        }
        in.close();
        br.close();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] line_matrix1 = value.toString().split("\t");
        String row1 = line_matrix1[0];
        String[] colValues1 = line_matrix1[1].split(",");
        double denominator1 = 0;
        for(String value1 : colValues1){
            Double score = Double.valueOf(value1.split("_")[1]);
            denominator1 += score * score;
        }
        denominator1 = Math.sqrt(denominator1);
        for(String line:cacheList){
            String[] line_matrix2 = line.toString().split("\t");
            String row2 = line_matrix2[0];
            String[] colValues2 = line_matrix2[1].split(",");

            double denominator2 = 0;
            for(String value2 : colValues2){
                Double score = Double.valueOf(value2.split("_")[1]);
                denominator2 += score * score;
            }
            denominator2 = Math.sqrt(denominator2);

            int result = 0;
            for(String value1:colValues1){
                String col_matrix1 = value1.split("_")[0];
                String val_matrix1 = value1.split("_")[1];

                for(String value2:colValues2){
                    if(value2.startsWith(col_matrix1+"_")){
                        String val_matrix2 = value2.split("_")[1];
                        result += Integer.valueOf(val_matrix1)*Integer.valueOf(val_matrix2);
                    }
                }
            }

            double cos = result / (denominator1*denominator2);

            if(cos == 0){
                continue;
            }
            outKey.set(row1);
            outValue.set(row2+"_"+df.format(cos));
            context.write(outKey,outValue);
        }
    }
}

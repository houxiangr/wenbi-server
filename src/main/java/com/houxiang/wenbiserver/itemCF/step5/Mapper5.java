package com.houxiang.wenbiserver.itemCF.step5;

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
import java.util.ArrayList;
import java.util.List;

public class Mapper5 extends Mapper<LongWritable, Text,Text,Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();
    private static String cache = "/itemCF/step1_output/part-r-00000";

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
        String item_matrix1 = value.toString().split("\t")[0];
        String[] uesr_score_array1 = value.toString().split("\t")[1].split(",");

        for(String line : cacheList){
            String item_matrix2 = line.split("\t")[0];
            String[] user_score_array2 = line.split("\t")[1].split(",");

            if(item_matrix2.equals(item_matrix1)){
                for(String user_score1 : uesr_score_array1){
                    String user1 = user_score1.split("_")[0];
                    String score1 = user_score1.split("_")[1];
                    boolean flag = false;
                    System.out.println("00000000000");
                    for(String user_score2: user_score_array2){
                        String user2 = user_score2.split("_")[0];
                        if(user1.equals(user2)){
                            System.out.println(user1+"-------"+user2);
                            flag = true;
                        }
                    }
                    if(!flag){
                        System.out.println(user1+"-------"+item_matrix1+"_"+score1);
                        outKey.set(user1);
                        outValue.set(item_matrix1+"_"+score1);
                        context.write(outKey,outValue);
                    }
                }
            }
        }
    }
}

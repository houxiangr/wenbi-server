package com.houxiang.wenbiserver.itemCF.step1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MR1 {
    private static String inPath = "/itemCF/step1_input/UserAction.txt";
    private static String outPath = "/itemCF/step1_output";
    private static String hdfs = "hdfs://192.168.42.132:9000";
    public int run() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.support.append", "true");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        conf.set("fs.defaultFS",hdfs);
        Job job = Job.getInstance(conf,"step1");

        job.setJarByClass(com.houxiang.wenbiserver.hadoopTest.step1.MR1.class);
        job.setMapperClass(Mapper1.class);
        job.setReducerClass(Reduce1.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileSystem fs = FileSystem.get(conf);
        Path inputPath = new Path(inPath);
        if(fs.exists(inputPath)){
            FileInputFormat.addInputPath(job,inputPath);
        }
        Path outputPath = new Path(outPath);
        fs.delete(outputPath,true);
        FileOutputFormat.setOutputPath(job,outputPath);
        fs.close();
        return job.waitForCompletion(true)?1:-1;
    }
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        int result = -1;
        result = new MR1().run();
        if(result == 1){
            System.out.println("step1运行成功");
        }else{
            System.out.println("step1运行失败");
        }
    }
}
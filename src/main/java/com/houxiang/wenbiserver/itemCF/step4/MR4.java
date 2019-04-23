package com.houxiang.wenbiserver.itemCF.step4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

public class MR4 {

    private static String inPath = "/itemCF/step2_output/part-r-00000";
    private static String outPath = "/itemCF/step4_output";
    private static String hdfs = "hdfs://192.168.42.132:9000";

    public int run() throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();
        conf.set("dfs.support.append", "true");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        conf.set("fs.defaultFS",hdfs);
        Job job = Job.getInstance(conf,"step4");

        job.setJarByClass(MR4.class);
        job.setMapperClass(Mapper4.class);
        job.setReducerClass(Reduce4.class);

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
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, URISyntaxException {
        int result = -1;
        result = new MR4().run();
        if(result == 1){
            System.out.println("step4运行成功");
        }else{
            System.out.println("step4运行失败");
        }
    }
}

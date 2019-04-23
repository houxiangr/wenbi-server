package com.houxiang.wenbiserver.itemCF.step5;

import com.houxiang.wenbiserver.itemCF.step4.Mapper4;
import com.houxiang.wenbiserver.itemCF.step4.Reduce4;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

public class MR5 {

    private static String inPath = "/itemCF/step4_output/part-r-00000";
    private static String outPath = "/itemCF/step5_output";
    private static String hdfs = "hdfs://192.168.42.132:9000";

    public int run() throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS",hdfs);
        conf.set("dfs.support.append", "true");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        Job job = Job.getInstance(conf,"step5");

        job.setJarByClass(MR5.class);
        job.setMapperClass(Mapper5.class);
        job.setReducerClass(Reduce5.class);

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
        result = new MR5().run();
        if(result == 1){
            System.out.println("step5运行成功");
        }else{
            System.out.println("step5运行失败");
        }
    }
}

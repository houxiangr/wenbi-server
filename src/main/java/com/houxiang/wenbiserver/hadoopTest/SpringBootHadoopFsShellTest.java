package com.houxiang.wenbiserver.hadoopTest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.fs.FsShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

//@SpringBootApplication
public class SpringBootHadoopFsShellTest implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("=========run start============");
        Configuration conf = new Configuration();
        conf.set("dfs.support.append", "true");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.42.132:9000"),conf);
        FSDataOutputStream out = fs.append(new Path("/itemCF/step1_input/UserAction.txt"));
        out.write("test".getBytes());
        fs.close();
        out.close();
        System.out.println("===========run end===========");
    }

    public static void main(String[] args) {
        System.out.println("===========main start===========");
        SpringApplication.run(SpringBootHadoopFsShellTest.class, args);
        System.out.println("===========main end===========");
    }
}

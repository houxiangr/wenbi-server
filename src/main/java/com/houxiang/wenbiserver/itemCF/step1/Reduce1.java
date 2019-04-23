package com.houxiang.wenbiserver.itemCF.step1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Reduce1 extends Reducer<Text, Text,Text,Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String itemID = key.toString();

        Map<String,Integer> map = new HashMap<>();

        for(Text value:values){
            String[] userIDScore = value.toString().split("_");
            String userID = userIDScore[0];
            Integer score = Integer.valueOf(userIDScore[1]);
            if(map.containsKey(userID)){
                Integer oldValue = map.get(userID);
                map.put(userID ,oldValue+score);
            }else{
                map.put(userID,score);
            }
        }
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            sb.append(entry.getKey()).append("_").append(entry.getValue()).append(",");
        }
        String line = null;
        if(sb.toString().endsWith(",")){
            line = sb.substring(0,sb.length()-1);
        }
        outKey.set(itemID);
        assert line != null;
        outValue.set(line);

        context.write(outKey,outValue);
    }
}

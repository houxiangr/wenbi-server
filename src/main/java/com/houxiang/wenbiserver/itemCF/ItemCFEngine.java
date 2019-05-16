package com.houxiang.wenbiserver.itemCF;

import com.houxiang.wenbiserver.itemCF.step1.MR1;
import com.houxiang.wenbiserver.itemCF.step2.MR2;
import com.houxiang.wenbiserver.itemCF.step3.MR3;
import com.houxiang.wenbiserver.itemCF.step4.MR4;
import com.houxiang.wenbiserver.itemCF.step5.MR5;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ItemCFEngine {

    public void getRecommendEssay() throws InterruptedException, IOException, ClassNotFoundException, URISyntaxException {
        System.out.println("------------------");
        int status1 = -1;
        int status2 = -1;
        int status3 = -1;
        int status4 = -1;
        int status5 = -1;

        System.out.println("正在进行step1");
        status1 = new MR1().run();
        if(status1 == -1){
            System.out.println("step1失败");
            return;
        }else{
            System.out.println("step1成功");
        }
        System.out.println("正在进行step2");
        status2 = new MR2().run();
        if(status2 == -1){
            System.out.println("step2失败");
            return;
        }else{
            System.out.println("step2成功");
        }
        System.out.println("正在进行step3");
        status3 = new MR3().run();
        if(status3 == -1){
            System.out.println("step3失败");
            return;
        }else{
            System.out.println("step3成功");
        }
        System.out.println("正在进行step4");
        status4 = new MR4().run();
        if(status4 == -1){
            System.out.println("step4失败");
            return;
        }else{
            System.out.println("step4成功");
        }
        System.out.println("正在进行step5");
        status5 = new MR5().run();
        if(status5 == -1){
            System.out.println("step5失败");
        }else{
            System.out.println("step5成功");
        }
    }
}

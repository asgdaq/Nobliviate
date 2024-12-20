package com.example.nobliviate;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PrintTest {

    public String getData(String type , int nr , Map<String , Object> map , HashSet<String> hashSet  , HashSet<String> doi){

        RandomQuestion worldAppApplication = new RandomQuestion();
        StringBuilder sb = new StringBuilder();

        //  System.out.println("______________"+worldAppApplication.key(map , hashSet));
        //  System.out.println("____________________ " + type);
        int a =0;
        while (nr > 0){

            switch (type){
                case "key":

                    sb.append("_____________________ -  ");
                    sb.append(worldAppApplication.key(map,doi)).append("\n\n");
                    break;

                case "value":

                    sb.append(worldAppApplication.value(map, hashSet));
                    sb.append(" - _____________________ ").append("\n\n");
                    break;

                case "random":

                    if(a ==0){
                        sb.append("_____________________ - ");
                        sb.append(worldAppApplication.key(map,doi)).append("\n\n");
                        a = 1;
                    }

                    else{
                        sb.append(worldAppApplication.value(map , hashSet ));
                        sb.append(" - _____________________ ").append("\n\n");
                        a = 0;
                    }

                    break;
            }
            nr--;
        }

        System.out.println(sb.toString());
        return sb.toString();

    }

}

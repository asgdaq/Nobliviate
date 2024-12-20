package com.example.nobliviate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomQuestion {


        public String key(Map<String, Object> map, Set<String> set) {

            map.remove("","");
            Random random = new Random();
            Object[]data = map.values().toArray();
            String rez = (String) data[random.nextInt(map.size())];


            if(set.size() == map.size()){
                set.clear();
                rez = (String) data[random.nextInt(map.size())];
                System.out.println(rez);
                set.add(rez);
                System.out.println(set);
                return rez;
            }

            while (set.contains(rez)) {
                rez = (String) data[random.nextInt(map.size())];
            }
            System.out.println(rez);
            set.add(rez);
            System.out.println(set);

            return rez;
        }
        public String value(Map<String, Object> map, Set<String>  set) {

            map.remove("","");
            Random random = new Random();
            Object[]data = map.keySet().toArray();
            String rez = (String) data[random.nextInt(map.size())];


            if(set.size() == map.size()){
                set.clear();
                rez = (String) data[random.nextInt(map.size())];
                System.out.println(rez);
                set.add(rez);
                System.out.println(set);
                return rez;
            }

            while (set.contains(rez)) {
                rez = (String) data[random.nextInt(map.size())];
            }
            System.out.println(rez);
            set.add(rez);
            System.out.println(set);

            return rez;

        }


        public String random(Map<String, Object> map, Set<String>  set , Set<String> doi, int a){
            if(a == 0){

                return key((HashMap<String, Object>) map, set);
            }
            else{

                return value(map , doi);
            }
        }

        public boolean checker(String ask, String type , int a , Map<String, Object> map , String question){


            switch (type){
                case "key":

                    if(map.containsKey(ask) && map.get(ask).equals(question)) {
                        return true;
                    }
                    break;

                case "value":

                    if(map.containsValue(ask)){
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getValue().equals(map.get(question))) {
                                return true;
                            }
                        }
                    }
                    break;
                case "random":

                    if(a == 0){
                        if(map.containsValue(ask)){
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                if (entry.getValue().equals(map.get(question))) {
                                    return true;
                                }
                            }
                        }
                    }
                    else{
                        if(map.containsKey(ask)  && map.get(ask).equals(question)){
                            return true;
                        }
                    }
                    break;
            }
            return false;
        }


}

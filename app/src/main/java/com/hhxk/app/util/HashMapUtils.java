package com.hhxk.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapUtils {

    //根据value值获取到对应的一个key值
    public static String getKey(HashMap<String,String> map, String value){
        String key = null;
        //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
        for(String getKey: map.keySet()){
            if(map.get(getKey).equals(value)){
                key = getKey;
            }
        }
        return key;
        //这个key肯定是最后一个满足该条件的key.
    }

    //根据value值获取到对应的所有的key值
    public static List<String> getKeyList(HashMap<String,String> map, String value){
        List<String> keyList = new ArrayList();
        for(String getKey: map.keySet()){
            if(map.get(getKey).equals(value)){
                keyList.add(getKey);
            }
        }
        return keyList;
    }


}

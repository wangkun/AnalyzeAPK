package com.jike.mobile.appsearch.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class analyzeAds {
    
    public static int allList;
    public static Map<String, Integer> AdsFrequency = new HashMap<String, Integer>();
    public static DecimalFormat df = new DecimalFormat("#0.00");
    public static void getAdsFrequency(List<String> AdsList){
        if (AdsList!=null&&AdsList.size()==0) {
            return;
        }
        for(String adsStr:AdsList){
            allList++;
            if (AdsFrequency.containsKey(adsStr)) {
                int i=AdsFrequency.get(adsStr)+1;
                AdsFrequency.put(adsStr, i);
            }else {
                AdsFrequency.put(adsStr, 1);
            }
        }
    }
    
    public static void SortMapValue(){
        List<Map.Entry<String, Integer>> infoIds =
                new ArrayList<Map.Entry<String, Integer>>(AdsFrequency.entrySet());

            //排序前
//            for (int i = 0; i < infoIds.size(); i++) {
//                String id = infoIds.get(i).toString();
//                System.out.println(id);
//            }
            //d 2
            //c 1
            //b 1
            //a 3

            //排序
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
                    return (o2.getValue() - o1.getValue()); 
//                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            }); 

            //排序后
            for (int i = 0; i < infoIds.size(); i++) {
                String id = infoIds.get(i).toString();
                System.out.println(id +" "+ df.format(infoIds.get(i).getValue()*100.0/allList)+"%");
            }
          //根据key排序
          //a 3
          //b 1
          //c 1
          //d 2
          //根据value排序
          //a 3
          //d 2
          //b 1
          //c 1
    }
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

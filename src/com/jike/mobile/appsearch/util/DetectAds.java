package com.jike.mobile.appsearch.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DetectAds {
    
    private static HashMap<String, String> adsMap = new HashMap<String, String>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        adsMap = Constants.AdsMap;
        System.out.println(adsMap);
        String rootFile = "D:/APK反编译/CoinFlip_1.0.5/";
        isHasAds(rootFile);
    }
  //adMob "/smali/com/admob"
  //adSence  "/smali/com/google/ads"
  //fortumo "/smali/com/fortumo"
  //paypal "/smali/com/paypal"
  //google analytics "/smali/com/google/android/apps/analytics"
  //youmi "/smali/net/youmi"
  //flurry "/smali/com/flurry"
  //adwhirl "/smali/com/adwhirl"
  //mobclix "/smali/com/mobclix"
  //skyhookwireless "/smali/com/skyhookwireless"
  //Quattro Wireless "/smali/com/qwapi"
  //smaato Wireless "/smali/com/smaato"
  //medialets Wireless "/smali/com/smaato"
  //jumptap Wireless "/smali/com/jumptap"
  //google licensing "/smali/com/android/vending/licensing"
  //millennialmedia "/smali/com/millennialmedia"
  //inmobi "/smali/com/inmobi"
  //zestadz "/smali/com/zestadz"
    public static HashMap<String, String> isHasAds(String filePath) {
        HashMap<String, String> findAdsMap = new HashMap<String, String>();
        for(Map.Entry<String, String> element:Constants.AdsMap.entrySet()){
            if (new File(filePath+element.getValue()).exists()) {
                System.out.println(element.getKey()+"-"+element.getValue());
                findAdsMap.put(element.getKey(),element.getValue());
            }
        }
        return findAdsMap;
    }

}

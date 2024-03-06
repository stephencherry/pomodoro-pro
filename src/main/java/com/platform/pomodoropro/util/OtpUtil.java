package com.platform.pomodoropro.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OtpUtil {
    //cache based on username and OPT MAX 8
    private static final Integer EXPIRE_MINS = 10;
    private LoadingCache<Integer, String> otpCache;

    public OtpUtil(){
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<>() {

                    @Override
                    public String load(Integer integer) throws Exception {
                        return null;
                    }
                });
    }

    public int generateOTP(String val){
        Random random = new Random();
        int key = 1000 + random.nextInt(9000);
        otpCache.put(key, val);
        System.out.println("OTP ====>>>>"+ key);
        return key;
    }

    public String getOtp(Integer key){
        try{
            String value = otpCache.get(key);
            clearOTP(key);
            return value;
        }catch (Exception e){
            return "";
        }
    }

    public void clearOTP(Integer key){
        otpCache.invalidate(key);
    }

}

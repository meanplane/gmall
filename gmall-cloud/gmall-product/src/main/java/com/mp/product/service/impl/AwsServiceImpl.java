package com.mp.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mp.product.utils.AESUtil;
import com.mp.product.utils.Base64;
import com.mp.product.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-17
 */
@Service
@Slf4j
public class AwsServiceImpl {
    @Autowired
    private RedisUtils redisUtils;

    // 泰坦接口URL
    public final String STS_URL = "http://sts.9-g.net/sts/getToken";

    // 泰坦接口请求秘钥对
    public final String AUTH_KEY = "0UviS8xq1l608e5Kjs2s";

    public final String SECRET_KEY = "7hARNxNNDlm975EF";

    public final String userType = "6";


    public final String HEADER_KEY_VERSION = "Version";
    public final String HEADER_KEY_AUTH = "Authorization";
    public final String HEADER_KEY_DEVICE_SEQ = "Seq";
    public final String HEADER_KEY_AUTH_TIMESTAMP = "X-Auth-TimeStamp";// 当前unix时间戳
    public final String HEADER_KEY_AUTH_NONCE = "X-Auth-Nonce";// 随机正整数
    public final String HEADER_KEY_AUTH_SIGN = "X-Auth-Sign";// 签名
    public final String HEADER_KEY_AUTH_KEY = "X-Auth-Key";
    // 泰坦接口请求参数
    private String userId = "1";
    private String token = "";
    private String uploadSpeed = "";
    private String downloadSpeed = "";
    private String referer = "";
    private String appEquipmentNum = "";
    private String expiredTime;// 30分钟

    public Map<String,Object> getStsToken(String uid, Long expiredSecond) throws Exception {
        String awskey = (String) redisUtils.get("awskey");
        if (awskey != null){
            String[] split = StringUtils.split(awskey, ",");
            HashMap<String, Object> map = new HashMap<>();
            map.put("secretKey",split[0]);
            map.put("accessKey",split[1]);
            return map;
        }

        Map<String, Object> map = requestKey(uid, expiredSecond);
        awskey = "";

        awskey += (String)map.get("secretKey");
        awskey += ",";
        awskey += (String)map.get("accessKey");

        redisUtils.set("awskey",awskey,expiredSecond);

        return map;
    }

    private Map<String, Object> requestKey(String uid, Long expiredSecond) throws Exception {
        userId = uid;
        // (new Date()).getTime() / 1000L +
        expiredTime = String.valueOf(expiredSecond);
        OkHttpClient okHttpClient = new OkHttpClient();
        String nonce = createRandomSecretKey(32);
        long time = System.currentTimeMillis();

        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(HEADER_KEY_AUTH_KEY, AUTH_KEY);
        paramsMap.put(HEADER_KEY_AUTH_NONCE, nonce);
        paramsMap.put(HEADER_KEY_AUTH_TIMESTAMP, time + "");
        paramsMap.put(HEADER_KEY_VERSION, "");
        paramsMap.put(HEADER_KEY_AUTH, "");
        paramsMap.put(HEADER_KEY_DEVICE_SEQ, "");
        paramsMap.put("userId", userId);
        paramsMap.put("userType", userType);
        paramsMap.put("token", token);
        paramsMap.put("uploadSpeed", uploadSpeed);
        paramsMap.put("downloadSpeed", downloadSpeed);
        paramsMap.put("referer", referer);
        paramsMap.put("appEquipmentNum", appEquipmentNum);
        paramsMap.put("expiredTime", expiredTime);

        String sign = getSign(paramsMap);

        FormBody body = new FormBody.Builder().add("userId", userId).add("userType", userType).add("token", token)
                .add("uploadSpeed", uploadSpeed).add("downloadSpeed", downloadSpeed).add("referer", referer)
                .add("appEquipmentNum", appEquipmentNum).add("expiredTime", expiredTime).build();

        Request request = new Request.Builder().url(STS_URL).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader(HEADER_KEY_AUTH_KEY, AUTH_KEY).addHeader(HEADER_KEY_AUTH_NONCE, nonce)
                .addHeader(HEADER_KEY_AUTH_TIMESTAMP, time + "").addHeader(HEADER_KEY_AUTH_SIGN, sign).post(body).build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            // } catch (IOException e) {
            // log.error("", e);
            // }
            ResponseBody responseBody = response.body();
            JSONObject object = null;
            String objString = null;
            // try {
            String responseBodyString = responseBody.string();
            log.info("调用getToken接口返回responseBody={}", responseBodyString);
            object = JSON.parseObject(responseBodyString);
            objString = object.getString("obj");
            // } catch (JSONException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // try {
            String decode = AESUtil.WTdecryptAES(objString, SECRET_KEY);
            log.info("调用getToken接口,解密obj,decodeObj={}", decode);
            JSONObject tokenJson = JSON.parseObject(decode);
            // String token = tokenJson.getString("token");
            // log.info("调用getToken接口,解密obj得到token={}", token);
            return tokenJson.getInnerMap();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("", e);
            throw e;
        } /*
         * catch (InvalidKeyException e) { // e.printStackTrace();
         * log.error("", e); throw e; } catch (NoSuchAlgorithmException e) {
         * // e.printStackTrace(); log.error("", e); throw e; } catch
         * (NoSuchPaddingException e) { // e.printStackTrace(); log.error("",
         * e); throw e; } catch (InvalidAlgorithmParameterException e) { //
         * e.printStackTrace(); log.error("", e); throw e; } catch
         * (IllegalBlockSizeException e) { // e.printStackTrace();
         * log.error("", e); throw e; } catch (BadPaddingException e) { //
         * e.printStackTrace(); log.error("", e); throw e; } catch
         * (UnsupportedEncodingException e) { // e.printStackTrace();
         * log.error("", e); throw e; } catch (JSONException e) { //
         * e.printStackTrace(); log.error("", e); throw e; }
         */
    }

    /**
     * 拼接处理参数排序进行签名
     */
    public String getSign(HashMap<String, Object> paramsMap) {
        // 排序
        String urlParams = "";
        Collection<String> keyset = paramsMap.keySet();

        List<String> list = new ArrayList<String>(keyset);

        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {

            urlParams = urlParams + list.get(i) + "=" + paramsMap.get(list.get(i));
            if (i != list.size() - 1) {
                urlParams = urlParams + "&";
            }
        }

        // 拼接请求方法，主机，路径
        String sign = "";
        sign = sign + "POST";
        URI uri = null;
        try {
            uri = new URI(STS_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri != null) {
            sign = sign + uri.getPath() + "?";
        }
        sign = sign + urlParams;

        // LogUtil.e(sign);

        // 生成签名串
        sign = getSignature(sign);
        sign = sign.replaceAll("\r|\n", "");// 执行此方法后会多出换行符

        // LogUtil.e(sign);
        // url编码
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // LogUtil.e(sign);
        return sign;
    }

    // 根据指定长度生成字母和数字的随机数
    // 0~9的ASCII为48~57
    // A~Z的ASCII为65~90
    // a~z的ASCII为97~122
    public String createRandomSecretKey(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();// 随机用以下三个随机生成器
        Random randdata = new Random();
        int data = 0;
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(3);
            // 目的是随机选择生成数字，大小写字母
            switch (index) {
                case 0:
                    data = randdata.nextInt(10);// 仅仅会生成0~9
                    sb.append(data);
                    break;
                case 1:
                    data = randdata.nextInt(26) + 65;// 保证只会产生65~90之间的整数
                    sb.append((char) data);
                    break;
                case 2:
                    data = randdata.nextInt(26) + 97;// 保证只会产生97~122之间的整数
                    sb.append((char) data);
                    break;
            }
        }
        String result = sb.toString();
        return result;
    }

    /**
     * Create：2018/8/22 Author:Aron Description:生成签名字符串
     */
    public String getSignature(String sign) {
        Mac mac1 = null;
        try {
            mac1 = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = null;
        if (mac1 != null) {
            SecretKeySpec secretKey = null;
            try {
                secretKey = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), mac1.getAlgorithm());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                mac1.init(secretKey);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            try {
                hash = mac1.doFinal(sign.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (hash == null) {
            return null;
        }
        return Base64.encodeToString(hash, Base64.DEFAULT);
    }

}

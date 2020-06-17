/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-09-07 17:30:16
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.mp.product.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES 工具类
 */
public class AESUtil {
    private final static String SHA1PRNG = "SHA1PRNG";

    // @IntDef({Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE})
    @interface AESType {
    }

    /**
     * Aes加密/解密 通用加解密方案，iOS Android通用
     */
    private static final String IV_STRING = "16-Bytes--String";

    public static String WTencryptAES(String content, String key)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

	byte[] byteContent = content.getBytes("UTF-8");

	// 注意，为了能与 iOS 统一
	// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
	byte[] enCodeFormat = key.getBytes();
	SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

	byte[] initParam = IV_STRING.getBytes();
	IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

	// 指定加密的算法、工作模式和填充方式
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

	byte[] encryptedBytes = cipher.doFinal(byteContent);

	// 同样对加密后数据进行 base64 编码
	return Base64Util.base64EncodeData(encryptedBytes);
    }

    public static String WTdecryptAES(String content, String key)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

	// base64 解码

	byte[] encryptedBytes = Base64Util.getDataBase64DecodedStr(content);

	byte[] enCodeFormat = key.getBytes();
	SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");

	byte[] initParam = IV_STRING.getBytes();
	IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

	byte[] result = cipher.doFinal(encryptedBytes);

	return new String(result, "UTF-8");
    }
}

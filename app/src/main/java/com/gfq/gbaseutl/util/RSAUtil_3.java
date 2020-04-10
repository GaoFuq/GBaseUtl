package com.gfq.gbaseutl.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 *
 *
 */

public class RSAUtil_3 {

    public static final boolean PWD_ON = true;

    //字符串公钥，可以直接保存在客户端
	//todo
    public static final String PUBLIC_KEY_STR = "";


    //构建Cipher实例时所传入的的字符串，默认为"RSA/NONE/PKCS1Padding"
    private static String sTransform = "RSA/NONE/PKCS1Padding";

    //进行Base64转码时的flag设置，默认为Base64.DEFAULT
    private static int sBase64Mode = Base64.DEFAULT;


    //初始化方法，设置参数
    public static void init(String transform, int base64Mode) {
        sTransform = transform;
        sBase64Mode = base64Mode;
    }


//    public static String encryptedPwd(String pwd) {
//        String key = (String) SPUtils.get(BaseApplication.getInstance(), Const.KAIZO_PUBLIC_KEY, "");
//        LogUtil.d(TAG,"encryptedPwd key: " + key);
//        LogUtil.d(TAG,"encryptedPwd pwd: " + pwd);
//
//        //获取公钥
//        PublicKey publicKey1 = keyStrToPublicKey(key);
//        //公钥加密结果
//        String publicEncryptedResult = encryptDataByPublicKey(pwd.getBytes(), publicKey1);
//        publicEncryptedResult = publicEncryptedResult.replaceAll("\r|\n", "");
//        LogUtil.d(TAG,"encryptedPwd pwd2: " + publicEncryptedResult);
//
//        return publicEncryptedResult;
//    }

    public static String fingerPIN() {
        String psw = randomPwd();
        String content = "123456";
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            //根据内容生成secret key
            PBEKeySpec keySpec = new PBEKeySpec(content.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHSHA1ANDDES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
            //存储key
            keyStore.setEntry("alias", entry, new KeyStore.PasswordProtection(psw.toCharArray()));
        } catch (KeyStoreException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return psw;
    }


    /**
     *  把16进制的数据，转换为ASCII码的字符
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex){
        StringBuilder sb = new StringBuilder();
        for( int i=0; i<hex.length()-1; i+=2 ){
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
        }
        return sb.toString();
    }

    public static String createKeyPair() {
        //生成2048长度的秘钥对
        KeyPair keyPair = generateRSAKeyPair(2048);

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String publickey = Base64.encodeToString(publicKey.getEncoded(), sBase64Mode).replaceAll("\r|\n", "");
        String privatekey = Base64.encodeToString(privateKey.getEncoded(), sBase64Mode).replaceAll("\r|\n", "");

//        LogUtil.d(TAG,"createKeyPair publicKey: " + Base64.encodeToString(publicKey.getEncoded(), sBase64Mode));
//        LogUtil.d(TAG,"createKeyPair privateKey: " + Base64.encodeToString(privateKey.getEncoded(), sBase64Mode));
//
//        SPUtils.put(BaseApplication.getInstance(), Const.KAIZO_PRIVATE_KEY, privatekey);

        return publickey;
    }

    /*
        产生密钥对
        @param keyLength
        密钥长度，小于1024长度的密钥已经被证实是不安全的，通常设置为1024或者2048，建议2048
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //设置密钥长度
            keyPairGenerator.initialize(keyLength);
            //产生密钥对
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return keyPair;
    }

    /*
        加密或解密数据的通用方法
        @param srcData
        待处理的数据
        @param key
        公钥或者私钥
        @param mode
        指定是加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE

     */
    private static byte[] processData(byte[] srcData, Key key, int mode) {

        //用来保存处理结果
        byte[] resultBytes = null;

        try {

            //获取Cipher实例
            Cipher cipher = Cipher.getInstance(sTransform);
            //初始化Cipher，mode指定是加密还是解密，key为公钥或私钥
            cipher.init(mode, key);
            //处理数据
            resultBytes = cipher.doFinal(srcData);

        } catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return resultBytes;
    }


    /*
        使用公钥加密数据，结果用Base64转码
     */
    public static String encryptDataByPublicKey(byte[] srcData, PublicKey publicKey) {

        byte[] resultBytes = processData(srcData, publicKey, Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes, sBase64Mode);

    }

    /*
        使用私钥解密，返回解码数据
     */
    public static byte[] decryptDataByPrivate(String encryptedData, PrivateKey privateKey) {

        byte[] bytes = Base64.decode(encryptedData, sBase64Mode);

        return processData(bytes, privateKey, Cipher.DECRYPT_MODE);
    }

    /*
        使用私钥进行解密，解密数据转换为字符串，使用utf-8编码格式
     */
    public static String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey) {
        return new String(decryptDataByPrivate(encryptedData, privateKey));
    }

    /*
        使用私钥解密，解密数据转换为字符串，并指定字符集
     */
    public static String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey, String charset) {
        try {

            return new String(decryptDataByPrivate(encryptedData, privateKey), charset);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    /*
        使用私钥加密，结果用Base64转码
     */

    public static String encryptDataByPrivateKey(byte[] srcData, PrivateKey privateKey) {

        byte[] resultBytes = processData(srcData, privateKey, Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes, sBase64Mode);
    }

    /*
        使用公钥解密，返回解密数据
     */

    public static byte[] decryptDataByPublicKey(String encryptedData, PublicKey publicKey) {

        byte[] bytes = Base64.decode(encryptedData, sBase64Mode);

        return processData(bytes, publicKey, Cipher.DECRYPT_MODE);

    }

    /*
        使用公钥解密，结果转换为字符串，使用默认字符集utf-8
     */
    public static String decryptedToStrByPublicKey(String encryptedData, PublicKey publicKey) {
        return new String(decryptDataByPublicKey(encryptedData, publicKey));
    }


    /*
        使用公钥解密，结果转换为字符串，使用指定字符集
     */

    public static String decryptedToStrByPublicKey(String encryptedData, PublicKey publicKey, String charset) {
        try {

            return new String(decryptDataByPublicKey(encryptedData, publicKey), charset);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }




    /*
        将字符串形式的公钥转换为公钥对象
     */

    public static PublicKey keyStrToPublicKey(String publicKeyStr) {

        PublicKey publicKey = null;

        byte[] keyBytes = Base64.decode(publicKeyStr, sBase64Mode);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;

    }

    /*
        将字符串形式的私钥，转换为私钥对象
     */

    public static PrivateKey keyStrToPrivate(String privateKeyStr) {

        PrivateKey privateKey = null;

        byte[] keyBytes = Base64.decode(privateKeyStr, sBase64Mode);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            privateKey = keyFactory.generatePrivate(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;

    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = null;
        str = new String(byteArray);

        return str;
    }

    public static String randomPwd() {
        String code = "";
        Random rand = new Random();//生成随机数
        for (int a = 0; a < 6; a++) {
            code += rand.nextInt(10);//生成6位验证码
        }
        return code;
    }
}


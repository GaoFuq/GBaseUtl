package com.gfq.gbaseutl.util;

/**
 * create by 高富强
 * on {2020/3/10} {14:25}
 * desctapion:
 */


import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {

    //软件注册: licenseCode = 6CBF88DB03C3299452DEA47524C2B9E6C3FE0880B0D74D40D431FDD0E01B52B9D2A97D523584446503F2724FA91BF5A4F317E083FDACEEEB36F9FEB3B0CCEC089C0CC2745465C0E947E04046E4205B9F40CBD4D1554F66DFD6B58DB808A8F9BD9A25BC7E423E69D466C2E077B6114A415C4D724A5976A811B66696CBEB06E208
    //软件注册: pubKey = AwEAAdEEORBUir19jLxC/qnFwevEe8rxuJIYQ2n4eVXgjmE6rQR2VH1b8fVRzu3+Oq56c5b1QIOLlNPIf2jiCT/zN/yBTSReZ9QS6hsmOYQa/vw2uwySk0wblm6iyYNpZYvY5vVPPtAxXDENuuRvVAkl3a4UIf69g88ZFO5fc/7zECu3

    //仪器注册: licenseCode = 39D12424A7C592F6751578AF7A234C8C48DCE9AF8D3C9DDB0FFFC0BF764470E2A9633530B57667ED54C79E354336D8A2E5BABA7D3342675B54AC31D11466841DCA64858F9836671CC33EF754538C67178B3E14416AC6F4A060C11CD09A14146CE1A52CE22CD2B3871E7AA918658E48BBAC870D8A73ED225108F57658BA35C01C
    //仪器注册  pubKey = AwEAAY/TUsWXlM3r9EMHuBgQq0fGuv1UgGVM8ITCP1dEHqO/Re9h/owpT/r8i+qKoIGYuSe2VF+0aUKAfzg3lUGwHQ0srVuNjZ5bFrzzMPu7s41r1dGm7Ly1LY+zCkAbU6mp1m8tB1hLrGLvopl4MKPE7h8QM2cMlWxl1WXsxsfnZAXd



    public static final String RSA_ALGORITHM = "RSA";
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // encrypt the message
//        byte [] encrypted = encrypt(privateKey, "This is a secret message");
//        System.out.println(base64Encode(encrypted));  // <<encrypted message>>

        // decrypt the message
        String lisCode = "2EC535F7BC2BFF1E5E5DD93511F3677B13A9925F6B08434701540405434B82CFA75405904712A99BEB6623F50A5BDB9B170EB15FDCD534FB7D6FA78B641D2A5EDDBAF9DA9AAA37BCE09C2849C7895B10F36BADB4E06EF3E0A7E6372289C764011DB5255D681E17CED3E5AEA047B61C677EDBA29AD7A46057C87BC8A595049D89";
        String pubkey = "AwEAAc3RWnusPs7mnUharXU0mLjYEsr7sXGKrGge2h1Yq1ZNej1Hn5w391s/7UXHZdd/llCpwwjCcM3FZ/Z4lMtbwWDq0ePTLpSOi3HDvBVSFCJcvtZRGKodWznwImuBgRaifNQwhZatdV8zAu/lX2L0yxmavZzbccdrx0XW3gs/mqm5";
        RSAPublicKey rsaPublicKey = loadPublicKey(pubkey);
        byte[] secret = decrypt(rsaPublicKey, lisCode.getBytes());
        System.out.println(new String(secret, UTF8));     // This is a secret message
    }

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(message.getBytes(UTF8));
    }

    public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encrypted);
    }

    /**
     * 从字符串中加载公钥
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer =publicKeyStr.getBytes();
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

//    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
//        try {
//            byte[] buffer = base64Decode(privateKeyStr);
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
//            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
//            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public void savePublicKey(PublicKey publicKey) throws IOException {
//        // 得到公钥字符串
//        String publicKeyString = base64Encode(publicKey.getEncoded());
//        System.out.println("publicKeyString=" + publicKeyString);
//        FileWriter fw = new FileWriter("publicKey.keystore");
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write(publicKeyString);
//        bw.close();
//    }
//
//    public void savePrivateKey(PrivateKey privateKey) throws IOException {
//        // 得到私钥字符串
//        String privateKeyString = base64Encode(privateKey.getEncoded());
//        System.out.println("privateKeyString=" + privateKeyString);
//
//        BufferedWriter bw = new BufferedWriter(new FileWriter("privateKey.keystore"));
//        bw.write(privateKeyString);
//        bw.close();
//    }

//    public static String base64Encode(byte[] data) {
//        return HexUtil.getHex(android.util.Base64.encode(data, 0));
//    }
//
//    public static byte[] base64Decode(String data) throws IOException {
//        return Base64.decode(data, 0);
//    }
}
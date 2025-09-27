package org.example.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 旧加密工具类
 */
public class AESEncryptor {
    private static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";
    private static final Pattern PATTERN = Pattern.compile("\\s*|\t|\r|\n");
    private static final String DEFAULT_KEY = "xxxxxxx";
    private static final SecretKeySpec CACHE_KEY;

    private AESEncryptor() {
    }

    static {
        byte[] finalKey = new byte[16];
        int i = 0;
        byte[] var4 = DEFAULT_KEY.getBytes(StandardCharsets.UTF_8);
        for (byte b : var4) {
            int var10001 = i++;
            finalKey[var10001 % 16] ^= b;
        }
        CACHE_KEY = new SecretKeySpec(finalKey, AES);
    }

    private static SecretKeySpec genAesKey() {
        return CACHE_KEY;
    }

    private static void validateArguments(String origin) {
        if (StringUtils.isBlank(origin)) {
            throw new IllegalArgumentException("content parameter is null!");
        }
    }

    public static String encrypt(String origin) throws Exception {
        validateArguments(origin);
        SecretKeySpec skeySpec = genAesKey();
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);
        cipher.init(1, skeySpec);
        byte[] encrypted = cipher.doFinal(origin.getBytes(StandardCharsets.UTF_8));
        String base64Str = Base64.getEncoder().encodeToString(encrypted);
        return PATTERN.matcher(base64Str).replaceAll("");
    }

    public static String decrypt(String encrypted) throws Exception {
        if (!StringUtils.isBlank(encrypted)) {
            try {
                SecretKeySpec skeySpec = genAesKey();
                Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);
                cipher.init(2, skeySpec);
                byte[] encrypted1 = Base64.getDecoder().decode(encrypted);
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception var7) {
                throw new Exception("", var7);
            }
        } else {
            throw new IllegalArgumentException("encrypted parameter is null!");
        }
    }
}
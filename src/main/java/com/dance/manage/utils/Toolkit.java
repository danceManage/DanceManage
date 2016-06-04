package com.dance.manage.utils;

import com.dance.manage.bean.user.UserInfo;
import org.nutz.log.Log;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Logs;


/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/5/24
 */
public class Toolkit {

    public static final Log log = Logs.get();

    public static String captcha_attr = "dance_Captcha";

    public static boolean checkCaptcha(String expected, String actual)
    {
        if (expected == null || actual == null || actual.length() == 0 || actual.length() > 24)
            return false;
        return actual.equalsIgnoreCase(expected);
    }

    public static String passwordEncode(String password, String slat)
    {
        String str = slat + password + slat + password.substring(4);
        return Lang.digest("SHA-512", str);
    }

    private static final String Iv = "\0\0\0\0\0\0\0\0";

    private static final String Transformation = "DESede/CBC/PKCS5Padding";

    public static String _3DES_encode(byte[] key, byte[] data)
    {
        SecretKey deskey = new SecretKeySpec(key, "DESede");
        IvParameterSpec iv = new IvParameterSpec(Iv.getBytes());
        try {
            Cipher c1 = Cipher.getInstance(Transformation);
            c1.init(Cipher.ENCRYPT_MODE, deskey, iv);
            byte[] re = c1.doFinal(data);
            return Lang.fixedHexString(re);
        } catch (Exception e) {
            log.info("3DES FAIL?", e);
            e.printStackTrace();
        }
        return null;
    }

    public static String _3DES_decode(byte[] key, byte[] data)
    {
        SecretKey deskey = new SecretKeySpec(key, "DESede");
        IvParameterSpec iv = new IvParameterSpec(Iv.getBytes());
        try {
            Cipher c1 = Cipher.getInstance(Transformation);
            c1.init(Cipher.DECRYPT_MODE, deskey, iv);
            byte[] re = c1.doFinal(data);
            return new String(re);
        } catch (Exception e) {
            log.debug("BAD 3DES decode", e);
        }
        return null;
    }

    public static NutMap kv2map(String kv)
    {
        NutMap re = new NutMap();
        if (kv == null || kv.length() == 0 || !kv.contains("="))
            return re;
        String[] tmps = kv.split(",");
        for (String tmp : tmps) {
            if (!tmp.contains("="))
                continue;
            String[] tmps2 = tmp.split("=", 2);
            re.put(tmps2[0], tmps2[1]);
        }
        return re;
    }

    public static String randomPasswd(UserInfo userInfo)
    {
        String passwd = R.sg(10).next();
        String slat = R.sg(48).next();
        userInfo.setSalt(slat);
        userInfo.setPassword(passwordEncode(passwd, slat));
        return passwd;
    }

    public static byte[] hexstr2bytearray(String str) {
        byte[] re = new byte[str.length() / 2];
        for (int i = 0; i < re.length; i++) {
            int r = Integer.parseInt(str.substring(i*2, i*2+2), 16);
            re[i] = (byte)r;
        }
        return re;
    }
}

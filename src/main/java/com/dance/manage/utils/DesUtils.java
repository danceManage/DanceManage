package com.dance.manage.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * 标准加密工具类
 *
 * @author xx.liu
 * @version 1.0
 */
public class DesUtils
{
    private final static String ENCRYPT_KEY = "D*uy!s(o9:B,X6yZ/M";

    /**
     * Description 根据键值进行加密
     */
    public static String encrypt( String data ) throws Exception
    {
        byte[] bt = encrypt( (data.toLowerCase() + data.toUpperCase()).getBytes(), ENCRYPT_KEY.getBytes() );
        return new BASE64Encoder().encode( bt );
    }

    /**
     * Description 根据键值进行解密
     */
    public static String decrypt( String data ) throws Exception
    {
        if( data == null )
        {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer( data );
        byte[] bt = decrypt( buf, ENCRYPT_KEY.getBytes() );
        return new String( bt );
    }


    /**
     * DES加密
     *
     * @param plainText 明文
     * @param key       密钥
     */
    public static String encoderByDES( String plainText, String key )
    {
        try
        {
            byte[] result = coderByDES( plainText.getBytes( "UTF-8" ), key, Cipher.ENCRYPT_MODE );
            return byteArr2HexStr( result );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            return "";
        }
    }


    /**
     * DES解密
     *
     * @param secretText 密文
     * @param key        密钥
     */
    public static String decoderByDES( String secretText, String key )
    {
        try
        {
            byte[] result = coderByDES( hexStr2ByteArr( secretText ), key, Cipher.DECRYPT_MODE );
            return new String( result, "UTF-8" );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            return "";
        }
    }



    private static byte[] coderByDES( byte[] plainText, String key, int mode ) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        SecureRandom sr = new SecureRandom();
        byte[] resultKey = makeKey( key );
        DESKeySpec desSpec = new DESKeySpec( resultKey );
        SecretKey secretKey = SecretKeyFactory.getInstance( "DES" ).generateSecret( desSpec );
        Cipher cipher = Cipher.getInstance( "DES" );
        cipher.init( mode, secretKey, sr );
        return cipher.doFinal( plainText );
    }


    private static byte[] makeKey( String key ) throws UnsupportedEncodingException
    {
        byte[] keyByte = new byte[ 8 ];
        byte[] keyResult = key.getBytes( "UTF-8" );
        for ( int i = 0; i < keyResult.length && i < keyByte.length; i++ )
        {
            keyByte[ i ] = keyResult[ i ];
        }
        return keyByte;
    }


    private static String byteArr2HexStr( byte[] arrB )
    {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer( iLen * 2 );
        for ( byte anArrB : arrB )
        {
            int intTmp = anArrB;
            // 把负数转换为正数
            while ( intTmp < 0 )
            {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if ( intTmp < 16 )
            {
                sb.append( "0" );
            }
            sb.append( Integer.toString( intTmp, 16 ) );
        }
        return sb.toString();
    }

    private static byte[] hexStr2ByteArr( String strIn ) throws NumberFormatException
    {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[ iLen / 2 ];
        for ( int i = 0; i < iLen; i = i + 2 )
        {
            String strTmp = new String( arrB, i, 2 );
            arrOut[ i / 2 ] = ( byte ) Integer.parseInt( strTmp, 16 );
        }
        return arrOut;
    }

    /**
     * Description 根据键值进行加密
     */
    private static byte[] encrypt( byte[] data, byte[] key ) throws Exception
    {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec( key );
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "DES" );
        SecretKey securekey = keyFactory.generateSecret( dks );
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance( "DES" );
        // 用密钥初始化Cipher对象
        cipher.init( Cipher.ENCRYPT_MODE, securekey, sr );
        return cipher.doFinal( data );
    }


    /**
     * Description 根据键值进行解密
     */
    private static byte[] decrypt( byte[] data, byte[] key ) throws Exception
    {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec( key );
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "DES" );
        SecretKey securekey = keyFactory.generateSecret( dks );
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance( "DES" );
        // 用密钥初始化Cipher对象
        cipher.init( Cipher.DECRYPT_MODE, securekey, sr );
        return cipher.doFinal( data );
    }

    public static void main( String [] args ) throws Exception
    {
        System.out.println(decrypt( "DpuaxALeu39+spBkBb/GWA==" ));
//        System.out.println(encrypt( "123456" ));
    }
}

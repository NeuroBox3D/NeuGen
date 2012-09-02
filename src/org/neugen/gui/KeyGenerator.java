package org.neugen.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Sergei
 */
public class KeyGenerator {

    private static String externLocalKey = "neuroKey";
    private static String internLocalKey = "01234567";

    public static String getInternCodedKey(String newPass) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(newPass.getBytes(), out, internLocalKey);
        newPass = new BASE64Encoder().encode(out.toByteArray());
        return newPass;
    }

    public static String getExternCodedKey(String newPass) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(newPass.getBytes(), out, externLocalKey);
        newPass = new BASE64Encoder().encode(out.toByteArray());
        return newPass;
    }

    public static String getInternDecodedKey(String codedPass) throws IOException, Exception {
        byte[] decode = new BASE64Decoder().decodeBuffer(codedPass);
        InputStream is = new ByteArrayInputStream(decode);
        String decodedLK = new String(decode(is, internLocalKey)).trim();
        return decodedLK;
    }

    public static String getExternDecodedKey(String codedPass) throws IOException, Exception {
        byte[] decode = new BASE64Decoder().decodeBuffer(codedPass);
        InputStream is = new ByteArrayInputStream(decode);
        String decodedLK = new String(decode(is, externLocalKey)).trim();
        return decodedLK;
    }

    public static void encode(byte[] bytes, OutputStream out, String pass) throws Exception {
        Cipher c = Cipher.getInstance("DES");
        Key k = new SecretKeySpec(pass.getBytes(), "DES");
        c.init(Cipher.ENCRYPT_MODE, k);

        OutputStream cos = new CipherOutputStream(out, c);
        cos.write(bytes);
        cos.close();
    }

    public static byte[] decode(InputStream is, String pass) throws Exception {
        Cipher c = Cipher.getInstance("DES");
        Key k = new SecretKeySpec(pass.getBytes(), "DES");
        c.init(Cipher.DECRYPT_MODE, k);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(is, c);

        for (int b = 0; b != - 1; b = cis.read()) {
            bos.write(b);
        }
        cis.close();
        return bos.toByteArray();
    }

    /**
     *
     * @param toEncode
     * @param pass 8 stellig (01234567)
     * @return toEncode
     */
    public static String getEncodedString(String toEncode, String pass) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(toEncode.getBytes(), out, pass);
        toEncode = new BASE64Encoder().encode(out.toByteArray());
        return toEncode;
    }

    public static void main(final String[] args) throws Exception {
        String newPass = "new pass ...";

        String externCoded = getExternCodedKey(newPass);
        System.out.println("save this code to lk file: " + externCoded);

        String decodedExternCode = getExternDecodedKey(externCoded);
        System.out.println("this is the key: " + decodedExternCode);

        String internCoded = getInternCodedKey(newPass);
        System.out.println("save this code in NeuGenApp: " + internCoded);
        String decodedInternCode = getInternDecodedKey(internCoded);
        System.out.println("this is the key: " + decodedInternCode);
    }
}

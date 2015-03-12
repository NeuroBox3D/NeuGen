/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */
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

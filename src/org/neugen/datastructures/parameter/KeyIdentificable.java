/*
 * File: KeyIdentificable.java
 *
 * Created on 15.07.2009, 12:23:11
 *
 */
package org.neugen.datastructures.parameter;

import org.apache.log4j.Logger;

/**
 * KeyIdentificable is a help class to construct key hierarchies.
 * It's useful to handle hierarchy of .NEU configuration files,
 * which define such by intentation. We say here "key" to describe
 * a text string formed by space divided nonspaced alphanumeric words.
 * Each KeyIdentificable has a part of a key. For Example:
 * A KeyIdentificable A is the container of B, both constructed with
 * A(null,"A") and B(&A,"B"). B.fullKey() gives back "A B".
 *
 * @author Alexander Wanner
 * 
 */
public class KeyIdentificable {

    /** use to log messages  */
    private static final Logger logger = Logger.getLogger(KeyIdentificable.class.getName());
    /** Pointer to KeyIdentificable container "containing" this object. */
    protected KeyIdentificable container;
    /** Special subkey, last part of the full key of this object. */
    protected String key = null;
    protected boolean detailed_output = false;
    private StringBuffer fullKey;

    /**
     * Constructs a KeyIdentificable
     */
    public KeyIdentificable(String lastkey) {
        key = lastkey;
        fullKey = new StringBuffer();
        fullKey.append(key);
    }

    /**
     * Constructs a KeyIdentificable contained in an other with
     * specified @param last key and @param container.
     */
    public KeyIdentificable(KeyIdentificable k, String lastkey) {
        container = k;
        key = lastkey;
        fullKey = new StringBuffer();
        //logger.info("lastKey: " + lastkey);
        //logger.info("fulllKey before: " + fullKey);
        if (container != null) {
            //fullKey.delete(0, fullKey.length());
            fullKey.append(container.getFullKey());
            fullKey.append("/").append(key);
        } else {
            //fullKey.delete(0, fullKey.length());
            fullKey.append(key);
        }
        //fullKey.append(key);
        //logger.info("fulllKey after: " + fullKey);
    }

    /**
     * Redefines a KeyIdentificable as contained in an other with
     * specified @param last key and @param container.
     * Returns true exactly if both container and lastkey
     * are not NULL.
     */
    public boolean init(KeyIdentificable container, String lastkey) {
        //System.out.println("KeyIdentificable init");
        if (container == null) {
            return false;
        }
        this.container = container;
        if (lastkey != null) {
            key = lastkey;
        }
        fullKey.delete(0, fullKey.length());
        fullKey.append(container.getFullKey());
        fullKey.append("/").append(key);
        return true;
    }

    /**
     * Redefines a KeyIdentificable as contained in an other with
     * specified @param last key and @param container.
     * If @param lastkey == NULL, then the old last key
     * saved before remains.
     */
    public void reinit(KeyIdentificable container, String lastkey) {
        this.container = container;
        if (lastkey != null) {
            key = lastkey;
            fullKey.delete(0, fullKey.length());
            if (container != null) {
                fullKey.append(container.getFullKey());
                fullKey.append("/").append(key);
            } else {
                fullKey.append(key);
            }
        }
    }

    /**
     * Returns full key for this KeyIdentificable.
     * 
     * @return the value of fullKey
     */
    public StringBuffer getFullKey() {
        return new StringBuffer(fullKey);
    }

    /** 
     * Return last part of the full key.
     *
     * @return the value of key
     */
    public String getLastKeyPart() {
        return key;
    }

    /**
     * Reeturn container.
     *
     * @return the value of container
     */
    public KeyIdentificable getContainer() {
        return container;
    }
}

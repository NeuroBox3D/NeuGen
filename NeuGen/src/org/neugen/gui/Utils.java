package org.neugen.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

/**
 *
 * @author Sergei
 */
public class Utils {

    private Logger logger;
    private Properties properties;

    public Utils(Logger logger, Properties prop) {
        this.logger = logger;
        this.properties = prop;
    }


    public float setFloatLabel(JTextField textField, String suffix) {
        String length = textField.getText();
        if (isFloatValid(length)) {
            float value = getFloatValue(length);
            properties.setProperty(textField.getName() + suffix, length);
            return value;
        } else {
            textField.setText("");
            properties.setProperty(textField.getName() + suffix, "");
            return 0.0f;
        }
    }

    public void setCheckBox(String key, String val) {
        properties.setProperty(key, val);
    }

    public int setIntegerLabel(JTextField textField, String suffix) {
        String length = textField.getText();
        if (isIntegerValid(length)) {
            int value = getIntegerValue(length);
            logger.info("name: " + textField.getName());
            logger.info("value: " + value);
            properties.setProperty(textField.getName() + suffix, length);
            return value;
        } else {
            textField.setText("");
            properties.setProperty(textField.getName() + suffix, "");
            return 0;
        }
    }

    public float getFloatValue(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            logger.error(e);
            return 0.0f;
        }
    }

    public boolean isFloatValid(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            logger.error(e);
            return false;
        }
    }

    public int getIntegerValue(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            logger.error(e);
            return 0;
        }
    }

    public boolean isIntegerValid(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            logger.error(e);
            return false;
        }
    }

    public void writeProp(File f) {
        try {
            logger.info("write prop to: " + f.getAbsolutePath());
            FileOutputStream propOutFile = new FileOutputStream(f);
            properties.store(propOutFile, "Dialog Data");
            properties.clear();
            propOutFile.flush();
            propOutFile.close();
        } catch (FileNotFoundException e) {
            logger.error("Can’t find " + f, e);
        } catch (IOException e) {
            logger.error("I/O failed.", e);
        }
    }
}

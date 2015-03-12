/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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

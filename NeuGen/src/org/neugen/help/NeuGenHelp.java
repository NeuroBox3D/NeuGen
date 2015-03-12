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
package org.neugen.help;

import java.net.URL;
import javax.help.CSH;
import javax.help.CSH.DisplayHelpFromSource;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import org.apache.log4j.Logger;

public class NeuGenHelp {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(NeuGenHelp.class.getName());
    private ClassLoader DEFAULT_CLASS_LOADER = null;
    private static final String PATH_TO_JAVA_HELP = "org/neugen/help/jhelpset.hs";
    private HelpBroker helpBroker;
    private CSH.DisplayHelpFromSource displayHelp;

    private static class NeuGenHelpHolder {

        private static final NeuGenHelp INSTANCE = new NeuGenHelp();
    }

    public static NeuGenHelp getInstance() {
        return NeuGenHelpHolder.INSTANCE;
    }

    public NeuGenHelp() {
        initHelpSystem();
    }

    /** Initialize the JavaHelp system. */
    private void initHelpSystem() {
        logger.info("init help");
        //optimization to avoid repeated init
        if (helpBroker != null && displayHelp != null) {
            return;
        }
        //(uses the classloader mechanism)
        ClassLoader loader = this.getClass().getClassLoader();
        URL helpSetURL = HelpSet.findHelpSet(loader, PATH_TO_JAVA_HELP);
        assert helpSetURL != null : "Cannot find help system.";
        try {
            HelpSet helpSet = new HelpSet(DEFAULT_CLASS_LOADER, helpSetURL);
            helpBroker = helpSet.createHelpBroker();
            displayHelp = new CSH.DisplayHelpFromSource(helpBroker);
        } catch (HelpSetException ex) {
            logger.error("Cannot create help system with: " + helpSetURL, ex);
        }
        assert helpBroker != null : "HelpBroker is null.";
    }

    public DisplayHelpFromSource getfDisplayHelp() {
        return displayHelp;
    }
}

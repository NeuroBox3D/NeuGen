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

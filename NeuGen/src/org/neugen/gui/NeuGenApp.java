package org.neugen.gui;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.neugen.utils.NeuGenLogger;

/**
 * The main class of the application.
 * 
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class NeuGenApp extends SingleFrameApplication {

    private final static Logger logger = Logger.getLogger(NeuGenApp.class.getName());
    public static boolean antiAliasing = true;

    /** At startup create and show the main frame of the application. */
    @Override
    protected void startup() {
        //UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        //Options.setDefaultIconSize(new Dimension(18, 18));
        String lafName =
                LookUtils.IS_OS_WINDOWS
                ? Options.getCrossPlatformLookAndFeelClassName()
                : Options.getSystemLookAndFeelClassName();

        try {
            UIManager.setLookAndFeel(lafName);
            logger.info("Look and feel name: " + lafName);
        } catch (Exception e) {
            logger.error("Can't set look & feel:" + e, e);
        }

        /*
        PlasticLookAndFeel.setPlasticTheme(new DesertBlue());
        try {
        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {
        }
         *
         */
        NeuGenView ngView = new NeuGenView(this);
        ngView.getFrame().setLocationRelativeTo(null);

        NeuGenView.setInstance(ngView);
        show(ngView);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    @Override
    protected void shutdown() {
        logger.info("End logging:*******************************************************\n\n");
        // The default shutdown saves session window state.
//        super.shutdown();
        // Now perform any other shutdown tasks you need.
//        System.exit(0);
    }

    /** A convenient static getter for the application instance. */
    public static NeuGenApp getApplication() {
        return Application.getInstance(NeuGenApp.class);
    }

    public static void handleLicenseKey() throws Exception {
        // License key
        File lkf = new File(".lk");
        boolean lkFound = false;
        //generated code: KeyGenerator.getInternCodedKey..(newPass)
        String encodedCode = "T0zBdfTmwauvn/7+QaOuaaSJgenjJ1+/";
        String decodedLK = KeyGenerator.getInternDecodedKey(encodedCode);

        if (lkf.exists()) {
            try {
                BufferedReader lkReader = new BufferedReader(new FileReader(lkf));
                String licenseString = lkReader.readLine();
                licenseString = KeyGenerator.getExternDecodedKey(licenseString);
                if (licenseString != null && licenseString.equals(decodedLK)) {
                    lkFound = true;
                }
            } catch (FileNotFoundException e) {
                logger.error(e, e);
            } catch (IOException e) {
                logger.error(e, e);
            }
        }

        if (!lkFound) {
            try {
                String licenseKey = "";
                NGKeyDialog keyDialog = new NGKeyDialog(getApplication().getMainFrame(), true);
                keyDialog.setLocationByPlatform(true);
                do {
                    keyDialog.setVisible(true);
                    if (NGKeyDialog.RET_OK == keyDialog.getReturnStatus()) {
                        char[] key = keyDialog.getKeyField().getPassword();
                        for (char c : key) {
                            licenseKey += c;
                        }
                        if (!licenseKey.equals(decodedLK)) {
                            keyDialog.getKeyField().setText("");
                            String messagePrefix = licenseKey + " Invalid license key. Please try again.. ";
                            JOptionPane.showMessageDialog(getApplication().getMainFrame(),
                                    messagePrefix,
                                    "Error Message",
                                    JOptionPane.ERROR_MESSAGE);
                            licenseKey = "";
                        }
                    } else {
                        System.exit(0);
                    }
                } while (!licenseKey.equals(decodedLK));
                FileWriter fw = new FileWriter(".lk");
                String codeToWrite = KeyGenerator.getExternCodedKey(decodedLK);
                fw.write(codeToWrite);
                fw.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    /** Main method launching the application.*/
    public static void main(File logProperties, final String[] args) {
        NeuGenLogger.initLogger(logProperties);

//        try {
//            handleLicenseKey();
//        } catch (Exception ex) {
//            logger.error(ex);
//        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    launch(NeuGenApp.class, args);
                }
            });

    }
    
    /** Main method launching the application.*/
    public static void main(final String[] args) {
        NeuGenLogger.initLogger();

//        try {
//            handleLicenseKey();
//        } catch (Exception ex) {
//            logger.error(ex);
//        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    launch(NeuGenApp.class, args);
                }
            });

    }
}

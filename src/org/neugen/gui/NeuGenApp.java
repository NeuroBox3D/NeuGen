package org.neugen.gui;

import org.neugen.utils.NeuGenLogger;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;

import javax.swing.UIManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.neugen.utils.Utils;

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
        super.shutdown();
        // Now perform any other shutdown tasks you need.
        System.exit(0);
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
    public static void main(final String[] args) {
        NeuGenLogger.initLogger();

        try {
            handleLicenseKey();
        } catch (Exception ex) {
            logger.error(ex);
        }

        if (args.length > 0) {
            logger.info("Start logging..:*******************************************************");
            logger.info("Run main program (number of arguments): " + args.length);
            String version = Utils.getJ3DVersion();
            logger.info("java3d version = " + version);

            
            if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            System.setProperty("j3d.rend", "jogl");
            } else if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            System.setProperty("j3d.rend", "d3d");
            } else {
            System.setProperty("j3d.rend", "ogl");
            }

            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    launch(NeuGenApp.class, args);
                }
            });
        } else {
            logger.info("Set first the path to java3d directory.");
            String file_sep = System.getProperty("file.separator");
            String path_sep = System.getProperty("path.separator");
            int heapSize = -1;
            String antialiasingValue = "false";
            while (heapSize == -1) {
                try {
                    /*
                    //sValue = JOptionPane.showInputDialog("Please choose the memory you want to give to the " + "process in MB", "1000");
                    String message = "Please choose the memory you want to give to the process in MB";
                    String title = "NeuGen Configuration";
                    Object[] options = {"Yes, please",
                        "No, thanks",
                        "No eggs, no ham!"};
                    JOptionPane.showInputDialog(getApplication().getMainFrame(), message, title, JOptionPane.QUESTION_MESSAGE, null, null, "1000");
                     *
                     */
                    logger.info("starte mem dialog");
                    MemoryDialog mDialog = new MemoryDialog(new javax.swing.JFrame(), true);
                    mDialog.setVisible(true);
                    mDialog.setLocationRelativeTo(null);
                    if (mDialog.getReturnStatus() == MemoryDialog.RET_OK) {
                        if(mDialog.getAnitaiasing()) {
                            antialiasingValue = "true";
                        }
                        heapSize = mDialog.getHeapSize();
                    } else {
                        System.exit(0);
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, heapSize + "is not a valid integer number! ");
                    logger.error(e1);
                    heapSize = -1;
                }
            }
            String binJ3dDir = "";
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
                logger.info("os.arch: " + System.getProperty("os.arch"));
                logger.info("java.vm.name: " + System.getProperty("java.vm.name"));
                logger.info("os.name: " + System.getProperty("os.name"));

                if (System.getProperty("os.arch").indexOf("64") >= 0) {
                    binJ3dDir = NeuGenConstants.J3D_WIN_64; //dll
                } else {
                    binJ3dDir = NeuGenConstants.J3D_WIN_32;
                    if (heapSize > 1500) {
                        heapSize = 1500;
                    }
                }
            } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0) {
                if (System.getProperty("os.arch").indexOf("64") >= 0) {
                    binJ3dDir = NeuGenConstants.J3D_LIN_64;
                } else {
                    binJ3dDir = NeuGenConstants.J3D_LIN_32;
                    if (heapSize > 1500) {
                        heapSize = 1500;
                    }
                }
            } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
                binJ3dDir = NeuGenConstants.J3D_MACOSX;
                if (System.getProperty("os.arch").indexOf("64") >= 0) {
                } else {
                    if (heapSize > 1500) {
                        heapSize = 1500;
                    }
                }
            }
            
            com.jogamp.common.nio.Buffers b;

            String call;
            // Mac (test -classpath)
            if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
                System.out.println("TEST!!!!!!");
                //call = "java -ea -Xms64m -Xmx" + sValue + "m -jar NeuGenJava.jar runProgram";
                call = "java -ea -Xms64m -Xmx" + heapSize + "m"
                        + "-Xbootclasspath/p:lib/j3dcore.jar:lib/j3dutils.jar:lib/vecmath.jar:lib/jogl-all.jar:lib/jogl-all-natives-macosx.jar:lib/gluegen-rt.jar:lib/gluegen.jar:lib/gluegen-rt-natives-macosx-universal.jar"
                        + " -Djava.ext.dirs=./lib "
//                        + " -Djava.library.path=." + file_sep + "lib" + path_sep + "."
//                        + file_sep + "lib" + file_sep + binJ3dDir
                        // + " -jar NeuGenJava.jar runProgram";
                        //+ " -Dj3d.rend=jogl"
                        //+ " -Dj3d.debug=true"
                        + " -Dj3d.implicitAntialiasing=" + antialiasingValue
                        // + " -Dj3d.d3dVertexProcess=mixed"
                        + " -jar NeuGenJava.jar runProgram";
            } // Windows
            else if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                call = "java -ea -Xms64m -Xmx" + heapSize + "m"
                        + " -Djava.ext.dirs=./lib "
                        + " -Djava.library.path=." + file_sep + "lib" + path_sep + "."
                        + file_sep + "lib" + file_sep + binJ3dDir
                        //+ " -Dj3d.rend=jogl"
                        //+ " -Dj3d.rend=d3d"
                        //+ " -Dj3d.debug=true"
                        //+ " -Dj3d.shadingLanguage=Cg"
                        + " -Dj3d.implicitAntialiasing=" + antialiasingValue
                        //+ " -Dj3d.d3dVertexProcess=mixed"
                        + " -jar NeuGenJava.jar runProgram";
            } // Linux
            else {
                call = "java -ea -Xms64m -Xmx" + heapSize + "m"
                        + " -Djava.library.path=." + file_sep + "lib" + path_sep + "."
                        + file_sep + "lib" + file_sep + binJ3dDir
                        //+ " -jar NeuGenJava.jar runProgram";
                        //+ " -Dj3d.rend=ogl"
                        //+ " -Dj3d.debug=true"
                        + " -Dj3d.implicitAntialiasing=" + antialiasingValue
                        //+ " -Dj3d.d3dVertexProcess=mixed"
                        + " -jar NeuGenJava.jar runProgram";
            }

            logger.info(call);
            String s = null;
            try {
                logger.info("\n" + "*************** NeuGen begin! **********************");
                logger.info("Running on JVM " + System.getProperty("java.version"));
                logger.info(System.getProperty("os.name"));
                // using the Runtime exec method:
                Process p = Runtime.getRuntime().exec(call);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                // read the output from the command
                //System.out.println("Here is the standard output of the command:\n");
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
                //read any errors from the attempted command
                //System.out.println("Here is the standard error of the command (if any):\n");
                while ((s = stdError.readLine()) != null) {
                    //System.out.println(s);
                    logger.error(s);
                }
                stdInput.close();
                stdError.close();

                System.gc();
                System.exit(0);
            } catch (IOException e) {
                logger.error("exception happened - here's what I know: ");
                logger.error(e, e);
                System.exit(-1);
            }
        }
    }
}

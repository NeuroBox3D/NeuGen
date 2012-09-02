package org.neugen.utils;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.media.j3d.VirtualUniverse;
import org.neugen.gui.NeuGenConstants;

/**
 * @author Jens Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class Utils {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(Utils.class.getName());


    public static String getMemoryStatus() {
        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        String mes;
        mes = ("free memory: " + freeMemory / 1048576) + " MB\n";
        mes += ("allocated memory: " + allocatedMemory / 1048576) + " MB\n";
        mes += ("max memory: " + maxMemory / 1048576) + " MB\n";
        mes += ("total free memory: " + (freeMemory + (maxMemory - allocatedMemory)) / 1048576) + " MB\n";
        return "\nNeuGen Memory:\n" + mes;
    }

    /*
    public static String getMemoryStatus() {
    Runtime rt = Runtime.getRuntime();
    return "\nNeuGen memory: " + ((rt.totalMemory() - rt.freeMemory()) / 1048576) + "MB used, " + rt.totalMemory() / 1048576 + "MB total\n cleaning\n\n";
    }
     *
     */
    public static void printOutOfMemoryMessage() {
        logger.info(("\nMemory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576)
                + "MB used " + Runtime.getRuntime().totalMemory() / 1048576 + "MB total\n" + " not enough memory available for visualization... aborting\n\n"));
    }

    public static void testJ3D() {
        getMemoryStatus();
        //System.gc();
        try {
            // test if java3d installed
            Class.forName("javax.media.j3d.Canvas3D");
            logger.info("java3d is installed on your runtime environment");
        } catch (ClassNotFoundException notFound) {
            logger.info(notFound);
            JOptionPane.showMessageDialog(null, "There seems to be no java3d installed on your java runtime environment,"
                    + "which you need to use the 3D visualization.",
                    "visualization impossible", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String getJ3DVersion() {
        try {
            Class.forName("javax.media.j3d.VirtualUniverse");
        } catch (ClassNotFoundException e) {
            logger.error(e);
            return "";
        }

        VirtualUniverse u = new VirtualUniverse();
        return (String) u.getProperties().get("j3d.specification.version");
    }

    /**
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Get the prefix of a file.
     */
    public static String getPrefix(File file) {
        String fileName = file.getName();
        int index = fileName.indexOf(NeuGenConstants.NEUGEN_SUFFIX);
        String prefix = "";
        if (index > 0 && index < fileName.length() - 1) {
            prefix = fileName.substring(0, index);
        }
        return prefix;
    }

    /**
     * @param fch The file chooser,
     * @return boolean true, if a file whith same name exists
     */
    public static boolean testExistingFile(JFileChooser fch) {
        int selection = 0;
        if (fch.getSelectedFile().exists()) {
            selection = JOptionPane.showOptionDialog(new JFrame(), fch.getSelectedFile().getName()
                    + " exists already. Do you want to overwrite?",
                    "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (selection == JFileChooser.APPROVE_OPTION) {
                boolean isDir = fch.getSelectedFile().isDirectory();
                del(fch.getSelectedFile());
                if (isDir) {
                    boolean mkDir = fch.getSelectedFile().mkdir();
                    while(!mkDir) {
                        mkDir = fch.getSelectedFile().mkdir();
                    }
                }
                return true;
            }
        } else if (!fch.getSelectedFile().exists()) {
            return true;
        }
        return false;
    }

    public static boolean del(File dir) {
        if (dir.isDirectory()) {
            String[] entries = dir.list();
            for (int i = 0; i < entries.length; i++) {
                File file = new File(dir.getPath(), entries[i]);
                del(file);
            }
            if (dir.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (dir.delete()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * @param fch
     * @return boolean
     */
    public static boolean fileExists(File file) {
        int selection = 0;
        if (file.exists()) {
            selection = JOptionPane.showOptionDialog(new JFrame(), file.getName()
                    + " exists already. Do you want to overwrite?",
                    "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (selection == JFileChooser.APPROVE_OPTION) {
                del(file);
                return true;
            }
        } else if (!file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * @param file Directory
     * @return
     */
    public static boolean testExistingDirectory(File file) {
        if (file.isFile()) {
            return true;
        }
        int selection = 0;
        if (file.exists()) {
            selection = JOptionPane.showOptionDialog(new JFrame(), file.getName()
                    + " exists already. Do you want to overwrite?",
                    "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (selection == JFileChooser.APPROVE_OPTION) {
                return true;
            }
        } else if (!file.exists()) {
            return true;
        }
        return false;
    }

    public static void copyDir(File source, File target) throws FileNotFoundException, IOException {
        File[] files = source.listFiles();
        target.mkdirs();

        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().equals("CVS")) {
                    copyDir(file, new File(target.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
                }
            } else {
                File targetFile = new File(target.getAbsolutePath() + System.getProperty("file.separator") + file.getName());
                copyFile(file, targetFile);
            }
        }
    }

    public static void copyFile(File source, File target) throws FileNotFoundException {
        try {
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(target);
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            logger.error(e, e);
        }
    }
}

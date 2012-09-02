package org.neugen.gui;

import org.neugen.utils.Utils;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * @author Sergei
 */
public final class NGFileFilter {

    public final static class NeuTriaFileFilter extends FileFilter {

        /** Accept all directories and all hoc files. */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_SHOC)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** the description of this file filter */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_SHOC;
        }
    }

    public final static class NeuGenVisualFileFilter extends FileFilter {

        /** Accept all directories and all neu files. */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_BG)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** The description of this filter. */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_NG_VISUAL;
        }
    }

    public final static class NeuGenConfFileFilter extends FileFilter {

        /** Accept all directories and all neu files. */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_NEU)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** The description of this filter. */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_NEU;
        }
    }

    public final static class ImageFileFilter extends FileFilter {

        /** Accept all directories and all gif, jpg, tiff, or png files.*/
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals("tif") || extension.equals("tiff")
                         || extension.equals("gif")
                         || extension.equals("jpeg")
                         || extension.equals("jpg")
                         || extension.equals("png")
                        ) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** The description of this filter */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_IMAGE;
        }
    }

    public final static class ImageSequenceFileFilter extends FileFilter {

        /** Accept all directories and all gif, jpg, tiff, or png files.*/
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals("tif") || extension.equals("tiff")
                        || extension.equals("gif")
                        || extension.equals("jpeg")
                        || extension.equals("jpg")
                        || extension.equals("png")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** The description of this filter */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_IMAGE_SEQUENCE;
        }
    }

    public static final class NeuroMLFileFilter extends FileFilter {

        /** Accept all directories and all xml files. */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_XML)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** the description of this file filter */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_NEUROML;
        }
    }

    public static final class CvappFileFilter extends FileFilter {

        /** Accept all directories and all xml files. */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            String fName = f.getName();
            if (extension != null) {
                if (extension.equals("txt")) {
                    if (fName.toLowerCase().endsWith(".swc.txt")) {
                        return true;
                    }
                }
            }

            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_SWC)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** the description of this file filter */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_SWC;
        }
    }

    public static final class NeuronFileFilter extends FileFilter {

        /** Accept all directories and all hoc files. */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_HOC)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /** the description of this file filter. */
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_HOC;
        }
    }

    public static final class TextFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_TXT)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_TEXT;
        }
    }

    public static final class NeuRAFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_TXT)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_NEURA;
        }
    }


    public static final class ObjFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_OBJ)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_OBJ;
        }
    }

    public static final class CSVFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Utils.getExtension(f);
            if (extension != null) {
                if (extension.equals(NeuGenConstants.EXTENSION_CSV)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_CSV;
        }
    }
}

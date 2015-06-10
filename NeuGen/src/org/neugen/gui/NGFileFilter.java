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
    
    public static final class NGXFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            
            String ext = Utils.getExtension(f);
            if (NeuGenConstants.EXTENSION_NGX.equals(ext)) {
                return true;
            } else {
                return false;
            }
        }
        
        @Override
        public String getDescription() {
            return NeuGenConstants.DESCRIPTION_NGX;
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

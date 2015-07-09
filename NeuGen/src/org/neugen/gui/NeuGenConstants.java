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

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * @author Sergei Wolf
 */
public final class NeuGenConstants {

    private static String currentDate;
    static{
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        currentDate = sdf.format(new Date());
        //System.out.println(sdf.format(new Date()));
    }

    private static final Logger logger = Logger.getLogger(NeuGenConstants.class.getName());
    //projects
    public static final String TCU_DATA_FILE = "tcu_data.txt";
    public static final String NEUGEN_PROJECT_FILE = "neugen_project.xml";
    public static final String PROP_PROJECT_NAME_KEY = "name";
    public static final String PROP_DATE_KEY = "date";
    public static final String COPYRIGHT = "by Jens P. Eberhard and Alexander Wanner. \n\u00a9 2011 Goethe-Universität Frankfurt am Main | Steinbeis-Transferzentrum für Technische Simulation";
    //public static String VERSION = "NeuGen Version 1.6.1 2011-01-26";
    public static final String VERSION = "NeuGen Version 1.7.1 " + currentDate;
    public static final String NEOCORTEX_PROJECT = "Neocortex";
    public static final String HIPPOCAMPUS_PROJECT = "Hippocampus";
    public static final String NEOCOREX_DESCRIPTION = "Creates a new project that generates cells of neocortex.";
    public static final String HIPPOCAMPUT_DESCRIPTION = "Creates a new project that generates cells of hippocampus.";
    //configure *.neu files
    public static final String PARAM_FNAME = "Param.neu";
    public static final String INTERNA_FNAME = "Interna.neu";
    public static final String HELP_FNAME = "Help.neu";
    public static final String PARAM = "Param";
    public static final String INTERNA = "Interna";
    public static final String OUTPUT = "OutputOptions";
    public static final String NET_FNAME = "Net.xml";
    public static final String NEUGEN_SUFFIX = ".neu";
    public static final String PROPERTIES_SUFFIX = ".properties";
    public static final String XML_SUFFIX = ".xml";
    public static final String PROPERTIES_KEY = "appendix";
    public static final String DTD_FNAME = "DTD.txt";
    //folders
    public static final String CONFIG_DIR = "config";
    public static final String FILE_SEP = System.getProperty("file.separator");
    public static final String J3D_WIN_64 = "j3d-1_5_2-windows-amd64";
    public static final String J3D_WIN_32 = "j3d-1_5_2-windows-i586";
    public static final String J3D_LIN_64 = "j3d-1_5_2-linux-amd64";
    public static final String J3D_LIN_32 = "j3d-1_5_2-linux-i586";
    public static final String J3D_MACOSX = "j3d-1_5_2-macosx";
    public static final String J3D_ERROR_MESSAGE = "There seems to be no java3d installed in your java runtime environment,"
            + "which you need to use the 3D visualization with NeuGen.";
    //file filters
    public static final String EXTENSION_HOC = "hoc";
    public static final String EXTENSION_NEU = "neu";
    public static final String EXTENSION_SHOC = "shoc";
    public static final String EXTENSION_XML = "xml";
    public static final String EXTENSION_DX = "dx";
    public static final String EXTENSION_TXT = "txt";
    public static final String EXTENSION_SWC = "swc";
    public static final String EXTENSION_CSV = "csv";
    public static final String EXTENSION_OBJ = "obj";
    public static final String EXTENSION_BG = "bg";
    public static final String EXTENSION_NGX = "ngx";
    public static final String DESCRIPTION_HOC = "Neuron Files (*.hoc)";
    public static final String DESCRIPTION_NEU = "NeuGen Files (*.neu)";
    public static final String DESCRIPTION_SHOC = "NeuTria Files (*.shoc)";
    public static final String DESCRIPTION_NG_VISUAL = "NeuGen Visual (*.bg)";
    public static final String DESCRIPTION_DX = "Data Explorer Files (*.dx)";
    public static final String DESCRIPTION_NEUROML = "NeuroML Files (*.xml)";
    public static final String DESCRIPTION_TEXT = "Text Files (*.txt)";
    public static final String DESCRIPTION_NEURA = "NeuRa Files (*.txt)";
    public static final String DESCRIPTION_IMAGE = "Image Stack (*.*)";
    public static final String DESCRIPTION_IMAGE_SEQUENCE = "Image Sequence (*.*)";
    public static final String DESCRIPTION_OBJ = "Wavefront Files (*.obj)";
    public static final String DESCRIPTION_SWC = "Cvapp Files (*.swc)";
    public static final String DESCRIPTION_CSV = "CSV Files (*.csv)";
    public static final String DESCRIPTION_NGX = "NGX Files (*.ngx)";
    public static final String DESCRIPTION_TXT = "TXT Files (*.txt)";
    
    public static boolean WITH_GUI = true;
    
    public static final String DEFAULT_COMPRESSION_METHOD = "bzip2";
    
    //////////////////////////////////
  

}

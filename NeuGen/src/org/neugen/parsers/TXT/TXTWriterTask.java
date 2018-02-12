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

/// package's name
package org.neugen.parsers.TXT;

/// imports
import java.io.File;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenView;
import org.neugen.parsers.HocWriterTask;

/**
 * @brief the TXT writer task
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public final class TXTWriterTask extends Task<Void, Void> {
    /// private static final members
    private static final Logger logger = Logger.getLogger(HocWriterTask.class.getName());
    
    /// private static members
    private static TXTWriterTask instance;

    /// private final members
    private final File file;
    private final WriteCompressedDialog dialog;
 
    /**
     * @brief set task
     * @param task 
     */
    public static void setInstance(TXTWriterTask task) {
	    instance = task;
    }
    
    /**
     * @brief get task
     * @return
     */
    public static TXTWriterTask getInstance() {
	    return instance;
    }

    /**
     * @brief ctor
     * @author stephanmg <stephan@syntaktischer-zucker.de>
     * 
     * @param app
     * @param f 
     */
    public TXTWriterTask(Application app, File f) {
        super(app);
        file = f;
	dialog = new WriteCompressedDialog(NeuGenView.getInstance().getFrame(), true);
	dialog.setVisible(true);
    }

    /**
     * @brief show progress bar
     * @author stephanmg <stephan@syntaktischer-zucker.de>
     * 
     * @param value
     * @param min
     * @param max 
     */
    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }
    
    /**
     * @brief sets progress
     * @author stephanmg <stephan@syntaktischer-zucker.de>
     * 
     * @param percentage
     */
    public void setMyProgress(float percentage) {
	if (Float.isInfinite(percentage)) {
            logger.info("percentage infinite: " + percentage);
	    setProgress(1.0f);
        }
        setProgress(percentage);
    }

    /**
     * @brief the actual export 
     * @author stephanmg <stephan@syntaktischer-zucker.de>
     * 
     * @return 
     */
    @Override
    @SuppressWarnings("deprecation")
    protected Void doInBackground() {
        Net net = NeuGenView.getInstance().getNet();
        TXTWriter txtWriter = new TXTWriter(net, file);
	
	logger.info("Exporting TXT data to file: " + file.getName());
        setMessage("Exporting TXT data to file: " + file.getName());
	
	boolean uncompressed = dialog.getUncompressed();
	boolean compressed = dialog.getCompressed();

	/// Write only network if compressed or uncompressed chosen
	if ( ! (uncompressed || compressed) ) {
		logger.info("Neither compressed nor uncompressed write selected. Aborting...");
		setMessage("Neither compressed nor uncompressed write selected. Aborting...");
	} else {
		txtWriter.setCompressionMethod(dialog.getMethod());
		txtWriter.setCompressed(dialog.getCompressed());
		txtWriter.setUncompressed(dialog.getUncompressed());
		txtWriter.setWithCellType(dialog.getWithCellType());
                txtWriter.setFileExistsDialog(new FileExistsDialog(NeuGenView.getInstance().getFrame(), true));
		txtWriter.exportNetToTXT();
	}
	
        return null;  
    }

    /**
     * @brief on success
     * @author stephanmg <stephan@syntaktischer-zucker.de>
     *  
     * @param result 
     */
    @Override
    protected void succeeded(Void result) {
    }
}

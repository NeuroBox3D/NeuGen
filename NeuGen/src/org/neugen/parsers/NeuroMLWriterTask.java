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
package org.neugen.parsers;

import java.io.File;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenView;

/**
 * @todo by stephanmg: this needs really to be generalized across all given
 * tasks, also if one task does multiple jobs, e. g. write synapses (lvl 3) and
 * network morphology (lvl 1), we cannot distinguish this in progress bar, which
 * is rather not flexible
 * @author Sergei
 */
public final class NeuroMLWriterTask extends Task<Void, Void> {

	private final File file;
	protected static NeuroMLWriterTask instance;
	private static final Logger logger = Logger.getLogger(NeuroMLWriterTask.class.getName());
	private static String name = "";

	public NeuroMLWriterTask(Application app, File f) {
		super(app);
		file = f;
		setInstance(this);
	}

	/**
	 * @brief returns the instance with the given progress name
	 * @author stephanmg
	 * @param name
	 * @return the value of instance
	 */
	@SuppressWarnings("static-access")
	public static NeuroMLWriterTask getInstance(String name) {
		instance.name = name;
		return instance;
	}

	/**
	 * @brief returns the instance with default progress name
	 * @author stephanmg
	 * @return
	 */
	public static NeuroMLWriterTask getInstance() {
		return instance;
	}

	/**
	 * Set the value of instance
	 *
	 * @param instance new value of instance
	 */
	public static void setInstance(NeuroMLWriterTask instance) {
		NeuroMLWriterTask.instance = instance;
	}

	@Override
	protected Void doInBackground() throws Exception {
		NeuGenView.getInstance().disableButtons();
		Net net = NeuGenView.getInstance().getNet();
		System.err.println("progress_name: " + name);
		setMessage((!name.isEmpty() ? "Exporting [" + name + "] ... " + file.getName() : "Exporting " + file.getName() + "..."));
		NeuroMLWriter write = new NeuroMLWriter();
		write.exportData(file, net);
		return null;
	}

	public void setMyProgress(float percentage) {
		if (Float.isInfinite(percentage)) {
			logger.info("percentage infinite: " + percentage);
			percentage = 1.0f;
		}
		setProgress(percentage);
	}

	@Override
	protected void succeeded(Void result) {
		NeuGenView.getInstance().enableButtons();
		// Runs on the EDT.  Update the GUI based on
		// the result computed by doInBackground().
	}

}

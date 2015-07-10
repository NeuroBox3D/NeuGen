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

/**
 * @brief TXTConnection
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTConnection extends TXTBase {
	/// private members
	private String from;
	private String to;
	private int from_loc;
	private int to_loc;

	/**
	 * @brief get from compartment
	 * @return 
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @brief set from compartment
	 * @param from 
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @brief get to compartment
	 * @return
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @brief set to compartment
	 * @param to 
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @brief get from loc
	 * @return 
	 */
	public int getFrom_loc() {
		return from_loc;
	}

	/**
	 * @brief set from loc
	 * @param from_loc
	 */
	public void setFrom_loc(int from_loc) {
		this.from_loc = from_loc;
	}

	/**
	 * @brief get to loc
	 * @return 
	 */
	public int getTo_loc() {
		return to_loc;
	}

	/**
	 * @brief set to loc
	 * @param to_loc 
	 */
	public void setTo_loc(int to_loc) {
		this.to_loc = to_loc;
	}
}

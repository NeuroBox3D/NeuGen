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
package org.neugen.parsers.NGX;

/**
 * @brief Exp2Synapse
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class NGXExp2Synapse extends NGXSynapse {
	/// private members
	private float tau1;
	private float tau2;
	private float e;
	private float gmax;
	private float delay;
	private float threshold;
	
	/**
	 * @brief ctor
	 */
	public NGXExp2Synapse() {
		this.tau1 = 0.2f;
		this.tau2 = 1.7f;
		this.e = 0f;
		this.threshold = -10f;
		this.delay = 0.5f;
		this.gmax = 0f;
	}

	/**
	 * @return the tau1
	 */
	public float getTau1() {
		return tau1;
	}

	/**
	 * @param tau1 the tau1 to set
	 */
	public void setTau1(float tau1) {
		this.tau1 = tau1;
	}

	/**
	 * @return the tau2
	 */
	public float getTau2() {
		return tau2;
	}

	/**
	 * @param tau2 the tau2 to set
	 */
	public void setTau2(float tau2) {
		this.tau2 = tau2;
	}

	/**
	 * @return the e
	 */
	public float getE() {
		return e;
	}

	/**
	 * @param e the e to set
	 */
	public void setE(float e) {
		this.e = e;
	}

	/**
	 * @return the gmax
	 */
	public float getGmax() {
		return gmax;
	}

	/**
	 * @param gmax the gmax to set
	 */
	public void setGmax(float gmax) {
		this.gmax = gmax;
	}

	/**
	 * @return the delay
	 */
	public float getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(float delay) {
		this.delay = delay;
	}

	/**
	 * @return the threshold
	 */
	public float getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
}

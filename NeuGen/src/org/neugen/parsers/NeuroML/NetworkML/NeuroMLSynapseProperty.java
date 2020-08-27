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
package org.neugen.parsers.NeuroML.NetworkML;

/// imports
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @brief NeuroMLSynapseProperty element
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
@XStreamAlias("properties")
public final class NeuroMLSynapseProperty {
	/// private members
	private float internal_delay;
	private float weight;
	private float threshold;
	@XStreamOmitField
	private String synapse_type;

	/**
	 * @brief construct empty synapse properties
	 */
	public NeuroMLSynapseProperty() {
		this.internal_delay = 0;
		this.weight = 0;
		this.threshold = 0;
		this.synapse_type = "";
	}
	
	/**
	 * @brief construct synapse properties
	 * @param internal_delay
	 * @param weight
	 * @param threshold
	 * @param synapse_type
	 */
	public NeuroMLSynapseProperty(final float internal_delay, final float weight, 
				      final float threshold, final String synapse_type) {
		this.internal_delay = internal_delay;
		this.weight = weight;
		this.threshold = threshold;
		this.synapse_type = synapse_type;
	}

	/**
	 * @brief get internal delay
	 * @return 
	 */
	public float getInternal_delay() {
		return internal_delay;
	}

	/**
	 * @brief get weight
	 * @return
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @brief get threshold
	 * @return
	 */
	public float getThreshold() {
		return threshold;
	}

	/**
	 * @brief get synapse type
	 * @return
	 */
	public String getSynapse_type() {
		return synapse_type;
	}
	
	/**
	 * @brief set internal delay
	 * @param internal_delay 
	 */
	public void setInternal_delay(float internal_delay) {
		this.internal_delay = internal_delay;
	}

	/**
	 * @set weight
	 * @param weight 
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @brief set threshold
	 * @param threshold 
	 */
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	/**
	 * @brief set synapse type
	 * @param synapse_type
	 */
	public void setSynapse_type(String synapse_type) {
		this.synapse_type = synapse_type;
	}
}
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
import javax.vecmath.Point3f;

/**
 * @brief NeuroML Level 3, i. e. NetworkML, Synapse: unilateral
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
@XStreamAlias("connection")
public final class NeuroMLSynapseUnilateral implements NeuroMLSynapse {
	@XStreamOmitField
	private final Point3f injection;
	@XStreamOmitField
	private final static String TYPE = "Unilateral (non-functional) Synapse";
	
	private int id;
	private int pre_cell_id;
	private int pre_segment_id;
	private float pre_fraction_along;

	private NeuroMLSynapseProperty properties = new NeuroMLSynapseProperty();

	public NeuroMLSynapseProperty getProperties() {
		return properties;
	}

	public void setProperties(NeuroMLSynapseProperty properties) {
		this.properties = properties;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPre_cell_id() {
		return pre_cell_id;
	}

	public void setPre_cell_id(int pre_cell_id) {
		this.pre_cell_id = pre_cell_id;
	}

	public int getPre_segment_id() {
		return pre_segment_id;
	}

	public void setPre_segment_id(int pre_segment_id) {
		this.pre_segment_id = pre_segment_id;
	}

	public float getPre_fraction_along() {
		return pre_fraction_along;
	}

	public void setPre_fraction_along(float pre_fraction_along) {
		this.pre_fraction_along = pre_fraction_along;
	}
	/**
	 * @brief ctor
	 * @param injection 
	 */
	public NeuroMLSynapseUnilateral(Point3f injection) {
		this.injection = injection;
	}
	
	/**
	 * @return
	 */
	@Override
	public String toString() {
		/**
		 * @todo implement
		 */
		final StringBuffer sb = new StringBuffer();
		sb.append("injection: ");
		sb.append(this.injection);
		return sb.toString();
	}

	/**
	 * @brief get type
	 * @return 
	 */
	@Override
	public String getType() {
		return TYPE;
	}
}
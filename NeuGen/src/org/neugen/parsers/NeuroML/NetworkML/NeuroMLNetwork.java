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
import java.util.ArrayList;

/**
 * @brief NeuroML Network element
 * @author stephanmg <stephan@syntaktischer-zucker.de>*
 * 
 * @todo should be renamed to NeuroMLConnections
 */
@XStreamAlias("connections")
public class NeuroMLNetwork implements NetworkMLElement {
	@XStreamOmitField
	private static String TYPE = "Network";
	@XStreamOmitField
	private int dim;
	
	private int size;
	private ArrayList<NetworkMLElement> elements = new ArrayList<NetworkMLElement>();

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ArrayList<NetworkMLElement> getElements() {
		return elements;
	}

	public void setElements(ArrayList<NetworkMLElement> elements) {
		this.elements = elements;
	}

	
	public NeuroMLNetwork() {
		
	}
	
	/**
	 * @brief ctor
	 * @param dim 
	 * @param elements
	 */
	public NeuroMLNetwork(int dim, ArrayList<NetworkMLElement> elements) {
		this.dim = dim;
		this.elements = elements;
	}
	
	/**
	 * @brief ctor with dim default to 3
	 * @param elements 
	 */
	public NeuroMLNetwork(ArrayList<NetworkMLElement> elements) {
		this.dim = 3;
		this.elements = elements;
	}
	
	/**
	 * @brief get type
	 * @return 
	 */
	@Override
	public String getType() {
		return TYPE;
	}

	/**
	 * @brief get dimension
	 * @return 
	 */
	public int getDim() {
		return this.dim;
	}
}
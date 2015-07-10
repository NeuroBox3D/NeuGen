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
import javax.vecmath.Point3f;

/**
 * @brief TXTSynapse
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTSynapse {
	/// private members
	private String from;
	private String to;
	private float from_loc;
	private float to_loc;
	private Point3f from_point_end;
	private Point3f from_point_start;
	private Point3f to_point_end;
	private Point3f to_point_start;

	/**
	 * @brief get from point end
	 * @return 
	 */
	public Point3f getFrom_point_end() {
		return from_point_end;
	}

	/**
	 * @brief set from point end
	 * @param from_point_end 
	 */
	public void setFrom_point_end(Point3f from_point_end) {
		this.from_point_end = from_point_end;
	}

	/**
	 * @brief get from point start
	 * @return 
	 */
	public Point3f getFrom_point_start() {
		return from_point_start;
	}

	/**
	 * @brief set from point start
	 * @param from_point_start 
	 */
	public void setFrom_point_start(Point3f from_point_start) {
		this.from_point_start = from_point_start;
	}

	/**
	 * @brief get to point end
	 * @return 
	 */
	public Point3f getTo_point_end() {
		return to_point_end;
	}

	/**
	 * @brief set to point end
	 * @param to_point_end 
	 */
	public void setTo_point_end(Point3f to_point_end) {
		this.to_point_end = to_point_end;
	}

	/**
	 * @brief get to point start
	 * @return 
	 */
	public Point3f getTo_point_start() {
		return to_point_start;
	}

	/**
	 * @brief set to point start
	 * @param to_point_start 
	 */
	public void setTo_point_start(Point3f to_point_start) {
		this.to_point_start = to_point_start;
	}
	
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
	 * @brief get from compartment location
	 * @return 
	 */
	public float getFrom_loc() {
		return from_loc;
	}

	/**
	 * @brief set from compartment location
	 * @param from_loc 
	 */
	public void setFrom_loc(float from_loc) {
		this.from_loc = from_loc;
	}

	/**
	 * @brief get to compartment location
	 * @return 
	 */
	public float getTo_loc() {
		return to_loc;
	}

	/**
	 * @brief set to compartment location
	 * @param to_loc 
	 */
	public void setTo_loc(float to_loc) {
		this.to_loc = to_loc;
	}

}

/// package's name
package org.neugen.parsers.NeuroML.NetworkML;

/// imports
import java.util.ArrayList;

/**
 * @brief NeuroML Network element
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class NeuroMLNetwork implements NetworkMLElement {
	private final static String TYPE = "Network";
	private final int dim;
	private final ArrayList<NetworkMLElement> elements;

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

/// package's name
package org.neugen.parsers.NeuroML.NetworkML;

import javax.vecmath.Point3f;

/**
 * @brief NeuroML Level 3, i. e. NetworkML, Synapse
 * @author stephan
 */
public final class NeuroMLSynapse implements NetworkMLElement {
	private final Point3f from;
	private final Point3f to;
	private final StringBuffer sb = new StringBuffer();
	
	/**
	 * @brief 
	 * @param from
	 * @param to
	 */
	public NeuroMLSynapse(final Point3f from, final Point3f to) {
		this.from = from;
		this.to = to;
	}
	
	/**
	 * 
	 * @return 
	 */
	@Override
	public String toString() {
		/**
		 * @todo implement
		 */
		sb.append("from: ");
		sb.append(this.from);
		sb.append("to: ");
		sb.append(this.to);
		return sb.toString();
	}

}
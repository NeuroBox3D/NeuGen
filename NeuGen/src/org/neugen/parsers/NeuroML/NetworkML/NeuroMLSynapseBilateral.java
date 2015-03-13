/// package's name
package org.neugen.parsers.NeuroML.NetworkML;

import javax.vecmath.Point3f;

/**
 * @brief NeuroML Level 3, i. e. NetworkML, Synapse: bilateral
 * @author stephan
 */
public final class NeuroMLSynapseBilateral extends NeuroMLSynapse {
	private final Point3f from;
	private final Point3f to;
	private final static String TYPE = "Bilateral (functional) Synapse";
	
	/**
	 * @brief 
	 * @param from
	 * @param to
	 */
	public NeuroMLSynapseBilateral(final Point3f from, final Point3f to) {
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

	/**
	 * @brief get type
	 * @return 
	 */
	@Override
	protected String getType() {
		return TYPE;
	}

}
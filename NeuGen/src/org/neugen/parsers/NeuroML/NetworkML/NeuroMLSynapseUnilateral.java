/// package's name
package org.neugen.parsers.NeuroML.NetworkML;

/// imports
import javax.vecmath.Point3f;

/**
 * @brief NeuroML Level 3, i. e. NetworkML, Synapse: unilateral
 * @author stephan
 */
public final class NeuroMLSynapseUnilateral extends NeuroMLSynapse {
	private final Point3f injection;
	private final static String TYPE = "Unilateral (non-functional) Synapse";
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
		sb.append("injection: ");
		sb.append(this.injection);
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

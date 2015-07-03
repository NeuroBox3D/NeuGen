/// package's name
package org.neugen.parsers.TXT;

/**
 * @brief TXTSynapse
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTExp2Synapse extends TXTSynapse {
	private float gmax;
	private final static int type = 0;

	/**
	 * 
	 * @param gmax 
	 */
	public void setGmax(float gmax) {
		this.gmax = gmax;
	}

	/**
	 * 
	 * @return 
	 */
	public float getGmax() {
		return this.gmax;
	}
	
	/**
	 * @brief
	 * @return
	 */
	public String getSynapseInfo() {
		return type + " " + this.gmax;
	}

}

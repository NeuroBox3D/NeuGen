/// package's name
package org.neugen.parsers.TXT;

/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTAlphaSynapse extends TXTSynapse {
	private float gmax;
	private static final int type = 1;
	private int from_index;
	private int to_index;

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
	 * 
	 * @param from_index 
	 */
	public void setFrom_Index(int from_index) {
		this.from_index = from_index;
	}

	/**
	 * 
	 * @param to_index 
	 */
	public void setTo_Index(int to_index) {
		this.to_index = to_index;
	}

	/**
	 * 
	 * @return 
	 */
	int getFrom_Index() {
		return this.from_index;
	}

	/**
	 * 
	 * @return 
	 */
	int getTo_Index() {
		return this.to_index;
	}

	/**
	 * @brief
	 * @return
	 */
	public String getSynapseInfo() {
		return type + " " + this.gmax;
	}
}

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

	private int to_index;
	private int from_index;
	public void setTo_Index(int to_index) {
		this.to_index = to_index;
	}

	public void setFrom_Index(int from_index) {
		this.from_index = from_index;
	}
	
	public int getTo_Index() {
		return this.to_index;
	}

	public int getFrom_Index() {
		return this.from_index;
	}


	
}

/// package's name
package org.neugen.parsers.NeuroML.NetworkML;

/**
 * @brief NeuroML Level 3, i. e. NetworkML Synapse
 * @author stephan
 */
public abstract class NeuroMLSynapse implements NetworkMLElement {
	protected final StringBuffer sb = new StringBuffer();
	abstract protected String getType();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/// package's name
package org.neugen.parsers.NeuroML.NetworkML;

/// imports
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.ArrayList;

/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
@XStreamAlias("connections")
public class NeuroMLConnections {
	private ArrayList<NetworkMLElement> elements = new ArrayList<NetworkMLElement>();
	private int size;

	public ArrayList<NetworkMLElement> getElements() {
		return elements;
	}

	public void setElements(ArrayList<NetworkMLElement> elements) {
		this.elements = elements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}

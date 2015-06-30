/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/// package's name
package org.neugen.parsers.TXT;

/// imports

/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTDend extends TXTBase {
	private String name;
	private int nn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return nn;
	}

	public void setId(int nn) {
		this.nn = nn;
	}
}

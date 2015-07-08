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
public class TXTAxon extends TXTBase {
	private static final int TYPE = TXTTypes.AXON.ordinal();
	private String name;
	private int nn;
	
	public int getType() {
		return TYPE;
	}
}

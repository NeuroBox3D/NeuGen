/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.parsers.TXT;

import java.util.ArrayList;

/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public interface WriteToTXT {
 	public ArrayList<TXTSynapse> writeExp2Synapses();
        public ArrayList<TXTSynapse> writeAlphaSynapses();
	
}

/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */
/// package's name
package org.neugen.backend.main;

/// imports
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.neugen.backend.NGBackend;
import org.neugen.gui.NeuGenConstants;

/**
 * @brief enhanced (and main) command line interface (CLI) for NeuGen's pseudo-backend
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 * TODO: backend / NeuGen is very chatty -> produce less output => will speedup network generation
 */
public final class EnhancedCLI {
	/// the backend for NeuGen
	private final static NGBackend ngBackend = new NGBackend();
	
	/**
	 * @brief main
	 * Execute as: java -cp NeuGen.jar org.neugen.backend.main.EnhancedCLI
	 * 
	 * @todo implement CLI enhanced (WIP... if necessar)
	 * @param args
	 */
	public static void main(String... args) {
		Options options = new Options();
		/// flags
		options.addOption("g", "generate", false, "If flag provided then network will be generated.");
		options.addOption("e", "export", false, "If flag provided then network will be exported.");
		/// options with parameters
		options.addOption("i", "input", true, "Base folder where the project should be stored");
		options.addOption("o", "output", true, "Output name of exported network after generation");
		options.addOption("d", "nPartsDensity", true, "Density from NeuGen's network generation routine");
		options.addOption("sd", "synapseDistance", true, "Change the threshold for synapse formation.");
		options.addOption("nSP", "nStartPyramidal", true, "Number of Star Pyramidal cells");
		options.addOption("nL4S", "nLayer4Stellate", true, "Number of Layer 4 Stellate cells");
		options.addOption("nL23P", "nLayer23Pyramidal", true, "Number of Layer 2/3 Pyramidal cells");
		options.addOption("nL5AP", "nLayer5APyramidal", true, "Number of Layer 5A Pyramidal cells");
		options.addOption("nL5BP", "nLayer5BPyramidal", true, "Number of Layer 5B Pyramidal cells");
		options.addOption("s", "size", true, "Network size. Creates number of cells specified. Does not"
			+ "allow to specify respectively overrides the number of individual specified cells if"
			+ "provided.");
		
    		try {
			if (args.length < 3) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(NeuGenConstants.VERSION + " enhanced CLI", options);
			} else {
 				CommandLine line = new DefaultParser().parse( options, args);
				if(line.hasOption("generate")) {
					/// If no project base path is given (2nd argument) the current working
					/// or execution dir of NeuGen is assumed. So the 1st argument should
					/// then really be a path to the project. Otherwise, Can specify a 
					/// network name as first argument and a separate base path as second
					/// argument. However then, one has to execute the NeuGen binary from
					/// the project base path! (Because folders relative to the base path
					/// are assumed when executing NeuGen!)
					ngBackend.create_and_open_project(line.getOptionValue("i"), "",
					NeuGenConstants.NEOCORTEX_PROJECT, true, false);
					
					if (line.hasOption("d")) {
						ngBackend.modifyNPartsDensity(Double.parseDouble(line.getOptionValue("d")));
					}
					
					if (line.hasOption("sd")) {
						ngBackend.modifySynapseDistance(Double.parseDouble(line.getOptionValue("sd")));
					}
					
					if (line.hasOption("s")) {
					        ngBackend.adjustNetworkSize(Integer.parseInt(line.getOptionValue("s")));
					}
					
					int nSP = Integer.parseInt(line.getOptionValue("nSP"));
					ngBackend.adjust_number_of_star_pyramidal_cells(nSP);
					int nL4S = Integer.parseInt(line.getOptionValue("nL4S"));
					ngBackend.adjust_number_of_stellate_cells(nL4S);
					int nL23P = Integer.parseInt(line.getOptionValue("nL23P"));
					ngBackend.adjust_number_of_layer_23_pyramidal(nL23P);
					int nL5AP = Integer.parseInt(line.getOptionValue("nL5AP"));
					ngBackend.adjust_number_of_layer_5_a_pyramidal(nL5AP);
					int nL5BP = Integer.parseInt(line.getOptionValue("nL5BP"));
					ngBackend.adjust_number_of_layer_5_b_pyramidal(nL5BP);
					ngBackend.generate_network();
					
					/// export if flag specified
					if (line.hasOption("e")) {
						String outputName = "export";
						if (line.hasOption("o")) {
							outputName = line.getOptionValue("o");
						}
						ngBackend.export_network("TXT", outputName, false);
					}
				}
			}
    		} catch(ParseException pe) {
			System.err.println("ParseException: " + pe);
    		}
	}
}
 
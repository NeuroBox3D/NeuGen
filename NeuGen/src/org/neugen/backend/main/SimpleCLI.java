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
import org.neugen.backend.NGBackend;
import org.neugen.gui.NeuGenConstants;
import static org.neugen.backend.NGBackend.logger;

/**
 * @brief simple command line interface (CLI) for NeuGen
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public final class SimpleCLI {
	@SuppressWarnings("CallToPrintStackTrace")
	public static void main(String... args) {
		if (args.length != 4) {
			usage();
		} else {
			/// string parameters
			String project_base_dir = args[0];
			String project_type = args[1];
			String export_format = args[2];
			String output_name = args[3];
			
			/// flags
			boolean open_only = false;
			boolean force = false;
			if ("OPEN".equalsIgnoreCase(args[4])) { open_only = true; }
			if ("FORCE".equalsIgnoreCase(args[5]) || ! args[5].isEmpty()) { force = true; }
			
			try {
				final NGBackend backend = new NGBackend();
				backend.create_and_open_project(project_base_dir, NeuGenConstants.NEOCORTEX_PROJECT, force, open_only);
				if (project_type.equalsIgnoreCase((NeuGenConstants.NEOCORTEX_PROJECT))) {
					project_type = NeuGenConstants.NEOCORTEX_PROJECT;
				} else {
					project_type = NeuGenConstants.HIPPOCAMPUS_PROJECT;
				}
				
				backend.generate_network(project_type);
				backend.export_network(export_format, output_name);
			} catch (Exception e) {
				logger.fatal(e);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @brief prints usage for the simple CLI
	 */
	private static void usage() {
		System.out.println("Usage: SimpleCLI PROJECT_BASE_DIR PROJECT_TYPE EXPORT_FORMAT OUTPUT_NAME [OPEN_OR_CREATE] [FORCE]");
		System.out.println("Project type either Neocortex or Hippocampus");
		System.out.println("Export format: TXT, HOC or NGX");
		System.out.println("Note: Only the first four parameters are mandatory.");
	}
}

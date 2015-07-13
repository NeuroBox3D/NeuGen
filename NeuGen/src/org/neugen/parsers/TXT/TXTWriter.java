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
package org.neugen.parsers.TXT;

/// imports
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.Trigger;
import org.neugen.parsers.MorphMLReader;
import org.neugen.utils.Utils;
import javax.vecmath.Vector4f;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FilenameUtils;
import org.neugen.backend.NGBackend;
import org.neugen.datastructures.Pair;

/**
 * @brief TXT writer (NeuGen XML)
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTWriter {
	/// private members
	private final Net net;
	private File file;
	private String hocFileName;
	private Trigger trigger;
	private String compressMethod = NeuGenConstants.DEFAULT_COMPRESSION_METHOD;
	private boolean compressed;
	private boolean uncompressed;
        private FileExistsDialog fed;

	/**
	 * @brief ctor
	 * @param net
	 * @param file
	 */
	public TXTWriter(Net net, File file) {
		this.net = net;
		this.file = file;
		hocFileName = file.getName();
		String[] str = hocFileName.split("\\.");
		hocFileName = str[0];
		if (NeuGenConstants.WITH_GUI) {
			trigger = Trigger.getInstance();
		}
	}

	/**
	 * @brief write a Segment to the TXT file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxbase TXT element
	 * @param start_end start or end
	 * @param segment HOC segment
	 */
	public final void writeSegmentToTXT(TXTBase ngxbase, int start_end, Segment segment) {
		if (start_end == 0) {
			ngxbase.getCoordinates().add(
				new Vector4f(
					segment.getStart().x,
					segment.getStart().y,
					segment.getStart().z,
					segment.getStartRadius() * 2
				)
			);
		}

		if (start_end == 1) {
			ngxbase.getCoordinates().add(
				new Vector4f(
					segment.getEnd().x,
					segment.getEnd().y,
					segment.getEnd().z,
					segment.getEndRadius() * 2
				)
			);

		}
	}

	/**
	 * @brief write the data of a soma cellipsoid into a TXT file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxsoma TXT element
	 * @param neuron the neuron of the given net
	 * @param name
	 */
	public final void writeSomaOfNeuronToTXT(TXTBase ngxsoma, Neuron neuron, String name) {
		Cellipsoid soma = neuron.getSoma();
		Section cyl = soma.getCylindricRepresentant();
		if (cyl == null) {
			cyl = soma.cylindricRepresentant();
		}

		for (Segment segment : cyl.getSegments()) {
			writeSegmentToTXT(ngxsoma, 0, segment);
		}

		int nsegs = cyl.getSegments().size();
		Segment segment = cyl.getSegments().get(nsegs - 1);
		writeSegmentToTXT(ngxsoma, 1, segment);
		ngxsoma.setName(name);
	}

	/**
	 * @brief write the data of a axon into a TXT file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxbase TXTBase elements
	 * @param connections TXTBase connections
	 * @param neuron the given neuron
	 * @param nn the number of the given neuron
	 */
	public final void writeAxonOfNeuronToTXT(ArrayList<TXTBase> ngxbase, ArrayList<TXTBase> connections,
		Neuron neuron, int nn) {
		int nsections = 0;
		Axon axon = neuron.getAxon();

		if (axon.getFirstSection() == null) {
			return;
		}

		Section.Iterator secIterator = axon.getFirstSection().getIterator();
		while (secIterator.hasNext()) {
			Section section = secIterator.next();
			int nsegs = section.getSegments().size();

			String secName = section.getName();
			section.getSectionType();
			Section parSec;

			if (section.getParentalLink() != null) {
				parSec = section.getParentalLink().getParental();
			} else {
				parSec = neuron.getSoma().getCylindricRepresentant();
			}

			float fractAlongParent = parSec.getFractAlongParentForChild(section);
			TXTConnection ngxconnex = new TXTConnection();
			//connect N0axon_hillock(0), N0soma(0)
			if (nsections == 0) {
				ngxconnex.setFrom("N" + nn + secName);
				ngxconnex.setFrom_loc(0);
				ngxconnex.setTo("N" + nn + "soma");
				ngxconnex.setTo_loc((int) fractAlongParent);
			} //connect N0axon_myel_0000(0), N0axon_000(1)
			else {
				String parentSecName = parSec.getName();
				ngxconnex.setFrom("N" + nn + secName);
				ngxconnex.setFrom_loc(0);
				ngxconnex.setTo("N" + nn + parentSecName);
				ngxconnex.setTo_loc((int) fractAlongParent);
			}

			connections.add(ngxconnex);

			TXTAxon ngxaxon = new TXTAxon();
			ngxaxon.setName("N" + nn + secName);
			ngxaxon.setId(nn);

			for (Segment segment : section.getSegments()) {
				writeSegmentToTXT(ngxaxon, 0, segment);
			}

			Segment segment = section.getSegments().get(nsegs - 1);
			writeSegmentToTXT(ngxaxon, 1, segment);

			ngxbase.add(ngxaxon);
			++nsections;

		}
	}

	/**
	 * @brief write the data of a dendrite into the UGX file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxbase TXTBase elements
	 * @param connections TXTBase connections
	 * @param neuron the neuron of neural network
	 * @param nn the number of neuron.
	 */
	public final void writeDendriteOfNeuronToTXT(ArrayList<TXTBase> ngxbase, ArrayList<TXTBase> connections,
		Neuron neuron, int nn) {
		int nsections = 0;
		for (Dendrite dendrite : neuron.getDendrites()) {
			Section firstSection = dendrite.getFirstSection();
			Section.Iterator secIterator = firstSection.getIterator();
			while (secIterator.hasNext()) {
				Section section = secIterator.next();
				String secName = section.getName();

				int nsegs = section.getSegments().size();
				Section parSec;

				if (section.getParentalLink() != null) {
					parSec = section.getParentalLink().getParental();
				} else {
					parSec = neuron.getSoma().getCylindricRepresentant();
				}

				float fractAlongParent = parSec.getFractAlongParentForChild(section);
				String parentSecName = parSec.getName();
				TXTConnection ngxconnex = new TXTConnection();

				if (nsections == 0 || parentSecName.contains("soma")) {
					ngxconnex.setFrom("N" + nn + secName);
					ngxconnex.setFrom_loc(0);
					ngxconnex.setTo("N" + nn + "soma");
					ngxconnex.setTo_loc((int) fractAlongParent);
				} else {
					ngxconnex.setFrom("N" + nn + secName);
					ngxconnex.setFrom_loc(0);
					ngxconnex.setTo("N" + nn + parentSecName);
					ngxconnex.setTo_loc((int) fractAlongParent);
				}

				connections.add(ngxconnex);
				TXTDend ngxdend = new TXTDend();
				ngxdend.setName("N" + nn + secName);
				ngxdend.setId(nn);

				for (Segment segment : section.getSegments()) {
					writeSegmentToTXT(ngxdend, 0, segment);
				}

				Segment segment = section.getSegments().get(nsegs - 1);
				writeSegmentToTXT(ngxdend, 1, segment);

				ngxbase.add(ngxdend);
				++nsections;
			}
		}
	}

	/**
	 * @brief writes NEURONs to TXT
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param neuronNum
	 * @return list of TXTBase elements
	 */
	public final Pair<ArrayList<TXTBase>, ArrayList<TXTBase>> writeNeuronToTXT(int neuronNum) {
		int i = neuronNum;
		String name = "N" + i + "soma";

		Neuron neuron = net.getNeuronList().get(i);
		ArrayList<TXTBase> neuron_sections = new ArrayList<TXTBase>();
		ArrayList<TXTBase> neuron_connex = new ArrayList<TXTBase>();

		/// write soma of NEURON i (exactly one soma!)
		TXTBase ngxbase = new TXTSoma();
		ngxbase.setId(i);
		writeSomaOfNeuronToTXT(ngxbase, neuron, name);
		neuron_sections.add(ngxbase);

		/// write axons of NEURON i (may be more than one axon!)
		ArrayList<TXTBase> ngxbases = new ArrayList<TXTBase>();
		ArrayList<TXTBase> connections = new ArrayList<TXTBase>();
		writeAxonOfNeuronToTXT(ngxbases, connections, neuron, i);
		neuron_connex.addAll(connections);
		neuron_sections.addAll(ngxbases);

		/// write dends of NEURON i (may be more than one dend!)
		ngxbases = new ArrayList<TXTBase>();
		connections = new ArrayList<TXTBase>();
		writeDendriteOfNeuronToTXT(ngxbases, connections, neuron, i);
		neuron_connex.addAll(connections);
		neuron_sections.addAll(ngxbases);

		/// return all sections of NEURON i
		return new Pair<ArrayList<TXTBase>, ArrayList<TXTBase>>(neuron_sections, neuron_connex);
	}

	/**
	 * @brief writes the net into the TXT file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 */
	@SuppressWarnings("CallToPrintStackTrace")
	public final void writeNetToTXT() {
		/// compressed output
		CompressorOutputStream cos = null;
		CompressorOutputStream cos2 = null;
		CompressorOutputStream cos3 = null;
		
		/// uncompressed output
		PrintWriter pw = null;
		PrintWriter pw2 = null;
		PrintWriter pw3 = null;

		try {
			String basefile = FilenameUtils.removeExtension(this.file.getAbsolutePath());
                        String[] subfiles = { "secs.txt", "connex.txt", "synapses.txt" };
			System.err.println(basefile);

			FileWriter fw = new FileWriter(new File(basefile + "_secs.txt"), true);
			FileWriter fw2 = new FileWriter(new File(basefile + "_connex.txt"), true);
			FileWriter fw3 = new FileWriter(new File(basefile + "_synapses.txt"), true);
                        
                        for (String s : subfiles) {
                            File f = new File(basefile + "_" + s);
                            if (f.exists() && !f.isDirectory()) {
                                fed.setVisible(true);
                                if (!fed.getStatus()) {
                                    return;
                                }
                                break;
                            }
                        }
                
           
                       
			
			pw = new PrintWriter(fw);
			pw2 = new PrintWriter(fw2);
			pw3 = new PrintWriter(fw3);
			
			FileOutputStream fos = new FileOutputStream(new File(basefile + "_secs.txt.bz2"));
			FileOutputStream fos2 = new FileOutputStream(new File(basefile + "_connex.txt.bz2"));
			FileOutputStream fos3 = new FileOutputStream(new File(basefile + "_synapses.txt.bz2"));
			
			cos  = new CompressorStreamFactory().createCompressorOutputStream(this.compressMethod, fos);
			cos2 = new CompressorStreamFactory().createCompressorOutputStream(this.compressMethod, fos2);
			cos3 = new CompressorStreamFactory().createCompressorOutputStream(this.compressMethod, fos3);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (CompressorException ce) {
			ce.printStackTrace();
		}

		List<Neuron> neuronList = net.getNeuronList();
		int nneuron = neuronList.size();
		for (int i = 0; i < nneuron; i++) {
			Pair<ArrayList<TXTBase>, ArrayList<TXTBase>> sections = writeNeuronToTXT(i);
			StringBuilder buffer = new StringBuilder();
			StringBuilder buffer2 = new StringBuilder();
			for (TXTBase base : sections.first) {
				buffer.append(base.getName());
				buffer.append(" ");
				buffer.append(base.getCoordinates().size());
				buffer.append(" ");
				buffer.append(base.getType());
				buffer.append(" ");
				for (Vector4f vec : base.getCoordinates()) {
					buffer.append(vec.x);
					buffer.append(" ");
					buffer.append(vec.y);
					buffer.append(" ");
					buffer.append(vec.z);
					buffer.append(" ");
					buffer.append(vec.w);
					buffer.append(" ");
				}
				buffer.append(" ");
			}
			if (uncompressed) {
				pw.write(buffer.toString());
			}
			
			if (compressed) {
				try {
					cos.write(buffer.toString().getBytes());
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			for (TXTBase base : sections.second) {
				TXTConnection connex = (TXTConnection) base;
				buffer2.append(connex.getFrom());
				buffer2.append(" ");
				buffer2.append(connex.getTo());
				buffer2.append(" ");
				buffer2.append(connex.getFrom_loc());
				buffer2.append(" ");
				buffer2.append(connex.getTo_loc());
				buffer2.append(" ");
			}
			
			if (uncompressed) {
				pw2.write(buffer2.toString());
			}
			if (compressed) {
				try {
					cos2.write(buffer2.toString().getBytes());
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (pw != null) {
			pw.close();
		}
		
		if (compressed) {
			if (cos != null) {
				try {
					cos.close();
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (pw2 != null) {
			pw2.close();
		}
		
		if (cos2 != null) {
			try {
				cos2.close();
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		ArrayList<TXTSynapse> exp2synapses = net.getTXTData().writeExp2Synapses();
		StringBuilder buffer3 = new StringBuilder();
		for (TXTSynapse synapse : exp2synapses) {
			TXTExp2Synapse exp2syn = (TXTExp2Synapse) synapse;
			buffer3.append(exp2syn.getFrom_point_start().x);
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_point_start().y);
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_point_start().z);
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_point_end().x);
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_point_end().y);
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_point_end().z);
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_point_start().x);
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_point_start().y);
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_point_start().z);
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_point_end().x);
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_point_end().y);
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_point_end().z);
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_loc());
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_loc());
			buffer3.append(" ");
			buffer3.append(exp2syn.getSynapseInfo());
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom());
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo());
			buffer3.append(" ");
			buffer3.append(exp2syn.getFrom_Index());
			buffer3.append(" ");
			buffer3.append(exp2syn.getTo_Index());
			buffer3.append(" ");
		}

		if (uncompressed) {
			pw3.write(buffer3.toString());
		}
		
		if (compressed) {
			try {
				cos3.write(buffer3.toString().getBytes());
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		

		ArrayList<TXTSynapse> alphasynapses = net.getTXTData().writeAlphaSynapses();
		for (TXTSynapse synapse : alphasynapses) {
			TXTAlphaSynapse alphasyn = (TXTAlphaSynapse) synapse;
			buffer3.append(alphasyn.getFrom_point_start().x);
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_point_start().y);
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_point_start().z);
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_point_end().x);
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_point_end().y);
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_point_end().z);
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_point_start().x);
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_point_start().y);
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_point_start().z);
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_point_end().x);
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_point_end().y);
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_point_end().z);
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_loc());
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_loc());
			buffer3.append(" ");
			buffer3.append(alphasyn.getSynapseInfo());
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom());
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo());
			buffer3.append(" ");
			buffer3.append(alphasyn.getFrom_Index());
			buffer3.append(" ");
			buffer3.append(alphasyn.getTo_Index());
			buffer3.append(" ");
		}

		if (uncompressed) {
			pw3.write(buffer3.toString());
		}
		
		if (pw3 != null) {
			pw3.close();
		}
		
		if (compressed) {
			try {
			cos3.write(buffer3.toString().getBytes());
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				if (cos3 != null) {
					try {
						cos3.close();
					} catch (IOException ex) {
						java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
	}

	/**
	 * @brief @param compress
	 */
	public void exportNetToTXT(boolean compress) {
		if (!compress) {
			exportNetToTXT();
		} else {
			exportNetToTXTCompressed(NeuGenConstants.DEFAULT_COMPRESSION_METHOD);
		}
	}
	
	/**
	 * @brief 
	 * @param compress
	 * @param method 
	 */
	public void exportNetToTXT(boolean compress, String method) {
		if (!compress) {
			exportNetToTXT();
		} else {
			exportNetToTXTCompressed(method);
		}
	}

	/**
	 * @brief exports the net with compression
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 * 
	 * @param method
	 */
	private void exportNetToTXTCompressed(String method) {
		String compression = method;
		if (method.isEmpty()) {
			compression  = NeuGenConstants.DEFAULT_COMPRESSION_METHOD;
		}
		
		
		String ngx = NeuGenConstants.EXTENSION_TXT;
		String extension = Utils.getExtension(file);

		if (!ngx.equals(extension)) {
			file = new File(file.getAbsolutePath() + "." + ngx);
		}

		if (NeuGenConstants.WITH_GUI) {
			trigger.outPrintln("Write TXT file for UG");
			trigger.outPrintln("\t" + hocFileName + "." + ngx);
		} else {
			NGBackend.logger.info("Write TXT file for UG");
			NGBackend.logger.info("\t" + hocFileName + "." + ngx);
		}
		
		writeNetToTXTCompressed(compression);
	}

	/**
	 * @brief exports the net without compression
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 */
	public void exportNetToTXT() {
		String ngx = NeuGenConstants.EXTENSION_TXT;
		String extension = Utils.getExtension(file);

		if (!ngx.equals(extension)) {
			file = new File(file.getAbsolutePath() + "." + ngx);
		}

		if (NeuGenConstants.WITH_GUI) {
			trigger.outPrintln("Write TXT file for UG");
			trigger.outPrintln("\t" + hocFileName + "." + ngx);
		} else {
			NGBackend.logger.info("Write TXT file for UG");
			NGBackend.logger.info("\t" + hocFileName + "." + ngx);
		}
		writeNetToTXT();
	}

	/**
	 * @brief main
	 * @param args
	 */
	public static void main(String args[]) {
		MorphMLReader netBuilder = new MorphMLReader();
		String fname = "bal.xml";
		netBuilder.runMorphMLReader(fname);
		Net net = netBuilder.getNet();

		System.out.println("Size of the neural net: " + net.getNeuronList().size());
		File file = new File("MyData.hoc");

		TXTWriter ngxwriter = new TXTWriter(net, file);
		ngxwriter.exportNetToTXT();
	}

	/**
	 * @brief todo implement
	 * @param method
	 */
	private void writeNetToTXTCompressed(String method) {
		try {
			FileOutputStream bzip2 = null;
			String basefile = FilenameUtils.removeExtension(this.file.getAbsolutePath());
			bzip2 = new FileOutputStream(new File(basefile + "_test.bz2"));
			StringBuffer buffer3 = new StringBuffer();
			try {
				CompressorOutputStream gzippedOut = new CompressorStreamFactory()
					.createCompressorOutputStream(method, bzip2);

				try {
					gzippedOut.write(buffer3.toString().getBytes());
					gzippedOut.close();
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
				}
			} catch (CompressorException ex) {
				java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
			}

		} catch (FileNotFoundException ex) {
			java.util.logging.Logger.getLogger(TXTWriter.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * @brief set compression method
	 * @param method 
	 */
	void setCompressionMethod(String method) {
		this.compressMethod = method;
	}
	
	/**
	 * @brief set compress 
	 * @param compress 
	 */
	void setCompressed(boolean compress) {
		this.compressed = compress;
	}

	/**
	 * @brief set to compress and not compressed write
	 * @param uncompressed
	 */
	void setUncompressed(boolean uncompressed) {
		this.uncompressed = uncompressed;
	}

    void setFileExistsDialog(FileExistsDialog fileExistsDialog) {
       this.fed = fileExistsDialog;
    }
}

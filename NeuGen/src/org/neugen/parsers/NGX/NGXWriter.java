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
package org.neugen.parsers.NGX;

/// imports
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
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
import org.neugen.datastructures.Pair;

/**
 * @brief NGX writer (NeuGen XML)
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class NGXWriter {
	public NGXWriter() {
		
	}

	private static final Logger logger = Logger.getLogger(NGXWriter.class.getName());
	private Net net;
	private File file;
	private String hocFileName;
	/**
	 * The postfix of the name of the file for the NetCon events data.
	 */
	private String netConEventsFilePostfix;
	/**
	 * The postfix of the name of the file for the voltage data.
	 */
	private String voltageFilePostfix;
	private Trigger trigger;

	public NGXWriter(Net net, File file) {
		this.net = net;
		this.file = file;
		hocFileName = file.getName();
		String[] str = hocFileName.split("\\.");
		hocFileName = str[0];
		trigger = Trigger.getInstance();
	}

	public void setNetConEventsFilePostfix(String netConEventsFilePostfix) {
		this.netConEventsFilePostfix = netConEventsFilePostfix;
	}

	public void setVoltageFilePostfix(String voltageFilePostfix) {
		this.voltageFilePostfix = voltageFilePostfix;
	}

	/**
	 * @brief write a Segment to the NGX file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxbase NGX element
	 * @param start_end start or end
	 * @param segment HOC segment
	 */
	public final void writeSegmentToNGX(NGXBase ngxbase, int start_end, Segment segment) {
		if (start_end == 0) {
			ngxbase.coordinates.add(
				new Vector4f(
				segment.getStart().x,
				segment.getStart().y,
				segment.getStart().z,
				segment.getStartRadius() * 2
				)
			);
		}
		
		if (start_end == 1) {
			ngxbase.coordinates.add(
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
	 * @brief write the data of a soma cellipsoid into a NGX file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxsoma NGX element
	 * @param neuron the neuron of the given net
	 * @param name 
	 */
	public final void writeSomaOfNeuronToNGX(NGXBase ngxsoma, Neuron neuron, String name) {
		Cellipsoid soma = neuron.getSoma();
		Section cyl = soma.getCylindricRepresentant();
		if (cyl == null) {
			cyl = soma.cylindricRepresentant();
		}
		
		for (Segment segment : cyl.getSegments()) {
			writeSegmentToNGX(ngxsoma, 0, segment);
		}
		
		int nsegs = cyl.getSegments().size();
		Segment segment = cyl.getSegments().get(nsegs - 1);
		writeSegmentToNGX(ngxsoma, 1, segment);
		ngxsoma.name = name;
	}

	/**
	 * @brief write the data of a axon into a NGX file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxbase NGXBase elements
	 * @param connections NGXBase connections
	 * @param neuron the given neuron
	 * @param nn the number of the given neuron
	 */
	public final void writeAxonOfNeuronToNGX(ArrayList<NGXBase> ngxbase, ArrayList<NGXBase> connections, 
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
			Section parSec;
			
			if (section.getParentalLink() != null) {
				parSec = section.getParentalLink().getParental();
			} else {
				parSec = neuron.getSoma().getCylindricRepresentant();
			}

			float fractAlongParent = parSec.getFractAlongParentForChild(section);
			NGXConnection ngxconnex = new NGXConnection();
			//connect N0axon_hillock(0), N0soma(0)
			if (nsections == 0) {
				ngxconnex.from = "N" + nn + secName;
				ngxconnex.from_loc = 0;
				ngxconnex.to = "N" + nn + "soma";
				ngxconnex.to_loc = (int) fractAlongParent;
			} //connect N0axon_myel_0000(0), N0axon_000(1)
			else {
				String parentSecName = parSec.getName();
				ngxconnex.from = "N" + nn + secName;
				ngxconnex.from_loc = 0;
				ngxconnex.to = "N" + nn + parentSecName;
				ngxconnex.to_loc = (int) fractAlongParent;
			}
			
			connections.add(ngxconnex);
			
			NGXAxon ngxaxon = new NGXAxon();
			ngxaxon.name = "N" + nn + secName;
			ngxaxon.id = nn;
			
			for (Segment segment : section.getSegments()) {
				writeSegmentToNGX(ngxaxon, 0, segment);
			}
			
			Segment segment = section.getSegments().get(nsegs - 1);
			writeSegmentToNGX(ngxaxon, 1, segment);
			
			ngxbase.add(ngxaxon);
			++nsections;
			
		}
	}

	/**
	 * @brief write the data of a dendrite into the UGX file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param ngxbase NGXBase elements
	 * @param connections NGXBase connections
	 * @param neuron the neuron of neural network
	 * @param nn the number of neuron.
	 */
	public final void writeDendriteOfNeuronToNGX(ArrayList<NGXBase> ngxbase, ArrayList<NGXBase> connections, 
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
				NGXConnection ngxconnex = new NGXConnection();
				
				if (nsections == 0 || parentSecName.contains("soma")) {
					ngxconnex.from = "N" + nn + secName;
					ngxconnex.from_loc = 0;
					ngxconnex.to = "N" + nn + "soma";
					ngxconnex.to_loc = (int) fractAlongParent;
				} else {
					ngxconnex.from = "N" + nn + secName;
					ngxconnex.from_loc = 0;
					ngxconnex.to = "N" + nn + parentSecName;
					ngxconnex.to_loc = (int) fractAlongParent;
				}
				
				connections.add(ngxconnex);
				NGXDend ngxdend = new NGXDend();
				ngxdend.name = "N" + nn + secName;
				ngxdend.id = nn;
				
				for (Segment segment : section.getSegments()) {
					writeSegmentToNGX(ngxdend, 0, segment);
				}
				
				Segment segment = section.getSegments().get(nsegs - 1);
				writeSegmentToNGX(ngxdend, 1, segment);
				
				ngxbase.add(ngxdend);
				++nsections;
			}
		}
	}

	/**
	 * @brief writes NEURONs to NGX
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 * 
	 * @param neuronNum
	 * @return list of NGXBase elements 
	 */
	public final Pair<ArrayList<NGXBase>, ArrayList<NGXBase>> writeNeuronToNGX(int neuronNum) {
		int i = neuronNum;
		String name = "N" + i + "soma";
			
		Neuron neuron = net.getNeuronList().get(i);
		ArrayList<NGXBase> neuron_sections = new ArrayList<NGXBase>();
		ArrayList<NGXBase> neuron_connections = new ArrayList<NGXBase>();
		
		/// write soma of NEURON i (exactly one soma!)
		NGXBase ngxbase = new NGXSoma();
		ngxbase.id = i;
		writeSomaOfNeuronToNGX(ngxbase, neuron, name);
		neuron_sections.add(ngxbase);
		
		/// write axons of NEURON i (may be more than one axon!)
		ArrayList<NGXBase> ngxbases = new ArrayList<NGXBase>();
		ArrayList<NGXBase> connections = new ArrayList<NGXBase>();
		writeAxonOfNeuronToNGX(ngxbases, connections, neuron, i);
		neuron_sections.addAll(ngxbases);
		neuron_connections.addAll(connections);
		
		/// write dends of NEURON i (may be more than one dend!)
		ngxbases = new ArrayList<NGXBase>();
		connections = new ArrayList<NGXBase>();
		writeDendriteOfNeuronToNGX(ngxbases, connections, neuron, i);
		neuron_sections.addAll(ngxbases);
		neuron_connections.addAll(connections);
		
		/// return all sections of NEURON i
		return new Pair<ArrayList<NGXBase>, ArrayList<NGXBase>>(neuron_sections, neuron_connections);
	}

	/**
	 * @brief writes the net into the NGX file
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 */
	public final void writeNetToNGX() {
		ArrayList<NGXBase> sections = new ArrayList<NGXBase>();
		ArrayList<NGXBase> connections = new ArrayList<NGXBase>();
		List<Neuron> neuronList = net.getNeuronList();
		int nneuron = neuronList.size();
		for (int i = 0; i < nneuron; i++) {
			// write neuron geometry
			Pair<ArrayList<NGXBase>, ArrayList<NGXBase>> pair = writeNeuronToNGX(i);
			sections.addAll(pair.first);
			connections.addAll(pair.second);
		}
		
		// get exp2 synapses
		ArrayList<NGXSynapse> alphasynapses = net.getNGXData().writeAlphaSynapses();
		
		// get alpha synapses
		ArrayList<NGXSynapse> exp2synapses = net.getNGXData().writeExp2Synapses();

		try {
			String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			String fileformat = NeuGenConstants.EXTENSION_NGX.toLowerCase();
			FileWriter writer = new FileWriter(this.file);
			writer.write(head);

			PrettyPrintWriter writerP = new PrettyPrintWriter(writer);
			writerP.startNode(fileformat);

			XStream xstream = new XStream();
			xstream.alias("soma", NGXSoma.class);
			xstream.alias("dendrite", NGXDend.class);
			xstream.alias("axon", NGXAxon.class);
			xstream.alias("pt3d", Vector4f.class);
			xstream.alias("connection", NGXConnection.class);
			xstream.alias("AlphaSyn", NGXAlphaSynapse.class);
			xstream.alias("Exp2Syn", NGXExp2Synapse.class);
		
			/**
		 	* @todo add some aliases, add some attributes maybe
			*/
		
			ObjectOutputStream oos = xstream.createObjectOutputStream(writerP, "sections");
			for (NGXBase section : sections) {
				oos.writeObject(section);
			}
			writerP.endNode();
		
			oos.flush();
			writerP.flush();

			oos = xstream.createObjectOutputStream(writerP, "connections");
			for (NGXBase connection : connections) {
				oos.writeObject(connection);
			}
			writerP.endNode();
			
			oos.flush();
			writerP.flush();

			oos = xstream.createObjectOutputStream(writerP, "exp2synapses");
			for (NGXBase exp2syn : exp2synapses) {
				oos.writeObject(exp2syn);
			}
			writerP.endNode();
			
			oos.flush();
			writerP.flush();
			
			writer.write(System.getProperty("line.separator") + "</ngx>");
			writer.flush();
			
			
			} catch (IOException ioe) {
				logger.fatal(ioe);
			}
		
			/**
			 * @TODO write XML from elements above (sections, connections, alphasynapses and exp2synapses) by xstream probably
			 */
			}

	public void exportNetToNGX() {
		String ngx = NeuGenConstants.EXTENSION_NGX;
		String extension = Utils.getExtension(file);
		
		if (!ngx.equals(extension)) {
			file = new File(file.getAbsolutePath() + "." + ngx);
		} 
		
		trigger.outPrintln("Write NGX file for UG");
		trigger.outPrintln("\t" + hocFileName + "." + ngx);
		writeNetToNGX();
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
		
		NGXWriter ngxwriter = new NGXWriter(net, file);
		ngxwriter.exportNetToNGX();
	}
}
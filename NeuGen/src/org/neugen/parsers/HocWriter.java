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
package org.neugen.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.backend.NGBackend;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Cons;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Utils;

/**
 * @author Alexander Wanner
 * @author Simone Eberhard
 * @author Sergei Wolf
 */
public final class HocWriter {

	private static final Logger logger = Logger.getLogger(HocWriter.class.getName());
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

	public HocWriter(Net net, File file) {
		this.net = net;
		this.file = file;
		hocFileName = file.getName();
		String[] str = hocFileName.split("\\.");
		hocFileName = str[0];
		if (NeuGenConstants.WITH_GUI) {
			trigger = Trigger.getInstance();
		}
	}

	public void setNetConEventsFilePostfix(String netConEventsFilePostfix) {
		this.netConEventsFilePostfix = netConEventsFilePostfix;
	}

	public void setVoltageFilePostfix(String voltageFilePostfix) {
		this.voltageFilePostfix = voltageFilePostfix;
	}

	/**
	 * Function to write data of segment into hoc file
	 *
	 * @param fw the stream for the hoc file
	 * @param start_end for 0 write data of start of segment, for 1 write
	 * data of end of segment.
	 * @param segment the current segment
	 * @throws IOException
	 */
	public final void writetohocSegment(Writer fw, int start_end, Segment segment) throws IOException {
		String data = "\t pt3dadd(";
		fw.write(data);
		if (start_end == 0) {
				Point3f sstart = segment.getStart();
				fw.write(HOCUtil.format(sstart.x) + ", " + HOCUtil.format(sstart.y) + ", " + HOCUtil.format(sstart.z) + ", ");
				fw.write(segment.getStartRadius() * 2 + ")\n");
			}
			if (start_end == 1) {
				Point3f send = segment.getEnd();
			fw.write(HOCUtil.format(send.x) + ", " + HOCUtil.format(send.y) + ", " + HOCUtil.format(send.z) + ", ");
			fw.write(segment.getEndRadius() * 2 + ")\n");
		}
	}

	/**
	 * Function to write the data of cellipsoid into a hoc file.
	 *
	 * @param fw the stream for the hoc file.
	 * @param neuron the neuron of net
	 * @throws IOException
	 */
	public final void writetohocSoma(Writer fw, Neuron neuron, String name) throws IOException {
		Cellipsoid soma = neuron.getSoma();
		Section cyl = soma.getCylindricRepresentant();
		if (cyl == null) {
			cyl = soma.cylindricRepresentant();
		}
		int nsegs = cyl.getSegments().size();
		fw.write(name + " {\n");
		fw.write("\t nseg = " + nsegs + "\n\t pt3dclear()\n");
		for (Segment segment : cyl.getSegments()) {
			writetohocSegment(fw, 0, segment);
		}
		Segment segment = cyl.getSegments().get(nsegs - 1);
		writetohocSegment(fw, 1, segment);
		fw.write("}\n");
		fw.flush();
	}

	/**
	 * Function to write the data of axon into a hoc file.
	 *
	 * @param fw the stream for the hoc file.
	 * @param nn the number of neuron.
	 * @throws IOException
	 */
	public final int writetohocAxon(Writer fw, Neuron neuron, int nn) throws IOException {
		int nsections = 0;
		Axon axon = neuron.getAxon();
		if (axon.getFirstSection() == null) {
			return 0;
		}
		Section.Iterator secIterator = axon.getFirstSection().getIterator();
		while (secIterator.hasNext()) {
			Section section = secIterator.next();
			int nsegs = section.getSegments().size();
			String secName = section.getName();
			String data = "create " + "N" + nn + secName + "\n";
			fw.write(data);
			Section parSec;
			if (section.getParentalLink() != null) {
				parSec = section.getParentalLink().getParental();
			} else {
				parSec = neuron.getSoma().getCylindricRepresentant();
			}

			float fractAlongParent = parSec.getFractAlongParentForChild(section);
			//connect N0axon_hillock(0), N0soma(0)
			if (nsections == 0) {
				data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + "soma" + "(" + (int) fractAlongParent + ")\n";
				fw.write(data);
			} //connect N0axon_myel_0000(0), N0axon_000(1)
			else {
				String parentSecName = parSec.getName();
				data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + parentSecName + "(" + (int) fractAlongParent + ")\n";
				fw.write(data);
			}
			fw.write("N" + nn + secName + " {\n");
			fw.write("\t nseg = " + nsegs + "\n\t pt3dclear()\n");
			for (Segment segment : section.getSegments()) {
				writetohocSegment(fw, 0, segment);
			}
			Segment segment = section.getSegments().get(nsegs - 1);
			writetohocSegment(fw, 1, segment);
			fw.write("}\n");
			fw.flush();
			++nsections;
		}
		return nsections;
	}

	/**
	 * Function to write the data of dendrite into a hoc file.
	 *
	 * @param fw the stream for the hoc file.
	 * @param neuron the neuron of neural network
	 * @param nn the number of neuron.
	 * @param suffix is for naming issues. A section is names "NXXsuffix"
	 * @throws IOException
	 */
	public final int writetohocDendrite(Writer fw, Neuron neuron, int nn) throws IOException {
		int nsections = 0;
		for (Dendrite dendrite : neuron.getDendrites()) {
			Section firstSection = dendrite.getFirstSection();
			Section.Iterator secIterator = firstSection.getIterator();
			while (secIterator.hasNext()) {
				Section section = secIterator.next();
				String secName = section.getName();
				int nsegs = section.getSegments().size();
                // the number of the dendrite of neuron for the hoc file
				//String secName = nn + suffix + i + "_" + secIterator.getSectionAdress();
				String data = "create " + "N" + nn + secName + "\n";
				fw.write(data);
				Section parSec;
				if (section.getParentalLink() != null) {
					parSec = section.getParentalLink().getParental();
				} else {
					parSec = neuron.getSoma().getCylindricRepresentant();
				}
				float fractAlongParent = parSec.getFractAlongParentForChild(section);
				String parentSecName = parSec.getName();

				if(parentSecName==null){
					data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + "undefined" + "(" + (int) fractAlongParent + ")\n";
					fw.write(data);
				}else {
					if (nsections == 0 || parentSecName.contains("soma")) {
						data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + "soma" + "(" + (int) fractAlongParent + ")\n";
						//data = "connect N" + secName + "(0), N" + nn + "soma(" + (int)fractAlongParent +")\n";
						fw.write(data);
					} else {
						data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + parentSecName + "(" + (int) fractAlongParent + ")\n";
						//data = "connect N" + secName + "(0), N" + nn + suffix + i + "_" + secIterator.getSectionAdress().parentAdress+ "(" + (int)fractAlongParent +")\n";
						fw.write(data);
					}
				}
				fw.write("N" + nn + secName + " {\n");
				fw.write("\t nseg = " + nsegs + "\n\t pt3dclear()\n");
				for (Segment segment : section.getSegments()) {
					writetohocSegment(fw, 0, segment);
				}
				Segment segment = section.getSegments().get(nsegs - 1);
				writetohocSegment(fw, 1, segment);
				fw.write("}\n");
				fw.flush();
				++nsections;
			}
		}
		return nsections;
	}

	public final void writetohocNeuron(Writer fw, int neuronNum) throws IOException {
		int i = neuronNum;
		Neuron neuron = net.getNeuronList().get(i);
		int axon_part_num = 0, den_part_num = 0;
		String name = "N" + i + "soma";
		fw.write("create " + name + "\n");
		if (i == 0) {
			fw.write("access " + "N0soma" + "\n");
		}
		writetohocSoma(fw, neuron, name);
		axon_part_num = writetohocAxon(fw, neuron, i);
		den_part_num = writetohocDendrite(fw, neuron, i);
		fw.write("N" + i + "ax = " + axon_part_num + "\n");
		fw.write("N" + i + "dend = " + den_part_num + "\n");
		fw.flush();
	}

	/**
	 * Function to write a hoc file for the data of net. NEURON will write a
	 * file with the voltage data of the somata.
	 *
	 * @param fw stream for the hoc file
	 */
	public final void writetohocNet(Writer fw) throws IOException {
		List<Neuron> neuronList = net.getNeuronList();
		int nneuron = neuronList.size();
		for (int i = 0; i < nneuron; i++) {
			// write neuron geometry
			writetohocNeuron(fw, i);
		}
		// write exp2 synapses
		net.getHocData().writetohocExp2Synapses(fw, null);
		fw.append("/* Input synapses. */ \n");
		// write alpha synapses
		net.getHocData().writetohocAlphaSynapses(fw);
		fw.close();

		// write model to file
		String sm_fname = hocFileName + "model.hoc";
		if (NeuGenConstants.WITH_GUI) {
			trigger.outPrintln("\t" + sm_fname);
		} else {
			NGBackend.logger.info("\t" + sm_fname);
		}
		String modelFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + sm_fname;
		fw = new FileWriter(new File(modelFilePath));

		fw.append("celsius = 37.0 \n"
			+ "wopen(\"" + hocFileName + "." + voltageFilePostfix + "\") \n"
			+ "xopen(\"" + hocFileName + ".hoc\") \n"
			+ "strdef fname \n\n"
			+ "proc wtvoltage(){ \n"
			+ "\tfprint(\"%g \",t) \n");
		int j = 0;
		for (int i = 0; i < nneuron; i += j) {
			fw.append("\tfprint(\"");
			for (j = 0; j < 20; j++) {
				if ((i + j) > (nneuron - 1)) {
					break;
				}
				fw.append("%g ");
			}
			fw.append("\\n\"");
			for (j = 0; j < 20; j++) {
				if ((i + j) > (nneuron - 1)) {
					break;
				}
				fw.append(",N" + (i + j) + "soma.v(.5)");
			}
			fw.append(") \n");
		}
		fw.append("} \n");

		fw.append("proc mkmovie() {" + "\n"
			+ "     print t" + "\n"
			+ "     wtvoltage()" + "\n"
			+ "     cvode.event(t+1, \"mkmovie()\")" + "\n"
			+ "}" + "\n\n");

		net.getHocData().writetohocModel(fw);
		net.getHocData().writetohocChannels(fw);

		fw.append("v_init = -70 \n"
			+ "t = 0 \n"
			+ "tstop = 100 \n"
			+ "dt = 0.025 \n"
			+ "steps_per_ms = 5 \n"
			+ "realtime = 0 \n"
			+ "run() \n"
			+ "wopen() \n"
			+ "quit() \n");

		fw.flush();
		fw.close();
	}

	/**
	 * Function to write a hoc file for the data of net. NEURON will write a
	 * file with the voltage data of the somata and a second file with the
	 * data of NetCon events.
	 *
	 * @param fname the name of the file.
	 * @throws IOException
	 */
	public final void writetohocNetVoltageAndEvents(Writer fw) throws IOException {  //(simone)
		List<Neuron> neuronList = net.getNeuronList();
		int nneuron = neuronList.size();
		for (int i = 0; i < nneuron; i++) {
			// write neuron geometry
			this.writetohocNeuron(fw, i);
		}
		// write synapses to file
		String c_fname = this.hocFileName + ".syn_coords";
		if (NeuGenConstants.WITH_GUI) {
			trigger.outPrintln("\t" + c_fname);
		} else {
			NGBackend.logger.info("\t" + c_fname);
		}
		String synapseFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + c_fname;
		Writer synFW = new FileWriter(new File(synapseFilePath));

		// write exp2 synapses
		net.getHocData().writetohocExp2Synapses(fw, synFW);
		fw.append("/* Input synapses. */ \n");

		// write alpha synapses
		net.getHocData().writetohocAlphaSynapses(fw);
		fw.close();

		// write model to file
		String sm_fname = hocFileName + "model.hoc";
		
		if (NeuGenConstants.WITH_GUI) {
		trigger.outPrintln("\t" + sm_fname);
		} else {
			NGBackend.logger.info("\t" + sm_fname);
		}
		String modelFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + sm_fname;
		fw = new FileWriter(new File(modelFilePath));

		fw.append("celsius = 37.0\n"
			+ "objref voltagesfile, nceventsfile\n\n" //(simone)
			+ "voltagesfile = new File()\n"
			+ "nceventsfile = new File()\n\n"
			+ "voltagesfile.wopen(\"" + hocFileName + "." + voltageFilePostfix + "\")\n\n"
			+ "nceventsfile.wopen(\"" + hocFileName + "." + netConEventsFilePostfix + "\")\n\n"
			+ "xopen(\"" + hocFileName + ".hoc\")\n\n\n"
			//netConEvents.out (simone)
			+ "objref list, netConList, netConI, tvec, idvec\n\n"
			+ "tvec = new Vector()   //vector for storing the times of recorded events\n"
			+ "idvec = new Vector()  //vector for storing the ids of recorded NetCons\n\n"
			+ "//list of all NetCons\n"
			+ "netConList = new List()\n\n");

		List<Cons> synapseList = net.getSynapseList();
		for (int n = 0; n < synapseList.size(); ++n) {
			Cons synapse = synapseList.get(n);
			//if index < 0 -> nf_synapse
			if (synapse.getNeuron1() == null) {
				continue;
			}
			fw.append("netConList.append(Nc" + n + ")" + "\n");
		}

		//voltages
		fw.append("proc wtvoltage(){" + "\n");
		fw.append("\tvoltagesfile.printf(\"%g \",t)" + "\n");
		int j = 0;
		for (int i = 0; i < nneuron; i += j) {
			fw.append("\tvoltagesfile.printf(\"");
			for (j = 0; j < 20; j++) {
				if ((i + j) > (nneuron - 1)) {
					break;
				}
				fw.append("%g ");
			}
			fw.append("\\n\"");
			for (j = 0; j < 20; j++) {
				if ((i + j) > (nneuron - 1)) {
					break;
				}
				fw.append(",N" + (i + j) + "soma.v(.5)");
			}
			fw.append(")\n");
		}
		fw.append("}\n\n");

		fw.append("proc mkmovie() {\n"
			+ "     print t\n"
			+ "     for i=0, netConList.count()-1 {\n"
			+ "         netConI =  netConList.object(i)\n"
			+ "         netConI.record(tvec, idvec)\n"
			+ "         }\n\n"
			+ "     wtvoltage()" + "\n"
			+ "     cvode.event(t+1, \"mkmovie()\")\n"
			+ "}\n\n");

		net.getHocData().writetohocModel(fw);
		net.getHocData().writetohocChannels(fw);
		fw.append("\n");
		fw.append("v_init = -70" + "\n"
			+ "t = 0" + "\n"
			+ "tstop = 100" + "\n"
			+ "dt = 0.025" + "\n"
			+ "steps_per_ms = 5" + "\n"
			+ "realtime = 0" + "\n"
			+ "run()" + "\n"
			+ "//write output file from recorded NetCon vectors" + "\n"
			+ "//each line contains the timestep and all ids of NetCons active in that timestep" + "\n"
			+ "for z=0, tstop{" + "\n"
			+ "    nceventsfile.printf(\"%d\" , z)" + "\n"
			+ "    for tv=0, tvec.size()-1 {" + "\n"
			+ "        if(z<=tvec.x[tv] && tvec.x[tv]<(z+1)){" + "\n"
			+ "            nceventsfile.printf(\" %d% \", idvec.x[tv])" + "\n"
			+ "        }" + "\n"
			+ "    }" + "\n"
			+ "    nceventsfile.printf(\"\\n\")" + "\n"
			+ "}" + "\n\n"
			+ "wopen()" + "\n"
			+ "quit()");

		fw.flush();
		fw.close();
	}

	/**
	 * Function to write a hoc file for the data of net. NEURON will write a
	 * file with the data of NetCon events.
	 *
	 * @param fname the name of the file.
	 * @param events_postfix the postfix of the name of the file for the
	 * NetCon events data.
	 */
	public final void writetohocNetOnlyEvents(File file, String events_postfix, Writer fw) throws IOException { //(simone)
		List<Neuron> neuronList = net.getNeuronList();
		int nneuron = neuronList.size();
		for (int i = 0; i < nneuron; i++) {
			// write neuron geometry
			this.writetohocNeuron(fw, i);
		}

		// write synapses to file
		String c_fname = this.hocFileName + ".syn_coords";
		if (NeuGenConstants.WITH_GUI) {
			trigger.outPrintln("\t" + c_fname);
		} else {
			NGBackend.logger.info("\t" + c_fname);
		}
		String synapseFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + c_fname;
		Writer synFW = new FileWriter(new File(synapseFilePath));

		// write exp2 synapses
		net.getHocData().writetohocExp2Synapses(fw, synFW);

		fw.append("/* Input synapses. */ \n");

		// write alpha synapses
		net.getHocData().writetohocAlphaSynapses(fw);
		fw.close();

		// write model to file
		String sm_fname = hocFileName + "model.hoc";
		if (NeuGenConstants.WITH_GUI) {
			trigger.outPrintln("\t" + sm_fname);
		} else {
			NGBackend.logger.info("\t " + sm_fname);
		}
		String modelFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + sm_fname;
		fw = new FileWriter(new File(modelFilePath));

		fw.write("celsius = 37.0\n");
		fw.append("wopen(\"" + hocFileName + "." + events_postfix + "\")\n");
		fw.append("xopen(\"" + hocFileName + ".hoc\")\n\n\n");

		//netConEvents.out (simone)
		fw.append("objref list, netConList, netConI, tvec, idvec\n\n");
		fw.append("tvec = new Vector()   //vector for storing the times of recorded events\n");
		fw.append("idvec = new Vector()  //vector for storing the ids of recorded NetCons\n\n");
		fw.append("//list of all NetCons\n");
		fw.append("netConList = new List()\n\n");

		List<Cons> synapseList = net.getSynapseList();
		for (int n = 0; n < synapseList.size(); ++n) {
			Cons synapse = synapseList.get(n);
			//if index < 0 -> nf_synapse
			if (synapse.getNeuron1() == null) {
				continue;
			}
			fw.append("netConList.append(Nc" + n + ")" + "\n");
		}

		fw.append("\nproc mkmovie() {\n\n"
			+ "     print t\n\n"
			+ "     for i=0, netConList.count()-1 {\n"
			+ "         netConI =  netConList.object(i)\n"
			+ "         netConI.record(tvec, idvec)\n"
			+ "         }\n\n"
			+ "     cvode.event(t+1, \"mkmovie()\")\n"
			+ "}\n\n");

		net.getHocData().writetohocModel(fw);
		net.getHocData().writetohocChannels(fw);

		fw.append("v_init = -70" + "\n"
			+ "t = 0" + "\n"
			+ "tstop = 100" + "\n"
			+ "dt = 0.025" + "\n"
			+ "steps_per_ms = 5" + "\n"
			+ "realtime = 0" + "\n"
			+ "run()\n"
			+ "//write output file from recorded NetCon vectors\n"
			+ "//each line contains the timestep and all ids of NetCons active in that timestep\n"
			+ "for z=0, tstop{ \n"
			+ "    fprint(\"%d\" , z)\n"
			+ "    for tv=0, tvec.size()-1 {\n"
			+ "        if(z<=tvec.x[tv] && tvec.x[tv]<(z+1)){\n"
			+ "            fprint(\" %d% \", idvec.x[tv])\n"
			+ "        }\n"
			+ "    }\n"
			+ "    fprint(\"\\n\")\n"
			+ "}\n\n"
			+ "wopen()\n"
			+ "quit()");
		fw.flush();
		fw.close();
	}

	public void exportNet() {
		Writer fw = null;
		String hoc = NeuGenConstants.EXTENSION_HOC;
		String extension = Utils.getExtension(file);
		if (!hoc.equals(extension)) {
			file = new File(file.getAbsolutePath() + "." + hoc);
		}
		if(netConEventsFilePostfix!=null) {
			if (netConEventsFilePostfix.equals("")) {
				netConEventsFilePostfix = null;
			}
		}

		if(voltageFilePostfix !=null) {
			if (voltageFilePostfix.equals("")) {
				voltageFilePostfix = null;
			}
		}
		try {
			if (NeuGenConstants.WITH_GUI) {
				trigger.outPrintln("Write hoc file for NEURON");
				trigger.outPrintln("\t" + hocFileName + ".hoc");
			} else {
				NGBackend.logger.info("Write hoc file for NEURON");
				NGBackend.logger.info("\t" + hocFileName + ".hoc");
			}
			fw = new FileWriter(file);
			// (simone) only events
			if (netConEventsFilePostfix != null && voltageFilePostfix == null) {
				logger.info("write only events (net con events file postfix): " + netConEventsFilePostfix);
				writetohocNetOnlyEvents(file, netConEventsFilePostfix, fw);
			} //(simone) only voltages
			else if (netConEventsFilePostfix == null && voltageFilePostfix != null) {
				logger.info("write only voltages (voltage file postfix): " + voltageFilePostfix);
				writetohocNet(fw);
			} //(simone) voltages and events
			else if (netConEventsFilePostfix != null && voltageFilePostfix != null) {
				logger.info("write voltages and events: " + netConEventsFilePostfix + ", " + voltageFilePostfix);
				writetohocNetVoltageAndEvents(fw);
			} // default writetohoc
			else {
				voltageFilePostfix = "out";
				logger.info("default write voltage: " + voltageFilePostfix);
				writetohocNet(fw);
			}
		} catch (Exception e) {
			logger.error("Could not create file: " + e, e);
            System.out.println("Error encountered: "+e.getMessage());
		}
	}

	public static void main(String args[]) {
		MorphMLReader netBuilder = new MorphMLReader();
		String fname = "bal.xml";
		netBuilder.runMorphMLReader(fname);
		Net net = netBuilder.getNet();
		System.out.println("Size of the neural net: " + net.getNeuronList().size());
		File file = new File("MyData.hoc");
		HocWriter exportHoc = new HocWriter(net, file);
		exportHoc.exportNet();
	}
}

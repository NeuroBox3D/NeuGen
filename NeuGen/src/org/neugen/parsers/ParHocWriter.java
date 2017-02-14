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
 * @author Markus Breit
 * 
 * derived from HocWriter implementation by 
 * Alexander Wanner, Simone Eberhard, Sergei Wolf
 */
public final class ParHocWriter
{
    private static final Logger logger = Logger.getLogger(ParHocWriter.class.getName());
    private final Net net;
    private File file;
    private final int nProcs;
    private String hocBaseName;
    
    // postfix of the name of the file for the NetCon events and voltage data.
    //private String netConEventsFilePostfix;
    private String voltageFilePostfix;
    
    private Trigger trigger;

    public ParHocWriter(Net net, File file, int nProcs)
    {
        this.net = net;
        this.file = file;
        this.nProcs = nProcs;
        hocBaseName = file.getName();
        String[] str = hocBaseName.split("\\.");
        hocBaseName = str[0];
        if (NeuGenConstants.WITH_GUI)
            trigger = Trigger.getInstance();
    }

   /*
    public void setNetConEventsFilePostfix(String netConEventsFilePostfix)
    {
        this.netConEventsFilePostfix = netConEventsFilePostfix;
    }

    public void setVoltageFilePostfix(String voltageFilePostfix)
    {
        this.voltageFilePostfix = voltageFilePostfix;
    }
    */
    
    /**
     * Function to write data of segment into hoc file
     *
     * @param fw the stream for the hoc file
     * @param start_end for 0 write data of start of segment, for 1 write
     * data of end of segment.
     * @param segment the current segment
     * @throws IOException
     */
    public final void writetohocSegment(Writer fw, int start_end, Segment segment)
        throws IOException
    {
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
     * @param name
     * @throws IOException
     */
    public final void writetohocSoma(Writer fw, Neuron neuron, String name)
        throws IOException
    {
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
     * @param neuron
     * @param nn the number of neuron.
     * @return 
     * @throws IOException
     */
    public final int writetohocAxon(Writer fw, Neuron neuron, int nn)
    throws IOException
    {
        int nsections = 0;
        Axon axon = neuron.getAxon();
        if (axon.getFirstSection() == null)
            return 0;

        Section.Iterator secIterator = axon.getFirstSection().getIterator();
        while (secIterator.hasNext())
        {
            Section section = secIterator.next();
            int nsegs = section.getSegments().size();
            String secName = section.getName();
            String data = "create " + "N" + nn + secName + "\n";
            fw.write(data);
            Section parSec;
            if (section.getParentalLink() != null)
                parSec = section.getParentalLink().getParental();
            else
                parSec = neuron.getSoma().getCylindricRepresentant();

            float fractAlongParent = parSec.getFractAlongParentForChild(section);
            //connect N0axon_hillock(0), N0soma(0)
            if (nsections == 0)
            {
                data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + "soma" + "(" + (int) fractAlongParent + ")\n";
                fw.write(data);
            } //connect N0axon_myel_0000(0), N0axon_000(1)
            else
            {
                String parentSecName = parSec.getName();
                data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + parentSecName + "(" + (int) fractAlongParent + ")\n";
                fw.write(data);
            }
            fw.write("N" + nn + secName + " {\n");
            fw.write("\t nseg = " + nsegs + "\n\t pt3dclear()\n");
            for (Segment segment : section.getSegments())
                writetohocSegment(fw, 0, segment);
            
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
     * @return 
     * @throws IOException
     */
    public final int writetohocDendrite(Writer fw, Neuron neuron, int nn)
    throws IOException
    {
        int nsections = 0;
        for (Dendrite dendrite : neuron.getDendrites())
        {
            Section firstSection = dendrite.getFirstSection();
            Section.Iterator secIterator = firstSection.getIterator();
            while (secIterator.hasNext())
            {
                Section section = secIterator.next();
                String secName = section.getName();
                int nsegs = section.getSegments().size();
// the number of the dendrite of neuron for the hoc file
                //String secName = nn + suffix + i + "_" + secIterator.getSectionAdress();
                String data = "create " + "N" + nn + secName + "\n";
                fw.write(data);
                Section parSec;
                if (section.getParentalLink() != null)
                    parSec = section.getParentalLink().getParental();
                else
                    parSec = neuron.getSoma().getCylindricRepresentant();
                
                float fractAlongParent = parSec.getFractAlongParentForChild(section);
                String parentSecName = parSec.getName();
                if (nsections == 0 || parentSecName.contains("soma"))
                {
                    data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + "soma" + "(" + (int) fractAlongParent + ")\n";
                    //data = "connect N" + secName + "(0), N" + nn + "soma(" + (int)fractAlongParent +")\n";
                    fw.write(data);
                }
                else
                {
                    data = "connect " + "N" + nn + secName + "(0), " + "N" + nn + parentSecName + "(" + (int) fractAlongParent + ")\n";
                    //data = "connect N" + secName + "(0), N" + nn + suffix + i + "_" + secIterator.getSectionAdress().parentAdress+ "(" + (int)fractAlongParent +")\n";
                    fw.write(data);
                }
                fw.write("N" + nn + secName + " {\n");
                fw.write("\t nseg = " + nsegs + "\n\t pt3dclear()\n");
                for (Segment segment : section.getSegments())
                    writetohocSegment(fw, 0, segment);
                
                Segment segment = section.getSegments().get(nsegs - 1);
                writetohocSegment(fw, 1, segment);
                fw.write("}\n");
                fw.flush();
                ++nsections;
            }
        }
        return nsections;
    }

    public final void writetohocNeuron(Writer fw, int neuronID)
    throws IOException
    {
        Neuron neuron = net.getNeuronList().get(neuronID);
        String name = "N" + neuronID + "soma";
        fw.write("create " + name + "\n");
        //if (neuronID == 0) fw.write("access " + "N0soma" + "\n");
        
        writetohocSoma(fw, neuron, name);
        
        // writing v to file
        fw.write("\n\n// record V at soma\n");
        fw.write("tmpvec = new Vector()\n");
        fw.write("{tmpvec.record(&" + name + ".v(0.5))}\n");
        fw.write("{tmpveclist.append(tmpvec)}\n");
        
        int axon_part_num = writetohocAxon(fw, neuron, neuronID);
        int den_part_num = writetohocDendrite(fw, neuron, neuronID);
        fw.write("N" + neuronID + "ax = " + axon_part_num + "\n");
        fw.write("N" + neuronID + "dend = " + den_part_num + "\n");
        fw.flush();
    }

    /**
     * Function to write parallel hoc files for the data of net.
     * NEURON will write a file with the voltage data of the somata.
     *
     * @param fw stream for the hoc file
     * @param p process id
     * @throws java.io.IOException
     */
    public final void writeToParHocNet(Writer fw, int p)
    throws IOException
    {
        List<Neuron> neuronList = net.getNeuronList();
        int nneuron = neuronList.size();
        
        // for storage of potential saving vectors
        fw.write("objref tmpvec, tmpveclist\n");
        fw.write("tmpveclist = new List()\n\n");
        
        // write neuron geometry    
        for (int i = p; i < nneuron; i += nProcs)
            writetohocNeuron(fw, i);

        // write exp2 synapses
        try
        {
            fw.append("\n\n\n//////////// interconnecting synapses //////////// \n");
            net.getParHocData(nProcs).writeToParHocExp2Synapses(fw, null, p);
        }
        catch (Exception e) {System.out.println("Error encountered while writing biexps: "+e.getMessage());}
        
        
        // write alpha synapses
        try
        {
            fw.append("\n\n\n//////////// input synapses //////////// \n");
            net.getParHocData(nProcs).writeToParHocAlphaSynapses(fw, p);
        }
        catch (Exception e) {System.out.println("Error encountered while writing alphas: "+e.getMessage());}
        fw.close();

        
        // write model to file
        String sm_fname = hocBaseName + "model.hoc";
        if (NeuGenConstants.WITH_GUI)
            trigger.outPrintln("\t" + sm_fname);
        else
            NGBackend.logger.info("\t" + sm_fname);
        
        String procAsString = String.format("%0"+(int)Math.ceil(Math.log10(nProcs+1))+"d", p);
        String procModelFileName = file.getParentFile().getPath()
            + NeuGenConstants.FILE_SEP + hocBaseName + "_model" + "_p" + procAsString
            + ".hoc";
        fw = new FileWriter(new File(procModelFileName));
        
        fw.append("celsius = 37.0 \n");
        fw.append("{xopen(\"" + hocBaseName + "_p" + procAsString + ".hoc\")}\n\n\n");
        
        
        // writing potential to outfile
        fw.append("objref voltagesfile\n"
              + "voltagesfile = new File()\n"
              + "{voltagesfile.wopen(\"" + hocBaseName + "_out_p" + procAsString
              + "." + voltageFilePostfix + "\")}\n\n");
        fw.append("proc wtvoltage(){" + "\n");
        fw.append("\tvoltagesfile.printf(\"%g\\t\",t)\n");
        
        int id = p;
        int cnt = 0;
        String printCmdPt1 = "";
        String printCmdPt2 = "";
        while (id < nneuron)
        {
            if (cnt == 0)
            {
                printCmdPt1 = "\tvoltagesfile.printf(\"";
                printCmdPt2 = "";
            }
            
            printCmdPt1 += "%g ";
            printCmdPt2 += ", N" + id + "soma.v(.5)";
            
            if (cnt == 19)
                fw.append(printCmdPt1 + "\\n\"" + printCmdPt2 + ")\n");
            
            id += nProcs;
            cnt = (cnt+1) % 20;
        }
        if (cnt != 0)
            fw.append(printCmdPt1 + "\\n\"" + printCmdPt2 + ")\n");
        
        //fw.append("\tcvode.event(t+dt, \"wtvoltage()\")\n\n\n");
        
        fw.append("}\n\n");
        //fw.append("cvode.event(0.0, \"wtvoltage()\")\n");
        
        
        try
        {
            net.getParHocData(nProcs).writeToParHocModel(fw);
            net.getParHocData(nProcs).writeToParHocChannels(fw, p);
        }
        catch (Exception e) {System.out.println("Error encountered while writing model and channels: "+e.getMessage());}

        fw.append("t = 0 \n"
            + "tstop = 20 \n"
            + "dt = 0.025\n"
            + "steps_per_ms = 5 \n"
            + "realtime = 0 \n"
            + "{finitialize(-70.0)}\n\n" 
            + "wtvoltage()\n"
            + "while (t < tstop) {\n"
            + "\tfadvance()\n"
            + "\twtvoltage()\n"
            + "}\n\n"
            //+ "run() \n"
            + "quit() \n");

        fw.flush();
        fw.close();
    }

    /**
     * Function to write a hoc file for the data of net. NEURON will write a
     * file with the voltage data of the somata and a second file with the
     * data of NetCon events.
     *
     * @param fw
     * @param p process id
     * @throws IOException
     */
    /*
    public final void writeToParHocNetVoltageAndEvents(Writer fw, int p)
    throws IOException
    {
            List<Neuron> neuronList = net.getNeuronList();
            int nneuron = neuronList.size();
                        
            // write neuron geometry
            for (int i = 0; i < nneuron; i++)
                this.writetohocNeuron(fw, i);
            
            // write synapses to file
            String c_fname = this.hocBaseName + ".syn_coords";
            if (NeuGenConstants.WITH_GUI)
                trigger.outPrintln("\t" + c_fname);
            else
                NGBackend.logger.info("\t" + c_fname);
            
            String synapseFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + c_fname;
            Writer synFW = new FileWriter(new File(synapseFilePath));

            // write exp2 synapses
            fw.append("\n\n\n//////////// interconnecting synapses //////////// \n");
            net.getParHocData(nProcs).writeToParHocExp2Synapses(fw, synFW, p);

            // write alpha synapses
            fw.append("\n\n\n//////////// input synapses //////////// \n");
            net.getParHocData(nProcs).writeToParHocAlphaSynapses(fw, p);
            fw.close();

            // write model to file
            String sm_fname = hocBaseName + "model.hoc";

            if (NeuGenConstants.WITH_GUI)
                trigger.outPrintln("\t" + sm_fname);
            else
                NGBackend.logger.info("\t" + sm_fname);
            
            String modelFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + sm_fname;
            fw = new FileWriter(new File(modelFilePath));

            fw.append("celsius = 37.0\n"
                + "objref voltagesfile, nceventsfile\n\n" //(simone)
                + "voltagesfile = new File()\n"
                + "nceventsfile = new File()\n\n"
                + "voltagesfile.wopen(\"" + hocBaseName + "." + voltageFilePostfix + "\")\n\n"
                + "nceventsfile.wopen(\"" + hocBaseName + "." + netConEventsFilePostfix + "\")\n\n"
                + "xopen(\"" + hocBaseName + ".hoc\")\n\n\n"
                //netConEvents.out (simone)
                + "objref list, netConList, netConI, tvec, idvec\n\n"
                + "tvec = new Vector()   //vector for storing the times of recorded events\n"
                + "idvec = new Vector()  //vector for storing the ids of recorded NetCons\n\n"
                + "//list of all NetCons\n"
                + "netConList = new List()\n\n");

            List<Cons> synapseList = net.getSynapseList();
            for (int n = 0; n < synapseList.size(); ++n)
            {
                Cons synapse = synapseList.get(n);
                
                //if index < 0 -> nf_synapse
                if (synapse.getNeuron1() == null) continue;

                fw.append("netConList.append(Nc" + n + ")" + "\n");
            }

            //voltages
            fw.append("proc wtvoltage(){" + "\n");
            fw.append("\tvoltagesfile.printf(\"%g \",t)" + "\n");
            int j;
            for (int i = 0; i < nneuron; i += j)
            {
                fw.append("\tvoltagesfile.printf(\"");
                for (j = 0; j < 20; j++)
                {
                    if (i+j > nneuron-1) break;
                    fw.append("%g ");
                }
                fw.append("\\n\"");
                for (j = 0; j < 20; j++)
                {
                    if (i+j > nneuron-1) break;
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

            net.getParHocData(nProcs).writeToParHocModel(fw);
            net.getParHocData(nProcs).writeToParHocChannels(fw, p);
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
    */
    /**
     * Function to write a hoc file for the data of net. NEURON will write a
     * file with the data of NetCon events.
     *
     * @param file
     * @param fw
     * @param events_postfix the postfix of the name of the file for the
     * NetCon events data.
     * @param p process id
     * @throws java.io.IOException
     */
    /*
    public final void writeToParHocNetOnlyEvents(File file, String events_postfix, Writer fw, int p)
    throws IOException
    {
        List<Neuron> neuronList = net.getNeuronList();
        int nneuron = neuronList.size();
        
        // write neuron geometry
        for (int i = 0; i < nneuron; i++)
            this.writetohocNeuron(fw, i);

        // write synapses to file
        String c_fname = this.hocBaseName + ".syn_coords";
        if (NeuGenConstants.WITH_GUI)
            trigger.outPrintln("\t" + c_fname);
        else
            NGBackend.logger.info("\t" + c_fname);
        
        String synapseFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + c_fname;
        Writer synFW = new FileWriter(new File(synapseFilePath));

        // write exp2 synapses
        fw.append("\n\n\n//////////// interconnecting synapses //////////// \n");
        net.getParHocData(nProcs).writeToParHocExp2Synapses(fw, synFW, p);

        // write alpha synapses
        fw.append("\n\n\n//////////// input synapses //////////// \n");
        net.getParHocData(nProcs).writeToParHocAlphaSynapses(fw, p);
        fw.close();

        // write model to file
        String sm_fname = hocBaseName + "model.hoc";
        if (NeuGenConstants.WITH_GUI)
            trigger.outPrintln("\t" + sm_fname);
        else
            NGBackend.logger.info("\t " + sm_fname);
        
        String modelFilePath = file.getParentFile().getPath() + NeuGenConstants.FILE_SEP + sm_fname;
        fw = new FileWriter(new File(modelFilePath));

        fw.write("celsius = 37.0\n");
        fw.append("wopen(\"" + hocBaseName + "." + events_postfix + "\")\n");
        fw.append("xopen(\"" + hocBaseName + ".hoc\")\n\n\n");

        //netConEvents.out (simone)
        fw.append("objref list, netConList, netConI, tvec, idvec\n\n");
        fw.append("tvec = new Vector()   //vector for storing the times of recorded events\n");
        fw.append("idvec = new Vector()  //vector for storing the ids of recorded NetCons\n\n");
        fw.append("//list of all NetCons\n");
        fw.append("netConList = new List()\n\n");

        List<Cons> synapseList = net.getSynapseList();
        for (int n = 0; n < synapseList.size(); ++n)
        {
            Cons synapse = synapseList.get(n);

            //if index < 0 -> nf_synapse
            if (synapse.getNeuron1() == null) continue;
            
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

        net.getParHocData(nProcs).writeToParHocModel(fw);
        net.getParHocData(nProcs).writeToParHocChannels(fw, p);

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
    */
    /**
    *
    */
    public void exportNet()
    {
        String hoc = NeuGenConstants.EXTENSION_HOC;
        String extension = Utils.getExtension(file);
        if (!hoc.equals(extension))
            file = new File(file.getAbsolutePath() + "." + hoc);
        
        /*
        if ("".equals(netConEventsFilePostfix))
            netConEventsFilePostfix = null;
        
        if ("".equals(voltageFilePostfix))
            voltageFilePostfix = "out";
        */
        
        try
        {
            if (NeuGenConstants.WITH_GUI)
            {
                trigger.outPrintln("Write hoc file for NEURON");
                trigger.outPrintln("\t" + hocBaseName + ".hoc");
            }
            else
            {
                NGBackend.logger.info("Write hoc file for NEURON");
                NGBackend.logger.info("\t" + hocBaseName + ".hoc");
            }
            
            // write an individual file for each proc
            // neuron distribution à la round robin
            for (int proc = 0; proc < nProcs; ++proc)
            {
                System.out.println("Exporting network for proc " + proc + ".");
                
                // open a filewriter for this proc
                String procAsString = String.format("%0"+(int)Math.ceil(Math.log10(nProcs+1))+"d", proc);
                String procFileName = file.getParentFile().getPath()
                    + NeuGenConstants.FILE_SEP + hocBaseName + "_p" + procAsString
                    + "." + hoc;
                Writer fw = new FileWriter(new File(procFileName));
                
                /*
                // only events
                if (netConEventsFilePostfix != null && voltageFilePostfix == null)
                {
                    logger.info("write only events (net con events file postfix): " + netConEventsFilePostfix);
                    writeToParHocNetOnlyEvents(file, netConEventsFilePostfix, fw, proc);
                }
                // only voltages
                else if (netConEventsFilePostfix == null && voltageFilePostfix != null)
                {
                    logger.info("write only voltages (voltage file postfix): " + voltageFilePostfix);
                    writeToParHocNet(fw, proc);
                }
                // voltages and events
                else if (netConEventsFilePostfix != null && voltageFilePostfix != null)
                {
                    logger.info("write voltages and events: " + netConEventsFilePostfix + ", " + voltageFilePostfix);
                    writeToParHocNetVoltageAndEvents(fw, proc);
                }
                // default writetohoc
                else
                {
                logger.info("default write voltage: " + voltageFilePostfix);
                */
                voltageFilePostfix = "dat";
                writeToParHocNet(fw, proc);
                //}
            }
            
            // write gathering file
	    Writer fw = null;
            try { 
		fw = new FileWriter(file);
                fw.append("// load the GUI and standard run libraries\n");
                fw.append("{load_file(\"nrngui.hoc\")}\n\n");
                fw.append("// initialize parallel context\n");
                fw.append("objref pc\n");
                fw.append("pc = new ParallelContext()\n\n");
                fw.append("// provide a nil object (needed for NetCon definitions)\n");
                fw.append("objref nil\n\n\n");
                fw.append("// each proc continues with their own part of the network\n\n");
                
                for (int p = 0; p < nProcs; ++p)
                {
                    String procAsString = String.format("%0"+(int)Math.ceil(Math.log10(nProcs+1))+"d", p);
                    fw.append("if (pc.id == " + p + ") {\n");
                    fw.append("\txopen(\"" + hocBaseName + "_model" + "_p" + procAsString + ".hoc\")\n");
                    fw.append("}\n\n");
                }
                
                fw.flush();
            }
            catch (Exception e) {System.out.println("Could not write gathering file: " + e.getMessage());}
	    finally { fw.close(); }
        }
        catch (Exception e)
        {
                logger.error("Could not create file: " + e, e);
                System.out.println("Error encountered: " + e.getMessage());
        }
    }

    
    public static void main(String args[])
    {
        int nProcs = 1;
        if (args.length > 0)
        {
            try {nProcs = Integer.parseInt(args[0]);}
            catch (NumberFormatException ex)
            {
                System.out.println("The first argument needs to be an integer number\n"
                        + "but value \"" + args[0] + "\" given.");
            }
        }
        
        MorphMLReader netBuilder = new MorphMLReader();
        String fname = "bal.xml";
        netBuilder.runMorphMLReader(fname);
        Net net = netBuilder.getNet();
        System.out.println("Size of the neural net: " + net.getNeuronList().size());
        File file = new File("MyData.hoc");
        ParHocWriter exportParHoc = new ParHocWriter(net, file, nProcs);
        exportParHoc.exportNet();
    }
}

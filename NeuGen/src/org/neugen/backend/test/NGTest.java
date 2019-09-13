package org.neugen.backend.test;

import org.neugen.backend.*;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.NetNeocortex;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NGTest {

    public static void main(String... args) {
        try {
            NGProject project = new NGProject(false);
            project.setProjectName("Neo");
            project.setSourceTemplate("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test");
            //project.setProjectType(NeuGenConstants.NEOCORTEX_PROJECT);
            project.loadParamTree();
            //project.createProject(true);

            NGParameter paramChange = new NGParameter(project.getParamTree());
            /*paramChange.modifySynapseDistance(2.5);
            paramChange.modifyNPartsDensity(0.1);
            paramChange.adjust_number_of_neocortex_neuron(1, "starpyramidal");

                paramChange.adjust_number_of_neocortex_neuron(2, "L4stellate");

                paramChange.adjust_number_of_neocortex_neuron(3, "L23pyramidal");

                paramChange.adjust_number_of_neocortex_neuron(4,"L5Apyramidal");

                paramChange.adjust_number_of_neocortex_neuron(5,"L5Bpyramidal");*/

            /*System.out.println("projectType:"+ NGParameter.getProjectTypefromXMLObject(project.getParamTree().get("Param")));
            System.out.println("isParam:"+NGParameter.isParamXMLObject(project.getParamTree().get("Param")));
            System.out.println("isInterna:"+NGParameter.isInternaXMLObject(project.getParamTree().get("Interna")));*/
            NGGenerator gen=new NGGenerator(paramChange.getParamTree(),NeuGenConstants.NEOCORTEX_PROJECT);

            gen.run();
            Net net=gen.getNet();
            File file=new File("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Neo/Netnml");
            //NGNetExport.export_network_in_ngx(net, file);
            Integer[] levels={1,2};
            NGNetExport.export_network_in_neuroML(net, file, levels);
            /*NGNetVisual netVis=new NGNetVisual(net, NGNeuronVisual.VisualMethod.VTA);

            //System.out.println("L23:"+NetNeocortex.NeuronTypes.valueOf("L23_PYRAMIDAL").getNum());
            List<NGNeuronAppearance> appList=new ArrayList<>();
            appList.add(0,new NGNeuronAppearance(Color.red));
            appList.add(1,new NGNeuronAppearance(Color.black));

            appList.add(2,new NGNeuronAppearance(Color.blue));
            appList.add(3,new NGNeuronAppearance(Color.cyan));

            appList.add(4,new NGNeuronAppearance(Color.yellow));

            netVis.setNetAppearance(appList);

            netVis.run(true, true);
            netVis.getNetShape3DArray();
            //Neuron neuron=net.getNeuronList().get(0);
            //NGNeuronVisual neuronVisual=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
            //neuronVisual.setScale(1);
            //neuronVisual.run(true);
            //neuronVisual.getShape3DArray();
            /*NGNetVisual netVisual=new NGNetVisual(net);
            netVisual.setVisualMethod("solid");
            netVisual.run(false, true);*/
            //System.out.println(netVisual.getNetTG().toString());


            /*List<Neuron> neuronList=net.getNeuronList();
            Neuron neuron=neuronList.get(0);
            NGNeuronVisual neuronVisual=new NGNeuronVisual(neuron);
            neuronVisual.setVisualMethod("line");
            neuronVisual.run(false);*/
            //System.out.println(neuronVisual.getShape3D().numGeometries());

            //NGProject.saveParamTree(paramChange.getParamTree(), "/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Neo1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

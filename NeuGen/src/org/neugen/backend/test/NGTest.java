package org.neugen.backend.test;

import org.neugen.backend.*;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;

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
            paramChange.modifyNPartsDensity(0.1);
            paramChange.adjust_number_of_neocortex_neuron(1,"starpyramidal");
            System.out.println("projectType:"+ NGParameter.getProjectTypefromXMLObject(project.getParamTree().get("Param")));
            System.out.println("isParam:"+NGParameter.isParamXMLObject(project.getParamTree().get("Param")));
            System.out.println("isInterna:"+NGParameter.isInternaXMLObject(project.getParamTree().get("Interna")));
            NGGenerator gen=new NGGenerator(paramChange.getParamTree(),NeuGenConstants.NEOCORTEX_PROJECT);
            gen.run();
            Net net=gen.getNet();
            Neuron neuron=net.getNeuronList().get(0);
            NGNeuronVisual neuronVisual=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
            //neuronVisual.setScale(1);
            neuronVisual.run(true);
            neuronVisual.getShape3DArray();
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

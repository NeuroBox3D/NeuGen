package org.neugen.backend.test;

import org.neugen.backend.*;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenConstants;

import java.io.File;
import java.util.List;

public class NGHippTest {

    public static void main(String... args) {
        try {
        NGProject project = new NGProject(false);
        project.setProjectName("Hipp");
        project.setSourceTemplate("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test");
        project.setProjectType(NeuGenConstants.HIPPOCAMPUS_PROJECT);
        project.createProject(true);
        project.loadParamTree();

        System.out.println("projectType:"+ NGParameter.getProjectTypefromXMLObject(project.getParamTree().get("Param")));
        System.out.println("isParam:"+NGParameter.isParamXMLObject(project.getParamTree().get("Param")));
        System.out.println("isInterna:"+NGParameter.isInternaXMLObject(project.getParamTree().get("Interna")));

        NGGenerator gen=new NGGenerator(project.getParamTree(), NeuGenConstants.HIPPOCAMPUS_PROJECT);
        gen.run();
        Net net=gen.getNet();
        List<String> typeName=net.getTypeCellNames();
        for(String name:typeName) {
            System.out.println("Name: "+name);
        }
        File file=new File("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Hipp/Netnml");
        //NGNetExport.export_network_in_ngx(net, file);
        Integer[] levels={1,2};
        NGNetExport.export_network_in_neuroML(net, file, levels);

        /*File file=new File("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Hipp/Net");
        NGNetExport export=new NGNetExport(net,file);
        export.export_network_in_shoc();

        File file=new File("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Hipp/Net");
        NGNeuronLoad load=new NGNeuronLoad(file);
        //Net net1=load.load_net_from_shoc();
        /*List<String> typeName=net1.getTypeCellNames();
        for(String name:typeName) {
            System.out.println("Name: "+name);
        }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

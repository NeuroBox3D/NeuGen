package org.neugen.backend.test;

import org.neugen.backend.NGGenerator;
import org.neugen.backend.NGParameter;
import org.neugen.backend.NGProject;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenConstants;

public class NGHippTest {

    public static void main(String... args) {
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
        System.out.println( net.getNumNeurons());

    }
}

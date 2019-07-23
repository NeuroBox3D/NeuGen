package org.neugen.backend.test;

import org.neugen.backend.NGGenerator;
import org.neugen.backend.NGParameter;
import org.neugen.backend.NGProject;
import org.neugen.gui.NeuGenConstants;

public class NGTest {

    public static void main(String... args) {
        try {
            NGProject project = new NGProject();
            project.setProjectName("Neo");
            project.setSourceTemplate("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test");
            //project.setProjectType(NeuGenConstants.NEOCORTEX_PROJECT);
            project.loadParamTree();
            //project.createProject(true);

            NGParameter paramChange = new NGParameter(project.getParamTree());
            paramChange.modifyNPartsDensity(0.1);
            paramChange.adjust_number_of_neocortex_neuron(1,"L4stellate");
            System.out.println("projectType:"+ NGParameter.getProjectTypefromXMLObject(project.getParamTree().get("Param")));
            System.out.println("isParam:"+NGParameter.isParamXMLObject(project.getParamTree().get("Param")));
            System.out.println("isInterna:"+NGParameter.isInternaXMLObject(project.getParamTree().get("Interna")));
            /*NGGenerator gen=new NGGenerator(paramChange.getParamTree(),NeuGenConstants.NEOCORTEX_PROJECT);
            gen.run();*/
            //NGProject.saveParamTree(paramChange.getParamTree(), "/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Neo1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

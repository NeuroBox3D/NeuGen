package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGGenerator;
import org.neugen.backend.NGParameter;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@ComponentInfo(name="Generate Neuron Network", category = "NeuGen", description = "...")
public class VRLNeuGenGenerator implements Serializable {
    private static final long serialVersionUID=2L;

    private NGGenerator gen;

    @OutputInfo(name="Neuron Network")
    public Net Generation(
            @ParamInfo(name="Parameter Map") Map<String, XMLObject> params
    ){
        gen=new NGGenerator(params);
        gen.run();

        return gen.getNet();
    }

    @OutputInfo(name="Neuron Network")
    public Net Generation(
            @ParamInfo(name="Param XMLObject") XMLObject paramRoot,
            @ParamInfo(name="Interna XMLObject") XMLObject internaRoot
    ){

        String projectTypeP= NGParameter.getProjectTypefromXMLObject(paramRoot);
        String projectTypeI= NGParameter.getProjectTypefromXMLObject(internaRoot);

        if(projectTypeI!=projectTypeP){
            System.err.println("The project types are not identifical (Param for "+projectTypeP+", Interna for "+projectTypeI+")");
        }

        Map<String, XMLObject> params=new HashMap<>();
        if(NGParameter.isParamXMLObject(paramRoot)) {
            params.put(NeuGenConstants.PARAM, paramRoot);
        }else{
            System.err.println("Please input a Param-XMLObject");
        }

        if(NGParameter.isInternaXMLObject(internaRoot)) {
            params.put(NeuGenConstants.INTERNA, internaRoot);
        }else{
            System.err.println("Please input a Interna-XMLObject for "+ projectTypeP+ "net.");
        }
        gen=new NGGenerator(params, projectTypeP);
        gen.run();

        return gen.getNet();
    }

}

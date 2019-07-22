package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGParameter;
import org.neugen.backend.NGProject;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ComponentInfo(name="Load NeuGen-Parameters", category = "NeuGen", description = "...")
public class VRLNeuGenParameterLoad implements Serializable {
    private static final long serialVersionUID=1L;


    @OutputInfo(name="Parameters")
    public Map<String, XMLObject> LoadParameterMap(
            @ParamInfo(name="Project File", style = "open-folder-dialog", options="") File file
    ){
        NGProject project=new NGProject();
        project.setProjectPath(file.getAbsolutePath());
        project.loadParamTree();

        return project.getParamTree();
    }

    @OutputInfo(name="Parameter XMLObject")
    public XMLObject LoadParamXMLObject(
            @ParamInfo(name="Project File", style = "open-folder-dialog", options="") File file
    ){

        return NGProject.loadParam(file);
    }

    public Map<String, XMLObject> CreateParameterMap(
            @ParamInfo(name="Param",  options="") XMLObject paramRoot,
            @ParamInfo(name="Interna",  options="") XMLObject internaRoot
    ){

        String projectTypeP=NGParameter.getProjectTypefromXMLObject(paramRoot);
        String projectTypeI=NGParameter.getProjectTypefromXMLObject(internaRoot);

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

        return params;
    }

}

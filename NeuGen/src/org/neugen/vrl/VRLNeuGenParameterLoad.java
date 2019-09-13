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

@ComponentInfo(name="Parameter Load", category = "NeuGen/Parameter", description = "...")
public class VRLNeuGenParameterLoad implements Serializable {
    private static final long serialVersionUID=1L;


    @OutputInfo(name="Parameter Map")
    public Map<String, XMLObject> LoadParameterMap(
            @ParamInfo(name="Project File", style = "load-folder-dialog", options="") File file
    ){
        NGProject project=new NGProject();
        project.setProjectPath(file.getAbsolutePath());
        project.loadParamTree();

        return project.getParamTree();
    }

    @OutputInfo(name="Parameter XMLObject")
    public XMLObject LoadParamXMLObject(
            @ParamInfo(name="Project File", style = "load-dialog", options="") File file
    ){

        return NGProject.loadParam(file);
    }


}

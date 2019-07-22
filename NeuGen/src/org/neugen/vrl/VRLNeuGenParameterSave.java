package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGProject;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenProject;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

@ComponentInfo(name="Save NeuGen-Parameter", category = "NeuGen", description = "...")
public class VRLNeuGenParameterSave implements Serializable {

    public void saveParameterTree(
            @ParamInfo(name="Parameter Map") Map<String, XMLObject> params,
            @ParamInfo(name="Project File", style = "save-folder-dialog", options="") File file
    ){
        NGProject.saveParamTree(params, file.getAbsolutePath());
    }

    public void saveXMLObject(
            @ParamInfo(name="Parameter Map")XMLObject param,
            @ParamInfo(name="Project File", style = "save-file-dialog", options="") File file
    ){
            String projectPath=file.getAbsolutePath();
            String projectName=projectPath.substring(projectPath.lastIndexOf("/")+1);
            projectPath=projectPath.substring(0,projectPath.lastIndexOf("/"));

            NGProject.saveXMLObject(param, projectPath, projectName);
    }
}

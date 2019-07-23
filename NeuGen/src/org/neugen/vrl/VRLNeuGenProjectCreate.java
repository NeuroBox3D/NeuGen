package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamGroupInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGParameter;
import org.neugen.backend.NGProject;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

@ComponentInfo(name="Create Parameter", category = "NeuGen", description = "...")
public class VRLNeuGenProjectCreate implements Serializable {
    private static final long serialVersionUID=1L;

    private transient NGProject project;
    private transient NGParameter paramChange;
    private transient Map<String, XMLObject> params;
    private transient String projectPath;
    private  transient String projectType;

   @OutputInfo(name="Parameter Map")
    public Map<String, XMLObject> CreateDefaultNeocortexProject(
            @ParamGroupInfo(group="General|true|General")
            @ParamInfo(name="Project File", style = "save-folder-dialog", options="") File file,
            @ParamInfo(name="Force", options="value=true")Boolean force,

            @ParamGroupInfo(group="Param|true|setting in param.neu")
            @ParamInfo(name="Distance Synapse", options="value=2.5")Double dist_synapse,
            @ParamGroupInfo(group="Param")
            @ParamInfo(name="Nparts_density", options="value=0.25")Double n_parts_density,

            @ParamGroupInfo(group="Param;NumberCells|false|Number of Cells")
            @ParamInfo(name="StartPyramidal", options="value=0")Integer nSP,
            @ParamGroupInfo(group="Param;NumberCells")
            @ParamInfo(name="Layer4Stellate", options="value=5")Integer nL4S,
            @ParamGroupInfo(group="Param;NumberCells")
            @ParamInfo(name="Layer23Pyramidal", options="value=3")Integer nL23P,
            @ParamGroupInfo(group="Param;NumberCells")
            @ParamInfo(name="Layer5APyramidal", options="value=2")Integer nL5AP,
            @ParamGroupInfo(group="Param;NumberCells")
            @ParamInfo(name="Layer5BPyramidal", options="value=2")Integer nL5BP
    ){
        this.projectType= NeuGenConstants.NEOCORTEX_PROJECT;//"Neocortex";
        this.projectPath=file.getAbsolutePath();

        project=new NGProject();
        project.setProjectPath(projectPath);
        project.setProjectType(projectType);
        project.createProject(force);

        Map<String, XMLObject> params=project.getParamTree();

        paramChange=new NGParameter(params);

        if(dist_synapse!=2.5)
            paramChange.modifySynapseDistance(dist_synapse.doubleValue());

        if(n_parts_density!=0.25)
            paramChange.modifyNPartsDensity(n_parts_density.doubleValue());


        if(nSP>0)
            paramChange.adjust_number_of_neocortex_neuron(nSP, "starpyramidal");

        if(nL4S!=5)
            paramChange.adjust_number_of_neocortex_neuron(nL4S, "L4stellate");

        if(nL23P!=3)
            paramChange.adjust_number_of_neocortex_neuron(nL23P, "L23pyramidal");

        if(nL5AP!=2)
            paramChange.adjust_number_of_neocortex_neuron(nL5AP,"L5Apyramidal");

        if(nL5BP!=2)
            paramChange.adjust_number_of_neocortex_neuron(nL5BP,"L5Bpyramidal");

        return paramChange.getParamTree();
    }

    public Map<String, XMLObject> CreateDefaultHippocampusProject(
            @ParamGroupInfo(group="General|true|General")
            @ParamInfo(name="Project File", style = "save-folder-dialog", options="") File file,
            @ParamInfo(name="Force", options="value=true")Boolean force,

            @ParamGroupInfo(group="Param|true|setting in param.neu")
            @ParamInfo(name="Distance Synapse", options="value=2.5")Double dist_synapse,
            @ParamGroupInfo(group="Param")
            @ParamInfo(name="Nparts_density", options="value=0.25")Double n_parts_density,

            @ParamGroupInfo(group="Param;NumberCells|false|Number of Cells")
            @ParamInfo(name="CA1Pyramidal", options="value=3")Integer nCA1py,

            @ParamGroupInfo(group="Param;NumberCells;Calretinin|false|Number of Calretinin Cells")
            @ParamInfo(name="oriens",options="value=0") Integer nCRor,
            @ParamGroupInfo(group="Param;NumberCells;Calretinin")
            @ParamInfo(name="pyramidal", options="0") Integer nCRpy,
            @ParamGroupInfo(group="Param;NumberCells;Calretinin")
            @ParamInfo(name="proximal_radiatum", options="value=0") Integer nCRpr,
            @ParamGroupInfo(group="Param;NumberCells;Calretinin")
            @ParamInfo(name="distal_radiatum", options="value=1") Integer nCRdr,

            @ParamGroupInfo(group="Param;NumberCells;Calbindin|false|Number of Calbindin Cells")
            @ParamInfo(name="oriens",options="value=0") Integer nCBor,
            @ParamGroupInfo(group="Param;NumberCells;Calbindin")
            @ParamInfo(name="proximal_radiatum", options="value=1") Integer nCBpr,
            @ParamGroupInfo(group="Param;NumberCells;Calbindin")
            @ParamInfo(name="distal_radiatum", options="value=0") Integer nCBdr,
            @ParamGroupInfo(group="Param;NumberCells;Calbindin")
            @ParamInfo(name="lacunosum/moleculare", options="value=0") Integer nCBla,

            @ParamGroupInfo(group="Param;NumberCells;Cholecystonkinin|false|Number of Cholecystonkinin Cells")
            @ParamInfo(name="pyramidal",options="value=0") Integer nCCKpy,
            @ParamGroupInfo(group="Param;NumberCells;Cholecystonkinin")
            @ParamInfo(name="distal_radiatum", options="value=1") Integer nCCKdr,

            @ParamGroupInfo(group="Param;NumberCells;Parvalbumin|false|Number of Parvalbumin Cells")
            @ParamInfo(name="oriens",options="value=0") Integer nPVor,
            @ParamGroupInfo(group="Param;NumberCells;Parvalbumin")
            @ParamInfo(name="pyramidal", options="value=1") Integer nPVpy,

            @ParamGroupInfo(group="Param;NumberCells")
            @ParamInfo(name="SomatostatinOriens", options="value=2")Integer nSOM
    ){
        this.projectType= NeuGenConstants.HIPPOCAMPUS_PROJECT;
        this.projectPath=file.getAbsolutePath();

        project=new NGProject();
        project.setProjectPath(projectPath);
        project.setProjectType(projectType);
        project.createProject(force);

        Map<String, XMLObject> params=project.getParamTree();

        paramChange=new NGParameter(params);
        if(dist_synapse!=2.5)
            paramChange.modifySynapseDistance(dist_synapse.doubleValue());
        if(n_parts_density!=0.25)
            paramChange.modifyNPartsDensity(n_parts_density.doubleValue());

        if(nCA1py!=3)
            paramChange.adjust_number_of_hippocampus_neuron(nCA1py,"CA1pyramidal","pyramidale");

        if(nCRor>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCRor,"Calretinin(CR)","oriens");
        if(nCRpy>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCRpy,"Calretinin(CR)","pyramidale");
        if(nCRpr>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCRpr,"Calretinin(CR)","proximal_radiatum");
        if(nCRdr!=1)
            paramChange.adjust_number_of_hippocampus_neuron(nCRdr,"Calretinin(CR)","distal_radiatum");

        if(nCBor>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCBor,"Calbindin(CB)","oriens");
        if(nCBpr!=1)
            paramChange.adjust_number_of_hippocampus_neuron(nCBpr,"Calbindin(CB)","proximal_radiatum");
        if(nCBdr>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCBdr,"Calbindin(CB)","distal_radiatum");
        if(nCBla>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCBla,"Calbindin(CB)","lacunosum/moleculare");

        if(nCCKpy>0)
            paramChange.adjust_number_of_hippocampus_neuron(nCCKpy,"Cholecystokinin(CCK)","pyramidale");
        if(nCCKdr!=1)
            paramChange.adjust_number_of_hippocampus_neuron(nCCKdr,"Cholecystokinin(CCK)","distal_radiatum");

        if(nPVor>0)
            paramChange.adjust_number_of_hippocampus_neuron(nPVor,"Parvalbumin(PV)","oriens");
        if(nPVpy!=1)
            paramChange.adjust_number_of_hippocampus_neuron(nPVpy,"Parvalbumin(PV)","pyramidale");

        if(nSOM!=2)
            paramChange.adjust_number_of_hippocampus_neuron(nSOM,"Somatostatin(SOM)","oriens");

        return paramChange.getParamTree();
    }

    public Map<String, XMLObject> CreateDefaultNeuGenProject(
            @ParamInfo(name="Project File", style = "save-folder-dialog", options="") File file,
            @ParamInfo(name="Force", options="value=true")Boolean force,
            @ParamInfo(name="Project Type",style="selection", options="value=[\"Neocortex\", \"Hippocampus\"]")String projectType

    ){
        this.projectType=projectType;
        this.projectPath=file.getAbsolutePath();

        project=new NGProject();
        project.setProjectPath(projectPath);
        project.setProjectType(projectType);
        project.createProject(force);

        return project.getParamTree();
    }


}

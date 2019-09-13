package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.v3d.Shape3DArray;
import eu.mihosoft.vrl.v3d.VGeometry3D;
import eu.mihosoft.vrl.v3d.VGeometry3DAppearance;
import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.neugen.backend.NGNetVisual;
import org.neugen.backend.NGNeuronAppearance;
import org.neugen.backend.NGNeuronVisual;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Section;

import javax.vecmath.Color3f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ComponentInfo(name="Net Visualization", category = "NeuGen/Net", description = "...")
public class VRLNeuGenNetVisual {
    public Shape3DArray NetShape3D(
            @ParamInfo(name="Neuron Net") Net net,
            //@ParamInfo(name="Zoom", style="silent", options="min=0.001;max=10")float zoom,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="Scale", options="value=0.001") Float scale,
            @ParamInfo(name="With Synapse", options="value=true")Boolean withSyn,
            @ParamInfo(name="Synapse Color", style="color-chooser")Color synCol
    ){
        NGNetVisual netVis=new NGNetVisual(net, NGNeuronVisual.VisualMethod.VTA);
        netVis.setScale(0.001f);
        netVis.setScale(scale.floatValue());
        netVis.setSynapseColor(new Color3f(synCol.getRed()/255.0f, synCol.getGreen()/255.0f, synCol.getBlue()/255.0f));
        netVis.run(somaSphere, withSyn);

        Shape3DArray vta=netVis.getNetShape3DArray();
        return vta;
    }

    public Shape3DArray NetShape3DColor(
            @ParamInfo(name="Neuron Net") Net net,
            @ParamInfo(name="Color List") List<NGNeuronAppearance> appList,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="Scale", options="value=0.001") Float scale,
            @ParamInfo(name="With Synapse", options="value=true")Boolean withSyn,
            @ParamInfo(name="Synapse Color", style="color-chooser")Color synCol
            ){

        NGNetVisual netVis=new NGNetVisual(net, NGNeuronVisual.VisualMethod.VTA);
        netVis.setScale(0.001f);
        netVis.setScale(scale.floatValue());

        if(appList!=null) {
            netVis.setNetAppearance(appList);
        }
        netVis.setSynapseColor(new Color3f(synCol.getRed()/255.0f, synCol.getGreen()/255.0f, synCol.getBlue()/255.0f));

        netVis.run(somaSphere, withSyn);

        Shape3DArray vta=netVis.getNetShape3DArray();
        return vta;

    }



    /*public List<NGNeuronAppearance> NeocortexNetColors(
            @ParamInfo(name="L23pyramidal",style="color-chooser") Color p23Col,
            @ParamInfo(name="L4stellate",style="color-chooser") Color s4Col,
            @ParamInfo(name="L5Apyramidal",style="color-chooser") Color p5ACol,
            @ParamInfo(name="L5Bpyramidal",style="color-chooser") Color p5BCol,
            @ParamInfo(name="starpyramidal",style="color-chooser") Color starCol
    ){
        List<NGNeuronAppearance> appList=new ArrayList<>();

        appList.add(0,new NGNeuronAppearance(p23Col));
        appList.add(1,new NGNeuronAppearance(s4Col));

        appList.add(2,new NGNeuronAppearance(p5ACol));
        appList.add(3,new NGNeuronAppearance(p5BCol));

        appList.add(4,new NGNeuronAppearance(starCol));

        return appList;

    }*/

}

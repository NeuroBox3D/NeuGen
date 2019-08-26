package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.v3d.Shape3DArray;
import eu.mihosoft.vrl.v3d.VGeometry3D;
import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.neugen.backend.NGNeuronVisual;
import org.neugen.datastructures.neuron.Neuron;

import java.io.Serializable;

@ComponentInfo(name="Neuron Visualization", category = "NeuGen", description = "...")
public class VRLNeuGenNeuronVisual implements Serializable {
    private static final long serialVersionUID=1L;

    public Shape3DArray NeuronShape3DArray(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Zoom", style="silent", options="min=0.001;max=10")float zoom,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.run(somaSphere);

        Shape3DArray sArray=neuronVis.getShape3DArray();
        return sArray;
    }

    public VGeometry3D NeuronVG(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.run(somaSphere);

        VTriangleArray vta=neuronVis.getVTriangleArray();
        return new VGeometry3D(vta);
    }

    public VGeometry3D NeuronSectionVG(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="Section Index", style="silent", options="min=0;max=6")int secNum
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.run(somaSphere);

        return neuronVis.getVGeometry3D(secNum);
    }





}

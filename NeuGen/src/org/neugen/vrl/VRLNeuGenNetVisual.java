package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.v3d.Shape3DArray;
import eu.mihosoft.vrl.v3d.VGeometry3D;
import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.neugen.backend.NGNetVisual;
import org.neugen.backend.NGNeuronVisual;
import org.neugen.datastructures.Net;

import java.util.List;

@ComponentInfo(name="Neuron Net Visualization", category = "NeuGen", description = "...")
public class VRLNeuGenNetVisual {
    public Shape3DArray NetVG(
            @ParamInfo(name="Neuron Net") Net net,
            @ParamInfo(name="Zoom", style="silent", options="min=0.001;max=10")float zoom,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="With Synapse", options="value=true")Boolean withSyn
    ){
        NGNetVisual netVis=new NGNetVisual(net, NGNeuronVisual.VisualMethod.VTA);
        netVis.run(somaSphere, withSyn);

        Shape3DArray vta=netVis.getNetShape3DArray();
        return vta;
    }

    /*public Shape3DArray NetShape3D(
            @ParamInfo(name="Neuron Net") Net net,
            @ParamInfo(name="Zoom", style="silent", options="min=0.001;max=10")float zoom,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="With Synapse", options="value=true")Boolean withSyn,
    ){

        NGNetVisual netVis=new NGNetVisual(Net, NGNeuronVisual.VisualMethod.VRL);
        netVis.run(somaSphere, withSyn);

        List<VTriangleArray> neuronList=netVis.get

    }*/

}

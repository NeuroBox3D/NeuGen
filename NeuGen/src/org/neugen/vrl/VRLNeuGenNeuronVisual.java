package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamGroupInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.v3d.Shape3DArray;
import eu.mihosoft.vrl.v3d.VGeometry3D;
import eu.mihosoft.vrl.v3d.VGeometry3DAppearance;
import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.neugen.backend.NGNeuronAppearance;
import org.neugen.backend.NGNeuronVisual;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.neuron.Neuron;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ComponentInfo(name="Neuron Visualization", category = "NeuGen", description = "...")
public class VRLNeuGenNeuronVisual implements Serializable {
    private static final long serialVersionUID=1L;

    /*public Shape3D NeuronShape3D(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.LINE);
        neuronVis.run(somaSphere);

        return neuronVis.getShape3D();

    } don't support*/

    public Shape3DArray NeuronShape3DArray(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.run(somaSphere);

        Shape3DArray sArray=neuronVis.getShape3DArray();
        return sArray;
    }

    public Shape3DArray NeuronShape3DArrayColor(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="Colors") List<Color3f> col
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.getNeuronAppearance().setColor(col);
        neuronVis.run(somaSphere);

        //VTriangleArray vta=neuronVis.getVTriangleArray();
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
            @ParamInfo(name="Section Index", style="slider", options="min=0;max=6")Integer secNum,
            @ParamInfo(name="Appearance") VGeometry3DAppearance vgApp
            //@ParamInfo(name="Color")Color col
            ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.getNeuronAppearance().changeColor(vgApp.getSolidColor(), secNum.intValue());
        neuronVis.run(somaSphere);

        return neuronVis.getVGeometry3D(secNum.intValue());
    }


    public List<Color3f> NeuronColorsFromAppearance(
            @ParamGroupInfo(group="Appearance|true|Appearance")
        @ParamInfo(name="Soma ")VGeometry3DAppearance somaApp,//index: Section.SectionType.Soma: 6
        @ParamInfo(name="Myelined Axon ") VGeometry3DAppearance myeApp,//3
        @ParamInfo(name="Non-myelined Axon ") VGeometry3DAppearance nonApp,//4
        @ParamInfo(name="Basal Dendrite ") VGeometry3DAppearance basApp,// BASAL: 0
        @ParamInfo(name="Apical Dendrite ") VGeometry3DAppearance apiApp,//1
        @ParamInfo(name="OBlique Dendrite ") VGeometry3DAppearance oblApp,//2
        @ParamInfo(name="Undefined Part ") VGeometry3DAppearance undApp// 5
    ){

        NGNeuronAppearance app=new NGNeuronAppearance();

        app.changeColor(basApp.getSolidColor(), Section.SectionType.BASAL.getSecNum());
        app.changeColor(apiApp.getSolidColor(), Section.SectionType.APICAL.getSecNum());
        app.changeColor(oblApp.getSolidColor(), Section.SectionType.OBLIQUE.getSecNum());

        app.changeColor(myeApp.getSolidColor(), Section.SectionType.MYELINIZED.getSecNum());
        app.changeColor(nonApp.getSolidColor(), Section.SectionType.NOT_MYELINIZED.getSecNum());
        app.changeColor(undApp.getSolidColor(), Section.SectionType.UNDEFINED.getSecNum());
        app.changeColor(somaApp.getSolidColor(), Section.SectionType.SOMA.getSecNum());

        return app.getNeuronColors();
    }
    /*public TransformGroup NeuronTG(
            @ParamInfo(name="Neuron") Neuron neuron,
            @ParamInfo(name="Spherical Soma", options="value=true")Boolean somaSphere,
            @ParamInfo(name="Visualisation", options="value=line")String visual
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.fromString(visual));
        neuronVis.run(somaSphere);

        return neuronVis.getTransformGroup();

    } don't support!*/






}

package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
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
            @ParamGroupInfo(group="Color|true|Color")
            @ParamInfo(name="Soma", style="color-chooser")Color somaCol,//index: Section.SectionType.Soma: 6
            @ParamInfo(name="Myelined Axon", style="color-chooser")Color myeCol,//3
            @ParamInfo(name="Non-myelined Axon", style="color-chooser")Color nonCol,//4
            @ParamInfo(name="Basal Dendrite", style="color-chooser")Color basCol,// BASAL: 0
            @ParamInfo(name="Apical Dendrite", style="color-chooser")Color apiCol,//1
            @ParamInfo(name="OBlique Dendrite", style="color-chooser")Color oblCol,//2
            @ParamInfo(name="Undefined Part", style="color-chooser")Color undCol// 5
    ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        NGNeuronAppearance app=new NGNeuronAppearance();

        app.changeColor(basCol, Section.SectionType.BASAL.getSecNum());
        app.changeColor(apiCol, Section.SectionType.APICAL.getSecNum());
        app.changeColor(oblCol, Section.SectionType.OBLIQUE.getSecNum());

        app.changeColor(myeCol, Section.SectionType.MYELINIZED.getSecNum());
        app.changeColor(nonCol, Section.SectionType.NOT_MYELINIZED.getSecNum());
        app.changeColor(undCol, Section.SectionType.UNDEFINED.getSecNum());
        app.changeColor(somaCol, Section.SectionType.SOMA.getSecNum());

        List<Color3f> col=app.getNeuronColors();

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
            @ParamInfo(name="Color", style="color-chooser") Color col
            //@ParamInfo(name="Color")Color col
            ){
        NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, NGNeuronVisual.VisualMethod.VTA);
        neuronVis.getNeuronAppearance().changeColor(col, secNum.intValue());
        neuronVis.run(somaSphere);

        return neuronVis.getVGeometry3D(secNum.intValue());
    }

    /*@OutputInfo(name="Neuron Section Colors")
    public NGNeuronAppearance NeuronSectionColors(
            @ParamInfo(name="Soma", style="color-chooser")Color somaCol,//index: Section.SectionType.Soma: 6
            @ParamInfo(name="Myelined Axon", style="color-chooser")Color myeCol,//3
            @ParamInfo(name="Non-myelined Axon", style="color-chooser")Color nonCol,//4
            @ParamInfo(name="Basal Dendrite", style="color-chooser")Color basCol,// BASAL: 0
            @ParamInfo(name="Apical Dendrite", style="color-chooser")Color apiCol,//1
            @ParamInfo(name="OBlique Dendrite", style="color-chooser")Color oblCol,//2
            @ParamInfo(name="Undefined Part", style="color-chooser")Color undCol// 5
    ){

        NGNeuronAppearance app=new NGNeuronAppearance();

        app.changeColor(basCol, Section.SectionType.BASAL.getSecNum());
        app.changeColor(apiCol, Section.SectionType.APICAL.getSecNum());
        app.changeColor(oblCol, Section.SectionType.OBLIQUE.getSecNum());

        app.changeColor(myeCol, Section.SectionType.MYELINIZED.getSecNum());
        app.changeColor(nonCol, Section.SectionType.NOT_MYELINIZED.getSecNum());
        app.changeColor(undCol, Section.SectionType.UNDEFINED.getSecNum());
        app.changeColor(somaCol, Section.SectionType.SOMA.getSecNum());

        return app;
    }*/


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

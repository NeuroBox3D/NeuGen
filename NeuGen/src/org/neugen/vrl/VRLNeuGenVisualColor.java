package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGNeuronAppearance;
import org.neugen.datastructures.NetHippocampus;
import org.neugen.datastructures.NetNeocortex;
import org.neugen.datastructures.Section;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ComponentInfo(name="NeuGen Color", category = "NeuGen", description = "...")
public class VRLNeuGenVisualColor {

    @OutputInfo(name="Neuron Section Colors")
    public NGNeuronAppearance NeuronSectionColors(
            @ParamInfo(name="Soma", style="color-chooser") Color somaCol,//index: Section.SectionType.Soma: 6
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
    }

    @OutputInfo(name="Neuron Color List")
    public List<NGNeuronAppearance> NeocortexNetColors(
            @ParamInfo(name="L4 stellate",style="color-chooser") Color s4Col,
            @ParamInfo(name="L23 pyramidal",style="color-chooser") Color p23Col,
            @ParamInfo(name="L5A pyramidal",style="color-chooser") Color p5ACol,
            @ParamInfo(name="L5B pyramidal",style="color-chooser") Color p5BCol,
            @ParamInfo(name="Starpyramidal",style="color-chooser") Color starCol
    ){
        List<NGNeuronAppearance> appList=new ArrayList<>();

        appList.add(new NGNeuronAppearance(s4Col));
        appList.add(new NGNeuronAppearance(p23Col));

        appList.add(new NGNeuronAppearance(p5ACol));
        appList.add(new NGNeuronAppearance(p5BCol));

        appList.add(new NGNeuronAppearance(starCol));

        return appList;

    }

    @OutputInfo(name="Neuron Color List")
    public List<NGNeuronAppearance> HippocampusNetColors(
            @ParamInfo(name="CA1 pyramidal",style="color-chooser") Color caCol,
            @ParamInfo(name="Callbindin",style="color-chooser") Color cbCol,
            @ParamInfo(name="Calertinin",style="color-chooser") Color crCol,
            @ParamInfo(name="Cholecystokinin",style="color-chooser") Color cckCol,
            @ParamInfo(name="Parvalbumin",style="color-chooser") Color pvCol,
            @ParamInfo(name="Somatosatin",style="color-chooser") Color somCol
    ){
        List<NGNeuronAppearance> appList=new ArrayList<>();

        appList.add(new NGNeuronAppearance(caCol));
        appList.add(new NGNeuronAppearance(cbCol));

        appList.add(new NGNeuronAppearance(crCol));
        appList.add(new NGNeuronAppearance(cckCol));

        appList.add(new NGNeuronAppearance(pvCol));
        appList.add(new NGNeuronAppearance(somCol));

        return appList;

    }

    @OutputInfo(name="Neuron Section Colors List")
    public List<NGNeuronAppearance> NeocortexNetSectionColors(
            @ParamInfo(name="L4 stellate") NGNeuronAppearance s4App,
            @ParamInfo(name="L23 pyramidal") NGNeuronAppearance p23App,
            @ParamInfo(name="L5A pyramidal") NGNeuronAppearance p5AApp,
            @ParamInfo(name="L5B pyramidal") NGNeuronAppearance p5BApp,
            @ParamInfo(name="Starpyramidal") NGNeuronAppearance starApp
    ){
        List<NGNeuronAppearance> appList=new ArrayList<>();

        appList.add(s4App);
        appList.add(p23App);

        appList.add(p5AApp);
        appList.add(p5BApp);

        appList.add(starApp);

        return appList;
    }


    @OutputInfo(name="Neuron Color List")
    public List<NGNeuronAppearance> HippocampusNetColors(
            @ParamInfo(name="CA1 pyramidal") NGNeuronAppearance caApp,
            @ParamInfo(name="Callbindin") NGNeuronAppearance cbApp,
            @ParamInfo(name="Calertinin") NGNeuronAppearance crApp,
            @ParamInfo(name="Cholecystokinin") NGNeuronAppearance cckApp,
            @ParamInfo(name="Parvalbumin") NGNeuronAppearance pvApp,
            @ParamInfo(name="Somatosatin") NGNeuronAppearance somApp
    ){
        List<NGNeuronAppearance> appList=new ArrayList<>();

        appList.add(caApp);
        appList.add(cbApp);

        appList.add(crApp);
        appList.add(cckApp);

        appList.add(pvApp);
        appList.add(somApp);

        return appList;

    }

}

package org.neugen.vrl;


@ComponentInfo(name="NeuGen Color", category = "NeuGen", description = "...")
public class VRLNeuGenVisualColor {

    @OutputInfo(name="Neuron Section Colors")
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
    }


    public List<NGNeuronAppearance> NeocortexNetColors(
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

    }

    public List<NGNeuronAppearance> NeocortexNetSectionColors(
            @ParamInfo(name="L23pyramidal") NGNeuronAppearance p23App,
            @ParamInfo(name="L4stellate") NGNeuronAppearance s4App,
            @ParamInfo(name="L5Apyramidal") NGNeuronAppearance p5AApp,
            @ParamInfo(name="L5Bpyramidal") NGNeuronAppearance p5BApp,
            @ParamInfo(name="starpyramidal") NGNeuronAppearance starApp
    ){
        List<NGNeuronAppearance> appList=new ArrayList<>();

        appList.add(0,p23App);
        appList.add(1,s4App);

        appList.add(2,p5AApp);
        appList.add(3,p5BApp);

        appList.add(4,starApp);

        return appList;
    }


}

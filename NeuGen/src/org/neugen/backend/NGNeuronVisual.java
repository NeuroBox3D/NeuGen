package org.neugen.backend;

//import eu.mihosoft.vrl.ext.com.jhlabs.vecmath.Point3f;
import com.sun.j3d.utils.geometry.Cylinder;
import eu.mihosoft.vrl.v3d.*;
import eu.mihosoft.vrl.v3d.Node;
import org.apache.log4j.Logger;
import org.neugen.datastructures.*;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;
import org.neugen.visual.Utils3D;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NGNeuronVisual {
    /// public static members
    public static final Logger logger = Logger.getLogger(NGNeuronVisual.class.getName());
    private Neuron neuron;
    private VisualMethod visM;
    private float scale;

    /*private List<Color3f> colList; // based on Section.SectionType: 6: SOMA, 3
    private List<Appearance> appList; //based on Section.SectionType:*/
    private NGNeuronAppearance app;

    private Shape3D shape3D; //VisualMethod.LINE
    private List<VTriangleArray> vtaList; //VisualMethod.VTA
    //private List<VGeometry3D> vg3DList;
    private List<TransformGroup> tgList; //VisualMethod.SOLID oder LINE


    //private visualParameter; //soma:emissiveColor, ambientColor, DiffuseColor, SpecularColor, Shininess,
    /*private VTriangleArray vta;
    private TransformGroup tgRoot;*/

    public enum VisualMethod {

        LINE("line"),VTA("vta"), SOLID("solid");//, WIRE("wire");

        private final String value;

        VisualMethod(String value) {
            this.value = value;
        }

        public String getText() {
            return this.value;
        }

        /**
         * @return the Enum representation for the given string.
         * @throws IllegalArgumentException if unknown string.
         */
        public static VisualMethod fromString(String s) throws IllegalArgumentException {
            return Arrays.stream(VisualMethod.values())
                    .filter(v -> v.value.equals(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
        }
    }

    // default visualisation with line
    public NGNeuronVisual(Neuron neuron){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.neuron=neuron;
        this.scale=0.001f;
        /*this.shape3D=new Shape3D();
        this.vta=new VTriangleArray();
        this.tgList=new ArrayList<TransformGroup>();*/
        this.visM=VisualMethod.LINE;
        /*this.colList=initColorList();
        this.appList=initAppearanceList();*/
        this.app=new NGNeuronAppearance();
    }

    public NGNeuronVisual(Neuron neuron, VisualMethod visM){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.neuron=neuron;
        this.scale=0.001f;
        /*this.shape3D=new Shape3D();
        this.vta=new VTriangleArray();
        this.tgList=new ArrayList<TransformGroup>();*/
        this.visM=visM;
        /*this.colList=initColorList();
        this.appList=initAppearanceList();*/
        this.app=new NGNeuronAppearance();
    }

    public void init(){
        switch (visM){
            case LINE:
                this.shape3D=new Shape3D();
                break;
            case VTA:
                this.vtaList=new ArrayList<VTriangleArray>();
                for(int secNum=0; secNum<7;secNum++)
                    this.vtaList.add(secNum, new VTriangleArray());
                //this.vg3DList=new ArrayList<VGeometry3D>();
                break;
            case SOLID:
                this.tgList=new ArrayList<TransformGroup>();
                for(int secNum=0; secNum<7;secNum++)
                    this.tgList.add(secNum,  new TransformGroup());
                break;
        }
    }

    ////////////////////////////////////////////////////////////
    ///// parameters: setting and getting functions
    /////////////////////////////////////////////////////////////
    public void setScale(float scale){
       this.scale=scale;
    }

    public void setVisualMethod(VisualMethod visM){this.visM=visM;}

    public void setVisualMethod(String visM){this.visM=VisualMethod.fromString(visM);}

    public void setAppearance(NGNeuronAppearance app){
        this.app=app;
    }


    ////////////////////////////////////////////////
    //// Visualisation functions:
    //////////////////////////////////////////////

    /**
     * The core function for segment visualisation:
     * used to visualise a segment of soma, axon, dendrites and undefinedSections
     * @param segment
     * @param secNum
     */
    private void segmentVisual(Segment segment, int secNum){
        //System.out.println("Print segment in Section "+secNum+ " with "+visM);
        NGNeuronSegmentVisual segVis=new NGNeuronSegmentVisual(segment);
        //System.out.println("SegmentScale:"+scale);
        segVis.setScale(scale);
        switch(visM){
            case LINE:
                shape3D.addGeometry(segVis.getLineArray(app.getSectionColor(secNum)));
                break;
            case VTA:
                vtaList.get(secNum).add(segVis.getTriangle());
                break;
            case SOLID:
                tgList.add(segVis.getCylinderTG(app.getSectionAppearance(secNum)));
                break;
        }
    }

    /**
     * section visualisation for the section of soma or undefinedSections
     * @param section
     */
    private void sectionVisual(Section section, int secNum){
        //System.out.println("segment type:"+section.getSectionType());
        //int secNum=section.getSectionType().getSecNum(); // Myelinized: 3, unmylinzed: 4
        //System.out.println("Print section:");
        for(Segment segment:section.getSegments()){
            /*System.out.println("SegmentStart"+segment.getStart());
            System.out.println("SegmentEnd"+segment.getEnd());*/
            segmentVisual(segment, secNum);
        }
    }

    /**
     * visualisation from firstSection,
     * which is used in the classes: Axon and Dendrite.
     * @param firstSection
     */
    private void firstSectionVisual(Section firstSection){
        if (firstSection != null) {
            Section.Iterator secIterator=firstSection.getIterator();
            while(secIterator.hasNext()){
                Section section=secIterator.next();
                int secNum=section.getSectionType().getSecNum();
                //System.out.println("Section Number"+secNum);
                sectionVisual(section, secNum);
            }
        }
    }



    //////////////////////////////////////////////////////////////////////////////////////////
    //// using the core functions to visualise soma, axon, dendrites and undefined sections
    //////////////////////////////////////////////////////////////////////////////////////////
    /**
     * visualise soma based on its segments, which are saved in CylindricRepresentant or
     * more details in segmentVisual
     * @param soma
     */
    public void somaSegmentVisual(Cellipsoid soma){
        System.out.println("Print segmental soma:");
        Section sec=soma.getCylindricRepresentant();

        if(sec==null){
            sec=soma.getEllipsoid();
        }

        int secNum=Section.SectionType.SOMA.getSecNum();
        sectionVisual(sec, secNum);
        /*if(visM==VisualMethod.VTA){
            Color col=colList.get(secNum).get();
            VGeometry3D vg=new VGeometry3D(vtaList.get(secNum),col,col,1,false);
            vg3DList.add(secNum, vg);
        }*/
    }

    private Point3f startLocation(Section firstSection){
        Section.Iterator secIterator=firstSection.getIterator();
        if(secIterator.hasNext()){
            Section section=secIterator.next();
            return section.getSegments().get(0).getStart();
        }
        return null;
    }

    private List<Point3f> startLocationList(){
        List<Point3f> startList=new ArrayList<>();
        ////////starting location of axon///////////////////////
        Point3f startLoc=startLocation(neuron.getAxon().getFirstSection());
        if(startLoc!=null)
            startList.add(startLoc);

        ////////starting locations of dendrites/////////////////
        for(Dendrite dendrite:neuron.getDendrites()){
            startLoc=startLocation(dendrite.getFirstSection());
            if(startLoc!=null)
                startList.add(startLoc);
        }

        /////// starting locations of undefinedSections/////////
        for(Section section:neuron.getUndefinedSections()){
            startList.add(section.getSegments().get(0).getStart());
        }

        return startList;
    }

    public void somaSphereVisual(Cellipsoid soma){
        System.out.println("Print spherical soma:");
        float rad=soma.getMeanRadius();
        Point3f loc=soma.getMid();
        //loc.scale(scale);
        NGNeuronNodeVisual nodVis=new NGNeuronNodeVisual(loc, rad);
        nodVis.setScale(scale);

        int secNum=Section.SectionType.SOMA.getSecNum();//6
        switch(visM){
            case LINE:
                List<Point3f> startList=startLocationList();
                for(Point3f loc1:startList){
                    //loc1.scale(scale);
                    shape3D.addGeometry(nodVis.getLineArray(loc1, app.getSectionColor(secNum)));
                }
                break;
            case VTA:
                vtaList.add(secNum, nodVis.getVTriangleArray());
                break;
            case SOLID:
                tgList.add(nodVis.getTransformGroup(app.getSectionAppearance(secNum)));
                break;
        }

    }


    public void axonVisual(){
        System.out.println("Print axon:");
        Axon axon=neuron.getAxon();

        firstSectionVisual(axon.getFirstSection());
    }

    public void dendriteVisual(Dendrite dendrite){
        System.out.println("Print Dendrite:");
        firstSectionVisual(dendrite.getFirstSection());
    }

    ////////////////////////////////////////////////////
    /// public function: run
    //////////////////////////////////////////////////

    public void run(boolean somaSphere){
        init();

        if(somaSphere){
            somaSphereVisual(neuron.getSoma());
        }else {
            somaSegmentVisual(neuron.getSoma());
        }

        axonVisual();

        for(Dendrite dendrite:neuron.getDendrites()){
            dendriteVisual(dendrite);
        }

        if(neuron.getUndefinedSections().size()>0) {
            System.out.println("Print UndefinedSections:");
            for (Section section : neuron.getUndefinedSections()) {
                sectionVisual(section, Section.SectionType.UNDEFINED.getSecNum());
            }
        }


    }





    //////////////////////////////////////////////////
    //// result: getting functions
    ////////////////////////////////////////////////////
    public Shape3D getShape3D(){return shape3D;}

    public VTriangleArray getVTriangleArray_Part(int secNum){return vtaList.get(secNum);}

    public VGeometry3D getVGeometry3D(int secNum){
        Color3f col3f=app.getSectionColor(secNum);
        if(col3f==null){
            col3f= Utils3D.white;
        }

        if(!vtaList.get(secNum).isEmpty()) {
            VGeometry3D vg = new VGeometry3D(vtaList.get(secNum), col3f.get(), col3f.get(), 1, false);
            return vg;
        }
        return null;
    }

    /*private List<VGeometry3D> getVGeometry3DList(){
        List<VGeometry3D> vgList=new ArrayList<>();
        for(int secNum=0; secNum<7;++secNum){
          vgList.add(getVGeometry3D(secNum));
        }
        return vgList;
    }*/

    public Shape3DArray getShape3DArray(){
        Shape3DArray shape3DA=new Shape3DArray();
        for(int secNum=0; secNum<7;++secNum) {
            VGeometry3D vg=getVGeometry3D(secNum);
            if(vg!=null) {
                shape3DA.addAll(vg.generateShape3DArray());
            }
        }
        return shape3DA;
    }

    public VTriangleArray getVTriangleArray(){
        VTriangleArray vta=new VTriangleArray();
        for(VTriangleArray part:vtaList){
             vta.addAll(part);
        }
        return vta;
    }


    /*public List<VTriangleArray> getVTriangleArrayList(){return vtaList;}

    public List<TransformGroup> getTransformGroupList(){return tgList;}*/

    public TransformGroup getTransformGroup(){
        TransformGroup spinner =new TransformGroup();
        spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        switch (visM) {
            case LINE:
                spinner.addChild(shape3D);
                break;
            case SOLID:
                for(TransformGroup tg:tgList){
                    spinner.addChild(tg);
                }
        }

        return spinner;
    }

    public NGNeuronAppearance getNeuronAppearance(){
        return app;
    }


}

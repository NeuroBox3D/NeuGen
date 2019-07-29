package org.neugen.backend;

//import eu.mihosoft.vrl.ext.com.jhlabs.vecmath.Point3f;
import com.sun.j3d.utils.geometry.Cylinder;
import eu.mihosoft.vrl.v3d.Node;
import eu.mihosoft.vrl.v3d.Triangle;
import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.apache.log4j.Logger;
import org.neugen.datastructures.*;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NGNeuronVisual {
    /// public static members
    public static final Logger logger = Logger.getLogger(NGNeuronVisual.class.getName());
    private Neuron neuron;
    private VisualMethod visM;
    private float scale;

    private List<Color3f> colList; // based on Section.SectionType: 6: SOMA, 3
    private List<Appearance> appList; //based on Section.SectionType:

    private Shape3D shape3D;
    private VTriangleArray vta;
    private List<TransformGroup> tgList;


    //private visualParameter; //soma:emissiveColor, ambientColor, DiffuseColor, SpecularColor, Shininess,
    /*private VTriangleArray vta;
    private TransformGroup tgRoot;*/

    public enum VisualMethod {

        LINE("line"),VRL("vrl"), SOLID("solid");//, WIRE("wire");

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

    public NGNeuronVisual(Neuron neuron){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.neuron=neuron;
        this.scale=0.001f;
        /*this.shape3D=new Shape3D();
        this.vta=new VTriangleArray();
        this.tgList=new ArrayList<TransformGroup>();*/
        this.visM=VisualMethod.LINE;
        this.colList=initColorList();
        this.appList=initAppearanceList();
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
        this.colList=initColorList();
        this.appList=initAppearanceList();
    }

    public void init(){
        switch (visM){
            case LINE:
                this.shape3D=new Shape3D();
                break;
            case VRL:
                this.vta=new VTriangleArray();
                break;
            case SOLID:
                this.tgList=new ArrayList<TransformGroup>();
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

    public static List<Color3f> initColorList(){
        List<Color3f> colList=new ArrayList<>();
        for(int i=0; i<7;++i){ //Section.SectionType: 0-6
            colList.add(i,null);
        }
        return colList;
    }

    public static List<Appearance> initAppearanceList(){
        List<Appearance> appList=new ArrayList<>();
        for(int i=0; i<7;++i){ //Section.SectionType: 0-6
            appList.add(i,null);
        }
        return appList;
    }

    public void changeColor(Color3f color, int ind){
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }

        colList.set(ind, color);
    }

    public void changeAppearance(Appearance app, int ind){ // ind<=6
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }
        appList.set(ind, app);
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
        NGNeuronSegmentVisual segVis=new NGNeuronSegmentVisual(segment);
        segVis.setScale(scale);
        switch(visM){
            case LINE:
                shape3D.addGeometry(segVis.getLineArray(colList.get(secNum)));
                break;
            case VRL:
                vta.add(segVis.getTriangle());
                break;
            case SOLID:
                tgList.add(segVis.getCylinderTG(appList.get(secNum)));
                break;
        }
    }

    /**
     * section visualisation for the section of soma or undefinedSections
     * @param section
     */
    private void sectionVisual(Section section){
        int secNum=section.getSectionType().getSecNum(); // Myelinized: 3, unmylinzed: 4
        for(Segment segment:section.getSegments()){
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
                sectionVisual(section);
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
        System.out.println("Print soma:");
        Section sec=soma.getCylindricRepresentant();
        if(sec==null){
            sec=soma.getEllipsoid();
        }
        sectionVisual(sec);
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
        System.out.println("Print soma:");
        float rad=soma.getMeanRadius();
        Point3f loc=soma.getMid();
        loc.scale(scale);
        NGNeuronNodeVisual nodVis=new NGNeuronNodeVisual(loc, rad*scale);

        int secNum=Section.SectionType.SOMA.getSecNum();//6
        switch(visM){
            case LINE:
                List<Point3f> startList=startLocationList();
                for(Point3f loc1:startList){
                    loc1.scale(scale);
                    shape3D.addGeometry(nodVis.getLineArray(loc1, colList.get(secNum)));
                }
                break;
            case VRL:
                vta.addAll(nodVis.getVTriangleArray());
                break;
            case SOLID:
                tgList.add(nodVis.getTransformGroup(appList.get(secNum)));
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
                sectionVisual(section);
            }
        }

    }

    //////////////////////////////////////////////////
    //// result: getting functions
    ////////////////////////////////////////////////////
    public Shape3D getShape3D(){return shape3D;}

    public VTriangleArray getVTriangleArray(){return vta;}

    public List<TransformGroup> getTransformGroupList(){return tgList;}

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


}

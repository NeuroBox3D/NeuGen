package org.neugen.backend;

import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.neugen.datastructures.Cons;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Segment;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.List;

public class NGNetVisual {
    private Net net;
    private float scale;
    private NGNeuronVisual.VisualMethod visM;
    private List<Color3f> colList; // based on Section.SectionType: 0-6 Neuron + 7: Synapse
    private List<Appearance> appList; //based on Section.SectionType: 0-6 Neuron +7: Synapse

    private Shape3D shape3DSyn;
    private VTriangleArray vtaSyn;
    private List<TransformGroup> tgListSyn;

    private List<VTriangleArray> vtaNeuron;
    private List<TransformGroup> tgListNeuron;

    public NGNetVisual(Net net){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.net=net;
        this.scale=0.001f;
        this.visM= NGNeuronVisual.VisualMethod.LINE;

        this.colList=NGNeuronVisual.initColorList();
        colList.add(7,null);
        this.appList=NGNeuronVisual.initAppearanceList();
        appList.add(7, null);
    }

    public NGNetVisual(Net net, NGNeuronVisual.VisualMethod visM){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.net=net;
        this.scale=0.001f;
        this.visM=visM;

        this.colList=NGNeuronVisual.initColorList();
        colList.add(7,null);
        this.appList=NGNeuronVisual.initAppearanceList();
        appList.add(7, null);
    }

    public void init(){
       initSynpase();
       if(visM==NGNeuronVisual.VisualMethod.VRL){
           this.vtaNeuron=new ArrayList<>();
       }else{
           this.tgListNeuron=new ArrayList<>();
       }

    }

    public void initSynpase(){
        switch(visM){
            case LINE:
                this.shape3DSyn=new Shape3D();
                break;
            case VRL:
                this.vtaSyn=new VTriangleArray();
                break;
            case SOLID:
                this.tgListSyn=new ArrayList<TransformGroup>();
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ///// parameter: setting and getting functions
    ////////////////////////////////////////////////////////////////////////
    public void setVisualMethod(NGNeuronVisual.VisualMethod visM){this.visM=visM;}

    public void setVisualMethod(String visM){this.visM= NGNeuronVisual.VisualMethod.fromString(visM);}

    public void changeColor(Color3f color, int ind){
        if(ind>7){
            System.err.println("The index must be equal to or smaller than 7. Please check Section.SectionType to ensure the correspanding section index and 7 for synapse.");
        }

        colList.set(ind, color);
    }

    public void changeAppearance(Appearance app, int ind){ // ind<=6
        if(ind>7){
            System.err.println("The index must be equal to or smaller than 7. Please check Section.SectionType to ensure the correspanding section index and 7 for synapse.");
        }
        appList.set(ind, app);
    }

    ///////////////////////
    /// core functions:
    ///////////////////////


    /**
     * synapse visualisation
     * @param synapse
     */
    public void synapseVisual(Cons synapse){
        if(synapse.getNeuron1()!=null) {
            System.out.println("print synapse");
            Segment axSeg = synapse.getNeuron1AxSegment();
            Segment denSeg = synapse.getNeuron2DenSectionSegment();

            //System.out.println("synpase center"+center(axSeg));
            Point3f axSegCenter = axSeg.getCenter();  //.getEnd();
            axSegCenter.scale(scale);
            Point3f denSegCenter = denSeg.getCenter();
            denSegCenter.scale(scale);

            Point3f center = new Point3f();
            center.add(axSegCenter, denSegCenter);
            center.scale(0.5f);

            float rad = scale * 3.0f;

            NGNeuronNodeVisual nodVis = new NGNeuronNodeVisual(center, rad);
            switch (visM) {
                case LINE:
                    nodVis.setLocation(axSegCenter);
                    shape3DSyn.addGeometry(nodVis.getLineArray(denSegCenter, colList.get(7)));
                    break;
                case VRL:
                    vtaSyn.addAll(nodVis.getVTriangleArray());
                    break;
                case SOLID:
                    tgListSyn.add(nodVis.getTransformGroup(appList.get(7)));
                    break;
            }
        }
    }

    /**
     *
     * @param somaSphere
     * @param withSyn
     */
    public void run(boolean somaSphere, boolean withSyn){
        init();

        /////////////print Neurons////////////////
        for(Neuron neuron:net.getNeuronList()){
            NGNeuronVisual neuronVis=new NGNeuronVisual(neuron, visM);
            neuronVis.run(somaSphere);
            if(visM== NGNeuronVisual.VisualMethod.VRL){
                vtaNeuron.add(neuronVis.getVTriangleArray());
            }else{
                tgListNeuron.add(neuronVis.getTransformGroup());
            }
        }

        if(withSyn) {
            /////////print Synpases////////////////////
            runSynapse();
        }
    }

    public void runSynapse(){
        initSynpase();
        for (Cons synpase : net.getSynapseList()) {
            synapseVisual(synpase);
        }
    }


    ////////////////////////////////////////////////////////////
    ///// results: getting functions
    //////////////////////////////////////////////////////////
    public TransformGroup getNeuronTG(int index){
        return tgListNeuron.get(index);
    }

    public VTriangleArray getNeuronVTA(int index){
        return vtaNeuron.get(index);
    }

    public VTriangleArray getSynapseVTA(){
        return vtaSyn;
    }

    public Shape3D getSynapseS3D(){
        return shape3DSyn;
    }

    public List<TransformGroup> getSynapseTG(){
        return tgListSyn;
    }

    public VTriangleArray getNetVTA(){
        VTriangleArray sum=new VTriangleArray();
        for(VTriangleArray vta:vtaNeuron){
            sum.addAll(vta);
        }

        if(!vtaSyn.isEmpty()) {
            sum.addAll(vtaSyn);
        }
        return sum;
    }

    public TransformGroup getNetTG(){
        TransformGroup spinner =new TransformGroup();
        spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        for(TransformGroup tg:tgListNeuron) {
            spinner.addChild(tg);
        }

        switch (visM) {
            case LINE:
                if (shape3DSyn != null) {
                    spinner.addChild(shape3DSyn);
                }
                break;
            case SOLID:
                if(tgListSyn.size()>0) {
                    for (TransformGroup tg : tgListSyn) {
                        spinner.addChild(tg);
                    }
                }
        }

        return spinner;
    }

}

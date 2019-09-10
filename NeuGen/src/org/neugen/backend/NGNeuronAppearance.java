package org.neugen.backend;

import eu.mihosoft.vrl.v3d.VGeometry3DAppearance;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.swing.text.AttributeSet;
import javax.vecmath.Color3f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NGNeuronAppearance {

    private List<Color3f> colList; //index: Section.SectionType
    private List<Appearance> appList;

    private Color3f color3fFromColor(Color col){
        Color3f col3f=new Color3f(col.getRed()/255.0f, col.getGreen()/255.0f, col.getBlue()/255.0f);
        return col3f;
    }

    private Appearance appearanceFromVGApp(VGeometry3DAppearance vgApp){
        Appearance app=new Appearance();
        ColoringAttributes ca=new ColoringAttributes(color3fFromColor(vgApp.getSolidColor()), ColoringAttributes.NICEST);
        app.setColoringAttributes(ca);

        return app;
    }

    private Appearance appearanceFromColor3f(Color3f color){
        Appearance app=new Appearance();
        ColoringAttributes ca=new ColoringAttributes(color, ColoringAttributes.NICEST);
        app.setColoringAttributes(ca);

        return app;
    }


    public NGNeuronAppearance(){
        this.colList=initColorList(null);
        this.appList=initAppearanceList(null);
    }

    public NGNeuronAppearance(Color col){
        Color3f col3f=color3fFromColor(col);
        this.colList=initColorList(col3f);
        this.appList=initAppearanceList(appearanceFromColor3f(col3f));
    }


    public static List<Color3f> initColorList(Color3f col){
        List<Color3f> colList=new ArrayList<>();
        for(int i=0; i<7;++i){ //Section.SectionType: 0-6
            colList.add(i,col);
        }
        return colList;
    }

    public static List<Appearance> initAppearanceList(Appearance app){
        List<Appearance> appList=new ArrayList<>();
        for(int i=0; i<7;++i){ //Section.SectionType: 0-6
            appList.add(i,app);
        }
        return appList;
    }
    //////////////////////////////////////////////////////////////////////////////
    //// Setting and Changing
    //////////////////////////////////////////////////////////////////////////

    public void setColor(List<Color3f> colList){
        this.colList=colList;
    }

    public void setAppearance(List<Appearance> appList){
        this.appList=appList;
    }

    public void changeColor(Color color, int ind){
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }

        colList.set(ind, color3fFromColor(color));
    }

    public void changeColor(Color3f color, int ind){
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }

        colList.set(ind,color);
    }

    public void changeAppearance(VGeometry3DAppearance vgApp, int ind){ // ind<=6
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }
        appList.set(ind, appearanceFromVGApp(vgApp));
    }

    public void changeAppearance(Appearance app, int ind){ // ind<=6
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }
        appList.set(ind, app);
    }

    public void setUniColor(Color col){
        for(int i=0; i<7;++i){ //Section.SectionType: 0-6
            changeColor(col,i);
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //// Getting
    ////////////////////////////////////////////////////////////////

    public List<Color3f> getNeuronColors(){
        return colList;
    }

    public List<Appearance> getNeuronAppearances(){
        return appList;
    }


    public Color3f  getSectionColor(int ind){
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }
        return colList.get(ind);
    }

    public Appearance getSectionAppearance(int ind){
        if(ind>6){
            System.err.println("The index must be equal to or smaller than 6. Please check Section.SectionType to ensure the correspanding section index.");
        }

        return appList.get(ind);
    }


}

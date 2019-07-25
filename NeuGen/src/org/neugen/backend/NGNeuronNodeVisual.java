package org.neugen.backend;

public class NGNeuronNodeVisual {
    private Point3f loc;
    private float radius;
    private float scale;

    public NGNeuronNodeVisual(Point3f loc, float radius){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.loc=loc;
        this.radius=radius;
        this.scale==0.0001f;
    }

    public void setScale(float scale){this.scale=scale;}

    public float getScale(){return scale;}

    /

}

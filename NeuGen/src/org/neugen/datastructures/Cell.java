package org.neugen.datastructures;

import javax.vecmath.Point3f;

public interface Cell {

    public String getName();

    public void setName(String name);

    public String getType();

    public Cellipsoid getSoma();

    public void setSoma(Point3f somaMid, float somaRadius);
}

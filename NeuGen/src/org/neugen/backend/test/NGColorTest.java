package org.neugen.backend.test;

import org.neugen.backend.NGNeuronAppearance;

import javax.media.j3d.Appearance;
import javax.vecmath.Color3f;
import java.awt.*;

public class NGColorTest {
    public static void main(String... args) {
        NGNeuronAppearance app1=new NGNeuronAppearance();
      app1.changeAppearance(new Appearance(),1);
        System.out.println(app1.getNeuronColors().get(1));
    }
}

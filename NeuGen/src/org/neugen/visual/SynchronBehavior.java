package org.neugen.visual;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnBehaviorPost;

/**
 *
 * @author Sergei Wolf
 */
public class SynchronBehavior extends Behavior {

    private WakeupCriterion criterion;
    private final int MOVE = 1;
    private TransformGroup transformGroupSource, transfornGroupTarget;
    private Transform3D trans = new Transform3D();

    public TransformGroup getTransformGroupSource() {
        return transformGroupSource;
    }

    public TransformGroup getTransfornGroupTarget() {
        return transfornGroupTarget;
    }

    public void setTransfornGroupTarget(TransformGroup transfornGroupTarget) {
        this.transfornGroupTarget = transfornGroupTarget;
    }

    public SynchronBehavior(TransformGroup source, TransformGroup target) {
        transformGroupSource = source;
        transfornGroupTarget = target;
    }

    @Override
    public void initialize() {
        criterion = new WakeupOnBehaviorPost(this, MOVE);
        wakeupOn(criterion);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        transformGroupSource.getTransform(trans);
        if (transfornGroupTarget != null) {
            transfornGroupTarget.setTransform(trans);
        } 
        wakeupOn(criterion);
    }

    public void move() {
        //System.out.println("move");
        postId(MOVE);
    }
}

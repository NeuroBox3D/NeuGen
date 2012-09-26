/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ObjectInfo;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import org.neugen.datastructures.VolumeOfVoxels;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ObjectInfo(serializeParam = false)
public class VisualizationContext {

    private BranchGroup branchGroup;
    private VolumeOfVoxels volumeOfVoxels;
    private float scale;
    private float scaleZ;

    public VisualizationContext(BranchGroup branchGroup,
            VolumeOfVoxels volumeOfVoxels, float scale, float scaleZ) {

        this.branchGroup = branchGroup;
        this.volumeOfVoxels = volumeOfVoxels;
        this.scale = scale;
        this.scaleZ = scaleZ;
    }

    /**
     * @return the branchGroup
     */
    public BranchGroup getBranchGroup() {
        return branchGroup;
    }

    /**
     * @return the volumeOfVoxels
     */
    public VolumeOfVoxels getVolumeOfVoxels() {
        return volumeOfVoxels;
    }

    /**
     * @return the scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * @return the scaleZ
     */
    public float getScaleZ() {
        return scaleZ;
    }
}
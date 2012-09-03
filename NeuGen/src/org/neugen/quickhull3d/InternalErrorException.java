package org.neugen.quickhull3d;

/**
 * Exception thrown when QuickHull3D encounters an internal error.
 */
@SuppressWarnings("serial")
public class InternalErrorException extends RuntimeException {

    public InternalErrorException(String msg) {
        super(msg);
    }
}

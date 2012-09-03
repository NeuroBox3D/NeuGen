package org.neugen.visual;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Sergei
 */
public class CanvasSynchronListener {

    private SynchronBehavior synchronBehavior;
    private CanvasMouseListener mouseListener;
    private CanvasMouseWheelListener wheelListener;
    private CanvasKeyListener keyListener;

    public CanvasSynchronListener(SynchronBehavior synBeh) {
        this.synchronBehavior = synBeh;
        this.mouseListener = new CanvasMouseListener();
        this.wheelListener = new CanvasMouseWheelListener();
        this.keyListener = new CanvasKeyListener();
    }

    public CanvasMouseListener getMouseListener() {
        return mouseListener;
    }

    public CanvasMouseWheelListener getMouseWheelListener() {
        return wheelListener;
    }

    public CanvasKeyListener getKeyListener() {
        return keyListener;
    }

    public class CanvasKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            synchronBehavior.move();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            synchronBehavior.move();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            synchronBehavior.move();
        }

    }

    public class CanvasMouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            synchronBehavior.move();
        }

    }

    public class CanvasMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("mouse pressed");
            synchronBehavior.move();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            synchronBehavior.move();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            synchronBehavior.move();
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

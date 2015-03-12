/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */
/*
 * MovieAdapter.java
 *
 * Created on June 14, 2006, 11:42 AM
 *
 */
package org.neugen.makemovie;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.Dimension;
import javax.imageio.ImageIO;

import javax.media.*;
import javax.media.control.*;
import javax.media.j3d.Alpha;
import javax.media.j3d.Canvas3D;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.datasink.*;
import javax.media.format.VideoFormat;


/**
 * This program takes a list of JPEG image files and convert them into
 * a QuickTime movie Or an avi movie.
 */
public class MovieAdapter implements ControllerListener, DataSinkListener {
    
    private boolean doIt(int width, int height, int frameRate, MediaLocator outML) {
        ImageDataSource ids = new ImageDataSource(width, height, frameRate);
        
        Processor p;
        
        try {
            System.err.println("- create processor for the image datasource ...");
            p = Manager.createProcessor(ids);
        } catch (Exception e) {
            System.err.println("Yikes! Cannot create a processor from the data source.");
            return false;
        }
        
        p.addControllerListener(this);
        
// Put the Processor into configured state so we can set
// some processing options on the processor.
        p.configure();
        if (!waitForState(p, p.Configured)) {
            System.err.println("Failed to configure the processor.");
            return false;
        }
        
// Set the output content descriptor to QuickTime.
        p.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.MSVIDEO));
        
// Query for the processor for supported formats.
// Then set it on the processor.
        TrackControl tcs[] = p.getTrackControls();
        Format f[] = tcs[0].getSupportedFormats();
        if (f == null || f.length <= 0) {
            System.err.println("The mux does not support the input format: " + tcs[0].getFormat());
            return false;
        }
        
        tcs[0].setFormat(f[0]);
        
        System.err.println("Setting the track format to: " + f[0]);
        
// We are done with programming the processor. Let's just
// realize it.
        p.realize();
        if (!waitForState(p, p.Realized)) {
            System.err.println("Failed to realize the processor.");
            return false;
        }
        
// Now, we'll need to create a DataSink.
        DataSink dsink;
        if ((dsink = createDataSink(p, outML)) == null) {
            System.err.println("Failed to create a DataSink for the given output MediaLocator: " + outML);
            return false;
        }
        
        dsink.addDataSinkListener(this);
        fileDone = false;
        
        System.err.println("start processing...");
        
// OK, we can now start the actual transcoding.
        try {
            p.start();
            dsink.start();
        } catch (IOException e) {
            System.err.println("IO error during processing");
            return false;
        }
        
// Wait for EndOfStream event.
        waitForFileDone();
        
// Cleanup.
        try {
            dsink.close();
        } catch (Exception e) {}
        p.removeControllerListener(this);
        
        System.err.println("...done processing.");
        
        return true;
    }
    
    
    /**
     * Create the DataSink.
     */
    private DataSink createDataSink(Processor p, MediaLocator outML) {
        
        DataSource ds;
        
        if ((ds = p.getDataOutput()) == null) {
            System.err.println("Something is really wrong: the processor does not have an output DataSource");
            return null;
        }
        
        DataSink dsink;
        
        try {
            System.err.println("- create DataSink for: " + outML);
            dsink = Manager.createDataSink(ds, outML);
            dsink.open();
        } catch (Exception e) {
            System.err.println("Cannot create the DataSink: " + e);
            return null;
        }
        
        return dsink;
    }
    
    
    private Object waitSync = new Object();
    private boolean stateTransitionOK = true;
    
    /**
     * Block until the processor has transitioned to the given state.
     * Return false if the transition failed.
     */
    private boolean waitForState(Processor p, int state) {
        synchronized (waitSync) {
            try {
                while (p.getState() < state && stateTransitionOK)
                    waitSync.wait();
            } catch (Exception e) {}
        }
        return stateTransitionOK;
    }
    
    
    /**
     * Controller Listener.
     */
    public void controllerUpdate(ControllerEvent evt) {
        
        if (evt instanceof ConfigureCompleteEvent ||
                evt instanceof RealizeCompleteEvent ||
                evt instanceof PrefetchCompleteEvent) {
            synchronized (waitSync) {
                stateTransitionOK = true;
                waitSync.notifyAll();
            }
        } else if (evt instanceof ResourceUnavailableEvent) {
            synchronized (waitSync) {
                stateTransitionOK = false;
                waitSync.notifyAll();
            }
        } else if (evt instanceof EndOfMediaEvent) {
            evt.getSourceController().stop();
            evt.getSourceController().close();
        }
    }
    
    
    private Object waitFileSync = new Object();
    private boolean fileDone = false;
    private boolean fileSuccess = true;
    
    /**
     * Block until file writing is done.
     */
    private boolean waitForFileDone() {
        synchronized (waitFileSync) {
            try {
                while (!fileDone)
                    waitFileSync.wait();
            } catch (Exception e) {}
        }
        return fileSuccess;
    }
    
    
    /**
     * Event handler for the file writer.
     */
    public void dataSinkUpdate(DataSinkEvent evt) {
        
        if (evt instanceof EndOfStreamEvent) {
            synchronized (waitFileSync) {
                fileDone = true;
                waitFileSync.notifyAll();
            }
        } else if (evt instanceof DataSinkErrorEvent) {
            synchronized (waitFileSync) {
                fileDone = true;
                fileSuccess = false;
                waitFileSync.notifyAll();
            }
        }
    }
    
    
    public static void main(String args[]) throws Exception{
//jpegCreator.main(null);
//if (args.length == 0)
// prUsage();
        
// Parse the arguments.
        int i = 0;
        int width = -1, height = -1, frameRate = 1;
        Vector inputFiles = new Vector();
        String outputURL = null;
        
        width=128;
        height=128;
        outputURL="test.avi";
        
// Generate the output media locators.
        MediaLocator oml;
        
        if ((oml = createMediaLocator(outputURL)) == null) {
            System.err.println("Cannot build media locator from: " + outputURL);
            System.exit(0);
        }
        
        MovieAdapter imageToMovie = new MovieAdapter();
        imageToMovie.doIt(width, height, frameRate, oml);
        
        System.exit(0);
    }
    
    static void prUsage() {
        System.err.println("Usage: java JpegImagesToMovie -w <width> -h <height> -f <frame rate> -o <output URL> <input JPEG file 1> <input JPEG file 2> ...");
        System.exit(-1);
    }
    
    /**
     * Create a media locator from the given string.
     */
    private static MediaLocator createMediaLocator(String url) {
        
        MediaLocator ml;
        
        if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null)
            return ml;
        
        if (url.startsWith(File.separator)) {
            if ((ml = new MediaLocator("file:" + url)) != null)
                return ml;
        } else {
            String file = "file:" + System.getProperty("user.dir") + File.separator + url;
            if ((ml = new MediaLocator(file)) != null)
                return ml;
        }
        
        return null;
    }
    
    
///////////////////////////////////////////////
//
// Inner classes.
///////////////////////////////////////////////
    
    
    /**
     * A DataSource to read from a list of JPEG image files and
     * turn that into a stream of JMF buffers.
     * The DataSource is not seekable or positionable.
     */
    private class ImageDataSource extends PullBufferDataSource {
        
        private ImageSourceStream streams[];
        
        ImageDataSource(int width, int height, int frameRate) {
            streams = new ImageSourceStream[1];
            streams[0] = new ImageSourceStream(width, height, frameRate);
        }
        
        public void setLocator(MediaLocator source) {
        }
        
        public MediaLocator getLocator() {
            return null;
        }
        
        /**
         * Content type is of RAW since we are sending buffers of video
         * frames without a container format.
         */
        public String getContentType() {
            return ContentDescriptor.RAW;
        }
        
        public void connect() {
        }
        
        public void disconnect() {
        }
        
        public void start() {
        }
        
        public void stop() {
        }
        
        /**
         * Return the ImageSourceStreams.
         */
        public PullBufferStream[] getStreams() {
            return streams;
        }
        
        /**
         * We could have derived the duration from the number of
         * frames and frame rate. But for the purpose of this program,
         * it's not necessary.
         */
        public Time getDuration() {
            System.out.println("dur is "+streams[0].nextImage);
//return new Time(1000000000);
            return DURATION_UNKNOWN;
        }
        
        public Object[] getControls() {
            return new Object[0];
        }
        
        public Object getControl(String type) {
            return null;
        }
    }
    
    
    /**
     * The source stream to go along with ImageDataSource (also capable to generate avi movies).
     */
    class ImageSourceStream implements PullBufferStream {
        
        final int width, height;
        final VideoFormat format;
        
        int nextImage = 0;	// index of the next image to be read.
        boolean ended = false;
        float frameRate;
        long seqNo = 0;
        
        public ImageSourceStream(int width, int height, int frameRate) {
            this.width = width;
            this.height = height;
            this.frameRate = (float) frameRate;
            
            final int rMask = 0x00ff0000;
            final int gMask = 0x0000FF00;
            final int bMask = 0x000000ff;
            
            format = new javax.media.format.RGBFormat(new Dimension(width,height),Format.NOT_SPECIFIED,
                    Format.intArray,frameRate,32,rMask,gMask,bMask);
        }
        
        /**
         * We should never need to block assuming data are read from files.
         */
        public boolean willReadBlock() {
            return false;
        }
        
        /**
         * This is called from the Processor to read a frame worth
         * of video data.
         */
        public void read(Buffer buf) throws IOException {
            
// Check if we've finished all the frames.
            if (nextImage >= 100) {
// We are done. Set EndOfMedia.
                System.err.println("Done reading all images.");
                buf.setEOM(true);
                buf.setOffset(0);
                buf.setLength(0);
                ended = true;
                return;
            }
            
            nextImage++;
            
            int data[] = null;
            
// Check the input buffer type & size.
            
            if (buf.getData() instanceof int[])
                data = (int[])buf.getData();
            
// Check to see the given buffer is big enough for the frame.
            if (data == null || data.length < width*height) {
                data = new int[width*height];
                buf.setData(data);
            }
            
            java.awt.Color clr=java.awt.Color.red;
            if(nextImage>30)clr=java.awt.Color.GREEN;
            if(nextImage>60)clr=java.awt.Color.BLUE;
            for(int i=0;i<width*height;i++){
                data[i] = clr.getRGB();
            }
            
            buf.setOffset(0);
            buf.setLength(width*height);
            buf.setFormat(format);
            buf.setFlags(buf.getFlags() | buf.FLAG_KEY_FRAME);
            
            long time = (long) (((float) seqNo) * (1000.0f / frameRate) * 1000000.0f);
            buf.setTimeStamp(time);
            buf.setSequenceNumber(seqNo++);
        }
        
        /**
         * Return the format of each video frame. That will be JPEG.
         */
        public Format getFormat() {
            return format;
        }
        
        public ContentDescriptor getContentDescriptor() {
            return new ContentDescriptor(ContentDescriptor.RAW);
        }
        
        public long getContentLength() {
            return 0;
        }
        
        public boolean endOfStream() {
            return ended;
        }
        
        public Object[] getControls() {
            return new Object[0];
        }
        
        public Object getControl(String type) {
            return null;
        }
    }
}
package org.neugen.makemovie;
/*
 * @(#)JpegImagesToMovie.java	1.3 01/03/13
 *
 * Copyright (c) 1999-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * This file has been extensively modified by John Swapceinski.
 *
 * Adapted fot NeuDV by J P Eberhard, June 23, 2006.
 */

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.Dimension;
import java.awt.Image;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;

import javax.media.*;
import javax.media.control.*;
import javax.media.j3d.Alpha;
import javax.media.j3d.Canvas3D;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.datasink.*;
import javax.media.format.VideoFormat;
import javax.media.util.ImageToBuffer;
import org.apache.log4j.Logger;
import org.neugen.gui.NeuGenView;

/**
 * This class creates QuickTime movies (.mov) or AVIs (.avi) out of a list of JPEG files or out
 * of an array of java.awt.Image objects.
 */
public class MovieMaker implements ControllerListener, DataSinkListener {

    private final Object waitSync = new Object();
    private final Object waitFileSync = new Object();
    private final Processor processor;
    private final MediaLocator outML;
    private boolean stateTransitionOK = true;
    private boolean fileDone = false;
    private boolean fileSuccess = true;
    private NeuGenView ngView;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(MovieMaker.class.getName());

    public MovieMaker(int width, int height, int frameRate, File outputFile, File[] jpegFiles) throws Exception {
        this(outputFile, new ImageDataSource(width, height, frameRate, jpegFiles));
        ngView = NeuGenView.getInstance();
    }

    public MovieMaker(int width, int height, int frameRate, File outputFile, Image[] images) throws Exception {
        this(outputFile, new ImageDataSource(width, height, frameRate, images));
        ngView = NeuGenView.getInstance();
    }

    private MovieMaker(File outputFile, ImageDataSource ids) throws Exception {
        this.processor = createProcessor(outputFile, ids);
        this.outML = new MediaLocator(outputFile.toURL());
        ngView = NeuGenView.getInstance();
    }

    private Processor createProcessor(File outputFile, ImageDataSource ids) throws Exception {
        NeuGenView.getInstance().outPrintln("- create processor for the image datasource ...");
        //System.out.println("- create processor for the image datasource ...");
        Processor p = Manager.createProcessor(ids);

        p.addControllerListener(this);

        // Put the Processor into configured state so we can set
        // some processing options on the processor.
        p.configure();
        if (!waitForState(p, p.Configured)) {
            logger.error("Failed to configure the processor.", new IOException("Failed to configure the processor."));
        }

        if (outputFile.getName().toLowerCase().endsWith(".mov")) {
            // Set the output content descriptor to QuickTime.
            p.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.QUICKTIME));
        } else if (outputFile.getName().toLowerCase().endsWith(".avi")) {
            // Set the output content descriptor to AVI (MSVIDEO).
            p.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.MSVIDEO));
        } else {
            logger.error("unsupported file extension: " + outputFile.getName(),
                    new IllegalArgumentException("unsupported file extension: " + outputFile.getName()));
        }
        return p;
    }

    public void makeMovie() throws Exception {
        // Query for the processor for supported formats.
        // Then set it on the processor.
        TrackControl[] tcs = processor.getTrackControls();
        Format[] f = tcs[0].getSupportedFormats();
        if (f == null || f.length == 0) {
            logger.error("The mux does not support the input format: " + tcs[0].getFormat(),
                    new Exception("The mux does not support the input format: " + tcs[0].getFormat()));
        }

        tcs[0].setFormat(f[0]);
        ngView.outPrintln("Setting the track format to: " + f[0]);
        // We are done with programming the processor. Let's just
        // realize it.
        processor.realize();
        if (!waitForState(processor, Processor.Realized)) {
            logger.error("Failed to realize the processor.", new Exception("Failed to realize the processor."));
        }

        // Now, we'll need to create a DataSink.
        DataSink dsink = createDataSink(processor, outML);
        if (dsink == null) {
            logger.error("Failed to create a DataSink for the given output MediaLocator: " + outML,
                    new Exception("Failed to create a DataSink for the given output MediaLocator: " + outML));
        }

        dsink.addDataSinkListener(this);
        fileDone = false;
        ngView.outPrintln("start processing...");

        // OK, we can now start the actual transcoding.
        processor.start();
        dsink.start();
        // Wait for EndOfStream event.
        waitForFileDone();
        // Cleanup.
        dsink.close();
        processor.removeControllerListener(this);
        ngView.outPrintln("... done processing.");
    }

    /**
     * Create the DataSink.
     */
    private static DataSink createDataSink(Processor p, MediaLocator outML) throws IOException, NoDataSinkException {
        DataSource ds = p.getDataOutput();
        if (ds == null) {
            logger.error("processor does not have an output DataSource", new IOException("processor does not have an output DataSource"));
        }
        NeuGenView.getInstance().outPrintln("- create DataSink for: " + outML);
        DataSink dsink = Manager.createDataSink(ds, outML);
        dsink.open();
        return dsink;
    }

    /**
     * Block until the processor has transitioned to the given state.
     * Return false if the transition failed.
     */
    private boolean waitForState(Processor p, int state) throws InterruptedException {
        synchronized (waitSync) {
            while (p.getState() < state && stateTransitionOK) {
                waitSync.wait();
            }
        }
        return stateTransitionOK;
    }

    /**
     * Controller Listener.
     */
    @Override
    public void controllerUpdate(ControllerEvent evt) {
        if (evt instanceof ConfigureCompleteEvent
                || evt instanceof RealizeCompleteEvent
                || evt instanceof PrefetchCompleteEvent) {
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

    /**
     * Block until file writing is done.
     */
    private boolean waitForFileDone() throws InterruptedException {
        synchronized (waitFileSync) {
            while (!fileDone) {
                waitFileSync.wait();
            }
        }
        return fileSuccess;
    }

    /**
     * Event handler for the file writer.
     */
    @Override
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

    /**
     * how to run:
     * java MovieMaker -w <width> -h <height> -f <frame rate per sec.> -o <output file> <input JPEG file 1> <input JPEG file 2> ...
     *
     * example:
     * java MovieMaker -w 320 -h 240 -f 1 -o temp/out.mov temp/foo1.jpg temp/foo2.jpg
     *
     * NOTE - the images are not scaled to the specified height and width; they are cropped to the specified size instead.
     * However, there seems to be problems even with cropping. Best results come from
     * having your images match the passed-in size args.
     * It would be nice if someone could add auto-scaling of jpeg images to this class.
     */
    /*    public static void main(String[] args) {
    try {
    if (args.length == 0)
    prUsage();

    // Parse the arguments.
    int i = 0;
    int width = -1, height = -1, frameRate = 1;
    // contains File objects
    Vector fileVec = new Vector();
    File outputFile = null;

    while (i < args.length) {

    if (args.equals("-w")) {
    i++;
    if (i >= args.length)
    prUsage();
    width = new Integer(args).intValue();
    } else if (args.equals("-h")) {
    i++;
    if (i >= args.length)
    prUsage();
    height = new Integer(args).intValue();
    } else if (args.equals("-f")) {
    i++;
    if (i >= args.length)
    prUsage();
    frameRate = new Integer(args).intValue();
    } else if (args.equals("-o")) {
    i++;
    if (i >= args.length)
    prUsage();
    outputFile = new File(args);
    } else {
    File file = new File(args);
    if (!file.exists()) {
    throw new FileNotFoundException(args);
    }
    fileVec.addElement(file);
    }
    i++;
    }

    if (outputFile == null || fileVec.size() == 0)
    prUsage();

    if (width < 0 || height < 0) {
    System.out.println("Please specify the correct image size.");
    prUsage();
    }

    File[] inputFiles = (File[])fileVec.toArray(new File[0]);

    MovieMaker movMaker = new MovieMaker(width, height, frameRate, outputFile, inputFiles);
    movMaker.makeMovie();

    System.out.println("done");
    System.exit(0);
    } catch (Throwable t) {
    t.printStackTrace();
    System.exit(1);
    }
    System.exit(0);
    }*/
    private static void prUsage() {
        System.out.println("Usage: java MovieMaker -w <width> -h <height> -f <frame rate> -o <output URL> <input JPEG file 1> <input JPEG file 2> ...");
        System.exit(-1);
    }

///////////////////////////////////////////////
//
// Inner classes.
///////////////////////////////////////////////
    /**
     * A DataSource to read from a list of JPEG image files or
     * java.awt.Images, and turn that into a stream of JMF buffers.
     * The DataSource is not seekable or positionable.
     */
    private static class ImageDataSource extends PullBufferDataSource {

        private final Time durTime;
        private final PullBufferStream[] streams = new JpegSourceStream[1];

        /**
         * Constructor for creating movies out of jpegs
         */
        ImageDataSource(int width, int height, int frameRate, File[] jpegFiles) {
            streams[0] = new JpegSourceStream(width, height, frameRate, jpegFiles);
            this.durTime = new Time(jpegFiles.length / frameRate);
        }

        /**
         * Constructor for creating movies out of Images
         * NOTE - this is all done IN MEMORY, so you'd better have enough
         */
        ImageDataSource(int width, int height, int frameRate, Image[] images) {
            streams[0] = new AWTImageSourceStream(width, height, frameRate, images);
            this.durTime = new Time(images.length / frameRate);
        }

        @Override
        public void setLocator(MediaLocator source) {
        }

        @Override
        public MediaLocator getLocator() {
            return null;
        }

        /**
         * Content type is of RAW since we are sending buffers of video
         * frames without a container format.
         */
        @Override
        public String getContentType() {
            return ContentDescriptor.RAW;
        }

        @Override
        public void connect() {
        }

        @Override
        public void disconnect() {
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }

        /**
         * Return the ImageSourceStreams.
         */
        @Override
        public PullBufferStream[] getStreams() {
            return streams;
        }

        @Override
        public Time getDuration() {
            return durTime;
        }

        @Override
        public Object[] getControls() {
            return new Object[0];
        }

        @Override
        public Object getControl(String type) {
            return null;
        }
    }

    /**
     * The jpeg-based source stream to go along with ImageDataSource.
     */
    private static class JpegSourceStream implements PullBufferStream {

        private final File[] jpegFiles;
        private final int width, height;
        private final VideoFormat videoFormat;
        private int nextImage = 0;	// index of the next image to be read.
        private boolean ended = false;
        // Bug fix from Forums - next one line
        long seqNo = 0;

        public JpegSourceStream(int width, int height, int frameRate, File[] jpegFiles) {
            this.width = width;
            this.height = height;
            this.jpegFiles = jpegFiles;

            this.videoFormat = new VideoFormat(VideoFormat.JPEG,
                    new Dimension(width, height),
                    Format.NOT_SPECIFIED,
                    Format.byteArray,
                    (float) frameRate);
        }

        /**
         * We should never need to block assuming data are read from files.
         */
        @Override
        public boolean willReadBlock() {
            return false;
        }

        /**
         * This is called from the Processor to read a frame worth
         * of video data.
         */
        @Override
        public void read(final Buffer buf) {
            try {
                // Check if we've finished all the frames.
                if (nextImage >= jpegFiles.length) {
                    // We are done. Set EndOfMedia.
                    NeuGenView.getInstance().outPrintln("Done reading all images.");
                    //System.out.println("Done reading all images.");
                    buf.setEOM(true);
                    buf.setOffset(0);
                    buf.setLength(0);
                    ended = true;
                    return;
                }

                File imageFile = jpegFiles[nextImage];
                nextImage++;

                NeuGenView.getInstance().outPrintln(" - reading image file: " + imageFile);
                // Open a random access file for the next image.
                RandomAccessFile raFile = new RandomAccessFile(imageFile, "r");
                byte[] data = (byte[]) buf.getData();

                // Check to see the given buffer is big enough for the frame.
                if (data == null || data.length < raFile.length()) {
                    // allocate larger buffer
                    data = new byte[(int) raFile.length()];
                    buf.setData(data);
                }

                // Read the entire JPEG image from the file.
                raFile.readFully(data, 0, (int) raFile.length());
                NeuGenView.getInstance().outPrintln(" read " + raFile.length() + " bytes.");

                // Bug fix for AVI files from Forums ( next 3 lines).
                long time = (long) (seqNo * (1000 / videoFormat.getFrameRate()) * 1000000);
                buf.setTimeStamp(time);
                buf.setSequenceNumber(seqNo++);

                buf.setOffset(0);
                buf.setLength((int) raFile.length());
                buf.setFormat(videoFormat);
                buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);

                // Close the random access file.
                raFile.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Return the format of each video frame. That will be JPEG.
         */
        @Override
        public Format getFormat() {
            return videoFormat;
        }

        @Override
        public ContentDescriptor getContentDescriptor() {
            return new ContentDescriptor(ContentDescriptor.RAW);
        }

        @Override
        public long getContentLength() {
            return LENGTH_UNKNOWN;
        }

        @Override
        public boolean endOfStream() {
            return ended;
        }

        @Override
        public Object[] getControls() {
            return new Object[0];
        }

        @Override
        public Object getControl(String type) {
            return null;
        }
    }

    /**
     * The java.awt.Image-based source stream to go along with ImageDataSource.
     * Not sure yet if this class works.
     */
    private static class AWTImageSourceStream implements PullBufferStream {

        private final Image[] images;
        private final int width, height;
        private final VideoFormat videoFormat;
        private int nextImage = 0;	// index of the next image to be read.
        private boolean ended = false;
// Bug fix from Forums - next one line
        private long seqNo = 0;

        public AWTImageSourceStream(int width, int height, int frameRate, Image[] images) {
            this.width = width;
            this.height = height;
            this.images = images;

            // not sure if this is correct, especially the VideoFormat value
            this.videoFormat = new VideoFormat(VideoFormat.RGB,
                    new Dimension(width, height),
                    Format.NOT_SPECIFIED,
                    Format.byteArray,
                    (float) frameRate);
        }

        /**
         * We should never need to block assuming data are read from files.
         */
        @Override
        public boolean willReadBlock() {
            return false;
        }

        /**
         * This is called from the Processor to read a frame worth
         * of video data.
         */
        @Override
        public void read(final Buffer buf) throws IOException {
            try {
                // Check if we've finished all the frames.
                if (nextImage >= images.length) {
                    // We are done. Set EndOfMedia.
                    NeuGenView.getInstance().outPrintln("Done reading all images.");
                    //System.out.println("Done reading all images.");
                    buf.setEOM(true);
                    buf.setOffset(0);
                    buf.setLength(0);
                    ended = true;
                    return;
                }
                Image image = images[nextImage];
                nextImage++;
                // Open a random access file for the next image.
                //RandomAccessFile raFile = new RandomAccessFile(imageFile, "r");
                Buffer myBuffer = ImageToBuffer.createBuffer(image, videoFormat.getFrameRate());
                buf.copy(myBuffer);

                // Bug fix for AVI files from Forums ( next 3 lines).
                long time = (long) (seqNo * (1000 / videoFormat.getFrameRate()) * 1000000);
                buf.setTimeStamp(time);
                buf.setSequenceNumber(seqNo++);

                //buf.setOffset(0);
                //buf.setLength((int)raFile.length());
                //buf.setFormat(videoFormat);
                //buf.setFlags(buf.getFlags() | buf.FLAG_KEY_FRAME);
            } catch (Exception e) {
                // it's important to print the stack trace here because the
                // sun class that calls this method silently ignores
                // any Exceptions that get thrown
                logger.error(e, new RuntimeException(e));
            }
        }

        /**
         * Return the format of each video frame.
         */
        @Override
        public Format getFormat() {
            return videoFormat;
        }

        @Override
        public ContentDescriptor getContentDescriptor() {
            return new ContentDescriptor(ContentDescriptor.RAW);
        }

        @Override
        public long getContentLength() {
            return LENGTH_UNKNOWN;
        }

        @Override
        public boolean endOfStream() {
            return ended;
        }

        @Override
        public Object[] getControls() {
            return new Object[0];
        }

        @Override
        public Object getControl(String type) {
            return null;
        }
    }

    public static void generateVideo(Canvas3D canvas3D, SimpleUniverse universe,
            long animationTime, Alpha alpha, File outputFile, int fps,
            int width, int height) {

        /* Initializing the animation */
        long startTime = alpha.getStartTime();
        alpha.pause(startTime);
        /* Milliseconds it takes to change the frame. */
        long msFrame = (long) (((float) 1 / fps) * 500);
        /* Frames you need to create the movie */
        long framesNeeded = animationTime / msFrame;
        logger.info("number of frames: " + framesNeeded);
        /* Create an offscreen canvas from where you'll get the image */
        ScreenShot screenShot = new ScreenShot(canvas3D, universe, 1.0f);
        //canvas3D.getView().addCanvas3D(screenShot);

        /*
         * CREATE FRAMES
         */
        String name = "img_";
        String fileType = "jpg";
        //String tempDir = System.getProperty("file.separator") + "tmp";
        String tempDir = "tmp";
        File tmpFile = new File(tempDir);
        // Create NeuDV's tmp directory
       /*
        boolean dirSuccess = (new File(tempDir + System.getProperty("file.separator") + "NeuDV")).mkdir();
        if (!dirSuccess) {
            // Directory creation failed
        }
        * 
        */
        //File fTempDir = new File(tempDir + System.getProperty("file.separator") + "NeuDV");
        File fTempDir = new File(tmpFile.getAbsolutePath() + System.getProperty("file.separator") + "NeuDV");
        Vector<File> vFiles = new Vector<File>();
        System.gc();
        for (long frameCount = 0; frameCount <= framesNeeded; frameCount++) {
            /* Stop the animation in the correct position */
            long time = startTime + frameCount * msFrame;
            alpha.pause(time);
            /* Get the renderized image at that moment */
            BufferedImage bi = screenShot.doRender(width, height);
            /* Create a JPEG file from the renderized image */
            String tempName = name + frameCount;
            File file = new File(fTempDir, tempName + "." + fileType);
            try {
                ImageIO.write(bi, fileType, file);
            } catch (IOException ex) {
                logger.error(ex,ex);
                return;
            }
            vFiles.add(file);
        }
        /*
         * GENERATE VIDEO
         */
        int frameRate = (int) ((framesNeeded + 1) / (animationTime / 500));
        logger.info("frame rate: " + frameRate);
        File[] files = vFiles.toArray(new File[0]);
        try {
            MovieMaker movieMaker = new MovieMaker(width, height, frameRate, outputFile, files);
            movieMaker.makeMovie();
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
    }
}

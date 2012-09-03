package org.neugen.datastructures;

import java.io.Serializable;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.parameter.KeyIdentificable;
import org.neugen.datastructures.parameter.Parameter;
import org.neugen.parsers.ConfigParser;
import org.neugen.parsers.ConfigParserContainer;

/**
 *
 * created on 30.06.2010
 * @author Sergei Wolf
 */
public class Region implements Serializable {

    private static final long serialVersionUID = -7163130069294524614L;
    /** use to log messages */
    private static final Logger logger = Logger.getLogger(Region.class.getName());
    /** The lower left corner of the region. */
    private Point3f lowLeftCorner;
    /** The upper right corner of the region. */
    private Point3f upRightCorner;
    private static Region INSTANCE;
    private static boolean cortColumn;
    private static boolean ca1Region;

    public Region() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
        Param.getInstance();
    }

    public Region(Point3f lowLeftCorner, Point3f upRightCorner) {
        this.lowLeftCorner = lowLeftCorner;
        this.upRightCorner = upRightCorner;
    }

    public static Region getInstance() {
        if (INSTANCE == null) {
            setInstance(new Region());
        }
        return INSTANCE;
    }

    public static void setInstance(Region instance) {
        Region.INSTANCE = instance;
    }

    public Point3f getLowLeftCorner() {
        return lowLeftCorner;
    }

    public Point3f getUpRightCorner() {
        return upRightCorner;
    }

    public static boolean isCa1Region() {
        return ca1Region;
    }

    public static void setCa1Region(boolean ca1Region) {
        Region.ca1Region = ca1Region;
    }

    public static boolean isCortColumn() {
        return cortColumn;
    }

    public static void setCortColumn(boolean cortColumn) {
        Region.cortColumn = cortColumn;
    }

    public static final class Param extends KeyIdentificable {

        private final ColumnParam columnParam;
        private final CA1Param ca1Param;
        private static Param instance;

        private Param(String lastKey) {
            super(lastKey);
            ca1Param = new CA1Param(this, "ca1");
            columnParam = new ColumnParam(this, "column");
        }

        /**
         * Get the value of instance
         *
         * @return the value of instance
         */
        public static Param getInstance() {
            if (instance == null) {
                Param.setInstance(new Param("region"));
            }
            return instance;
        }

        /**
         * Set the value of instance
         *
         * @param instance new value of instance
         */
        public static void setInstance(Param instance) {
            Param.instance = instance;
        }

        /**
         * Get the value of ca1Param
         *
         * @return the value of ca1Param
         */
        public CA1Param getCa1Param() {
            return ca1Param;
        }

        public ColumnParam getColumnParam() {
            return columnParam;
        }

        public boolean valid(Parameter<Float> val) {
            if (val.isValid()) {
                if (val.getValue() >= 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public final class ColumnParam extends KeyIdentificable {

            private final Parameter<Float> width;
            private final Parameter<Float> length;
            private final float height;
            private final Parameter<Float> layer1;
            private final Parameter<Float> layer23;
            private final Parameter<Float> layer4;
            private final Parameter<Float> layer5A;
            private final Parameter<Float> layer5B;
            private final Parameter<Float> layer6;

            public ColumnParam(KeyIdentificable parent, String lastKey) {
                super(parent, lastKey);
                ConfigParser paramParser = ConfigParserContainer.getParamParser();
                length = new Parameter<Float>(paramParser, this, "length");
                width = new Parameter<Float>(paramParser, this, "width");
                layer1 = new Parameter<Float>(paramParser, this, "layer1");
                layer23 = new Parameter<Float>(paramParser, this, "layer2/3");
                layer4 = new Parameter<Float>(paramParser, this, "layer4");
                layer5A = new Parameter<Float>(paramParser, this, "layer5A");
                layer5B = new Parameter<Float>(paramParser, this, "layer5B");
                layer6 = new Parameter<Float>(paramParser, this, "layer6");
                height = getLayer1() + getLayer23() + getLayer4() + getLayer5A() + getLayer5B() + getLayer6();
            }

            public float getHeight() {
                return height;
            }

            public float getLayer1() {
                if (valid(layer1)) {
                    return layer1.getValue();
                } else {
                    return 0;
                }
            }

            public float getLayer23() {
                if (valid(layer23)) {
                    return layer23.getValue();
                } else {
                    return 0;
                }
            }

            public float getLayer4() {
                if (valid(layer4)) {
                    return layer4.getValue();
                } else {
                    return 0;
                }
            }

            public float getLayer5A() {
                if (valid(layer5A)) {
                    return layer5A.getValue();
                } else {
                    return 0;
                }
            }

            public float getLayer5B() {
                if (valid(layer5B)) {
                    return layer5B.getValue();
                } else {
                    return 0;
                }
            }

            public float getLayer6() {
                if (valid(layer6)) {
                    return layer6.getValue();
                } else {
                    return 0;
                }
            }

            public float getLength() {
                if (valid(length)) {
                    return length.getValue();
                } else {
                    return 0;
                }
            }

            public float getWidth() {
                if (valid(width)) {
                    return width.getValue();
                } else {
                    return 0;
                }
            }
        }

        public final class CA1Param extends KeyIdentificable {

            private final Parameter<Float> width;
            private final Parameter<Float> length;
            private final float height;
            private final Parameter<Float> stratumOriens;
            private final Parameter<Float> stratumPyramidale;
            private final Parameter<Float> stratumRadiatum;
            private final Parameter<Float> stratumLacunosum;

            public CA1Param(KeyIdentificable parent, String lastKey) {
                super(parent, lastKey);
                ConfigParser paramParser = ConfigParserContainer.getParamParser();
                length = new Parameter<Float>(paramParser, this, "length");
                width = new Parameter<Float>(paramParser, this, "width");
                stratumOriens = new Parameter<Float>(paramParser, this, "stratum_oriens/alveus");
                stratumPyramidale = new Parameter<Float>(paramParser, this, "stratum_pyramidale");
                stratumRadiatum = new Parameter<Float>(paramParser, this, "stratum_radiatum");
                stratumLacunosum = new Parameter<Float>(paramParser, this, "stratum_lacunosum/moleculare");
                height = getStratumOriens() + getStratumPyramidale() + getStratumRadiatum() + getStratumLacunosum();
            }

            public float getHeight() {
                return height;
            }

            /**
             * Get the value of stratumLacunosum
             *
             * @return the value of stratumLacunosum
             */
            public float getStratumLacunosum() {
                if (valid(stratumLacunosum)) {
                    return stratumLacunosum.getValue();
                } else {
                    return 0;
                }
            }

            /**
             * Get the value of stratumRadiatum
             *
             * @return the value of stratumRadiatum
             */
            public float getStratumRadiatum() {
                if (valid(stratumRadiatum)) {
                    return stratumRadiatum.getValue();
                } else {
                    return 0;
                }
            }

            /**
             * Get the value of stratumPyramidale
             *
             * @return the value of stratumPyramidale
             */
            public float getStratumPyramidale() {
                if (valid(stratumPyramidale)) {
                    return stratumPyramidale.getValue();
                } else {
                    return 0;
                }
            }

            /**
             * Get the value of stratumOriens
             *
             * @return the value of stratumOriens
             */
            public float getStratumOriens() {
                if (valid(stratumOriens)) {
                    return stratumOriens.getValue();
                } else {
                    return 0;
                }
            }

            /**
             * Get the value of width
             *
             * @return the value of width
             */
            public float getWidth() {
                if (valid(width)) {
                    return width.getValue();
                } else {
                    return 0;
                }
            }

            /**
             * Get the value of length
             *
             * @return the value of length
             */
            public float getLength() {
                if (valid(length)) {
                    return length.getValue();
                } else {
                    return 0;
                }
            }
        }
    }

    public void setColumnSize() {
        Param.ColumnParam par = Param.getInstance().getColumnParam();
        Point3f s = new Point3f(0.0f, 0.0f, 0.0f);
        Point3f e = new Point3f(par.getLength(), par.getWidth(), par.getHeight());
        logger.info("column size: " + e.toString());
        lowLeftCorner = s;
        upRightCorner = e;
    }

    public void setCA1Size() {
        Param.CA1Param regPar = Param.getInstance().getCa1Param();
        Point3f s = new Point3f(0.0f, 0.0f, 0.0f);
        Point3f e = new Point3f(regPar.getLength(), regPar.getWidth(), regPar.getHeight());
        lowLeftCorner = s;
        upRightCorner = e;
    }

    public void setCellRegionSize(Point3f lowLeftCorner, Point3f upRightCorner) {
        this.lowLeftCorner = lowLeftCorner;
        this.upRightCorner = upRightCorner;
    }
}

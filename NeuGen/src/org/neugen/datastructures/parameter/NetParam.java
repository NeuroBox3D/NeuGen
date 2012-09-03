package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import org.neugen.parsers.ConfigParserContainer;

public final class NetParam extends KeyIdentificable {

    private final Parameter<Integer> seed;
    private final Parameter<Float> distSynapse;
    // cells of neocortex
    private final Parameter<Integer> nL4stellate, nL23pyramidal, nStarpyramidal, nL5Apyramidal, nL5Bpyramidal;
    // cells of hippocampus CA1 region
    private final NetCA1Param nCA1pyramidal, nCalretinin, nCalbindin, nCholecystokinin, nParvalbumin, nSomatostatin;
    private static NetParam instance;

    public class NetCA1Param extends KeyIdentificable {

        private final Parameter<Integer> numOriens;
        private final Parameter<Integer> numPyramidale;
        private final Parameter<Integer> numProximalRadiatum;
        private final Parameter<Integer> numDistalRadiatum;
        private final Parameter<Integer> numLacunosumMoleculare;

        public NetCA1Param(String lastKey, KeyIdentificable parent) {
            super(parent, lastKey);
            ConfigParser paramParser = ConfigParserContainer.getParamParser();
            numOriens = new Parameter<Integer>(paramParser, this, "oriens");
            numPyramidale = new Parameter<Integer>(paramParser, this, "pyramidale");
            numProximalRadiatum = new Parameter<Integer>(paramParser, this, "proximal_radiatum");
            numDistalRadiatum = new Parameter<Integer>(paramParser, this, "distal_radiatum");
            numLacunosumMoleculare = new Parameter<Integer>(paramParser, this, "lacunosum/moleculare");
        }

        public int getNumProximalRadiatum() {
            if (numProximalRadiatum.getValue() == null) {
                return 0;
            }
            return numProximalRadiatum.getValue() < 0 ? 0 : numProximalRadiatum.getValue();
        }

        public int getNumDistalRadiatum() {
            if (numDistalRadiatum.getValue() == null) {
                return 0;
            }
            return numDistalRadiatum.getValue() < 0 ? 0 : numDistalRadiatum.getValue();
        }

        public int getNumLacunosumMoleculare() {
            if (numLacunosumMoleculare.getValue() == null) {
                return 0;
            }
            return numLacunosumMoleculare.getValue() < 0 ? 0 : numLacunosumMoleculare.getValue();
        }

        public int getNumOriens() {
            if (numOriens.getValue() == null) {
                return 0;
            }
            return numOriens.getValue() < 0 ? 0 : numOriens.getValue();
        }

        public int getNumPyramidale() {
            if (numPyramidale.getValue() == null) {
                return 0;
            }
            return numPyramidale.getValue() < 0 ? 0 : numPyramidale.getValue();
        }

        public int getNum() {
            return getNumProximalRadiatum() + getNumDistalRadiatum() + getNumLacunosumMoleculare() + getNumOriens() + getNumPyramidale();
        }
    }

    private NetParam(String lastKey) {
        super(lastKey);
        seed = new Parameter<Integer>(ConfigParserContainer.getInternaParser(), this, "seed");
        ConfigParser paramParser = ConfigParserContainer.getParamParser();
        distSynapse = new Parameter<Float>(paramParser, this, "dist_synapse");
        // neocortex
        nL4stellate = new Parameter<Integer>(paramParser, this, "nL4stellate");
        nL23pyramidal = new Parameter<Integer>(paramParser, this, "nL23pyramidal");
        nL5Apyramidal = new Parameter<Integer>(paramParser, this, "nL5Apyramidal");
        nL5Bpyramidal = new Parameter<Integer>(paramParser, this, "nL5Bpyramidal");
        nStarpyramidal = new Parameter<Integer>(paramParser, this, "nstarpyramidal");
        // hippocampus
        /*
        nCA1pyramidal = new Parameter<Integer>(paramParser, this, "nCA1pyramidal");
        nCalretinin = new Parameter<Integer>(paramParser, this, "nCalretinin(CR)");
        nCalbindin = new Parameter<Integer>(paramParser, this, "nCalbindin(CB)");
        nCholecystokinin = new Parameter<Integer>(paramParser, this, "nCholecystokinin(CCK)");
        nParvalbumin = new Parameter<Integer>(paramParser, this, "nParvalbumin(PV)");
        nSomatostatin = new Parameter<Integer>(paramParser, this, "nSomatostatin(SOM)");
         *
         */
        nCA1pyramidal = new NetCA1Param("nCA1pyramidal", this);
        nCalretinin = new NetCA1Param("nCalretinin(CR)", this);
        nCalbindin = new NetCA1Param("nCalbindin(CB)", this);
        nCholecystokinin = new NetCA1Param("nCholecystokinin(CCK)", this);
        nParvalbumin = new NetCA1Param("nParvalbumin(PV)", this);
        nSomatostatin = new NetCA1Param("nSomatostatin(SOM)", this);
    }

    public int getSeedValue() {
        return seed.getValue().intValue();
    }

    public float getDistSynapse() {
        return distSynapse.getValue() < 0 ? 0 : distSynapse.getValue();
    }

    public int getNumL23pyramidal() {
        return nL23pyramidal.getValue() < 0 ? 0 : nL23pyramidal.getValue();
    }

    public int getNumL4stellate() {
        return nL4stellate.getValue() < 0 ? 0 : nL4stellate.getValue();
    }

    public int getNumL5Apyramidal() {
        return nL5Apyramidal.getValue() < 0 ? 0 : nL5Apyramidal.getValue();
    }

    public int getNumL5Bpyramidal() {
        return nL5Bpyramidal.getValue() < 0 ? 0 : nL5Bpyramidal.getValue();
    }

    public int getNumStarpyramidal() {
        return nStarpyramidal.getValue() < 0 ? 0 : nStarpyramidal.getValue();
    }

    public NetCA1Param getnCA1pyramidal() {
        return nCA1pyramidal;
    }

    public NetCA1Param getnCalbindin() {
        return nCalbindin;
    }

    public NetCA1Param getnCalretinin() {
        return nCalretinin;
    }

    public NetCA1Param getnCholecystokinin() {
        return nCholecystokinin;
    }

    public NetCA1Param getnParvalbumin() {
        return nParvalbumin;
    }

    public NetCA1Param getnSomatostatin() {
        return nSomatostatin;
    }

    /*
    public int getNumCA1pyramidal() {
    return nCA1pyramidal.getValue() < 0 ? 0 : nCA1pyramidal.getValue();
    }

    public int getNumCalbindin() {
    return nCalbindin.getValue() < 0 ? 0 : nCalbindin.getValue();
    }

    public int getNumCalretinin() {
    return nCalretinin.getValue() < 0 ? 0 : nCalretinin.getValue();
    }

    public int getNumCholecystokinin() {
    return nCholecystokinin.getValue() < 0 ? 0 : nCholecystokinin.getValue();
    }

    public int getNumParvalbumin() {
    return nParvalbumin.getValue() < 0 ? 0 : nParvalbumin.getValue();
    }
    
    public int getNumSomatostatin() {
    return nSomatostatin.getValue() < 0 ? 0 : nSomatostatin.getValue();
    }
     *
     */
    /**
     * Get the value of instance
     *
     * @return the value of instance
     */
    public static NetParam getInstance() {
        if (instance == null) {
            NetParam netParam = new NetParam(ParameterConstants.LAST_KEY_NET);
            NetParam.setInstance(netParam);
        }
        return instance;
    }

    /**
     * Set the value of instance
     *
     * @param instance new value of instance
     */
    public static void setInstance(NetParam instance) {
        NetParam.instance = instance;
    }
}

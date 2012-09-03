package org.neugen.datastructures;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Sergei Wolf
 */
public interface WriteToHoc {

    public void writetohocExp2Synapses(Writer fw, Writer synFW) throws IOException;

    public void writetohocChannels(Writer fw) throws IOException;

    public void writetohocAlphaSynapses(Writer fw) throws IOException;

    public float get_uEPSP_Value(int typeN1, int typeN2);

    public void writetohocModel(Writer fw) throws IOException;
}

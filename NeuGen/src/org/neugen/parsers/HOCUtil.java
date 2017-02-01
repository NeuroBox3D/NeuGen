/// package's name
package org.neugen.parsers;

import java.util.Locale;

/**
 * @brief hoc utility functions
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public final class HOCUtil {
	/// precision for HOC output
	private static final String FORMAT = "%.12f";
	
	/**
	 * @brief ctor
	 */
	private HOCUtil() {
		
	}
	
	/**
	 * @brief format the number to our precision.
	 * We need to refrain from exponential notation, since some tools, i. e. NEURON, seem not to
	 * handle exponential notations when importing certain morphologies 
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 * 
	 * @param number
	 * @return 
	 */
	public static String format(Number number) {
		return String.format(Locale.ENGLISH, FORMAT, number);
	}

}

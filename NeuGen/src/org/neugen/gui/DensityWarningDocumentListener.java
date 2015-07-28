/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
/// package's name
package org.neugen.gui;

/// imports
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @brief warns if density is too high for reasonable large-scale networks This
 * means, if the nparts_density is too high, we except many vertices which are
 * almost identical (double vertices) and this does not suit our needs in
 * subsequent grid generation and simulation
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 * @see javax.swing.event.DocumentListener
 */
public final class DensityWarningDocumentListener implements DocumentListener {
	/// private final members
	private final JLabel warningLabel;
	
        /// private static final members
        private static final Float maxDensity = 0.5f;
        private static final String warningMessage = "Density way too high";
        private static final String warningMessageTooltip = "For large scale-networks " 
                + "a density below " + maxDensity + " is recommended to avoid double "
		+ "vertices. You can go on with the given density, however we expect a "
		+ "slower grid generation in the end.";
        private static final String warningMessageTooltipPrefix = 
                "<html><p style=\"text-align: justify\" width=\"400\">"; 
        private static final String warningMessageTooltipPostfix = "</p></html>";
                       
                                                                   
	/**
	 * @brief ctor
	 * @param warningLabel
	 */
	public DensityWarningDocumentListener(final JLabel warningLabel) {
		this.warningLabel = warningLabel;
	}

	/**
	 * @brief visually warns if density is too high
	 * @param e
	 */
	private void warn(DocumentEvent e) {
		try {
			Document densityTf = e.getDocument();
			String text = densityTf.getText(0, densityTf.getLength());
			if (!text.isEmpty()) {
				if (Float.parseFloat(text) > 0.5) {
					this.warningLabel.setVisible(true);
                                        this.warningLabel.setForeground(Color.RED);
              				this.warningLabel.setText(warningMessage);
 			                this.warningLabel.setToolTipText(warningMessageTooltipPrefix 
						+ warningMessageTooltip + warningMessageTooltipPostfix);
                                 
				} else {
                                     this.warningLabel.setVisible(false);
                                }
			} else {
                             this.warningLabel.setVisible(false);
                        }
		} catch (BadLocationException ex) {
                        this.warningLabel.setVisible(false);
		} catch (NumberFormatException nfe) {
                        this.warningLabel.setVisible(false);
		}
	}

	/**
	 * @brief 
	 * @see javax.swing.event.DocumentListener#insertUpdate 
	 * @param e 
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		warn(e);
	}

	/**
	 * @brief 
	 * @see javax.swing.event.DocumentListener#removeUpdate
	 * @param e 
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		warn(e);
	}

	/**
	 * @brief
	 * @see javax.swing.event.DocumentListener#changedUpdate
	 * @param e
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		warn(e);
	}
}

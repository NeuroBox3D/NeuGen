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
import java.awt.Frame;
import java.util.Enumeration;
import java.util.Map.Entry;
import javax.swing.JDialog;
import org.apache.log4j.Logger;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;

/**
 * @brief set global parameters for projects upon project creation
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class GlobalParameterDialog extends JDialog {
	/// private static-final members
	private static final int RET_CANCEL = 0;
	private static final int RET_OK = 1;
	private static final long serialVersionUID = 1L;
    	private static final Logger logger = Logger.getLogger(NeuGenView.class.getName());
        
	// defaults
	private static final float DENSITY_DEFAULT = 0.25f;
	private static final float SYNAPSE_DISTANCE_DEFAULT = 2.5f;
        
	/// tooltips
        //private final static String TOOLTIP_DENSITY = "A lower value results in " 
        //        + "a more sparse compartment structure and a higher value results in "
        //        + "a more dense compartment structure, i. e. less or more points are "
        //        + "utilized for each compartment respectively.";
        private final static String TOOLTIP_DIST_SYNAPSE = "Threshold value for interconnecting " 
                + "synapses between supported cells types (note: not all cell types can form a "
                + "synapse!). If the distance between two points describing a potential synapse is "
                + "above the threshold distance then the potential synapse will be discarded. ";
        private final static String TOOLTIP_SECTION_LENGTH = "A lower value results in "
                + "a more dense compartment structure and a higher value results in "
                + "a more sparse compartment structure, i. e. more or less points are "
                + "utilized for each compartment respectively. (Note: section-length "
                + "parameter is overriden by the nparts-density parameter if the latter"
                + "parameter is not set!";
	private final static String TOOLTIP_SECTION_LENGTH_VS_DENSITY = "The n-parts-Density"
		+ "parameter overrides the section-length parameter.";
	
	
	/// private final members
	private final String DENSITY_IDENTIFIER = "nparts_density";
	private final String SYNAPSE_DISTANCE_IDENTIFIER = "dist_synapse";
  
	
	/// members
	private int returnStatus = RET_CANCEL;
	private float density;
	private float synapse_distance;
        
	/**
	 * @brief creates form for global parameter dialog
	 * @param parent
	 * @param modal
	 */
	public GlobalParameterDialog(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getResourceMap(GlobalParameterDialog.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jLabel3.setToolTipText("<html><p style=\"text-align: justify\" width=\"400\">" +TOOLTIP_SECTION_LENGTH_VS_DENSITY+"</p></html>");

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jTextField3.setText(resourceMap.getString("jTextField3.text")); // NOI18N
        jTextField3.setName("jTextField3"); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel4)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jLabel3)
                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 343, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(64, 64, 64)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jTextField2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 70, Short.MAX_VALUE)
                        .add(jLabel6))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel8)
                        .add(53, 53, 53)
                        .add(jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 106, Short.MAX_VALUE)
                        .add(jLabel9)))
                .add(39, 39, 39))
            .add(layout.createSequentialGroup()
                .add(72, 72, 72)
                .add(jButton1)
                .add(111, 111, 111)
                .add(jButton2)
                .addContainerGap(133, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel3)
                .add(1, 1, 1)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel7)
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .add(18, 18, 18)
                .add(jLabel4)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(jButton2))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setToolTipText("<html><p style=\"text-align: justify\" width=\"400\">" +TOOLTIP_DIST_SYNAPSE+"</p></html>");
        jLabel7.setText("");
        jLabel7.setVisible(true);
        jLabel7.setSize(87, 16);
        jLabel7.setVisible(true);
        jTextField3.getDocument().addDocumentListener(new org.neugen.gui.SectionLengthWarningDocumentListener(this.jLabel7));
        jLabel9.setToolTipText("<html><p style=\"text-align: justify\" width=\"400\">" +TOOLTIP_SECTION_LENGTH+"</p></html>");

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
	doClose(RET_CANCEL);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
	doClose(RET_OK);
}//GEN-LAST:event_jButton1ActionPerformed

private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed

}//GEN-LAST:event_jTextField3ActionPerformed

	/**
	 * @brief indicates OK or CANCEL status
	 * @param retStatus
	 */
	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();
		logger.info("Closed with status: " + returnStatus);
	}

	/**
	 * @brief get dist-synapse parameter
	 * @return
	 */
	public float getSynapseDistance() {
		String synapse_distance_str = jTextField2.getText();

		if (!synapse_distance_str.isEmpty()) {
			this.synapse_distance = Float.parseFloat(synapse_distance_str);
		} else {
			this.synapse_distance = SYNAPSE_DISTANCE_DEFAULT;
		}

		return this.synapse_distance;
	}

	/**
	 * @brief get nparts-density parameter
	 * @return
	 */
	public float getNpartsDensity() {
		String section_str = jTextField3.getText();
        if (!section_str.isEmpty()) {
            this.density = 1.0f / Float.parseFloat(section_str);
        } else {
            this.density = DENSITY_DEFAULT;
        }

		return this.density;
	}

	/**
	 * @brief set's the global gen0 parameter (enhance just before the
	 * method body's end)
	 */
	private void set_global_gen0_parameters() {
		set_global_gen0_parameter(DENSITY_IDENTIFIER, this.density);
	}

	/**
	 * @brief set global synapse distance parameter
	 */
	private void set_global_synapse_distance_parameter() {
		set_global_synapse_distance_parameter(SYNAPSE_DISTANCE_IDENTIFIER, this.synapse_distance);
	}

	/**
	 * @brief set all parameters
	 */
	public void set_global_parameters() {
		set_global_gen0_parameters();
		set_global_synapse_distance_parameter();
	}

	/**
	 * @brief set's global synapse distance (dist-synapse) parameter for
	 * project
	 * @author stephanmg <stephan@sytaktischer-zucker.de>
	 *
	 * @param identifier
	 * @param replacement
	 */
	@SuppressWarnings("unchecked")
	private void set_global_synapse_distance_parameter(String identifier, float replacement) {
		for (Entry<String, XMLObject> entry : NeuGenView.getInstance().getParamTrees().entrySet()) {
			/// logger.info("Key:" + entry.getKey());
			XMLObject obj = entry.getValue();
			//if ("net".equals(entry.getKey())) {
			Enumeration<XMLNode> childs = obj.children();

			while (childs.hasMoreElements()) {
				XMLNode node = childs.nextElement();
				/// logger.info("Node: " + node.toString());
				if ("net".equals(node.toString())) {
					Enumeration<XMLNode> childs2 = node.children();
					while (childs2.hasMoreElements()) {
						XMLNode node2 = childs2.nextElement();
						/// logger.info("*** CHILD *** :" + node2);
						if (identifier.equals(node2.getKey())) {
							node2.setValue(replacement);

						}
					}

				}
			}
		}

	}

	/**
	 * @brief set's a gen0 parameter for the project globally
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param identifier
	 * @param replacement
	 */
	@SuppressWarnings("unchecked")
	private void set_global_gen0_parameter(String identifier, float replacement) {
		for (Entry<String, XMLObject> entry : NeuGenView.getInstance().getParamTrees().entrySet()) {
			/// logger.info("Key:" + entry.getKey());
			XMLObject obj = entry.getValue();
			Enumeration<XMLNode> childs = obj.children();

			while (childs.hasMoreElements()) {
				XMLNode node = childs.nextElement();
				/// logger.info("Node: " + node.toString());
				if ("neuron".equals(node.toString())) {
					Enumeration<XMLNode> childs2 = node.children();
					while (childs2.hasMoreElements()) {
						XMLNode node2 = childs2.nextElement();
						if ("axon".equals(node2.toString())) {
							Enumeration<XMLNode> childs3 = node2.children();
							while (childs3.hasMoreElements()) {
								XMLNode child4 = childs3.nextElement();
								/// logger.info("axon child: " + child4.toString());
								if ("gen_0".equals(child4.toString())) {
									Enumeration<XMLNode> childs5 = child4.children();

									while (childs5.hasMoreElements()) {
										XMLNode child6 = childs5.nextElement();

										if (identifier.equals(child6.getKey())) {
											/// logger.info("child6 (before): " + child6.toString());
											/// logger.info("child6's key (before): " + child6.getKey());
											child6.setValue(density);

											/// logger.info("child6 (after): " + child6.toString());
											/// logger.info("child6's key (after): " + child6.getKey());
										}

										if ("siblings".equals(child6.getKey())) {
											correct_siblings(child6, identifier, replacement);
										}

									}
								}
							}

						} else if ("dendrite".equals(node2.toString())) {

							Enumeration<XMLNode> childs3 = node2.children();
							while (childs3.hasMoreElements()) {
								XMLNode child4 = childs3.nextElement();
								/// System.err.println("axon child: " + child4.toString());
								if ("gen_0".equals(child4.toString())) {
									Enumeration<XMLNode> childs5 = child4.children();

									while (childs5.hasMoreElements()) {
										XMLNode child6 = childs5.nextElement();

										if ("nparts_density".equals(child6.getKey())) {
											/// logger.info("child6 (before): " + child6.toString());
											/// logger.info("child6's key (before): " + child6.getKey());
											child6.setValue(replacement);

											/// logger.info("child6 (after): " + child6.toString());
											/// logger.info("child6's key (after): " + child6.getKey());
										}

										if ("siblings".equals(child6.getKey())) {
											correct_siblings(child6, identifier, replacement);
										}

									}
								}
							}

						} else {
						}
					}
				}
			}
		}
	}

	/**
	 * @brief replaces content of a given node specified by identifier with
	 * replacement
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param child
	 * @param identifier
	 * @param replacement
	 */
	@SuppressWarnings("unchecked")
	private void correct_siblings(XMLNode child, String identifier, float replacement) {
		/// only one child called siblings within childs of current XMLNode child
		Enumeration<XMLNode> childs = child.children();
		XMLNode sibling = childs.nextElement();
		Enumeration<XMLNode> childs_of_sibling = sibling.children();

		while (childs_of_sibling.hasMoreElements()) {
			/// current child of current sibling's child
			XMLNode current_child = childs_of_sibling.nextElement();

			/// replace node's content
			if (identifier.equals(current_child.getKey())) {
				current_child.setValue(replacement);
			}

			/// more siblings?
			if ("siblings".equals(current_child.getKey())) {
				correct_siblings(current_child, identifier, replacement);
			}
		}

	}

	/**
	 * @brief run dialog standalone
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 *
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(GlobalParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(GlobalParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(GlobalParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(GlobalParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
        //</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
	
			@Override
			public void run() {
				GlobalParameterDialog dialog = new GlobalParameterDialog(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {

					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}

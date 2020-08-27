# NeuGen
NeuGen is made for generation of dendritic and axonal morphology of realistic neurons and networks in 3D

NeuGen generates nonidentical neurons of morphological classes of the cortex, e.g., pyramidal cells and stellate neurons, and synaptically connected neural networks in 3D. It is based on sets of descriptive and iterative rules which represent the axonal and dendritic geometry of neurons by inter-correlating morphological parameters. The generation algorithm stochastically samples parameter values from distribution functions induced by experimental data. The generator is adequate for the geometric modelling and for the construction of the morphology.

The generated neurons can be exported into a 3D graphic format for visualization and into multi-compartment files for simulations with the program NEURON. NeuGen is intended for scientists aiming at simulations of realistic networks in 3D.

[![CI Build Status (master)](https://travis-ci.org/NeuroBox3D/NeuGen.svg?branch=master)](https://travis-ci.org/NeuroBox3D/NeuGen)
[![Build status (devel)](https://ci.appveyor.com/api/projects/status/ovyhr78ydpolbjfc?svg=true)](https://ci.appveyor.com/project/stephanmg/neugen)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/b3714ff664c1490893efdf05afb52027)](https://www.codacy.com/app/stephan_5/NeuGen)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Build prerequisites
1. Ant >= 1.9
2. JDK >= 6 and JDK <= 12 

# Howto build NeuGen
1. Netbeans: Clean & Build
2. Ant: ant from NeuGen folder

# Howto run NeuGen
1. Netbeans: Run File ```NeuGenApp.java```
2. Terminal: Goto rls folder - execute run script according to your OS

## Releases
NeuGen 2.0 - git tag v2.0
Current development on devel branch, current stable on master branch. See also the project website [NeuGen](http://www.neugen.org) and [NeuroBox3D](http://neurobox.eu).

## Samples
![](/resources/img/synapse.jpg)
![](/resources/img/soma.jpg)
![](/resources/img/neugen.jpg)


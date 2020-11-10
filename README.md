# NeuGen
NeuGen is made for generation of dendritic and axonal morphology of realistic neurons and networks in 3D

NeuGen generates nonidentical neurons of morphological classes of the cortex, e.g., pyramidal cells and stellate neurons, and synaptically connected neural networks in 3D. It is based on sets of descriptive and iterative rules which represent the axonal and dendritic geometry of neurons by inter-correlating morphological parameters. The generation algorithm stochastically samples parameter values from distribution functions induced by experimental data. The generator is adequate for the geometric modelling and for the construction of the morphology.

The generated neurons can be exported into a 3D graphic format for visualization and into multi-compartment files for simulations with the program NEURON. NeuGen is intended for scientists aiming at simulations of realistic networks in 3D.

[![CI Build Status](https://travis-ci.org/NeuroBox3D/NeuGen.svg?branch=master)](https://travis-ci.org/NeuroBox3D/NeuGen)
[![CI Build Status](https://travis-ci.org/NeuroBox3D/NeuGen.svg?branch=devel)](https://travis-ci.org/NeuroBox3D/NeuGen)
[![CI Coverage Status](https://coveralls.io/repos/NeuroBox3D/NeuGen/badge.png)](https://coveralls.io/r/NeuroBox3D/NeuGen)
[![Build status](https://ci.appveyor.com/api/projects/status/ovyhr78ydpolbjfc?svg=true)](https://ci.appveyor.com/project/stephanmg/neugen)
[![LGPLv3](https://img.shields.io/badge/license-LGPLv3-blue.svg)](./README.md)

# Howto build NeuGen
1. Netbeans: Clean & Build in GUI
2. Ant: Enter `ant` from NeuGen folder

# Howto run NeuGen
1. Netbeans: Run File ```NeuGenApp.java```
2. Terminal: Goto rls folder - execute run (`run.sh`) script according to your OS

## Releases
NeuGen 1.0 - git tag v1.0
NeuGen 2.0 - git tag v2.0
Current development on devel branch (NeuGen 3.0), current stable version is on master branch.
VRL integration of NeuGen 2.0 into VRL-Studio on branch vrl-devel.
See also please the project websites [NeuGen](http://www.neugen.org) and [NeuroBox3D](http://www.neurobox.eu)

## Samples
![](/resources/img/synapse.jpg)
![](/resources/img/soma.jpg)
![](/resources/img/neugen.jpg)

## Documentation
Export .ngx (NeuGen XML) from the GUI by Save Dialog or Ctrl-Shift-N.
One can pre-coarsen by supplying the (global!) level of detail by 
a positive number for the n-parts-density textfield when opening a project.
Also one can specify the cutoff distance for synapse generation by a positive
number in analogue for the synapse-distance textfield. 

## Backend
See Backend.java in package org.neugen.backend for usage.

## Important note
1. Hippocampal networks cannot be exported by now to any format correctly, since
either not implemented at all or no synapse export present - see open issues.
2. If you want to run the final NeuGen.jar from command line by ```java -classpath NeuGen.jar org.neugen.gui.NeuGenApp```
from a given directory make sure, that the relative path ```config/``` exists, from the directory you are invoking the
startup command (better use the script ```run.sh```!).
3. To increase heap memory, use ```-Xmx``` in run options in netbeans or set ```MAX_HEAP_MEMORY```in ```run.sh```.

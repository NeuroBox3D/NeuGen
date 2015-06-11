# NeuGen
NeuGen is made for generation of dendritic and axonal morphology of realistic neurons and networks in 3D

NeuGen generates nonidentical neurons of morphological classes of the cortex, e.g., pyramidal cells and stellate neurons, and synaptically connected neural networks in 3D. It is based on sets of descriptive and iterative rules which represent the axonal and dendritic geometry of neurons by inter-correlating morphological parameters. The generation algorithm stochastically samples parameter values from distribution functions induced by experimental data. The generator is adequate for the geometric modelling and for the construction of the morphology.

The generated neurons can be exported into a 3D graphic format for visualization and into multi-compartment files for simulations with the program NEURON. NeuGen is intended for scientists aiming at simulations of realistic networks in 3D.

## Releases
NeuGen 2.0 - git tag v2.0
Current development on devel branch, current stable on master branch.

## See Also
[NeuGen](http://www.neugen.org)

## Samples
![](/resources/img/synapse.jpg)
![](/resources/img/soma.jpg)
![](/resources/img/neugen.jpg)

## Development
### CI
* [![CI Build Status](https://travis-ci.org/NeuroBox3D/NeuGen.svg?branch=master)](https://travis-ci.org/NeuroBox3D/NeuGen)
[![CI Build Status](https://travis-ci.org/NeuroBox3D/NeuGen.svg?branch=devel)](https://travis-ci.org/NeuroBox3D/NeuGen)
[![CI Coverage Status](https://coveralls.io/repos/NeuroBox3D/NeuGen/badge.png)](https://coveralls.io/r/NeuroBox3D/NeuGen)

### BuildHive
* [![Build Status](https://buildhive.cloudbees.com/job/NeuroBox3D/job/NeuGen/badge/icon)](https://buildhive.cloudbees.com/job/NeuroBox3D/job/NeuGen/)

### Issues
* [![Stories in Backlog](https://badge.waffle.io/NeuroBox3D/NeuGen.png?label=backlog&title=Backlog)](http://waffle.io/NeuroBox3D/NeuGen)
[![Stories in Ready](https://badge.waffle.io/NeuroBox3D/NeuGen.png?label=ready&title=Ready)](http://waffle.io/NeuroBox3D/NeuGen)
[![Stories in In Progress](https://badge.waffle.io/NeuroBox3D/NeuGen.png?label=in progress&title=In Progress)](http://waffle.io/NeuroBox3D/NeuGen)
[![Stories in Done](https://badge.waffle.io/NeuroBox3D/NeuGen.png?label=done&title=Done)](http://waffle.io/NeuroBox3D/NeuGen)

## Documentation
Export .ngx (NeuGen XML) from the GUI by Save Dialog or Ctrl-Shift-N.
One can pre-coarsen by supplying the (global!) level of detail by 
a positive number for the n-parts-density textfield when opening a project.
Also one can specify the cutoff distance for synapse generation by a positive
number in analogue for the synapse-distance textfield. 

## Important note
Hippocampal networks cannot be exported by now to any format correctly, since
either not implemented at all or no synapse export present - see open issues.

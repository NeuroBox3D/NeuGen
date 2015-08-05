#!/bin/sh
## author: stephanmg
## questions: stephan@syntaktischer-zucker.de

# options
EXT_LIBS_DIR=libs/
MAX_HEAP_MEMORY=6000m
MAIN=org.neugen.gui.NeuGenApp
CLASSPATH=dist/NeuGen.jar

# execute
java -Djava.ext.dirs=$EXT_LIBS_DIR -ea -Xmx$MAX_HEAP_MEMORY -Dj3d.implicitAntialiasing=true -cp $CLASSPATH $MAIN

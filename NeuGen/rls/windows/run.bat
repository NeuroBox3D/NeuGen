@echo off

set EXT_LIBS_DIR=..\libs\
set MAX_HEAP_MEMORY=6000m
set MAIN=org.neugen.gui.NeuGenApp
set CLASSPATH=dist\NeuGen.jar
set APPDIR=..\..\

chdir "%APPDIR%"
java -Djava.ext.dirs=$EXT_LIBS_DIR -ea -Xmx$MAX_HEAP_MEMORY -Dj3d.implicitAntialiasing=true -cp "%CLASSPATH%" "%MAIN%"

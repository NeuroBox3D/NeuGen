package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.OutputInfo;

import java.awt.*;

public class Test {
    private static final long serialVersionUID=1;

        @OutputInfo(
                style = "multi-out",
            elemTypes = {String.class, Integer.class, Color.class}
             )
        public Object[] returnValues(){
        return new Object[]{"Return Value", 42, Color.RED};
        }
}

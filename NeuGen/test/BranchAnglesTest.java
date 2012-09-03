/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sergeiwolf
 */
public class BranchAnglesTest {

    private static float grad2rad = (float) (2.0f * Math.PI / 360.0f);

    public BranchAnglesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void hello() {
       float val1 = 0.174533f;
       float val2 = 0.523599f;
       
       float min = 10;
       float max = 30;

       float tmp1 = ((Float) min).floatValue() * grad2rad;

       System.out.println("new min" + tmp1);
       System.out.println("new max: " + max * grad2rad );


     }

}
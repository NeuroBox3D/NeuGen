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
public class StringBufferTest {

    private StringBuffer sB1 = null;
    private StringBuffer sB2 = null;

    public StringBufferTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        sB1 = new StringBuffer();
        sB2 = new StringBuffer();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hello() {
        sB1.append("string eins");
        sB2.append(sB1);
        sB2.append(" noch irgendetwas");
        System.out.println(sB1.toString());
        System.out.println(sB2.toString());

        StringBuffer copy = new StringBuffer("copy buffer");
        
        copy.append(sB1);
        //copy.append("muell");
        System.out.println(copy.toString());
        System.out.println(sB1.toString());
    }

}
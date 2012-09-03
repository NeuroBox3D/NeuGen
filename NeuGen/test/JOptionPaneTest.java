/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JOptionPane;
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
public class JOptionPaneTest {

    public JOptionPaneTest() {
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
         String message = "do you want to load new parameter data?";
         int res = JOptionPane.showConfirmDialog(null, message, "info", JOptionPane.YES_NO_OPTION);
         if (res == JOptionPane.OK_OPTION) {
         }

     }

}
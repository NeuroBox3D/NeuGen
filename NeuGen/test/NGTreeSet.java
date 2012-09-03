import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neugen.datastructures.Section;

/**
 *
 * @author sergeiwolf
 */
public class NGTreeSet {

    public NGTreeSet() {
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
        HashSet<Section> secSet = new HashSet<Section>();
        Section branch0 = new Section();
        Section branch1 = new Section();
        secSet.add(branch1);
        secSet.add(branch1);
        secSet.add(branch0);
        secSet.add(branch0);
        System.out.println("secSet size: " + secSet.size());


         //ArrayList<Sec>

     }

}
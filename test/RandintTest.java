import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author sergeiwolf
 */
public class RandintTest {

    public long randx;
    public RandintTest() {
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


    //@Test
    public void testRandomFile() throws Exception {

        ArrayList<String> randomNumberList = new ArrayList<String>();
        File file = new File("test/randomNumbers.txt");
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str = in.readLine();

        while (str != null) {
            System.out.println(str);
            str = in.readLine();
            if (str != null) {
                randomNumberList.add(str.trim());
            }
        }
        in.close();
        System.out.println("randomNumberList: " + randomNumberList.size());
    }


    @Test
    public void testRand() {
        
        for(int i = 0; i < 1000; i++) {
            long tmp = (1103515245 * randx + 12345);
            long m = (long) Math.pow(2, 31);
            randx =  (tmp % m);
            System.out.println("randx: " + randx);
        }
    }
}

package at.tugraz.ist.ase.debugging;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ConfigurationTest {

    private String result;
    private String result1;

    @BeforeMethod
    public void setUp() {
        StringBuilder st = new StringBuilder();
        StringBuilder st1 = new StringBuilder();

        st.append("showEvaluations:true\n");
        st.append("showDebug:true\n");
        st.append("CF:[10, 20, 50, 100, 500, 1000]\n");
        st.append("TC:[5, 10, 25, 50, 100, 250, 500]\n");
        st.append("numGenFM:3\n");
        st.append("CTC:0.4\n");
        st.append("numIter:3\n");
        st.append("perViolated_nonViolated:0.3\n");

        st1.append(st.toString());

        st.append("dataPath:./data/\n");
        st.append("resultPath:./results/\n");

        st1.append("dataPath:./data/data\n");
        st1.append("resultPath:./results/results\n");

        result = st.toString();
        result1 = st1.toString();
    }

    @Test
    public void testGetInstance() {
        Configuration conf = Configuration.getInstance("src/test/resources/conf.txt");

        assertEquals(conf.toString(), result);
    }

    @Test
    public void testGetInstance1() {
        Configuration conf = Configuration.getInstance("src/test/resources/conf1.txt");

        assertEquals(conf.toString(), result1);
    }

    @Test
    public void testGetInstance2() {
        Configuration conf = Configuration.getInstance("src/test/resources/conf2.txt");

        assertEquals(conf.toString(), result);
    }
}
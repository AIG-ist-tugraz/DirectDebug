/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * Represents a test suite, i.e., a list of test cases.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class TestSuite {
    List<TestCase> testSuite; // list of test cases

    /**
     * Creates an empty TestSuite.
     */
    public TestSuite() {
        testSuite = new ArrayList<>();
    }

    /**
     * Constructor
     * @param file a testsuite file
     */
    public TestSuite(File file) {
        testSuite = new ArrayList<>();

        readTestSuite(file);
    }

    /**
     * Gets the number of test cases.
     * @return the number of test cases.
     */
    public int size() {
        return this.testSuite.size();
    }

    /**
     * Gets all test cases.
     * @return a list of test cases.
     */
    public List<TestCase> getTestSuite() {
        return testSuite;
    }

    /**
     * Sets test cases
     * @param testCaseList
     */
    public void setTestSuite(List<TestCase> testCaseList) {
        this.testSuite = new ArrayList<>(testCaseList);
    }

    // TODO - add setTestcase
    // TODO - support read/write testsuite files

    // support newts
    private void readTestSuite(File file) {
        BufferedReader reader;
        try {

            // Open file
            reader = new BufferedReader(new FileReader(file));

            String line;
            // Read the total number of test cases
            line = reader.readLine();
            int totalTestCases = Integer.parseInt(line);

            // Read all test cases
            for (int i = 0; i < totalTestCases; i++)
            {
                line = reader.readLine();
                TestCase tc = new TestCase(line);
                testSuite.add(tc);
            }

            // Close file
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO - add save

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < testSuite.size(); i++) {
            if (i != 0) st.append("\n");
            st.append(testSuite.get(i));
        }
        return st.toString();
    }
}

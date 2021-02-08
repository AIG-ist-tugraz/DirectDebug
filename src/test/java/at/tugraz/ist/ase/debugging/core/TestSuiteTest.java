/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.core;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class TestSuiteTest {

    private TestSuite testSuite;

    @BeforeMethod

    public void setUp() {
        File fileTC = new File("src/test/resources/FM_10_0.newts.ts");
        testSuite = new TestSuite(fileTC);
    }

    @Test
    public void testSize() {
        assertEquals(testSuite.size(), 11);
    }

    @Test
    public void testTestToString() {
        String st = "~F1 & F2 & ~F6 & ~F3 & F4 & ~F5";
        String st1 = "~F1 & ~F6";

        assertEquals(testSuite.getTestSuite().get(4).toString(), st1);
        assertEquals(testSuite.getTestSuite().get(10).toString(), st);
    }
}
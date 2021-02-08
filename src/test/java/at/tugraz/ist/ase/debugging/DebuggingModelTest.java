/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.debugging.core.TestCase;
import at.tugraz.ist.ase.debugging.core.TestSuite;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.parser.ParserException;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.Variable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.featuremodel.core.Utilities.printConstraints;
import static org.testng.Assert.*;

public class DebuggingModelTest {

    private FeatureModel featureModel;
    private TestSuite testSuite;

    private DebuggingModel debuggingModel;

    @BeforeMethod
    public void setUp() throws ParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        SXFMParser parser = new SXFMParser();
        featureModel = parser.parse(fileFM);

        File fileTC = new File("src/test/resources/FM_10_0.newts.ts");
        testSuite = new TestSuite(fileTC);

        debuggingModel = new DebuggingModel(featureModel, testSuite);
    }

    @Test
    public void TestModel() {
        Model model = debuggingModel.getModel();

        assertEquals(model.getCstrs()[model.getNbCstrs() - 1].toString(), "ARITHM ([FM_10_0 = 1])");
        assertTrue(debuggingModel.getCorrectConstraints().contains(model.getCstrs()[model.getNbCstrs() - 1].toString()));
        assertFalse(debuggingModel.getCorrectConstraints().contains(model.getCstrs()[0].toString()));

        assertEquals(model.getNbCstrs() - 1, debuggingModel.getPossiblyFaultyConstraints().size());

        for (int i = 0; i < model.getNbCstrs() - 1; i++) {
            Constraint c = model.getCstrs()[i];
            assertTrue(debuggingModel.getPossiblyFaultyConstraints().contains(c.toString()));
        }

        assertEquals(model.getCstrs()[0].toString(), "ARITHM ([FM_10_0 + not(F1) >= 1])");
        assertEquals(model.getCstrs()[1].toString(), "ARITHM ([FM_10_0 + not(F2) >= 1])");
        assertEquals(model.getCstrs()[2].toString(), "ARITHM ([F2 + not(FM_10_0) >= 1])");
        assertEquals(model.getCstrs()[3].toString(), "ARITHM ([FM_10_0 + not(F3) >= 1])");
        assertEquals(model.getCstrs()[4].toString(), "ARITHM ([FM_10_0 + not(F4) >= 1])");
        assertEquals(model.getCstrs()[5].toString(), "ARITHM ([FM_10_0 + not(F5) >= 1])");
        assertEquals(model.getCstrs()[6].toString(), "SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])");
        assertEquals(model.getCstrs()[7].toString(), "ARITHM ([FM_10_0 + not(F6) >= 1])");
        assertEquals(model.getCstrs()[8].toString(), "ARITHM ([FM_10_0 + not(F7) >= 1])");
        assertEquals(model.getCstrs()[9].toString(), "ARITHM ([not(F6) + not(F7) >= 1])");
        assertEquals(model.getCstrs()[10].toString(), "SUM ([not(FM_10_0) + F7 + F6 >= 1])");
        assertEquals(model.getCstrs()[11].toString(), "ARITHM ([F2 + not(F8) >= 1])");
        assertEquals(model.getCstrs()[12].toString(), "ARITHM ([F6 + not(F8) >= 1])");
        assertEquals(model.getCstrs()[13].toString(), "ARITHM ([not(F1) + not(F4) >= 1])");
        assertEquals(model.getCstrs()[14].toString(), "SUM ([not(F1) + F7 + F8 >= 1])");
    }

    @Test
    public void TestTestCases() {
        TestCase t1 = debuggingModel.getTestCase("FM_10_0");
        assertEquals(t1.getConstraints().get(0).toString(),
                "ARITHM ([FM_10_0 = 1])");

        TestCase t2 = debuggingModel.getTestCase("F1");
        assertEquals(t2.getConstraints().get(0).toString(),
                "ARITHM ([F1 = 1])");

        TestCase t3 = debuggingModel.getTestCase("~F6");
        assertEquals(t3.getConstraints().get(0).toString(),
                "ARITHM ([not(F6) = 1])");

        TestCase t4 = debuggingModel.getTestCase("~F1 & F6");
        assertEquals(t4.getConstraints().size(), 2);
        assertEquals(t4.getConstraints().get(0).toString(),
                "ARITHM ([F6 = 1])");
        assertEquals(t4.getConstraints().get(1).toString(),
                "ARITHM ([not(F1) = 1])");

        TestCase t5 = debuggingModel.getTestCase("~F1 & ~F6");
        assertEquals(t5.getConstraints().size(), 2);
        assertEquals(t5.getConstraints().get(0).toString(),
                "ARITHM ([not(F1) = 1])");
        assertEquals(t5.getConstraints().get(1).toString(),
                "ARITHM ([not(F6) = 1])");

        TestCase t6 = debuggingModel.getTestCase("~F1 & F2 & ~F6 & ~F3 & F4 & F5");
        assertEquals(t6.getConstraints().size(), 6);
        assertEquals(t6.getConstraints().get(0).toString(),
                "ARITHM ([F2 = 1])");
        assertEquals(t6.getConstraints().get(1).toString(),
                "ARITHM ([F4 = 1])");
        assertEquals(t6.getConstraints().get(2).toString(),
                "ARITHM ([F5 = 1])");
        assertEquals(t6.getConstraints().get(3).toString(),
                "ARITHM ([not(F1) = 1])");
        assertEquals(t6.getConstraints().get(4).toString(),
                "ARITHM ([not(F3) = 1])");
        assertEquals(t6.getConstraints().get(5).toString(),
                "ARITHM ([not(F6) = 1])");
    }

    @Test
    public void TestVariables() {
        Model model = debuggingModel.getModel();

        int i = 0;
        for (String var: debuggingModel.getVariables()) {
            Variable v = model.getVar(i);
            assertEquals(v.getName(), var);
            i++;
        }

    }
}
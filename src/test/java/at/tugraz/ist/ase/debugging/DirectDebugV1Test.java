package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.MBDiagLib.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement;
import at.tugraz.ist.ase.debugging.core.TestSuite;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.parser.ParserException;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;
import org.apache.commons.collections4.IteratorUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.*;
import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.COUNTER_CONSISTENCY_CHECKS;
import static org.testng.Assert.*;

public class DirectDebugV1Test {

    private FeatureModel featureModel;
    private TestSuite testSuite;

    private DebuggingModel debuggingModel;
    private ChocoConsistencyChecker checker;

    @BeforeMethod
    public void setUp() throws ParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        SXFMParser parser = new SXFMParser();
        featureModel = parser.parse(fileFM);

        File fileTC = new File("src/test/resources/FM_10_0.newts.ts");
        testSuite = new TestSuite(fileTC);

        debuggingModel = new DebuggingModel(featureModel, testSuite);

        checker = new ChocoConsistencyChecker(debuggingModel);
    }

    @Test
    public void testDebug() {
        Set<String> diag = null;

        DirectDebug directDebug = new DirectDebug(checker);

        PerformanceMeasurement.reset();
        diag = directDebug.debug(debuggingModel.getPossiblyFaultyConstraints(),
                debuggingModel.getCorrectConstraints(),
                debuggingModel.getTestcases());

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) getTimer(TIMER_FIRST).total() / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()));

        Set<String> cs = new LinkedHashSet<>();
        cs.add((String) IteratorUtils.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 2));
        cs.add((String) IteratorUtils.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 9));
        cs.add((String) IteratorUtils.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 13));

        assertEquals(diag, cs);
    }
}
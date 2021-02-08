/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.testcases;

import at.tugraz.ist.ase.MBDiagLib.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.MBDiagLib.model.KBModel;
import at.tugraz.ist.ase.debugging.DebuggingModel;
import at.tugraz.ist.ase.debugging.core.TestCase;
import at.tugraz.ist.ase.debugging.core.TestSuite;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.core.FeatureModelException;
import at.tugraz.ist.ase.featuremodel.parser.ParserException;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;
import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class that classifies test cases into non-violated test cases and violated test cases.
 * The input of this class is a CSV file in which:
 * - first column is the path to a feature model file
 * - second column is the path to the corresponding testsuite file of the feature model
 * - third column is the path to the corresponding classified testsuite file of the feature model
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class TestCasesClassifier {

    private static int totalTestCases;
    private String configurationFile;

    /**
     * A constructor
     * @param configurationFile
     */
    public TestCasesClassifier(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    public void classify() throws IOException, ParserException, FeatureModelException {
        SXFMParser parser = new SXFMParser();

        // filepath to configuration.csv
        System.out.println("Reading configurations...");
        List<List<String>> configurations = readConfigurations(configurationFile);
        System.out.println("#Configurations: " + configurations.size());

        System.out.println();
        // Take each file in datasets/
        for (List<String> conf : configurations) {
            File fileFM = new File(conf.get(0)); // feature model
            File fileTS = new File(conf.get(1)); // testsuite
            File fileCTS = new File(conf.get(2)); // classified testsuite

            System.out.println("Feature model: " + fileFM.getName());
            System.out.println("Testsuite: " + fileTS.getName());

            FeatureModel featureModel = parser.parse(fileFM);
            TestSuite testSuite = readTestSuite(fileTS);

            KBModel model = new DebuggingModel(featureModel, testSuite);
            Set<String> C = model.getAllConstraints();

            ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);

            int totalViolatedTC = 0;
            int totalNonViolatedTC = 0;

            List<String> nonviolatedTestCases = new ArrayList<>();
            List<String> violatedTestCases = new ArrayList<>();

            System.out.println("\tTotal of test cases: " + totalTestCases);
            System.out.print("\tClassifying...");

            for (TestCase tc : testSuite.getTestSuite()) {
                if (checker.isConsistent(C, tc.toString())) {
                    totalNonViolatedTC += 1;
                    nonviolatedTestCases.add(tc.toString());
                    tc.setViolated(true);
                } else {
                    totalViolatedTC += 1;
                    violatedTestCases.add(tc.toString());
                    tc.setViolated(false);
                }
            }

            System.out.println();
            System.out.println("\tTotal of non-violated test cases: " + totalNonViolatedTC);
            System.out.println("\tTotal of violated test cases: " + totalViolatedTC);

            // WRITE TO FILE
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileCTS));
            System.out.println("\tSaving to file " + fileCTS.getName() + "...");

            // writing violated test cases
            writer.write(totalViolatedTC + "\n");
            for (String tc : violatedTestCases) {
                writer.write(tc + "\n");
                writer.flush();
            }

            // writing non-violated test cases
            writer.write(totalNonViolatedTC + "\n");
            for (String tc : nonviolatedTestCases) {
                writer.write(tc + "\n");
                writer.flush();
            }

            // close file
            writer.close();
        }
        System.out.println("DONE");
    }

    private static List<List<String>> readConfigurations(String filepath) {
        List<List<String>> configurations = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filepath))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                List<String> conf = new ArrayList<>();

                conf.add(lineInArray[0]);
                conf.add(lineInArray[1]);
                conf.add(lineInArray[2]);

                configurations.add(conf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return configurations;
    }

    private static TestSuite readTestSuite(File file) {
        BufferedReader reader;
        TestSuite testSuite = new TestSuite();
        List<TestCase> testCaseList = new ArrayList<>();
        try {

            // Open file
            reader = new BufferedReader(new FileReader(file));

            readHeaderOfTestSuite(reader);

            // Read all test cases
            String line;
            for (int i = 0; i < totalTestCases; i++)
            {
                line = reader.readLine();
                TestCase tc = new TestCase(line);
                testCaseList.add(tc);
            }

            testSuite.setTestSuite(testCaseList);

            // Close file
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testSuite;
    }

    private static void readHeaderOfTestSuite(BufferedReader reader) throws IOException {
        String line;
        // Read the total number of test cases
        line = reader.readLine();
        totalTestCases = Integer.parseInt(line);

        // Read the number of test cases for dead features
        line = reader.readLine();

        // Read the number of test cases for false optional
        line = reader.readLine();

        // Read the number of test cases for full mandatory
        line = reader.readLine();

        // Read the number of test cases for false mandatory
        line = reader.readLine();

        // Read the number of test cases for for partial configuration
        line = reader.readLine();
    }
}

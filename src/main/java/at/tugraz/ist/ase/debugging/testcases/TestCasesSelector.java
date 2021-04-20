/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.testcases;

import at.tugraz.ist.ase.debugging.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class that selects test cases (scenarios) for the DirectDebug evaluation.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class TestCasesSelector {
    private Configuration conf;

    private final List<String> violatedTestCases = new ArrayList<>();
    private final List<String> nonviolatedTestCases = new ArrayList<>();
    private int totalViolatedTestCases;
    private int totalNonViolatedTestCases;

    public TestCasesSelector(Configuration conf) {
        this.conf = conf;
    }

    public void select(int numScenarios, double violatedPercent) throws IOException {

        // Take each file in datasets/
        for (int numConstraints: conf.getCardCF()) {
            for (int i = 0; i < conf.getNumGenFM(); i++) {
                File fileTS = new File(conf.getClassifiedTSFilenameInData(numConstraints, i)); // classified testsuite
                String pathTestCases = conf.getScenariosPathInResults(); // path to where stores test cases

                System.out.println("Testsuite: " + fileTS.getName());

                select(fileTS, pathTestCases, numScenarios, violatedPercent);
            }
        }
        System.out.println("DONE");
    }

    private void select(File fileTS, String pathTestCases, int numTestCases, double violatedPercent) throws IOException {
        // Read test suite
        System.out.println("\tLoading test suite...");
        readTestSuite(fileTS);
        System.out.println("\t\t#Non-violated test cases: " + totalNonViolatedTestCases);
        System.out.println("\t\t#Violated test cases: " + totalViolatedTestCases);

        System.out.println("\tSelecting with " + (violatedPercent * 100) + " test cases which are violated");

        if (totalNonViolatedTestCases == 0) return;

        // select test cases
        for (int total : conf.getCardTS()) {
            System.out.println("\t\tCardinality: " + total);

            for (int count = 0; count < numTestCases; count++) {
                // calculate the real number of test cases
                int numViolatedTC = Math.min(Math.max((int)Math.round(total * violatedPercent), 1), totalViolatedTestCases);
                int numNonViolatedTC = Math.min(total - numViolatedTC, totalNonViolatedTestCases);

                if ((numNonViolatedTC + numViolatedTC) != total) {
                    numViolatedTC = total - numNonViolatedTC;
                }

                if (numViolatedTC > totalViolatedTestCases) {
                    System.out.println("NOT ENOUGH NON_VIOLATED TEST CASES!!!!!!");
                    System.in.read();
                }

                System.out.println("\t\t\tSelecting scenario " + (count + 1) + "...");

                List<String> testcases = new ArrayList<>();

                Collections.shuffle(violatedTestCases);
                Collections.shuffle(nonviolatedTestCases);

                // selecting violated test cases
                for (int j = 0; j < numViolatedTC; j++) {
                    testcases.add(violatedTestCases.get(j));
                }

                // selecting non-violated test cases
                for (int j = 0; j < numNonViolatedTC; j++) {
                    testcases.add(nonviolatedTestCases.get(j));
                }

                Collections.shuffle(testcases);

                System.out.println("\t\t\t\t#Selected non-violated test cases: " + numNonViolatedTC);
                System.out.println("\t\t\t\t#Selected violated test cases: " + numViolatedTC);

                writeToFile(fileTS, pathTestCases, testcases, total, count);
            }
        }
    }

    private void readTestSuite(File fileTS) {
        BufferedReader reader;
        String line;
        try {

            // Open file
            reader = new BufferedReader(new FileReader(fileTS));

            // Read the total number of violated test cases
            line = reader.readLine();
            totalViolatedTestCases = Integer.parseInt(line);

            // Read all violated test cases
            for (int i = 0; i < totalViolatedTestCases; i++)
            {
                line = reader.readLine();
                violatedTestCases.add(line);
            }

            // Read the total number of non-violated test cases
            line = reader.readLine();
            totalNonViolatedTestCases = Integer.parseInt(line);

            // Read all violated test cases
            for (int i = 0; i < totalNonViolatedTestCases; i++)
            {
                line = reader.readLine();
                nonviolatedTestCases.add(line);
            }

            // Close file
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createFileName(String pathTestCases, String name, int cardinality, int index) {
        return pathTestCases + "/" + name + "_c" + cardinality + "_" + index + ".testcases";
    }

    private void writeToFile(File fileTS, String pathTestCases, List<String> testcases, int cardinality, int index) throws IOException {
        String fileNameWithoutExtension = fileTS.getName().substring(0, fileTS.getName().length() - 13);
        String fileName = createFileName(pathTestCases, fileNameWithoutExtension, cardinality, index);

        System.out.println("\t\t\tSave to file... " + fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write(testcases.size() + "\n");

        for (String st: testcases) {
            writer.write(st + "\n");
            writer.flush();
        }

        // close file
        writer.close();
    }
}
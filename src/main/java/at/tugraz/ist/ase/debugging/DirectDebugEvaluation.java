/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.MBDiagLib.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement;
import at.tugraz.ist.ase.MBDiagLib.model.KBModel;
import at.tugraz.ist.ase.debugging.core.TestSuite;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.parser.ParserException;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;
import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.MBDiagLib.core.DebugConfigurations.showEvaluations;
import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.*;

/**
 * An evaluation of the DirectDebug algorithm.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class DirectDebugEvaluation {
    private String configurationFile;
    private String filesave;

    public DirectDebugEvaluation(String configurationFile, String filesave) {
        this.configurationFile = configurationFile;
        this.filesave = filesave;
    }

    private List<List<String>> readConfigurations(String filepath) {
        List<List<String>> configurations = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filepath))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                List<String> conf = Arrays.asList(lineInArray);

                configurations.add(conf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return configurations;
    }

    private double[][] createResultsTable(int col, int row) {
        double[][] results = new double[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                results[i][j] = 0.0;
            }
        }
        return results;
    }

    // EVALUATION
    public void evaluate() throws ParserException, IOException {

        System.out.println("Reading configurations...");
        List<List<String>> configurations = readConfigurations(configurationFile);
        System.out.println("#Configurations: " + configurations.size());
        System.out.println();

        System.out.println("Preparing evaluation...");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filesave)); // "data/results.txt"

        SXFMParser parser = new SXFMParser();
        showEvaluations = true;

        int col = 6;
        int row = 7;
        double[][] results = createResultsTable(col, row);

        System.out.println("Evaluating...");
        // first loop just aims to start JVM
        // second loop will measure the runtimes
        for (int count = 0; count < 2; count++) {
            int indexCol = 0;

            if (count == 0)
                System.out.println("\tA warmup round for Java Virtual Machine...");
            else if (count == 1)
                System.out.println("\tMeasuring the runtime...");

            for (List<String> column : configurations) {
                int indexRow = 0;
                for (String cell : column) {
                    List<List<String>> scenarios = readConfigurations(cell);

                    for (List<String> scenario : scenarios) {
                        File fileFM = new File(scenario.get(0));

                        for (int i = 1; i < 4; i++) {
                            FeatureModel featureModel = parser.parse(fileFM);

                            File fileTC = new File(scenario.get(i));
                            TestSuite testSuite = new TestSuite(fileTC);

                            System.out.print("\t\t" + fileFM.getName().toUpperCase());
                            System.out.println("\t" + fileTC.getName().toUpperCase());
                            if (count == 1) {
                                writer.write("\t\t" + fileFM.getName().toUpperCase());
                                writer.write("\t" + fileTC.getName().toUpperCase() + "\n");
                            }

                            KBModel debuggingModel = new DebuggingModel(featureModel, testSuite);
                            ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

                            Set<String> diag = null;

                            DirectDebug directDebug = new DirectDebug(checker);

                            PerformanceMeasurement.reset();
                            diag = directDebug.debug(debuggingModel.getPossiblyFaultyConstraints(),
                                    debuggingModel.getCorrectConstraints(),
                                    debuggingModel.getTestcases());

                            if (!diag.isEmpty()) {
                                System.out.println("\t\t\tDiag size: " + diag.size());
                                if (count == 1) {
                                    writer.write("\t\t\tDiag: " + diag + "\n");
                                    writer.write("\t\t\tDiag size: " + diag.size() + "\n");
                                }

                                if (count == 1) {
                                    results[indexCol][indexRow] += (double) getTimer(TIMER_FIRST).total() / 1_000_000_000.0;

                                    System.out.println("\t\t\tTime for first: " + ((double)getTimer(TIMER_FIRST).total() / 1_000_000_000.0));
                                }
                            } else {
                                System.out.println("\t\t\tNO DIAGNOSIS----------------");
                                if (count == 1) writer.write("\t\t\tNO DIAGNOSIS----------------" + "\n");
                            }
                            if (count == 1) {
                                printEvaluations(1, writer);
                                writer.flush();
                            }
                        }
                    }
                    indexRow++;
                }
                indexCol++;
            }
        }

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                results[i][j] /= 9;
            }
        }

        System.out.println();
        System.out.println("Direct Debug Evaluation Results:");
        for (int j = 0; j < row; j++) {
            for (int i = 0; i < col; i++) {
                System.out.print(results[i][j] + "\t");
                writer.write(results[i][j] + "\t");
            }
            System.out.println();
            writer.write("\n");
        }

        System.out.println("DONE");

        // close file
        writer.close();
    }
}

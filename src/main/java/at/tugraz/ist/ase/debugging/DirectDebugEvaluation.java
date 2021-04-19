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

import java.io.*;
import java.util.Set;

import static at.tugraz.ist.ase.MBDiagLib.core.DebugConfigurations.showDebugs;
import static at.tugraz.ist.ase.MBDiagLib.core.DebugConfigurations.showEvaluations;
import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.*;

/**
 * An evaluation of the DirectDebug algorithm.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class DirectDebugEvaluation {
    private Configuration conf;

    public DirectDebugEvaluation(Configuration conf) {
        this.conf = conf;
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

        System.out.println("Preparing evaluation...");
        BufferedWriter writer = new BufferedWriter(new FileWriter(conf.getResultPath() + "results.txt")); // "data/results.txt"

        SXFMParser parser = new SXFMParser();
        showEvaluations = conf.getShowEvaluation();
        showDebugs = conf.getShowDebug();

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

            for (int numFeatures: conf.getCardCF()) {
                int indexRow = 0;
                for (int numTS: conf.getCardTS()) {

                    for (int i = 0; i < conf.getNumGenFM(); i++) {
                        File fileFM = new File(conf.getFMSFilenameInData(numFeatures, i));

                        // take #iter scenarios
                        for (int j = 0; j < conf.getNumIter(); j++) {
                            FeatureModel featureModel = parser.parse(fileFM);

                            File fileTC = new File(conf.getScenariosFilenameInData(numFeatures, i, numTS, j));
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

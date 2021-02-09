/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.debugging.testcases.TestCasesClassifier;
import at.tugraz.ist.ase.debugging.testcases.TestCasesSelector;
import at.tugraz.ist.ase.debugging.testcases.TestSuiteGenerator;
import at.tugraz.ist.ase.featuremodel.apps.FMGenerator;
import at.tugraz.ist.ase.featuremodel.apps.FMStatistics;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;

import java.io.File;

/**
 * Main app.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0 || args[0].equals("-h")) {
            help();
        } else {
            switch (args[0]) {
                case "-s": // statistics
                    // dd -s ./data/fms statistics.txt
                    if (args.length == 3) {
                        FMStatistics stat = new FMStatistics(false, args[1], args[2]);
                        stat.calculate();
                    } else {
                        help();
                    }
                    break;
                case "-g": // feature model generating
                    // dd -g 2 500 40 10000 ./data/fms
                    if (args.length == 6) {
                        FMGenerator.generate(Integer.parseInt(args[1]),
                                Integer.parseInt(args[2]),
                                Integer.parseInt(args[3]),
                                Integer.parseInt(args[4]),
                                args[5]);
                    } else {
                        help();
                    }
                    break;
                case "-ts": // testsuite generation
                    // dd -ts -g ./data/fms ./data/testsuite 1000 false
                    if (args.length == 6 && args[1].equals("-g")) {
                        SXFMParser parser = new SXFMParser();

                        File folder = new File(args[2]);
                        String pathtoSave = args[3];
                        int maxCombinations = Integer.parseInt(args[4]);
                        boolean randomlySearch = Boolean.parseBoolean(args[5]);

                        // Takes each file in the folder
                        for (final File file : folder.listFiles()) {
                            if (file.getName().endsWith(".splx")) {

                                String filename = file.getName();
                                System.out.println(filename);

                                FeatureModel featureModel = parser.parse(file);

                                TestSuiteGenerator generator = new TestSuiteGenerator(featureModel, filename);

                                System.out.println("\tGenerating...");
                                generator.generate(pathtoSave, maxCombinations, randomlySearch);

                                System.out.println("Done - " + file.getName());
                            }
                        }
                    } else {
                        help();
                    }
                    break;
                case "-tc":
                    if (args.length == 3 && args[1].equals("-c")) { // classifies test cases
                        // dd -tc -c ./data/configurations.csv
                        TestCasesClassifier classification = new TestCasesClassifier(args[2]);
                        classification.classify();
                    } else if (args.length == 5 && args[1].equals("-s")) { // selects test cases
                        // dd -tc -s ./data/configurations.csv 5 0.3
                        int numScenarios = Integer.parseInt(args[3]);
                        double violatedPercent = Double.parseDouble(args[4]);

                        TestCasesSelector selector = new TestCasesSelector(args[2]);
                        selector.select(numScenarios, violatedPercent);
                    } else {
                        help();
                    }
                    break;
                case "-e":
                    if (args.length == 3) {
                        // dd -e ./data/conf/conf.csv ./data/results2.txt
                        DirectDebugEvaluation evaluator = new DirectDebugEvaluation(args[1], args[2]);
                        evaluator.evaluate();
                    } else {
                        help();
                    }
                    break;
            }
        }
    }

    private static void help() {
        System.out.println("DirectDebug Evaluation");
        System.out.println("Help: dd -h");
        System.out.println("FMStatistics: dd -s <folder path> <output file path>");
        System.out.println("FM generation: dd -g <#feature models> <#features> <%CTC> <#max products> <path to save>");
        System.out.println("Testsuite generation: dd -ts -g <folder path> <path to save> <#max combinations> <randomly search>");
        System.out.println("Test cases classification: dd -tc -c <configuration file>");
        System.out.println("Test cases selection: dd -tc -s <configuration file> <#scenarios> <%violated test cases>");
        System.out.println("DirectDebug evaluation: dd -e <configuration file>");
    }
}

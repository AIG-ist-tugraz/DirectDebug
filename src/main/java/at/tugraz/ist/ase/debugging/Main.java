package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.debugging.testcases.TestCasesClassifier;
import at.tugraz.ist.ase.debugging.testcases.TestCasesSelector;
import at.tugraz.ist.ase.debugging.testcases.TestSuiteGenerator;
import at.tugraz.ist.ase.featuremodel.apps.FMGenerator;
import at.tugraz.ist.ase.featuremodel.apps.FMStatistics;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;

import java.io.File;

import static at.tugraz.ist.ase.featuremodel.core.Utilities.checkAndCreateFolder;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0 || args[0].equals("-h")) {
            help();
        } else {
            Configuration conf;
            if (args.length == 2) {
                conf = Configuration.getInstance(args[1]);

                switch (args[0]) {
                    case "-st": // statistics
                        // [old version] d2bug_eval -st ./data/fms statistics.txt
                        // d2bug_eval -st conf.txt
                        // calculate the statistics for all feature models stored in "dataPath + /fms"
                        // the results will be stored in "resultsPath + /statistics.txt"
                        FMStatistics stat = new FMStatistics(false, conf);
                        stat.calculate();
                        break;
                    case "-g": // feature model generating
                        // [old version] d2bug_eval -g 2 500 40 10000 ./data/fms
                        // d2bug_eval -g conf.txt
                        // the generated feature models will be stored in "resultsPath + /fms"
                        FMGenerator.generate(conf);
                        break;
                    case "-ts": // testsuite generation
                        // [old version] d2bug_eval -ts -g ./data/fms ./data/testsuite 1000 false
                        // d2bug_eval -ts conf1.txt
                        // The feature models have to store in "dataPath + /fms"
                        // Generated testsuites will be stored in "resultsPath + /testsuite"
                        SXFMParser parser = new SXFMParser();

                        File folder = new File(conf.getFMSPathInData());
                        String pathtoSave = conf.getTestSuitePathInResults();
                        checkAndCreateFolder(pathtoSave);
                        int maxCombinations = 10000;
                        boolean randomlySearch = false;

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
                        break;
                    case "-tc": // test case classification
                        // [old version] d2bug_eval -tc -c ./data/configurations.csv
                        // d2bug_eval -tc conf.txt
                        // The feature models have to store in "dataPath + /fms"
                        // The testsuites have to store in "dataPath + /testsuite"
                        // Classified testsuites will be stored in "resultsPath + /classifiedTS"
                        checkAndCreateFolder(conf.getClassifiedTSPathInResults());
                        TestCasesClassifier classification = new TestCasesClassifier(conf);
                        classification.classify();
                        break;
                    case "-ss": // scenario selection
                        // [old version] d2bug_eval -tc -s ./data/configurations.csv 3 0.3
                        // d2bug_eval -ss conf.txt
                        // The classified testsuites have to store in "dataPath + /classifiedTS"
                        // Scenarios will be stored in "resultsPath + /scenarios"
                        checkAndCreateFolder(conf.getScenariosPathInResults());
                        TestCasesSelector selector = new TestCasesSelector(conf);
                        selector.select(conf.getNumIter(), conf.getPerViolated_nonViolated());
                        break;
                    case "-e": // DirectDebug evaluation
                        // [old version] d2bug_eval -e ./data/conf/conf.csv ./data/results2.txt
                        // d2bug_eval -e conf.txt
                        DirectDebugEvaluation evaluator = new DirectDebugEvaluation(conf);
                        evaluator.evaluate();
                        break;
                }
            } else {
                help();
            }
        }
    }

    private static void help() {
        System.out.println("d2bug_eval - DirectDebug Evaluation");
        System.out.println("Help: d2bug_eval -h");
        System.out.println("FMStatistics: d2bug_eval -st <configuration file>");
        System.out.println("FM generation: d2bug_eval -g <configuration file>");
        System.out.println("Testsuite generation: d2bug_eval -ts <configuration file>");
        System.out.println("Test case classification: d2bug_eval -tc <configuration file>");
        System.out.println("Scenario selection: d2bug_eval -ss <configuration file>");
        System.out.println("DirectDebug evaluation: d2bug_eval -e <configuration file>");
    }
}

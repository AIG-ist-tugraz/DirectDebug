/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.testcases;

import at.tugraz.ist.ase.MBDiagLib.core.RandomGaussian;
import at.tugraz.ist.ase.featuremodel.core.Feature;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.core.FeatureModelException;
import at.tugraz.ist.ase.featuremodel.parser.ParserException;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.IteratorUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class that generates a testsuite for a feature model.
 * Generated test cases includes dead feature test cases, full mandatory test cases,
 * false optional test cases, false mandatory test cases, and partial configuration test cases.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class TestSuiteGenerator {
    private final FeatureModel featureModel;
    private String filename;

    private final List<String> testsuite = new ArrayList<>();
    private int numDeadFeatures;
    private int numFalseOptional;
    private int numFullMandatory;
    private int numFalseMandatory;
    private int numPartialConfiguration;

    public TestSuiteGenerator(FeatureModel featureModel, String filename) {
        this.featureModel = featureModel;
        this.filename = filename;
    }

    public void generate(String pathtoSave, int maxCombinations, boolean randomlySearch) throws IOException, FeatureModelException {
        int oldSize;
        // generate test cases for dead feature property
        System.out.println("\tGenerating test cases for dead feature property");
        generateDeadFeatureTestCases();
        numDeadFeatures = testsuite.size();

        // generate test cases for false optional property
        System.out.println("\tGenerating test cases for false optional property");
        oldSize = testsuite.size();
        generateFalseOptionalTestCases();
        numFalseOptional = testsuite.size() - oldSize;

        // generate test cases for full mandatory property
        System.out.println("\tGenerating test cases for full mandatory property");
        oldSize = testsuite.size();
        generateFullMandatoryTestCases();
        numFullMandatory = testsuite.size() - oldSize;

        // generate test cases for false mandatory property
        System.out.println("\tGenerating test cases for false mandatory property");
        oldSize = testsuite.size();
        generateFalseMandatoryTestCases();
        numFalseMandatory = testsuite.size() - oldSize;

        // generate test cases for partial configuration
        System.out.println("\tGenerating test cases for partial configuration");
        oldSize = testsuite.size();
        generatePartialConfigurationTestCases(maxCombinations, randomlySearch);
        numPartialConfiguration = testsuite.size() - oldSize;

        int sum = numDeadFeatures + numFalseOptional + numFullMandatory + numFalseMandatory + numPartialConfiguration;
        System.out.println("\tNumber of generated test cases: " + sum);
        System.out.println("\tNumber of generated test cases for dead features: " + numDeadFeatures);
        System.out.println("\tNumber of generated test cases for false optional: " + numFalseOptional);
        System.out.println("\tNumber of generated test cases for full mandatory: " + numFullMandatory);
        System.out.println("\tNumber of generated test cases for false mandatory: " + numFalseMandatory);
        System.out.println("\tNumber of generated test cases for partial configuration: " + numPartialConfiguration);

        // write to file
        System.out.println("\tWriting to file...");
        writeToFile(pathtoSave);
    }

    private void generateDeadFeatureTestCases() throws FeatureModelException {
        // create dead feature test cases
        for (int i = 1; i < featureModel.getNumOfFeatures(); i++) {
            Feature feature = featureModel.getFeature(i);
            // if feature is not the root feature
            // and doesn't exist in testsuite
            if (!testsuite.contains(feature.getName())) {
                testsuite.add(feature.getName()); // f_i = true
            }
        }
    }

    // Conditionally Dead is not an error
//    private static void generateConditionallyDeadTestCases(FeatureModel featureModel) throws IOException {
//        // create file name
//        String fileName = createFileName("conditionallydead", featureModel.getName());
//        // open file
//        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//
//        // create conditionally dead test cases
////        for (int i = 1; i < featureModel.getNumOfFeatures(); i++) {
////            Feature fi = featureModel.getFeature(i);
////
////            // a feature is not DEAD and have to be optional
////            if (featureModel.isOptionalFeature(fi)) {
////
////                for (int j = 1; j < featureModel.getNumOfFeatures(); j++) {
////                    if (j != i) {
////                        Feature fj = featureModel.getFeature(j);
////
////                        // add {fi = true}
////                        model.addClauseTrue(vi);
////                        // add {fj = true}
////                        model.addClauseTrue(vj);
////
//////                        System.out.println("Before the checking----------------");
//////                        System.out.println(model.getNbCstrs());
//////                        printConstraints(model);
////
////                        model.getSolver().reset();
////                        if (!model.getSolver().solve()) {
//////                            System.out.println("------------> inConsistent: " + fi.toString());
////
////                            if (!isExistInArrayList(conditionallydeads, fi.toString())) { // neu chua moi them vao
////                                conditionallydeads.add(fi.toString());
////                                fi.setAnomalyType(CONDITIONALLYDEAD);
////
////                                List<Set<Constraint>> allDiag = calculateAllDiagnoses();
////
//////                                System.out.println("All diagnoses:---------------------------");
//////                                printAllDiagnoses(allDiag);
////
////                                explanations.put(fi.getName(), new ArrayList<>());
////                                createExplanations(allDiag, fi.getName(), explanations);
////                            }
////                        }
////                    }
////                }
////            }
////        }
//
//        for (Feature f: featureModel.getFeatures()) {
//            writer.write(f.getName() + "\n");
//        }
//
//        // close file
//        writer.close();
//    }

    private void generateFullMandatoryTestCases() throws FeatureModelException {
        // create full mandatory test cases
        for (int i = 1; i < featureModel.getNumOfFeatures(); i++) {
            Feature feature = featureModel.getFeature(i);

            // check whether the feature is optional
            if (featureModel.isOptionalFeature(feature)) {
                // add {f_opt = false}
                String testcase = "~" + feature.getName();
                if (!testsuite.contains(testcase)) {
                    testsuite.add(testcase);
                }
            }
        }
    }

    private void generateFalseOptionalTestCases() throws FeatureModelException {
        // create false optional test cases
        // f_p = true /\ f_opt = false
        for (int i = 1; i < featureModel.getNumOfFeatures(); i++) {
            Feature feature = featureModel.getFeature(i);

            // check whether the feature is optional
            if (featureModel.isOptionalFeature(feature)) {

                ArrayList<Feature> parents = featureModel.getMandatoryParents(feature);

                if (parents.size() > 0) { // and in the right part of a requires constraint

                    // add {f_opt = false}
                    String tc = "~" + feature.getName();

                    for (Feature parent : parents) {

                        if (featureModel.isMandatoryFeature(parent)) { // trong mot so truong hop no co the bat cau
                            // add {f_p = true}
                            String testcase = tc + " & " + parent.getName();

                            if (!testsuite.contains(testcase)) {
                                testsuite.add(testcase);
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateFalseMandatoryTestCases() throws FeatureModelException {
        // create false optional test cases
        for (int i = 1; i < featureModel.getNumOfFeatures(); i++) {
            Feature feature = featureModel.getFeature(i);

            // check whether the feature is optional
            if (featureModel.isMandatoryFeature(feature)) {
                String testcase = "~" + feature.getName();

                if (!testsuite.contains(testcase)) {
                    testsuite.add(testcase);
                }
            }
        }
    }

    private static final int MAX_ITEMS = 5;
    private void generatePartialConfigurationTestCases(int maxCombinations, boolean randomlySearch) throws FeatureModelException {
        int numTC = 0;
        int numFeatures = featureModel.getNumOfFeatures() - 1; // ignore f_0

        Set<Integer> targetSet = Sets.newHashSet(createIndexesArray(numFeatures));

        int numItems = Math.min(numFeatures, MAX_ITEMS);

        for (int numSelectedFeatures = 2; numSelectedFeatures <= numItems; numSelectedFeatures++) {
            System.out.println("\tGenerating test cases for partial configuration with cardinality: " + numSelectedFeatures);

            Set<Set<Integer>> combinations = Sets.combinations(targetSet, numSelectedFeatures);

            System.out.println("\t\tNumber of Combinations: " + combinations.size());

            if (combinations.size() > maxCombinations) {
                int max = maxCombinations;

                if (randomlySearch) {
                    List<Set<Integer>> list = new ArrayList<>(combinations);
//                    Collections.shuffle(list);
                    Set<Integer> indexes = getRandomlyIndexes(max, combinations.size());

                    for (int i = 0; i < indexes.size(); i++) {
                        Integer index = IteratorUtils.get(indexes.iterator(), i);

                        Set<Integer> selected = IteratorUtils.get(combinations.iterator(), index);

                        List<String> tcs = new ArrayList<>();
                        tcs.add("");
                        for (Integer id : selected) {
                            Feature f = featureModel.getFeature(id);

                            tcs = generateTestCases(tcs, f.getName());
                        }

                        for (String tc : tcs) {
                            if (!testsuite.contains(tc)) {
                                numTC = numTC + 1;
                                testsuite.add(tc);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < max; i++) {
                        Set<Integer> selected = IteratorUtils.get(combinations.iterator(), i);

                        List<String> tcs = new ArrayList<>();
                        tcs.add("");
                        for (Integer id : selected) {
                            Feature f = featureModel.getFeature(id);

                            tcs = generateTestCases(tcs, f.getName());
                        }

                        for (String tc : tcs) {
                            if (!testsuite.contains(tc)) {
                                numTC = numTC + 1;
                                testsuite.add(tc);
                            }
                        }
                    }
                }
            } else {
                for (Set<Integer> indexes: combinations) {
                    List<String> tcs = new ArrayList<>();
                    tcs.add("");
                    for (Integer id : indexes) {
                        Feature f = featureModel.getFeature(id);

                        tcs = generateTestCases(tcs, f.getName());
                    }

                    for (String tc : tcs) {
                        if (!testsuite.contains(tc)) {
                            numTC = numTC + 1;
                            testsuite.add(tc);
                        }
                    }
                }
            }

            System.out.println("\t\tNumber of added test cases: " + numTC);
        }
    }

    private Set<Integer> getRandomlyIndexes(int num, int size) {
        Set<Integer> indexes = new LinkedHashSet<>();

        int id = getRandom(0, size - 1);
        int maxStep = Math.max((size / num) - 1, 1);

        int count = 0;
        while (count < num) {
            if (!indexes.contains(id)) {
                indexes.add(id);
                count++;
            }

            int step = maxStep == 1 ? 1 : getRandom(1, maxStep);

            if ((size - id) > step) {
                id = id + step;
            } else {
                id = step - (size - id);
            }
        }

        return indexes;
    }

    private List<String> generateTestCases(List<String> tcs, String fName) {
        List<String> newTCS = new ArrayList<>();

        for (String tc: tcs) {
            if (tc.isEmpty()) {
                newTCS.add(fName); // true checking
                newTCS.add("~" + fName); // false checking
            } else {
                newTCS.add(tc + " & " + fName); // true checking
                newTCS.add(tc + " & " + "~" + fName); // false checking
            }
        }

        return newTCS;
    }

    private Integer[] createIndexesArray(int numFeatures) {
        Integer[] indexesArr = new Integer[numFeatures];
        for (int i = 0; i < numFeatures; i++) {
            indexesArr[i] = i + 1;
        }
        return indexesArr;
    }

    private int getRandom(int min, int max) {
        double mean = min + (double)(max - min) / 2;
        double variance = (double)(max - min) / 2;
        RandomGaussian r = new RandomGaussian(mean, variance, min, max);

        return (int)r.getGaussian();
    }

    private String createFileName(String pathtoSave) {
        return pathtoSave + "/" + filename.substring(0, filename.length() - 5) + ".testsuite";
    }

    private void writeToFile(String pathtoSave) throws IOException {
        String fileName = createFileName(pathtoSave);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        int sum = numDeadFeatures + numFalseOptional + numFullMandatory + numFalseMandatory + numPartialConfiguration;

        writer.write(sum + "\n");
        writer.write(numDeadFeatures + "\n");
        writer.write(numFalseOptional + "\n");
        writer.write(numFullMandatory + "\n");
        writer.write(numFalseMandatory + "\n");
        writer.write(numPartialConfiguration + "\n");

        for (String st: testsuite) {
            writer.write(st + "\n");
            writer.flush();
        }

        // close file
        writer.close();
    }
}

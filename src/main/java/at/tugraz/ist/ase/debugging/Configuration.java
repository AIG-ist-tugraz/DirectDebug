/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores parameters of the program
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class Configuration {
    private boolean showEvaluation = true;
    private boolean showDebug = true;
    private List<Integer> cardCF = Arrays.asList(10, 20, 50, 100, 500, 1000);
    private List<Integer> cardTS = Arrays.asList(5, 10, 25, 50, 100, 250, 500);
    private int numGenFM = 3;
    private double ratioCTC = 0.4;
    private double perViolated_nonViolated = 0.3;
    private int numIter = 3;
    private String dataPath = "./data/";
    private String resultPath = "./results/";

    private boolean forOriginalTest = false;

    private Configuration() {}

    public boolean getShowEvaluation() {
        return showEvaluation;
    }

    public boolean getShowDebug() {
        return showDebug;
    }

    public List<Integer> getCardCF() {
        return cardCF;
    }

    public List<Integer> getCardTS() {
        return cardTS;
    }

    public int getNumGenFM() {
        return numGenFM;
    }

    public double getRatioCTC() {
        return ratioCTC;
    }

    public double getPerViolated_nonViolated() {
        return perViolated_nonViolated;
    }

    public int getNumIter() {
        return numIter;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getResultPath() {
        return resultPath;
    }

    public String getFMSPathInData() {
        return dataPath + "fms";
    }

    public String getFMSPathInResults() {
        return resultPath + "fms";
    }

    public String getTestSuitePathInData() {
        return dataPath + "testsuite";
    }

    public String getTestSuitePathInResults() {
        return resultPath + "testsuite";
    }

    public String getClassifiedTSPathInData() {
        return dataPath + "classifiedTS";
    }

    public String getClassifiedTSPathInResults() {
        return resultPath + "classifiedTS";
    }

    public String getScenariosPathInData() {
        return dataPath + "scenarios";
    }

    public String getScenariosPathInResults() {
        return resultPath + "scenarios";
    }

    public String createFMName(int numConstraints, int count) {
        String name = "FM_" + numConstraints + "_" + count;
        return name;
    }

    public String getFMSFilenameInResults(int numConstraints, int count) {
        String filename = getFMSPathInResults() + "/" + createFMName(numConstraints, count) + ".splx";
        return filename;
    }

    public String getFMSFilenameInData(int numConstraints, int count) {
        String filename = getFMSPathInData() + "/" + createFMName(numConstraints, count) + ".splx";
        return filename;
    }

    public String getTestSuiteFilenameInData(int numConstraints, int count) {
        String filename = getTestSuitePathInData() + "/FM_" + numConstraints + "_" + count + ".testsuite";
        return filename;
    }

    public String getClassifiedTSFilenameInResults(int numConstraints, int count) {
        String filename = getClassifiedTSPathInResults() + "/FM_" + numConstraints + "_" + count + ".classifiedts";
        return filename;
    }

    public String getClassifiedTSFilenameInData(int numConstraints, int count) {
        String filename = getClassifiedTSPathInData() + "/FM_" + numConstraints + "_" + count + ".classifiedts";
        return filename;
    }

    public String getScenariosFilenameInData(int numConstraints, int count, int numTS, int iter) {
        String filename = getScenariosPathInData() + "/FM_" + numConstraints + "_" + count + "_c" + numTS + "_" + iter + ".testcases";
        return filename;
    }

    public static Configuration getInstance(String confFile) {
        Configuration conf = new Configuration();
        // read the configuration in confFile
        conf.readConfigurationFromFile(confFile);

        return conf;
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();

        st.append("showEvaluations:" + (showEvaluation ? "true" : "false") + "\n");
        st.append("showDebug:" + (showDebug ? "true" : "false") + "\n");
        st.append("CF:" + cardCF + "\n");
        st.append("TC:" + cardTS + "\n");
        st.append("numGenFM:" + numGenFM + "\n");
        st.append("CTC:" + ratioCTC + "\n");
        st.append("numIter:" + numIter + "\n");
        st.append("perViolated_nonViolated:" + perViolated_nonViolated + "\n");
        st.append("dataPath:" + dataPath + "\n");
        st.append("resultPath:" + resultPath + "\n");

        return st.toString();
    }

    private void readConfigurationFromFile(String confFile) {
        File file = new File(confFile);

        BufferedReader reader;
        try {

            // Open file
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                // parser the line
                List<String> pairs = Arrays.asList(line.split(":"));

                if (pairs.size() != 2) continue;

                String token = pairs.get(0).trim();
                String value = pairs.get(1).trim();

                List<String> items;

                switch (token) {
                    case "showEvaluations":
                        showEvaluation = Boolean.parseBoolean(value);
                        break;
                    case "showDebug":
                        showDebug = Boolean.parseBoolean(value);
                        break;
                    case "CF":
                        items = Arrays.asList(value.split(","));

                        cardCF = new LinkedList<>();
                        for (String item: items) {
                            cardCF.add(Integer.parseInt(item));
                        }
                        break;
                    case "CTC":
                        ratioCTC = Double.parseDouble(value);
                        break;
                    case "TC":
                        items = Arrays.asList(value.split(","));

                        cardTS = new LinkedList<>();
                        for (String item: items) {
                            cardTS.add(Integer.parseInt(item));
                        }
                        break;
                    case "numGenFM":
                        numGenFM = Integer.parseInt(value);
                        break;
                    case "numIter":
                        numIter = Integer.parseInt(value);
                        break;
                    case "perViolated_nonViolated":
                        perViolated_nonViolated = Double.parseDouble(value);
                        break;
                    case "dataPath":
                        dataPath = value;
                        break;
                    case "resultsPath":
                        resultPath = value;
                        break;
                    default:
                        break;
                }
            }

            // Close file
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
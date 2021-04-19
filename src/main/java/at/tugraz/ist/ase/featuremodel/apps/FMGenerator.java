/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.apps;

import at.tugraz.ist.ase.debugging.Configuration;
import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.generator.FM.Evolutionay.EvolutionaryFMGenerator;
import es.us.isa.generator.FM.Evolutionay.FitnessFunction;
import es.us.isa.generator.FM.GeneratorCharacteristics;
import es.us.isa.utils.FMStatistics;
import es.us.isa.utils.FMWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static at.tugraz.ist.ase.featuremodel.core.Utilities.checkAndCreateFolder;

/**
 * The class that generates feature models using Betty framework.
 *
 *  For further details on Betty framework, we refer to https://www.isa.us.es/betty/welcome
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FMGenerator {
    /**
     * @param conf the configuration object
     * @throws Exception
     */
    public static void generate(Configuration conf) throws Exception {
        String path = conf.getFMSPathInResults();
        checkAndCreateFolder(path);

        for (int numFeatures: conf.getCardCF()) {
            for (int i = 0; i < conf.getNumGenFM(); i++) {
                String FMname = conf.createFMName(numFeatures, i);

                // STEP 1: Specify the user's preferences for the generation (characteristics)
                GeneratorCharacteristics characteristics = new GeneratorCharacteristics();
                characteristics.setNumberOfFeatures(numFeatures);            // Number of features.
                characteristics.setPercentageCTC((int) (conf.getRatioCTC() * 100));                // Percentage of constraints.

                // Max number of products of the feature model to be generated. Too large values could cause memory overflows or the program getting stuck.
                characteristics.setMaxProducts(10000);

                characteristics.setModelName(FMname);

                // STEP 2: Generate the model with the specific characteristics (FaMa FM metamodel is used)
                EvolutionaryFMGenerator generator = new EvolutionaryFMGenerator();

                //STEP 2.3: Set the fitness function for the genetic algorithm and the max number of generations allowed
                generator.setFitnessFunction(new BranchFitness());
//                generator.setFitnessFunction(new CTCFitness());
                generator.setMaximize(false);
                generator.setMaxGenerations(20);

                VariabilityModel geneticg = generator.generateFM(characteristics);

                // STEP 3: Save the model and the products
                String filename = conf.getFMSFilenameInResults(numFeatures, i);

                FMWriter writer = new FMWriter();
                writer.saveFM(geneticg, filename);

                addFMName(filename, FMname);
            }
        }
    }

    private static void addFMName(String filename, String fmName) throws IOException {
        Path path = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;
        List<String> lines = new ArrayList<>();
        if (Files.exists(path)) {
            lines = Files.readAllLines(path, charset);

            lines.remove(0);
            lines.add(0, "<feature_model name=\"" + fmName + "\">");
        }
        Files.write(path, lines, charset);
    }

    private static class BranchFitness implements FitnessFunction {
        @Override
        public double fitness(FAMAFeatureModel fm) {
            FMStatistics statistics= new FMStatistics(fm);
            return statistics.getMaxBranchingFactor();
        }
    }

    private static class CTCFitness implements FitnessFunction {
        @Override
        public double fitness(FAMAFeatureModel fm) {
            FMStatistics statistics= new FMStatistics(fm);
            return statistics.getCTCR();
        }
    }
}
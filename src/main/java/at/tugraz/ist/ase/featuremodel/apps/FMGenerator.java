/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.apps;

import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.generator.FM.Evolutionay.EvolutionaryFMGenerator;
import es.us.isa.generator.FM.Evolutionay.FitnessFunction;
import es.us.isa.generator.FM.GeneratorCharacteristics;
import es.us.isa.utils.FMStatistics;
import es.us.isa.utils.FMWriter;

/**
 * The class that generates feature models using Betty framework.
 *
 *  For further details of Betty framework, we refer to https://www.isa.us.es/betty/welcome
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FMGenerator {
    /**
     *
     * @param numFM number of feature models to be generated
     * @param numFeatures number of features
     * @param rateCTC rate of cross-tree constraints
     * @param maxProducts the max number of products to be generated
     * @param path the path of the save file.
     * @throws Exception
     */
    public static void generate(int numFM, int numFeatures, int rateCTC, int maxProducts, String path) throws Exception {

        for (int i = 0; i < numFM; i++) {
            // STEP 1: Specify the user's preferences for the generation (characteristics)
            GeneratorCharacteristics characteristics = new GeneratorCharacteristics();
            characteristics.setNumberOfFeatures(numFeatures);            // Number of features.
            characteristics.setPercentageCTC(rateCTC);                // Percentage of constraints.

            // Max number of products of the feature model to be generated. Too large values could cause memory overflows or the program getting stuck.
            characteristics.setMaxProducts(maxProducts);

            characteristics.setModelName("name");

            // STEP 2: Generate the model with the specific characteristics (FaMa FM metamodel is used)
            EvolutionaryFMGenerator generator = new EvolutionaryFMGenerator();

            //STEP 2.3: Set the fitness function for the genetic algorithm and the max number of generations allowed
            generator.setFitnessFunction(new BranchFitness());
            generator.setMaximize(false);
            generator.setMaxGenerations(20);

            VariabilityModel geneticg = generator.generateFM(characteristics);

            // STEP 3: Save the model and the products
            FMWriter writer = new FMWriter();
            writer.saveFM(geneticg, path + i + ".splx");
        }
    }

    private static class BranchFitness implements FitnessFunction {
        @Override
        public double fitness(FAMAFeatureModel fm) {
            FMStatistics statistics= new FMStatistics(fm);
            return statistics.getMaxBranchingFactor();
        }
    }
}
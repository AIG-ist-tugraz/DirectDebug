package at.tugraz.ist.ase.featuremodel.apps;

import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.generator.FM.Evolutionay.EvolutionaryFMGenerator;
import es.us.isa.generator.FM.Evolutionay.FitnessFunction;
import es.us.isa.generator.FM.GeneratorCharacteristics;
import es.us.isa.utils.FMStatistics;
import es.us.isa.utils.FMWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate some feature models for Matrix Factorization-based Conflict Detection
 */
public class MFCDFeatureModelGenerator {
    public static void main(String[] args) throws Exception {
        // STEP 1: Specify the user's preferences for the generation (characteristics)
        GeneratorCharacteristics characteristics = new GeneratorCharacteristics();
        characteristics.setNumberOfFeatures(5000); // Number of features.
        characteristics.setPercentageCTC((int) (0.4 * 100));
        // Max number of products of the feature model to be generated. Too large values could cause memory overflows or the program getting stuck.
        characteristics.setMaxProducts(20000);

        characteristics.setModelName("FM5000");

        // STEP 2: Generate the model with the specific characteristics (FaMa FM metamodel is used)
        EvolutionaryFMGenerator generator = new EvolutionaryFMGenerator();

        //STEP 2.3: Set the fitness function for the genetic algorithm and the max number of generations allowed
        generator.setFitnessFunction(new BranchFitness());
//                        generator.setFitnessFunction(new CTCFitness());
        generator.setMaximize(true);
        generator.setMaxGenerations(20);

        VariabilityModel geneticg = generator.generateFM(characteristics);

        // STEP 3: Save the model and the products
        FMWriter writer = new FMWriter();
        writer.saveFM(geneticg, "FM5000.splx");

        addFMName("FM5000.splx", "FM5000"); // add header
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
            es.us.isa.utils.FMStatistics statistics= new FMStatistics(fm);
            return statistics.getMaxBranchingFactor();
        }
    }
}

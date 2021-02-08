/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.apps;

import at.tugraz.ist.ase.MBDiagLib.model.KBModel;
import at.tugraz.ist.ase.debugging.DebuggingModel;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.core.Relationship;
import at.tugraz.ist.ase.featuremodel.parser.ParserException;
import at.tugraz.ist.ase.featuremodel.parser.SXFMParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The class that calculates the statistics of feature model.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FMStatistics {

    Boolean filter;
    String folderPath;
    String outputFilePath;

    /**
     * A constructor with a folder's path which stores feature model's files,
     * and the output file's path which will save the statistics.
     * @param filter
     * @param folderPath
     * @param outputFilePath
     */
    public FMStatistics(Boolean filter, String folderPath, String outputFilePath) {
        this.filter = filter;
        this.folderPath = folderPath;
        this.outputFilePath = outputFilePath;
    }

    public void calculate() throws IOException {
        File folder = new File(folderPath);
        SXFMParser parser = new SXFMParser();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        int count = 0;
        for (final File file : folder.listFiles()) {
            if (file.getName().endsWith(".splx")) {
                count++;
                System.out.println(file.getName());
                writer.write(count + "\n");
                writer.write(file.getName() + "\n");

                try {
                    FeatureModel featureModel = parser.parse(file);

                    KBModel model = new DebuggingModel(featureModel, null);

                    double ctc = (double)featureModel.getNumOfConstraints() / model.getAllConstraints().size();

                    if (filter
                            && (!featureModel.isConsistency()
                            || featureModel.getNumOfConstraints() == 0
                            || featureModel.getNumOfRelationships(Relationship.RelationshipType.MANDATORY) == 0
                            || featureModel.getNumOfRelationships(Relationship.RelationshipType.OPTIONAL) == 0
                            || featureModel.getNumOfRelationships(Relationship.RelationshipType.ALTERNATIVE) == 0
                            || featureModel.getNumOfRelationships(Relationship.RelationshipType.OR) == 0
                            || ctc < 0.1))
                        continue;

                    writer.write("\tName: " + featureModel.getName() + "\n");
                    writer.write("\tNumber of constraints: " + model.getAllConstraints().size() + "\n");
                    writer.write("\tCTC ratio: " + ctc + "\n");
                    writer.write("\tFeature Model consistent: " + featureModel.isConsistency() + "\n");
                    writer.write("\n");
                    writer.write("\tNumber of features: " + featureModel.getNumOfFeatures() + "\n");
                    writer.write("\tNumber of relationships: " + featureModel.getNumOfRelationships() + "\n");
                    writer.write("\tNumber of constraints: " + featureModel.getNumOfConstraints() + "\n");
                    writer.write("\tNumber of MANDATORY: " + featureModel.getNumOfRelationships(Relationship.RelationshipType.MANDATORY) + "\n");
                    writer.write("\tNumber of OPTIONAL: " + featureModel.getNumOfRelationships(Relationship.RelationshipType.OPTIONAL) + "\n");
                    writer.write("\tNumber of ALTERNATIVE: " + featureModel.getNumOfRelationships(Relationship.RelationshipType.ALTERNATIVE) + "\n");
                    writer.write("\tNumber of OR: " + featureModel.getNumOfRelationships(Relationship.RelationshipType.OR) + "\n");
                    writer.write("\tNumber of REQUIRES: " + featureModel.getNumOfRelationships(Relationship.RelationshipType.REQUIRES) + "\n");
                    writer.write("\tNumber of EXCLUDES: " + featureModel.getNumOfRelationships(Relationship.RelationshipType.EXCLUDES) + "\n");
                } catch (Exception e) {
                    writer.write(e.getMessage() + "\n");
                }

                System.out.println("Done - " + file.getName());
            }
        }

        System.out.println(count);
        writer.close();
    }
}

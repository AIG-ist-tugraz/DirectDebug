/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.parser;

import at.tugraz.ist.ase.featuremodel.core.FeatureModel;

import java.io.File;

/**
 * An interface for all parsers
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public interface BaseParser {
    /**
     * Checks the format of a feature model file.
     *
     * @param filePath - a {@link File}
     * @return true - if the feature model file has the same format with the parser
     *         false - otherwise
     */
    boolean checkFormat(File filePath);

    /**
     * Parses the feature model file into a {@link FeatureModel}.
     *
     * @param filePath - a {@link File}
     * @return a {@link FeatureModel}
     * @throws ParserException - a PaserException
     */
    FeatureModel parse(File filePath) throws ParserException;
}

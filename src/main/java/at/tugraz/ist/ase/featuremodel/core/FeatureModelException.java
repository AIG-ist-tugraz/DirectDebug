/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.core;

/**
 * An exception for errors which occur in parsing feature model files
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FeatureModelException extends Exception {
    public FeatureModelException(String message) {
        super(message);
    }

    public FeatureModelException(String message, Throwable throwable) {
        super(message, throwable);
    }
}


/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.parser;

/**
 * An exception for errors which occur in parsing feature model files
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class ParserException extends Exception {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

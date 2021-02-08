/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.checker;

import java.util.Collection;

/**
 * A common interface for the different consistency checkers.
 * Note that checkers must be not modify any of the input parameters!
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public interface IConsistencyChecker {

    /**
     * Checks the consistency of a set of constraints with a test case.
     * @param C       set of constraints
     * @param testcase
     * @return true if the given test case isn't violated to the set of constraints, and false otherwise.
     */
    boolean isConsistent(Collection<String> C, String testcase);

    /**
     * Supports a way to reset the internal checker
     */
    void reset();

    void dispose();
}

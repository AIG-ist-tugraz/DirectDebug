/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.core;

import org.chocosolver.solver.constraints.Constraint;

import java.util.ArrayList;
import java.util.List;

/***
 * Represents a test case.
 *
 * A test case is formed as follows:
 * ~F1 & F2 & F6 & F5
 * where the symbols Fi is literals, & denotes to a conjunct.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class TestCase {
    private final String testcase; // a test case

    private List<Clause> clauses; // a list of clauses of the test case
    private List<Constraint> constraints; // a list of Choco constraints which are translated from this test case

    private boolean isViolated; // represents the violation of this test case with the knowledge base

    /**
     * Constructor.
     * @param testcase a test case.
     */
    public TestCase(String testcase) {
        this.testcase = testcase;
        clauses = new ArrayList<>();
        constraints = new ArrayList<>();
        splitTestCase(testcase);
    }

    /**
     * Gets the test case.
     * @return the test case.
     */
    public String getTestcase() {
        return testcase;
    }

    /**
     * Gets clauses of the test case.
     * @return a list of clauses.
     */
    public List<Clause> getClauses() {
        return clauses;
    }

    /**
     * Sets a Choco constraint translated from this test case.
     * @param constraint a Choco constraint
     */
    public void setConstraint(Constraint constraint) {
        this.constraints.add(constraint);
    }

    /**
     * Gets Choco constraints of the test case.
     * @return a list of Choco constraints.
     */
    public List<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * Changes the value which represents the violation of this test case with the knowledge base.
     * @param violated
     */
    public void setViolated(boolean violated) {
        this.isViolated = violated;
    }

    /**
     * Gets the violation of the test case
     * @return true if the test case is violated, and false if otherwise
     */
    public boolean getViolated() {
        return isViolated;
    }

    @Override
    public String toString() {
        return testcase;
    }

    /**
     * Splits a test case into clauses.
     * @param testcase
     */
    private void splitTestCase(String testcase) {
        String[] clauses = testcase.split(" & ");

        for (String c: clauses) {
            Clause clause = new Clause(c);
            this.clauses.add(clause);
        }
    }
}

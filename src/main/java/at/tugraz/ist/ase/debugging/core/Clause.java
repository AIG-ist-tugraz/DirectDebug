/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging.core;

/***
 * Represents a clause of a test case.
 *
 * A Clause is written as follows:
 * F1 - i.e., F1 = true
 * ~F1 - i.e., F1 = false
 * where the symbol F1 is a literal, and ~ denotes to the negation.
 *
 * This class supports also feature models of Splot with 3-CNF formulas.
 * Refer to http://www.splot-research.org for more info regarding 3-CNF.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class Clause {
    private final String literal; // name of clause
    private final boolean value; // value of clause (true or false)

    /**
     * Constructor
     * @param clause in one of two forms: 1) A (i.e., A = true) or 2) ~A (i.e., A = false)
     */
    public Clause(String clause) {
        if (clause.startsWith("~")) {
            value = false;
            this.literal = clause.substring(1, clause.length());
        } else {
            value = true;
            this.literal = clause;
        }
    }

    /**
     * Gets the name of the clause
     * @return name of the clause
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Gets the value of the clause
     * @return value of the clause
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return literal + " = " + (value ? "true" : "false");
    }
}

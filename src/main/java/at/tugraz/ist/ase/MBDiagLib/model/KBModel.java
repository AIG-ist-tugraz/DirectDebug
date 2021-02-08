/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.model;

import org.apache.commons.collections4.SetUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.COUNTER_UNION_OPERATOR;
import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.incrementCounter;

/**
 * A knowledge base for constraint satisfaction problems.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public abstract class KBModel {

    private final String name;

    /**
     * The set of variables.
     */
    private Set<String> variables = new LinkedHashSet<>();

    /**
     * The set of constraints which we assume to be always correct (background knowledge).
     */
    private Set<String> correctConstraints = new LinkedHashSet<>();

    /**
     * The set of constraints which could be faulty = KB (knowledge base).
     */
    private Set<String> possiblyFaultyConstraints = new LinkedHashSet<>();

    // TODO - bring to DebuggingModel
    /**
     * The set of test cases.
     */
    private Set<String> testcases = new LinkedHashSet<>();

    /**
     * Creates an empty diagnosis model.
     */
    public KBModel(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the model.
     * @return name of the model.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the set of variables of the model.
     * @return the set of variables.
     */
    public Set<String> getVariables() { return variables; }

    /**
     * Changes the set of variables.
     * @param variables a new collection of variables.
     */
    public void setVariables(Collection<String> variables) {
        this.variables = new LinkedHashSet<>(variables);
    }

    /**
     * Gets the correct constraints (or background knowledge).
     * @return set of correct constraints.
     */
    public Set<String> getCorrectConstraints() {
        return correctConstraints;
    }

    /**
     * Changes the correct constraints (or background knowledge).
     * @param correctConstraints a new collection of correct constraints.
     */
    public void setCorrectConstraints(Collection<String> correctConstraints) {
        this.correctConstraints = new LinkedHashSet<>(correctConstraints);
    }

    /**
     * Gets the possibly faulty constraints (or knowledge base).
     * @return the set of possibly faulty constraints.
     */
    public Set<String> getPossiblyFaultyConstraints() {
        return possiblyFaultyConstraints;
    }

    /**
     * Changes the possibly faulty constraints (or knowledge base).
     * @param possiblyFaultyConstraints a new collection of possibly faulty constraints.
     */
    public void setPossiblyFaultyConstraints(Collection<String> possiblyFaultyConstraints) {
        this.possiblyFaultyConstraints = new LinkedHashSet<>(possiblyFaultyConstraints);
    }

    /**
     * Gets all constraints of the model.
     * @return a set of all constraints.
     */
    public Set<String> getAllConstraints() {
        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(correctConstraints, possiblyFaultyConstraints);
    }

    /**
     * Gets the set of test cases.
     * @return the set of test cases.
     */
    public Set<String> getTestcases() {
        return this.testcases;
    }

    /**
     * Changes the set of test cases.
     * @param testcases a new collection of test cases.
     */
    public void setTestcases(Collection<String> testcases) {
        this.testcases = new LinkedHashSet<>(testcases);
    }

    /**
     * Override this method to add all variables, constraints, and test cases.
     * Have to call this method after creating a new object of this class.
     */
    public abstract void initialize();

    @Override
    public String toString() {
        return "DiagnosisModel{" + '\n' + " Name=" + this.name +
                '\n' + ',' + variables.size() + " variables=" + variables +
                '\n' + ',' + correctConstraints.size() + " correctConstraints=" + correctConstraints +
                '\n' + ',' + possiblyFaultyConstraints.size() + " possiblyFaultyConstraints=" + possiblyFaultyConstraints +
                '\n' + ',' + testcases.size() + " testcases=" + testcases +
                '}';
    }
}

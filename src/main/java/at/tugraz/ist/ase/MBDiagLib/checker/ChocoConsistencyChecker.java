/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.checker;

import at.tugraz.ist.ase.debugging.core.TestCase;
import at.tugraz.ist.ase.debugging.DebuggingModel;
import at.tugraz.ist.ase.MBDiagLib.model.KBModel;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.*;

/**
 * A consistency checker using the Choco solver, version 4.10.2.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class ChocoConsistencyChecker implements IConsistencyChecker {
    /**
     * An internal models
     */
    private Model model; // Choco model
    private KBModel diagModel; // knowledge base
    private List<Constraint> cstrs; // all constraints of knowledge base

    /**
     * A constructor with a given {@link KBModel} ojbect.
     * @param diagModel a {@link KBModel} object.
     */
    public ChocoConsistencyChecker(KBModel diagModel) {
        this.diagModel = diagModel;
        model = ((DebuggingModel)diagModel).getModel();
        cstrs = Arrays.asList(model.getCstrs());
    }

    /**
     * Checks the consistency of a set of constraints with a test case.
     * @param C       set of constraints
     * @param testcase
     * @return true if the given test case isn't violated to the set of constraints, and false otherwise.
     */
    public boolean isConsistent(Collection<String> C, String testcase) {
        // remove constraints do not present in the constraints of the parameter C
        for (Constraint c1 : cstrs) {
            incrementCounter(COUNTER_CONSTAINS_CONSTRAINT);
            if (!C.contains(c1.toString())) {
                model.unpost(c1); incrementCounter(COUNTER_UNPOST_CONSTRAINT);
            }
        }

        // add test case
        addTestCase(testcase);

        // Call solve()
        try {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            boolean isFeasible = model.getSolver().solve();

            // resets the model to the beginning status
            // restores constraints which are removed at the beginning of the function
            reset();

            if (isFeasible) {
                incrementCounter(COUNTER_FEASIBLE);
            } else {
                incrementCounter(COUNTER_INFEASIBLE);
            }

            return isFeasible;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception here, " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks the consistency of a set of constraints with a set of test cases.
     * This function returns true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false. Test cases inducing
     * an inconsistency with {@param C} are stored in {@param TCp}.
     * @param C a set of constraints
     * @param TC a considering test cases
     * @param TCp a remaining inconsistent test cases
     * @return true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false.
     */
    public boolean isConsistent(Collection<String> C, Collection<String> TC, Collection<String> TCp) {
        boolean consistent = true;
        for (String tc: TC) {
            if (!isConsistent(C, tc)) {
                TCp.add(tc);
                consistent = false;
            }
        }
        return consistent;
    }

    /**
     * Resets the model to the beginning status
     * Restores constraints which are removed in the {@func isConsistent} function.
     */
    @Override
    public void reset() {
        model.getSolver().reset();
        incrementCounter(COUNTER_UNPOST_CONSTRAINT, model.getNbCstrs());
        model.unpost(model.getCstrs()); // unpost all constraints
        for (Constraint c : cstrs) { // repost all constraints
            model.post(c);
            incrementCounter(COUNTER_POST_CONSTRAINT);
        }
    }

    @Override
    public void dispose() {
        this.model = null;
        this.diagModel = null;
        this.cstrs = null;
    }

    /**
     * Adds the corresponding constraints of a textual test case to the model.
     * @param testcase a textual test case
     */
    private void addTestCase(String testcase) {
        TestCase tc = ((DebuggingModel)diagModel).getTestCase(testcase);
        if (tc != null) {
            for (Constraint c : tc.getConstraints()) {
                model.post(c);
                incrementCounter(COUNTER_POST_CONSTRAINT);
            }
        }
    }
}

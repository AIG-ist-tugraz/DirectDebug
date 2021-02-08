/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.MBDiagLib.model.KBModel;
import at.tugraz.ist.ase.debugging.core.Clause;
import at.tugraz.ist.ase.debugging.core.TestCase;
import at.tugraz.ist.ase.debugging.core.TestSuite;
import at.tugraz.ist.ase.featuremodel.core.FeatureModel;
import at.tugraz.ist.ase.featuremodel.core.FeatureModelException;
import at.tugraz.ist.ase.featuremodel.core.Relationship;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.Variable;

import java.util.*;

/**
 * An extension class of {@link KBModel} for a debugging task.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class DebuggingModel extends KBModel {

    private Model model;
    private FeatureModel featureModel;
    private TestSuite testSuite;

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     * @param testSuite a {@link TestSuite}
     */
    public DebuggingModel(FeatureModel fm, TestSuite testSuite) {
        super(fm.getName());

        this.featureModel = fm;
        this.testSuite = testSuite;
        this.model = null;

        initialize(); // TODO - take out
    }

    /**
     * This function creates a Choco models, variables, constraints
     * for a corresponding feature models. Besides, test cases are
     * also translated to Choco constraints.
     */
    @Override
    public void initialize() {
        // creates model
        try {
            createModel();
        } catch (FeatureModelException ex) {
            System.out.println("ERROR - " + ex.getMessage());
            return;
        }

        // sets possibly faulty constraints to super class
        List<String> C = new ArrayList<>();
        for (Constraint c: model.getCstrs()) {
            C.add(c.toString());
        }
        this.setPossiblyFaultyConstraints(C);

        // sets correct constraints to super class
        // {f0 = true}
        model.addClauseTrue(getVarWithName(featureModel.getName()));
        List<String> B = new ArrayList<>();
        B.add(model.getCstrs()[model.getNbCstrs() - 1].toString());
        this.setCorrectConstraints(B);

        // check the consistency of the knowledge base
//        featureModel.setConsistency(model.getSolver().solve());
//        model.getSolver().reset();

        // translates test cases to Choco constraints
        if (testSuite != null) {
            int firstCstrIdx = model.getNbCstrs();
            createTestCases();
            int lastCstrIdx = model.getNbCstrs();

            // sets the translated constraints to KBModel (super class)
            List<String> TC = new ArrayList<>();
            for (TestCase tc : testSuite.getTestSuite()) {
                TC.add(tc.getTestcase());
            }
            this.setTestcases(TC);

            // remove test case's constraints from knowledge base (Choco model)
            int i = firstCstrIdx;
            while (i < lastCstrIdx) {
                Constraint c = model.getCstrs()[firstCstrIdx]; // remove at firstCstrIdx
                model.unpost(c);
                i++;
            }
        }
    }

    /**
     * Gets the Choco model of this knowledge base.
     * @return a Choco model.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Gets a corresponding {@link TestCase} object of a textual testcase.
     * @param testcase a textual testcase.
     * @return a corresponding {@link TestCase} object.
     */
    public TestCase getTestCase(String testcase) {
        for (TestCase tc: testSuite.getTestSuite()) {
            if (tc.toString().equals(testcase)) { // TODO optimizes this
                return tc;
            }
        }
        return null;
    }

    /**
     * Creates a Choco model on the basis of {@link FeatureModel}.
     * @throws FeatureModelException
     */
    private void createModel() throws FeatureModelException {
        model = new Model(featureModel.getName());

        createVariables();
        createConstraints();
    }

    /**
     * On the basic of a given {@link FeatureModel}, this function creates
     * corresponding variables for the model.
     */
    private void createVariables() throws FeatureModelException {
        BoolVar[] x = new BoolVar[featureModel.getNumOfFeatures()];
        List<String> variables = new ArrayList<>();

        for (int i = 0; i < featureModel.getNumOfFeatures(); i++) {
            String name = featureModel.getFeature(i).getName(); // get the name of a feature

            x[i] = model.boolVar(name); // create Choco variables
            variables.add(name);
        }
        this.setVariables(variables); // set variables to KBModel (superclass)
    }

    /**
     * On the basic of a given {@link FeatureModel}, this function creates
     * corresponding constraints for the model.
     */
    private void createConstraints() {
        // first converts relationships into constraints
        for (Relationship relationship: featureModel.getRelationships()) {
            BoolVar leftVar = getVarWithName(relationship.getLeftSide());
            BoolVar rightVar;

            switch (relationship.getType())
            {
                case MANDATORY:
                    rightVar = getVarWithName(relationship.getRightSide().get(0));
                    // leftVar <=> rightVar
                    model.addClauses(LogOp.ifOnlyIf(leftVar, rightVar));

                    break;
                case OPTIONAL:
                    rightVar = getVarWithName(relationship.getRightSide().get(0));
                    // leftVar => rightVar
                    model.addClauses(LogOp.implies(leftVar, rightVar));

                    break;
                case OR:
                    // LogOp of rule {A \/ B \/ ... \/ C}
                    LogOp rightLogOp = getRightSideOfOrRelationship(relationship.getRightSide());
                    // leftVar <=> rightLogOp
                    model.addClauses(LogOp.ifOnlyIf(leftVar, rightLogOp));

                    break;
                case ALTERNATIVE:
                    // LogOp of an ALTERNATIVE relationship
                    LogOp op = getLogOpOfAlternativeRelationship(relationship);
                    model.addClauses(op);

                    break;
            }
        }

        // second convert constraints of {@link FeatureModel} into ChocoSolver constraints
        for (Relationship relationship: featureModel.getConstraints()) {
            if (relationship.getType() == Relationship.RelationshipType.SPECIAL) {

                LogOp op = LogOp.or();
                for (Clause clause: relationship.getClauses()) {
                    boolean value = clause.getValue();
                    BoolVar var = getVarWithName(clause.getLiteral());

                    if (value) {
                        op.addChild(var);
                    } else {
                        op.addChild(LogOp.nor(var));
                    }
                }
                model.addClauses(op);
            } else {
                BoolVar leftVar = getVarWithName(relationship.getLeftSide());
                BoolVar rightVar = getVarWithName(relationship.getRightSide().get(0));

                switch (relationship.getType()) {
                    case REQUIRES:
                        model.addClauses(LogOp.implies(leftVar, rightVar));
                        break;
                    case EXCLUDES:
                        model.addClauses(LogOp.or(LogOp.nor(leftVar), LogOp.nor(rightVar)));
                        break;
                }
            }
        }
    }

    /**
     * Creates a {@link LogOp} representing an ALTERNATIVE relationship.
     * The form of rule is {C1 <=> (not C2 /\ ... /\ not Cn /\ P) /\
     *                      C2 <=> (not C1 /\ ... /\ not Cn /\ P) /\
     *                      ... /\
     *                      Cn <=> (not C1 /\ ... /\ not Cn-1 /\ P)
     *
     * @param relationship an Alternative {@link Relationship} of {@link FeatureModel}
     * @return A {@link LogOp} representing an ALTERNATIVE relationship
     */
    private LogOp getLogOpOfAlternativeRelationship(Relationship relationship) {
        LogOp op = LogOp.and(); // a LogOp of AND operators
        for (int i = 0; i < relationship.getRightSide().size(); i++) {
            BoolVar rightVar = getVarWithName(relationship.getRightSide().get(i));
            // (not C2 /\ ... /\ not Cn /\ P)
            LogOp rightSide = getRightSideOfAlternativeRelationship(relationship.getLeftSide(), relationship.getRightSide(), i);
            // {C1 <=> (not C2 /\ ... /\ not Cn /\ P)}
            LogOp part = LogOp.ifOnlyIf(rightVar, rightSide);
            op.addChild(part);
        }
        return op;
    }

    /**
     * Creates a {@link LogOp} representing the rule {(not C2 /\ ... /\ not Cn /\ P)}.
     * This is the right side of the rule {C1 <=> (not C2 /\ ... /\ not Cn /\ P)}
     *
     * @param leftSide - the name of the parent feature
     * @param rightSide - an {@link ArrayList} storing names of the child features
     * @param removedIndex - the index of the child feature that is the left side of the rule
     * @return a {@link LogOp} representing the rule {(not C2 /\ ... /\ not Cn /\ P)}.
     * @throws IllegalArgumentException when couldn't find the variable in the model
     */
    private LogOp getRightSideOfAlternativeRelationship(String leftSide, ArrayList<String> rightSide, int removedIndex) throws IllegalArgumentException {
        BoolVar leftVar = getVarWithName(leftSide);
        LogOp op = LogOp.and(leftVar);
        for (int i = 0; i < rightSide.size(); i++) {
            if (i != removedIndex) {
                op.addChild(LogOp.nor(getVarWithName(rightSide.get(i))));
            }
        }
        return op;
    }

    /**
     * Creates a {@link LogOp} representing the right side of an OR relationship.
     * The form of rule is {A \/ B \/ ... \/ C}.
     *
     * @param rightSide - an {@link ArrayList} of feature names which belong to the right side of an OR relationship
     * @return a {@link LogOp} or null if the rightSide is empty
     * @throws IllegalArgumentException when couldn't find a variable which corresponds to the given feature name
     */
    private LogOp getRightSideOfOrRelationship(ArrayList<String> rightSide) throws IllegalArgumentException {
        // TODO - have to Not empty
        if (rightSide.size() == 0) return null;
        LogOp op = LogOp.or(); // create a LogOp of OR operators
        for (String s : rightSide) {
            BoolVar var = getVarWithName(s);
            op.addChild(var);
        }
        return op;
    }

    /**
     * On the basic of a feature name, this function return
     * the corresponding ChocoSolver variable in the model.
     *
     * @param name - a feature name
     * @return the corresponding ChocoSolver variable in the model or null
     * @throws IllegalArgumentException when couldn't find the variable in the model
     */
    public BoolVar getVarWithName(String name) throws IllegalArgumentException {
        Variable var = null;
        for (Variable v : model.getVars()) {
            if (v.getName().equals(name)) {
                var = v;
                break;
            }
        }
        if (var == null)
            throw new IllegalArgumentException("The feature " + name + " is not exist in the feature model!");
        return (BoolVar) var;
    }

    /**
     * Translates test cases to Choco constraints.
     */
    private void createTestCases() {
        List<TestCase> testCaseList = testSuite.getTestSuite();
        for (TestCase tc: testCaseList) { // for each test case
            int firstCstrIdx = model.getNbCstrs();

            LogOp op = LogOp.and(); // creates a AND LogOp
            for (Clause c: tc.getClauses()) { // get each clause
                BoolVar v = getVarWithName(c.getLiteral()); // get the corresponding variable
                if (c.getValue()) { // true
                    op.addChild(v);
                } else { // false
                    op.addChild(LogOp.nor(v));
                }
            }
            model.addClauses(op); // add the translated constraints to the Choco model
            int lastCstrIdx = model.getNbCstrs();

            // add the translated constraints to the TestCase object
            setConstraintsToTestCase(firstCstrIdx, lastCstrIdx, tc);
        }
    }

    /**
     * Sets translated Choco constraints to the {@link TestCase} object.
     * @param firstCstrIdx
     * @param lastCstrIdx
     * @param testCase
     */
    private void setConstraintsToTestCase(int firstCstrIdx, int lastCstrIdx, TestCase testCase) {
        Constraint[] constraints = model.getCstrs();
        for (int i = firstCstrIdx; i < lastCstrIdx; i++) {
            testCase.setConstraint(constraints[i]);
        }
    }
}

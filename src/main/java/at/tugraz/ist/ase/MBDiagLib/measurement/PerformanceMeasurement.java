/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.measurement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static at.tugraz.ist.ase.MBDiagLib.core.DebugConfigurations.showDebugs;
import static at.tugraz.ist.ase.MBDiagLib.core.DebugConfigurations.showEvaluations;

/**
 * Manages all measurements of the project.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class PerformanceMeasurement {

    public static final String COUNTER_FEASIBLE = "feasible";
    public static final String COUNTER_INFEASIBLE = "isfeasible";
    public static final String COUNTER_DIRECTDEBUG_CALLS = "directdebug.calls";
    public static final String COUNTER_CONSISTENCY_CHECKS = "consistency.checks";
    public static final String COUNTER_CHOCO_SOLVER_CALLS = "choco.solver.calls";
    public static final String COUNTER_SIZE_CONSISTENCY_CHECKS = "constraints.consistency.checks";
    public static final String COUNTER_UNION_OPERATOR = "union.operator";
    public static final String COUNTER_ADD_OPERATOR = "add.operator";
    public static final String COUNTER_DIFFERENT_OPERATOR = "different.operator";
    public static final String COUNTER_SPLIT_SET = "split.set";
    public static final String COUNTER_LEFT_BRANCH_CALLS = "left.branch.calls";
    public static final String COUNTER_RIGHT_BRANCH_CALLS = "right.branch.calls";
    public static final String COUNTER_UNPOST_CONSTRAINT = "unpost.constraint";
    public static final String COUNTER_POST_CONSTRAINT = "post.constraint";
    public static final String COUNTER_CONSTAINS_CONSTRAINT = "constains.constraint";

    public static final String TIMER_FIRST = "diagnosis.first";

    private static ConcurrentHashMap<String, Counter> counters = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();

    /**
     * Returns a counter with the given name.
     * If counter does not exist, it will be created by the method and added
     * to the set of counters.
     *
     * @param name of the counter
     * @return a counter
     */
    public static Counter getCounter(String name) {
        return counters.computeIfAbsent(name, (key) -> new Counter(name));
    }

    /**
     * Returns a timer with the given name.
     * If timer does not exist, it will be created by the method and added to
     * the set of timers
     *
     * @param name of the timer
     * @return a timer
     */
    public static Timer getTimer(String name) {
        return timers.computeIfAbsent(name, (key) -> new Timer(name));
    }

    /**
     * Increments a counter and returns its new value
     *
     * @param name of the counter
     * @return new value of the counter
     */
    public static long incrementCounter(String name) {
        return incrementCounter(name, 1);
    }

    /**
     * Increments a counter to a given number of steps
     *
     * @param name of the counter
     * @param step to increment the counter
     * @return new value of the counter
     */
    public static long incrementCounter(String name, int step) {
        return getCounter(name).increment(step);
    }

    /**
     * Starts a timer with the given name
     *
     * @param name of the counter
     */
    public static void start(String name) {
        getTimer(name).start();
    }

    /**
     * Stops a timer with the given name
     *
     * @param name of the timer
     * @return elapsed time since the timer was started
     */
    public static long stop(String name) {
        return getTimer(name).stop();
    }

    /**
     * @param name of the timer
     * @return the total time that the timer was running
     */
    public static long total(String name) {
        return getTimer(name).total();
    }

    /**
     * @return an unmodifiable map of counters
     */
    public static Map<String, Counter> getCounters() {
        return Collections.unmodifiableMap(counters);
    }

    /**
     * @return an unmodifiable map of timers
     */
    public static Map<String, Timer> getTimers() {
        return Collections.unmodifiableMap(timers);
    }

    /**
     * Reinitialize all existing counters.
     */
    public static void reset() {
        counters = new ConcurrentHashMap<>();
        timers = new ConcurrentHashMap<>();
    }

    /**
     * Prints the evaluations to the screen and to a {@link BufferedWriter}.
     * @param itNumber the number of iterations used to the evaluation.
     * @param writer a {@link BufferedWriter} object.
     * @throws IOException
     */
    public static void printEvaluations(int itNumber, BufferedWriter writer) throws IOException {

        if (showEvaluations) {
            writer.write("\t\t\tThe number of DirectDebug calls:" + (getCounter(COUNTER_DIRECTDEBUG_CALLS).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of Consistency checks:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of Choco Solver calls:" + (getCounter(COUNTER_CHOCO_SOLVER_CALLS).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of consistent:" + (getCounter(COUNTER_FEASIBLE).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of INconsistent:" + (getCounter(COUNTER_INFEASIBLE).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe size of Consistency checks:" + (getCounter(COUNTER_SIZE_CONSISTENCY_CHECKS).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of union:" + (getCounter(COUNTER_UNION_OPERATOR).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of add:" + (getCounter(COUNTER_ADD_OPERATOR).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of different:" + (getCounter(COUNTER_DIFFERENT_OPERATOR).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of split set:" + (getCounter(COUNTER_SPLIT_SET).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of left branch calls:" + (getCounter(COUNTER_LEFT_BRANCH_CALLS).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of right branch calls:" + (getCounter(COUNTER_RIGHT_BRANCH_CALLS).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of unpost constraints:" + (getCounter(COUNTER_UNPOST_CONSTRAINT).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of post constraints:" + (getCounter(COUNTER_POST_CONSTRAINT).getValue() / itNumber) + "\n");
            writer.write("\t\t\tThe number of contains calls:" + (getCounter(COUNTER_CONSTAINS_CONSTRAINT).getValue() / itNumber) + "\n");
            writer.write("\t\t\tTime for first: " + ((double)getTimer(TIMER_FIRST).total() / 1_000_000_000.0 / itNumber) + "\n");
        }

        if (showDebugs) {
            System.out.println("\t\t\tThe number of DirectDebug calls:" + (getCounter(COUNTER_DIRECTDEBUG_CALLS).getValue() / itNumber));
            System.out.println("\t\t\tThe number of Consistency checks:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue() / itNumber));
            System.out.println("\t\t\tThe number of Choco Solver calls:" + (getCounter(COUNTER_CHOCO_SOLVER_CALLS).getValue() / itNumber));
            System.out.println("\t\t\tThe number of consistent:" + (getCounter(COUNTER_FEASIBLE).getValue() / itNumber));
            System.out.println("\t\t\tThe number of INconsistent:" + (getCounter(COUNTER_INFEASIBLE).getValue() / itNumber));
            System.out.println("\t\t\tThe size of Consistency checks:" + (getCounter(COUNTER_SIZE_CONSISTENCY_CHECKS).getValue() / itNumber));
            System.out.println("\t\t\tThe number of union:" + (getCounter(COUNTER_UNION_OPERATOR).getValue() / itNumber));
            System.out.println("\t\t\tThe number of add:" + (getCounter(COUNTER_ADD_OPERATOR).getValue() / itNumber));
            System.out.println("\t\t\tThe number of different:" + (getCounter(COUNTER_DIFFERENT_OPERATOR).getValue() / itNumber));
            System.out.println("\t\t\tThe number of split set:" + (getCounter(COUNTER_SPLIT_SET).getValue() / itNumber));
            System.out.println("\t\t\tThe number of left branch calls:" + (getCounter(COUNTER_LEFT_BRANCH_CALLS).getValue() / itNumber));
            System.out.println("\t\t\tThe number of right branch calls:" + (getCounter(COUNTER_RIGHT_BRANCH_CALLS).getValue() / itNumber));
            System.out.println("\t\t\tThe number of unpost constraints:" + (getCounter(COUNTER_UNPOST_CONSTRAINT).getValue() / itNumber));
            System.out.println("\t\t\tThe number of post constraints:" + (getCounter(COUNTER_POST_CONSTRAINT).getValue() / itNumber));
            System.out.println("\t\t\tThe number of contains calls:" + (getCounter(COUNTER_CONSTAINS_CONSTRAINT).getValue() / itNumber));
            System.out.println("\t\t\tTime for first: " + ((double)getTimer(TIMER_FIRST).total() / 1_000_000_000.0 / itNumber));
        }
    }
}

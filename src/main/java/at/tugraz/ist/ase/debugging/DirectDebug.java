/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.debugging;

import at.tugraz.ist.ase.MBDiagLib.checker.ChocoConsistencyChecker;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.*;

/**
 * Implementation of DirectDebug.
 *
 * <ul>
 *     <li>Viet-Man Le et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models.
 *     In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results
 *     (ICSE-NIER 2021). (to appear)</li>
 * </ul>
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class DirectDebug {

    private ChocoConsistencyChecker checker;

    /**
     * A constructor with a checker of {@link ChocoConsistencyChecker}.
     * @param checker
     */
    public DirectDebug(ChocoConsistencyChecker checker) {
        this.checker = checker;
    }

    /**
     * This function will activate DirectDebug algorithm if there exists at least one positive test case,
     * which induces an inconsistency in C U B. Otherwise, it returns an empty set.
     *
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @param TC a set of test cases
     * @return a diagnosis or an empty set
     */
    public Set<String> debug(Set<String> C, Set<String> B, Set<String> TC)
    {
        incrementCounter(COUNTER_DIRECTDEBUG_CALLS);

        Set<String> BwithC = SetUtils.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        Set<String> TCp = new LinkedHashSet<>();
        if (C.isEmpty()
                || checker.isConsistent(BwithC, TC, TCp)) {
            return Collections.emptySet();
        }
        else{ // else return C \ directDebug(C, B, T'π)
            incrementCounter(COUNTER_DIRECTDEBUG_CALLS);
            start(TIMER_FIRST);
            Set<String> d = directDebug(C, B, TCp);
            stop(TIMER_FIRST);

            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            return SetUtils.difference(C, d);
        }
    }

    /**
     * The implementation of DirectDebug algorithm.
     * The algorithm determines a maximal satisfiable subset MSS (Γ) of C U B U TC.
     *
     * // Func DirectDebug(C = {c1..cn}, B, Tπ) : Γ
     * // if IsConsistent(B U C, Tπ, T'π) return C;
     * // if singleton(C) return Φ;
     * // k = n/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cn};
     * // Γ2 = DirectDebug(C1, B, T'π);
     * // Γ1 = DirectDebug(C2, B U Γ2, T'π);
     * // return Γ1 ∪ Γ2;
     *
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @param TC a set of test cases which induce an inconsistency in C U B
     * @return a maximal satisfiable subset MSS of C U B U TC.
     */
    private Set<String> directDebug(Set<String> C, Set<String> B, Set<String> TC) {
        Set<String> TCp = new LinkedHashSet<>();
        Set<String> BwithC = SetUtils.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        // if IsConsistent(B U C, Tπ, T'π) return C;
        if (checker.isConsistent(BwithC, TC, TCp)) {
            return C;
        }

        // if singleton(C) return Φ;
        int n = C.size();
        if (n == 1) {
            return Collections.emptySet();
        }

        int k = n / 2;  // k = n/2;
        // C1 = {c1..ck}; C2 = {ck+1..cn};
        List<String> firstSubList = new ArrayList<>(C).subList(0, k);
        List<String> secondSubList = new ArrayList<>(C).subList(k, n);
        Set<String> C1 = new LinkedHashSet<>(firstSubList);
        Set<String> C2 = new LinkedHashSet<>(secondSubList);
        incrementCounter(COUNTER_SPLIT_SET);

        // Γ2 = DirectDebug(C1, B, T'π);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_DIRECTDEBUG_CALLS);
        Set<String> Δ2 = directDebug(C1, B, TCp);

        // Γ1 = DirectDebug(C2, B U Γ2, T'π);
        Set<String> BwithΔ2 = SetUtils.union(Δ2, B); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_DIRECTDEBUG_CALLS);
        Set<String> Δ1 = directDebug(C2, BwithΔ2, TCp);

        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(Δ1, Δ2);
    }
}

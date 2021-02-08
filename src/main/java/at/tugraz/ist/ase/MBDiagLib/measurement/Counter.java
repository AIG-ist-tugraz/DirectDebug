/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.measurement;

/**
 * A counter used for evaluations.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class Counter extends Measurement {
    private long value = 0;

    /**
     * A constructor with a specific name of the counter.
     * @param name the name of the counter.
     */
    Counter(String name) {
        super(name);
    }

    /**
     * Gets the value of the counter.
     * @return the current value of the counter.
     */
    public long getValue() {
        return value;
    }

    /**
     * Increments the counter by a step and returns a new value of the counter.
     * @param step
     * @return the new value of the counter after incrementing.
     */
    public long increment(int step) {
        this.value = this.value + step;
        return getValue();
    }

    @Override
    public String toString() {
        return Long.toString(getValue());
    }
}
